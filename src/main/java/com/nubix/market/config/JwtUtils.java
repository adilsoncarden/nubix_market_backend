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
 * Componente de utilidad para la gestión de JSON Web Tokens (JWT).
 * Se encarga de la generación, validación y extracción de información (claims) 
 * de los tokens utilizados para la autenticación de usuarios en la API.
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}")
    private long expirationTime;

    /**
     * Genera la clave criptográfica necesaria para firmar y validar los tokens.
     * Utiliza el secreto configurado en las propiedades de la aplicación (application.properties o .yml).
     *
     * @return La clave HMAC generada a partir del secreto.
     */
    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * Crea un nuevo token JWT para un usuario autenticado.
     * Incluye el nombre de usuario como sujeto (subject), el rol como un claim personalizado,
     * la fecha de emisión y la fecha de expiración.
     *
     * @param username El nombre de usuario (o email) que se autenticó.
     * @param rol      El rol asignado al usuario en el sistema.
     * @return Un String que representa el token JWT firmado y compactado.
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
     * Desencripta y valida un token JWT utilizando la clave de firma de la aplicación.
     *
     * @param token El token JWT recibido en la cabecera de la petición.
     * @return El cuerpo del token (Claims) que contiene los datos del usuario.
     */
    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae el nombre de usuario (subject) almacenado dentro de un token JWT.
     *
     * @param token El token JWT del cual se extraerá la información.
     * @return El nombre de usuario.
     */
    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    /**
     * Extrae el rol del usuario almacenado como un claim personalizado dentro del token JWT.
     *
     * @param token El token JWT del cual se extraerá el rol.
     * @return El nombre del rol como String, o {@code null} si no existe dicho claim.
     */
    public String extractRol(String token) {
        Object rol = parseClaims(token).get("rol");
        return rol != null ? rol.toString() : null;
    }
}
