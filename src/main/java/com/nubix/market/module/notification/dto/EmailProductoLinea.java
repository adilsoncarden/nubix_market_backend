package com.nubix.market.module.notification.dto;

/**
 * Línea de producto incluida en un correo de confirmación.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class EmailProductoLinea {

    /** Nombre descriptivo. */
    private String nombre;
    /** Cantidad de unidades. */
    private int cantidad;
    /** Subtotal sin impuestos. */
    private double subtotal;

    public EmailProductoLinea() {
    }

    public EmailProductoLinea(String nombre, int cantidad, double subtotal) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    /**
     * GetNombre.
     * @return resultado de la operación
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * SetNombre.
     * @param nombre Nombre descriptivo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * GetCantidad.
     * @return resultado de la operación
     */
    public int getCantidad() {
        return cantidad;
    }

    /**
     * SetCantidad.
     * @param cantidad Cantidad de unidades.
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * GetSubtotal.
     * @return resultado de la operación
     */
    public double getSubtotal() {
        return subtotal;
    }

    /**
     * SetSubtotal.
     * @param subtotal Subtotal sin impuestos.
     */
    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
