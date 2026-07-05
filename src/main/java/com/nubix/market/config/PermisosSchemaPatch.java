package com.nubix.market.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Parche PostgreSQL/Supabase que añade la columna {@code permisos.modulo} con valor
 * por defecto para filas existentes, antes de que Hibernate o el seeder fallen por NOT NULL.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Component
@Order(5)
public class PermisosSchemaPatch implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PermisosSchemaPatch.class);

    private final JdbcTemplate jdbcTemplate;

    /**
     * Inyecta el template JDBC usado para consultar el catálogo y aplicar ALTER TABLE.
     *
     * @param jdbcTemplate acceso JDBC de Spring
     */
    public PermisosSchemaPatch(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Ejecuta el parche de la columna {@code modulo} si la tabla {@code permisos} ya existe.
     *
     * @param args argumentos de arranque de Spring Boot (no utilizados)
     */
    @Override
    public void run(ApplicationArguments args) {
        if (!tableExists("permisos")) {
            log.debug("Tabla permisos aún no existe; se omitirá parche de modulo");
            return;
        }
        patchModuloColumn();
    }

    private boolean tableExists(String tableName) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    """
                            SELECT COUNT(*) FROM information_schema.tables
                            WHERE table_schema = current_schema()
                              AND table_name = ?
                            """,
                    Integer.class,
                    tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            log.warn("No se pudo verificar tabla {}: {}", tableName, e.getMessage());
            return false;
        }
    }

    private boolean columnExists(String tableName, String columnName) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    """
                            SELECT COUNT(*) FROM information_schema.columns
                            WHERE table_schema = current_schema()
                              AND table_name = ?
                              AND column_name = ?
                            """,
                    Integer.class,
                    tableName,
                    columnName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void patchModuloColumn() {
        try {
            if (!columnExists("permisos", "modulo")) {
                jdbcTemplate.execute(
                        "ALTER TABLE permisos ADD COLUMN modulo VARCHAR(80) DEFAULT 'General'");
                log.info("permisos.modulo: columna creada con DEFAULT 'General'");
            }

            jdbcTemplate.execute(
                    """
                            UPDATE permisos SET modulo = 'Dashboard'
                            WHERE nombre = 'ver:dashboard'
                              AND (modulo IS NULL OR modulo = '' OR modulo = 'General')
                            """);
            jdbcTemplate.execute(
                    """
                            UPDATE permisos SET modulo = 'Productos'
                            WHERE nombre LIKE '%:productos'
                              AND (modulo IS NULL OR modulo = '' OR modulo = 'General')
                            """);
            jdbcTemplate.execute(
                    """
                            UPDATE permisos SET modulo = 'Categorías'
                            WHERE nombre LIKE '%:categorias'
                              AND (modulo IS NULL OR modulo = '' OR modulo = 'General')
                            """);
            jdbcTemplate.execute(
                    """
                            UPDATE permisos SET modulo = 'Proveedores'
                            WHERE nombre LIKE '%:proveedores'
                              AND (modulo IS NULL OR modulo = '' OR modulo = 'General')
                            """);
            jdbcTemplate.execute(
                    """
                            UPDATE permisos SET modulo = 'Ventas'
                            WHERE nombre LIKE '%:ventas'
                              AND (modulo IS NULL OR modulo = '' OR modulo = 'General')
                            """);
            jdbcTemplate.execute(
                    """
                            UPDATE permisos SET modulo = 'Seguridad'
                            WHERE nombre LIKE 'gestionar:%'
                              AND (modulo IS NULL OR modulo = '' OR modulo = 'General')
                            """);
            jdbcTemplate.execute(
                    """
                            UPDATE permisos SET modulo = 'General'
                            WHERE modulo IS NULL OR modulo = ''
                            """);

            jdbcTemplate.execute(
                    "ALTER TABLE permisos ALTER COLUMN modulo SET DEFAULT 'General'");
            jdbcTemplate.execute(
                    "ALTER TABLE permisos ALTER COLUMN modulo SET NOT NULL");

            log.info("permisos.modulo: backfill y NOT NULL aplicados correctamente");
        } catch (Exception e) {
            log.warn("Parche permisos.modulo no aplicado (Hibernate puede completarlo): {}", e.getMessage());
        }
    }
}
