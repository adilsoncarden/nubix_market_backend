package com.nubix.market.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * Componente de utilidades JWT para emitir y validar tokens de sesión stateless
 * en la autenticación de clientes y personal administrativo de Nubix Market.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Component
public class JwtUtils {

    /** Secreto HMAC configurado en {@code jwt.secret}. */
    @Value("${jwt.secret}")
    private String secret;

    /** Tiempo de expiración del token en milisegundos ({@code jwt.expiration-ms}). */
    @Value("${jwt.expiration-ms:86400000}")
    private long expirationTime;

    /**
     * Deriva la clave de firma HMAC-SHA a partir del secreto configurado.
     *
     * @return clave de firma para operaciones JJWT
     */
    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Genera un token JWT firmado con el nombre de usuario y el rol en la claim {@code rol}.
     *
     * @param username identificador del usuario (subject del token)
     * @param rol      nombre del rol asignado al usuario
     * @return token JWT compacto listo para enviar al cliente
     */
    public String generateToken(String username, String rol) {
        return Jwts.builder()
                .setSubject(username)
                .claim("rol", rol)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Parsea y valida la firma del token, devolviendo todas las claims.
     *
     * @param token token JWT en formato compacto
     * @return claims extraídas del token validado
     */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Obtiene el nombre de usuario (subject) del token.
     *
     * @param token token JWT válido
     * @return nombre de usuario o {@code null} si el subject no está presente
     */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Obtiene la claim personalizada {@code rol} del token.
     *
     * @param token token JWT válido
     * @return nombre del rol como cadena, o {@code null} si la claim no existe
     */
    public String extractRol(String token) {
        Object rol = parseClaims(token).get("rol");
        return rol != null ? rol.toString() : null;
    }
}
