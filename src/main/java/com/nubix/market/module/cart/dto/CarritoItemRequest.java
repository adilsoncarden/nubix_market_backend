package com.nubix.market.module.cart.dto;

/**
 * Objeto de transferencia de datos (DTO) utilizado para encapsular la información 
 * enviada desde el frontend cuando un cliente desea añadir un producto a su carrito 
 * o modificar la cantidad de uno existente.
 */
public class CarritoItemRequest {

    /** Identificador único del producto en el catálogo. */
    private Integer productoId;

    /** Cantidad de unidades que el usuario desea añadir o establecer para este producto. */
    private Integer cantidad;

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }
}
