package com.nubix.market.enums;

/**
 * Representa los posibles estados financieros de una transacción o pedido.
 */
public enum EstadoPago {
    /** El pago aún no se ha recibido o procesado. */
    PENDIENTE,
    /** El pago se ha completado exitosamente por parte del cliente. */
    PAGADO,
    /** El pago ha sido verificado y autorizado (útil para transferencias o Yape). */
    APROBADO,
    /** El pago fue denegado, falló o fue cancelado. */
    RECHAZADO
}
