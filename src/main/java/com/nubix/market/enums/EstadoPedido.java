package com.nubix.market.enums;

/**
 * Define el ciclo de vida logístico de un pedido dentro del sistema.
 */
public enum EstadoPedido {
    /** El pedido fue ingresado pero aún no se ha empezado a preparar. */
    PENDIENTE,
    /** El pedido está siendo empacado o preparado por el personal. */
    EN_PROCESO,
    /** El pedido está empaquetado y esperando en el área de Fast Lane o mostrador. */
    LISTO_PARA_RECOJO,
    /** El pedido ha salido del local y está en ruta (Delivery). */
    EN_CAMINO,
    /** El pedido ha sido entregado satisfactoriamente al cliente. */
    ENTREGADO
}
