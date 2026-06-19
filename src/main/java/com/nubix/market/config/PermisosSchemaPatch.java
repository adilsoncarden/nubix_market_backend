package com.nubix.market.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Parche de base de datos ejecutado al iniciar la aplicación para actualizar 
 * el esquema de la tabla 'permisos'.
 * Añade la columna 'modulo' con un valor por defecto para las filas existentes, 
 * evitando errores de restricción NOT NULL antes de que Hibernate o los seeders actúen.
 */
@Component
@Order(5)
public class PermisosSchemaPatch implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(PermisosSchemaPatch.class);

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructor para inyección de dependencias.
     *
     * @param jdbcTemplate Herramienta para ejecutar consultas SQL nativas.
     */
    public PermisosSchemaPatch(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Punto de entrada del ApplicationRunner.
     * Verifica la existencia de la tabla antes de proceder con el parche.
     *
     * @param args Argumentos de la línea de comandos con los que se inició la aplicación.
     */
    @Override
    public void run(ApplicationArguments args) {
        if (!tableExists("permisos")) {
            log.debug("Tabla permisos aún no existe; se omitirá parche de modulo");
            return;
        }
        patchModuloColumn();
    }

    /**
     * Verifica si una tabla específica existe en el esquema actual de la base de datos.
     *
     * @param tableName El nombre de la tabla a verificar.
     * @return {@code true} si la tabla existe, {@code false} en caso contrario o si ocurre un error.
     */
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

    /**
     * Verifica si una columna específica existe dentro de una tabla determinada.
     *
     * @param tableName  El nombre de la tabla.
     * @param columnName El nombre de la columna a buscar.
     * @return {@code true} si la columna existe en la tabla, {@code false} en caso contrario.
     */
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

    /**
     * Aplica la lógica principal del parche:
     * 1. Crea la columna 'modulo' si no existe.
     * 2. Asigna módulos específicos (Dashboard, Productos, etc.) basados en el nombre del permiso.
     * 3. Configura la columna como NOT NULL con un valor por defecto ('General').
     */
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
