package com.nubix.market.enums;

/**
 * Enumera las opciones de pago habilitadas para que los clientes realicen una compra.
 */
public enum MetodoPago {
    /** Pago en dinero físico al momento de la entrega o en caja. */
    EFECTIVO,
    /** Pago a través de la billetera digital Yape. */
    YAPE,
    /** Pago mediante transferencia bancaria directa. */
    TRANSFERENCIA,
    /** Pago procesado mediante tarjeta de crédito o débito (POS o pasarela web). */
    TARJETA,
    /** Venta a crédito, requiere autorización previa y registro en cuentas por cobrar. */
    CREDITO
}
