package com.nubix.market.module.auth.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RecoveryBarSignature {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    private final byte[] secretBytes;

    public RecoveryBarSignature(@Value("${jwt.secret}") String secret) {
        this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
    }

    public String sign(Long tokenId) {
        return truncateUrlSafe(hmac(String.valueOf(tokenId)));
    }

    public boolean verify(Long tokenId, String signature) {
        if (tokenId == null || signature == null || signature.isBlank()) {
            return false;
        }
        return sign(tokenId).equals(signature.trim());
    }

    private String hmac(String payload) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secretBytes, HMAC_ALGORITHM));
            byte[] digest = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo firmar la barra de recuperación", ex);
        }
    }

    private static String truncateUrlSafe(String value) {
        return value.length() <= 22 ? value : value.substring(0, 22);
    }
}
