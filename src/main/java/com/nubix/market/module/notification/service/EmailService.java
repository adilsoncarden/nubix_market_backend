package com.nubix.market.module.notification.service;

import com.nubix.market.module.notification.dto.EmailConfirmacionContext;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final String FROM_DISPLAY_NAME = "NUBIX MARKET";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Value("${spring.mail.from:${spring.mail.username:}}")
    private String fromAddress;

    public void enviarCodigoRecuperacion(String email, String codigo) {
        String html = emailTemplateService.recuperacionContrasena(codigo);
        enviarHtml(email, "Recuperación de contraseña — Nubix Market", html);
    }

    public void enviarConfirmacionCompra(EmailConfirmacionContext context) {
        String html = emailTemplateService.confirmacionCompra(context);
        String numero = context.getNumero() != null ? context.getNumero() : "Nubix Market";
        enviarHtml(context.getEmail(), "Confirmación de compra — " + numero, html);
    }

    private void enviarHtml(String to, String subject, String html) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(buildFromAddress());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error enviando email a {}: {}", to, e.getMessage());
            throw new RuntimeException("No se pudo enviar el correo electrónico");
        }
    }

    private InternetAddress buildFromAddress() throws UnsupportedEncodingException {
        String address = fromAddress != null && !fromAddress.isBlank()
                ? fromAddress.trim()
                : "noreply@nubixmarket.com";
        return new InternetAddress(address, FROM_DISPLAY_NAME, StandardCharsets.UTF_8.name());
    }
}
