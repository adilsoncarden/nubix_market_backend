-- Coordenadas y Place ID de Google (campos opcionales, no afectan registros existentes)
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS latitud DOUBLE PRECISION NULL;
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS longitud DOUBLE PRECISION NULL;
ALTER TABLE usuario ADD COLUMN IF NOT EXISTS google_place_id VARCHAR(255) NULL;
