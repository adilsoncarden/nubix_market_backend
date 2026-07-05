package com.nubix.market.module.cart.dto;

/**
 * DTO de solicitud para agregar o modificar un ítem del carrito.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class CarritoItemRequest {
    /** Campo productoId. */
    private Integer productoId;
    /** Cantidad de unidades. */
    private Integer cantidad;

    /**
     * GetProductoId.
     * @return resultado de la operación
     */
    public Integer getProductoId() {
        return productoId;
    }

    /**
     * SetProductoId.
     * @param productoId valor del parámetro
     */
    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    /**
     * GetCantidad.
     * @return resultado de la operación
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * SetCantidad.
     * @param cantidad Cantidad de unidades.
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
