-- Campos opcionales de perfil / envío (no rompe registros existentes)
ALTER TABLE usuario ADD COLUMN telefono VARCHAR(20) NULL;
ALTER TABLE usuario ADD COLUMN direccion VARCHAR(255) NULL;
ALTER TABLE usuario ADD COLUMN departamento VARCHAR(80) NULL;
ALTER TABLE usuario ADD COLUMN provincia VARCHAR(80) NULL;
ALTER TABLE usuario ADD COLUMN distrito VARCHAR(80) NULL;
ALTER TABLE usuario ADD COLUMN referencia VARCHAR(255) NULL;
