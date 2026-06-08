package com.nubix.market.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Alinea el esquema de ventas/pagos con el módulo actual (cliente_id nullable,
 * estado_pago VARCHAR).
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
                            SELECT is_nullable FROM information_schema.columns
                            WHERE table_schema = current_schema()
                              AND table_name = 'ventas'
                              AND column_name = 'cliente_id'
                            """,
                    String.class);
            if ("NO".equalsIgnoreCase(nullable)) {
                jdbcTemplate.execute("ALTER TABLE ventas ALTER COLUMN cliente_id DROP NOT NULL");
                log.info("ventas.cliente_id: ahora permite NULL");
            }
        } catch (Exception e) {
            log.warn("No se pudo parchear ventas.cliente_id: {}", e.getMessage());
        }
    }

    private void patchEstadoPago(String tableName) {
        try {
            String dataType = jdbcTemplate.queryForObject(
                    """
                            SELECT data_type FROM information_schema.columns
                            WHERE table_schema = current_schema()
                              AND table_name = ?
                              AND column_name = 'estado_pago'
                            """,
                    String.class,
                    tableName);
            if (dataType == null) {
                return;
            }
            if ("USER-DEFINED".equalsIgnoreCase(dataType)) {
                jdbcTemplate.execute(
                        "UPDATE " + tableName + " SET estado_pago = 'RECHAZADO' WHERE estado_pago::text = 'CANCELADO'");
                jdbcTemplate.execute(
                        "ALTER TABLE " + tableName
                                + " ALTER COLUMN estado_pago TYPE VARCHAR(20) USING estado_pago::text");
                jdbcTemplate.execute(
                        "ALTER TABLE " + tableName + " ALTER COLUMN estado_pago SET NOT NULL");
                log.info("{}.estado_pago: ENUM migrado a VARCHAR(20)", tableName);
            }
        } catch (Exception e) {
            log.warn("No se pudo parchear {}.estado_pago: {}", tableName, e.getMessage());
        }
    }
}
