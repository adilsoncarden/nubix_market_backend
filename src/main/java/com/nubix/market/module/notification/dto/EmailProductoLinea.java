package com.nubix.market.module.notification.dto;

/**
 * Objeto de transferencia de datos (DTO) que representa una línea de producto individual 
 * dentro del recibo o factura enviada por correo electrónico al cliente.
 */
public class EmailProductoLinea {

    /** Nombre del producto comprado. */
    private String nombre;

    /** Cantidad de unidades adquiridas. */
    private int cantidad;

    /** Subtotal calculado para esta línea específica (precio unitario x cantidad). */
    private double subtotal;

    public EmailProductoLinea() {
    }

    public EmailProductoLinea(String nombre, int cantidad, double subtotal) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
