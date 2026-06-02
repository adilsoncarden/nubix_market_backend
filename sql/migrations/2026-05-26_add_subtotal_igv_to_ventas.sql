-- MIGRACIÓN MANUAL (opcional) si NO dependes de spring.jpa.hibernate.ddl-auto=update
-- Agrega desglose de impuestos (IGV 13%) a cabecera de ventas.

ALTER TABLE ventas
  ADD COLUMN subtotal DOUBLE NULL DEFAULT 0 AFTER total,
  ADD COLUMN igv DOUBLE NULL DEFAULT 0 AFTER subtotal;

