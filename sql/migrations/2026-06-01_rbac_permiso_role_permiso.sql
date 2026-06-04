-- RBAC: tablas permisos y role_permiso (PostgreSQL)
-- Ejecutar manualmente si no se usa spring.jpa.hibernate.ddl-auto=update

CREATE TABLE IF NOT EXISTS permisos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NOT NULL,
    modulo VARCHAR(80) NOT NULL DEFAULT 'Seguridad',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Paso 1: columna nullable con DEFAULT (compatible con filas existentes en Supabase)
ALTER TABLE permisos ADD COLUMN IF NOT EXISTS modulo VARCHAR(80) DEFAULT 'General';

UPDATE permisos SET modulo = 'Dashboard' WHERE nombre = 'ver:dashboard' AND (modulo IS NULL OR modulo = '' OR modulo = 'General');
UPDATE permisos SET modulo = 'Productos' WHERE nombre LIKE '%:productos' AND (modulo IS NULL OR modulo = '' OR modulo = 'General');
UPDATE permisos SET modulo = 'Categorías' WHERE nombre LIKE '%:categorias' AND (modulo IS NULL OR modulo = '' OR modulo = 'General');
UPDATE permisos SET modulo = 'Proveedores' WHERE nombre LIKE '%:proveedores' AND (modulo IS NULL OR modulo = '' OR modulo = 'General');
UPDATE permisos SET modulo = 'Ventas' WHERE nombre LIKE '%:ventas' AND (modulo IS NULL OR modulo = '' OR modulo = 'General');
UPDATE permisos SET modulo = 'Seguridad' WHERE nombre LIKE 'gestionar:%' AND (modulo IS NULL OR modulo = '' OR modulo = 'General');
UPDATE permisos SET modulo = 'General' WHERE modulo IS NULL OR modulo = '';

ALTER TABLE permisos ALTER COLUMN modulo SET DEFAULT 'General';
ALTER TABLE permisos ALTER COLUMN modulo SET NOT NULL;

CREATE TABLE IF NOT EXISTS role_permiso (
    role_id INTEGER NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permiso_id INTEGER NOT NULL REFERENCES permisos(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permiso_id)
);

CREATE INDEX IF NOT EXISTS idx_role_permiso_permiso ON role_permiso(permiso_id);

ALTER TABLE roles ADD COLUMN IF NOT EXISTS descripcion VARCHAR(500);

UPDATE roles SET descripcion = 'Administrador del sistema con acceso total'
WHERE nombre = 'ADMIN' AND (descripcion IS NULL OR descripcion = '');

UPDATE roles SET descripcion = 'Usuario de la tienda web pública'
WHERE nombre = 'CLIENTE' AND (descripcion IS NULL OR descripcion = '');
