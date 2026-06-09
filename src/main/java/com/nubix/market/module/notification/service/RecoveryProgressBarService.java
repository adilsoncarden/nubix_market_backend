package com.nubix.market.module.notification.service;

import com.nubix.market.module.auth.model.ContraseñaResetToken;
import com.nubix.market.module.auth.repository.ReseteoContraseñaRepository;
import com.nubix.market.module.auth.util.RecoveryBarSignature;
import com.nubix.market.module.auth.util.RecoveryProgressCalculator;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class RecoveryProgressBarService {

    private static final int BAR_WIDTH = 504;
    private static final int BAR_HEIGHT = 12;
    private static final Color BRAND = Color.decode("#006634");
    private static final Color TRACK = Color.decode("#e5e7eb");
    private static final DateTimeFormatter EXPIRES_FORMAT =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.forLanguageTag("es-PE"));

    private final ReseteoContraseñaRepository reseteoContraseñaRepository;
    private final RecoveryBarSignature recoveryBarSignature;
    private final String publicBaseUrl;

    public RecoveryProgressBarService(
            ReseteoContraseñaRepository reseteoContraseñaRepository,
            RecoveryBarSignature recoveryBarSignature,
            @Value("${app.public-base-url:http://localhost:8080}") String publicBaseUrl) {
        this.reseteoContraseñaRepository = reseteoContraseñaRepository;
        this.recoveryBarSignature = recoveryBarSignature;
        this.publicBaseUrl = trimTrailingSlash(publicBaseUrl);
    }

    public String buildBarImageUrl(Long tokenId) {
        String signature = recoveryBarSignature.sign(tokenId);
        return publicBaseUrl
                + "/api/public/email/recovery-bar?id="
                + tokenId
                + "&sig="
                + signature;
    }

    public byte[] renderBarImage(long tokenId, String signature) {
        ContraseñaResetToken token = resolveToken(tokenId, signature);
        int percent = currentRemainingPercent(token);
        return drawPng(percent);
    }

    public String renderBarTableHtml(long tokenId, String signature) {
        ContraseñaResetToken token = resolveToken(tokenId, signature);
        int percent = currentRemainingPercent(token);
        return buildTableProgressBarHtml(percent);
    }

    public String buildTableProgressBarHtml(int percent) {
        int green = Math.max(0, Math.min(100, percent));
        int gray = 100 - green;

        if (green <= 0) {
            return """
                    <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                      <tr>
                        <td width="100%%" bgcolor="#e5e7eb" height="12" style="font-size:0;line-height:0;mso-line-height-rule:exactly;">&nbsp;</td>
                      </tr>
                    </table>
                    """;
        }

        if (gray <= 0) {
            return """
                    <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                      <tr>
                        <td width="100%%" bgcolor="#006634" height="12" style="font-size:0;line-height:0;mso-line-height-rule:exactly;">&nbsp;</td>
                      </tr>
                    </table>
                    """;
        }

        return """
                <table role="presentation" width="100%%" cellpadding="0" cellspacing="0" style="border-collapse:collapse;">
                  <tr>
                    <td width="%d%%" bgcolor="#006634" height="12" style="font-size:0;line-height:0;mso-line-height-rule:exactly;">&nbsp;</td>
                    <td width="%d%%" bgcolor="#e5e7eb" height="12" style="font-size:0;line-height:0;mso-line-height-rule:exactly;">&nbsp;</td>
                  </tr>
                </table>
                """.formatted(green, gray);
    }

    public String formatExpiresAt(LocalDateTime expiresAt) {
        return expiresAt.format(EXPIRES_FORMAT);
    }

    private ContraseñaResetToken resolveToken(long tokenId, String signature) {
        if (!recoveryBarSignature.verify(tokenId, signature)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Optional<ContraseñaResetToken> tokenOpt = reseteoContraseñaRepository.findById(tokenId);
        if (tokenOpt.isEmpty() || tokenOpt.get().isUtilizado()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return tokenOpt.get();
    }

    private int currentRemainingPercent(ContraseñaResetToken token) {
        return RecoveryProgressCalculator.remainingPercent(
                token.getFechaExpiracion(), LocalDateTime.now());
    }

    private byte[] drawPng(int percent) {
        int fillWidth = (int) Math.round(BAR_WIDTH * (Math.max(0, Math.min(100, percent)) / 100.0));

        BufferedImage image = new BufferedImage(BAR_WIDTH, BAR_HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setColor(TRACK);
            graphics.fillRect(0, 0, BAR_WIDTH, BAR_HEIGHT);
            if (fillWidth > 0) {
                graphics.setColor(BRAND);
                graphics.fillRect(0, 0, fillWidth, BAR_HEIGHT);
            }
        } finally {
            graphics.dispose();
        }

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            ImageIO.write(image, "png", output);
            return output.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo generar la barra de progreso", ex);
        }
    }

    private static String trimTrailingSlash(String value) {
        if (value == null || value.isBlank()) {
            return "http://localhost:8080";
        }
        String trimmed = value.trim();
        return trimmed.endsWith("/") ? trimmed.substring(0, trimmed.length() - 1) : trimmed;
    }
}
