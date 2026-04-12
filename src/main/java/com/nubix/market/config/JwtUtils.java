package com.nubix.market.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Llave secreta para firmar el token (en producción esto va en application.properties)
    private final String SECRET_KEY = "NubixMarketClaveSecretaMuyLargaYSegura2026"; 
    private final long EXPIRATION_TIME = 86400000; // 24 horas en milisegundos

    // Genera una clave segura a partir del String
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}