package com.nubix.market.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Componente que ejecuta correcciones automáticas sobre el esquema de la base de datos 
 * al iniciar la aplicación, enfocado específicamente en los módulos de ventas y pagos.
 * Soluciona problemas de restricciones y tipos de datos heredados.
 */
@Component
public class VentasSchemaPatch implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(VentasSchemaPatch.class);

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param jdbcTemplate Herramienta para ejecutar sentencias SQL nativas.
     */
    public VentasSchemaPatch(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Punto de entrada del ApplicationRunner. Ejecuta secuencialmente los parches necesarios.
     *
     * @param args Argumentos de inicio de la aplicación.
     */
    @Override
    public void run(ApplicationArguments args) {
        patchClienteIdNullable();
        patchEstadoPago("ventas");
        patchEstadoPago("pagos");
    }

    /**
     * Modifica la columna 'cliente_id' en la tabla 'ventas' para permitir valores nulos.
     * Esto es útil para soportar ventas a clientes no registrados o compras en tienda física.
     */
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

    /**
     * Migra la columna 'estado_pago' de un tipo ENUM (USER-DEFINED) a un VARCHAR(20) estándar.
     * Esta conversión previene errores de compatibilidad y facilita la inserción de nuevos 
     * estados sin necesidad de modificar el tipo de dato en la base de datos.
     * Convierte previamente los estados 'CANCELADO' a 'RECHAZADO' por reglas de negocio.
     *
     * @param tableName El nombre de la tabla que contiene la columna 'estado_pago' (ej. "ventas" o "pagos").
     */
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
