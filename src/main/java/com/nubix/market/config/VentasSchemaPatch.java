package com.nubix.market.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Alinea el esquema de ventas/pagos con el módulo actual (cliente_id nullable, estado_pago VARCHAR).
 */
@Component
public class VentasSchemaPatch implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(VentasSchemaPatch.class);

    private final JdbcTemplate jdbcTemplate;

    public VentasSchemaPatch(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        patchClienteIdNullable();
        patchEstadoPago("ventas");
        patchEstadoPago("pagos");
    }

    private void patchClienteIdNullable() {
        try {
            String nullable = jdbcTemplate.queryForObject(
                    """
                    SELECT IS_NULLABLE FROM information_schema.COLUMNS
                    WHERE TABLE_SCHEMA = DATABASE()
                      AND TABLE_NAME = 'ventas'
                      AND COLUMN_NAME = 'cliente_id'
                    """,
                    String.class);
            if ("NO".equalsIgnoreCase(nullable)) {
                jdbcTemplate.execute("ALTER TABLE ventas MODIFY cliente_id INT NULL");
                log.info("ventas.cliente_id: ahora permite NULL");
            }
        } catch (Exception e) {
            log.warn("No se pudo parchear ventas.cliente_id: {}", e.getMessage());
        }
    }

    private void patchEstadoPago(String tableName) {
        try {
            String columnType = jdbcTemplate.queryForObject(
                    """
                    SELECT COLUMN_TYPE FROM information_schema.COLUMNS
                    WHERE TABLE_SCHEMA = DATABASE()
                      AND TABLE_NAME = ?
                      AND COLUMN_NAME = 'estado_pago'
                    """,
                    String.class,
                    tableName);
            if (columnType == null) {
                return;
            }
            if (columnType.toLowerCase().startsWith("enum")) {
                jdbcTemplate.execute(
                        "UPDATE " + tableName + " SET estado_pago = 'RECHAZADO' WHERE estado_pago = 'CANCELADO'");
                jdbcTemplate.execute(
                        "ALTER TABLE " + tableName + " MODIFY estado_pago VARCHAR(20) NOT NULL");
                log.info("{}.estado_pago: ENUM migrado a VARCHAR(20)", tableName);
            }
        } catch (Exception e) {
            log.warn("No se pudo parchear {}.estado_pago: {}", tableName, e.getMessage());
        }
    }
}
