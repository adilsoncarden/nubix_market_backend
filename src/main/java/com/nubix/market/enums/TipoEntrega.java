package com.nubix.market.enums;

/**
 * Determina la modalidad logística elegida por el cliente para recibir sus productos.
 */
public enum TipoEntrega {
    /** Retiro rápido en tienda sin hacer colas, flujo principal del sistema. */
    FAST_LANE,
    /** Envío directo a la dirección especificada por el cliente. */
    DELIVERY,
    /** Compra tradicional y entrega directamente en la caja del local. */
    PRESENCIAL
}
