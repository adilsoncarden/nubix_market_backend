-- Identidad del usuario (campos opcionales)
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS dni_ruc VARCHAR(11) NULL;
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS nombre_razon_social VARCHAR(255) NULL;
