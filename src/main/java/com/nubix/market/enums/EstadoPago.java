package com.nubix.market.enums;

/**
 * Estados del pago asociado a una venta.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public enum EstadoPago {
    /** Pago pendiente de confirmación. */
    PENDIENTE,
    /** Pago registrado. */
    PAGADO,
    /** Pago aprobado por el sistema o pasarela. */
    APROBADO,
    /** Pago rechazado. */
    RECHAZADO
}
