-- Parche manual (opcional). El arranque de la app aplica estos cambios de forma idempotente.
-- 1) Ticket sin cliente registrado
ALTER TABLE ventas MODIFY cliente_id INT NULL;

-- 2) Normalizar estados legacy y usar VARCHAR (evita truncamiento con ENUM desalineado)
UPDATE ventas SET estado_pago = 'RECHAZADO' WHERE estado_pago = 'CANCELADO';
UPDATE pagos SET estado_pago = 'RECHAZADO' WHERE estado_pago = 'CANCELADO';

ALTER TABLE ventas MODIFY estado_pago VARCHAR(20) NOT NULL;
ALTER TABLE pagos MODIFY estado_pago VARCHAR(20) NOT NULL;
