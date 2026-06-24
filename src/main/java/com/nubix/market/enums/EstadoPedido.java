package com.nubix.market.enums;

/**
 * Estados del ciclo de vida de un pedido.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public enum EstadoPedido {
    /** Pedido registrado y pendiente de procesamiento. */
    PENDIENTE,
    /** Pedido en preparación. */
    EN_PROCESO,
    /** Pedido listo para ser recogido por el cliente. */
    LISTO_PARA_RECOJO,
    /** Pedido en camino hacia el destino. */
    EN_CAMINO,
    /** Pedido entregado al cliente. */
    ENTREGADO
}
