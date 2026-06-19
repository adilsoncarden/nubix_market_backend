package com.nubix.market.enums;

/**
 * Especifica los tipos de documentos tributarios o comprobantes de venta emitidos.
 */
public enum TipoComprobante {
    /** Comprobante de control interno sin valor fiscal estricto. */
    TICKET,
    /** Comprobante de pago válido para consumidores finales (requiere DNI si supera el monto límite). */
    BOLETA,
    /** Comprobante de pago válido para empresas o personas con RUC que requieren sustentar gastos. */
    FACTURA
}
