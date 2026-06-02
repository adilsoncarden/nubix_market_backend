package com.nubix.market.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Asegura que la tabla de tokens de reset tenga ID autoincremental.
 */
@Component
public class PasswordResetSchemaPatch {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetSchemaPatch.class);

    private final JdbcTemplate jdbcTemplate;

    public PasswordResetSchemaPatch(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void patch() {
        try {
            jdbcTemplate.execute("""
                    CREATE TABLE IF NOT EXISTS password_reset_tokens (
                        id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        codigo VARCHAR(6) NOT NULL,
                        fecha_expiracion DATETIME(6) NOT NULL,
                        utilizado BIT NOT NULL DEFAULT 0,
                        usuario_id INT NOT NULL,
                        CONSTRAINT fk_reset_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id)
                    )
                    """);
            try {
                jdbcTemplate.execute(
                        "ALTER TABLE password_reset_tokens MODIFY id BIGINT NOT NULL AUTO_INCREMENT");
            } catch (Exception ignored) {
                // Ya configurado
            }
            log.debug("Tabla password_reset_tokens verificada");
        } catch (Exception e) {
            log.warn("No se pudo aplicar parche de password_reset_tokens: {}", e.getMessage());
        }
    }
}
