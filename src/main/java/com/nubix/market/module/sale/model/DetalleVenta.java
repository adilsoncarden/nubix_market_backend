package com.nubix.market.module.sale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.nubix.market.common.jackson.JsonViews;
import com.nubix.market.module.product.model.Producto;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa una línea de producto dentro del carrito o boleta de un cliente.
 * Guarda un registro histórico de a qué precio se vendió el producto en ese momento exacto, 
 * independientemente de si el precio cambia en el futuro.
 */
@JsonView(JsonViews.Detail.class)
@Entity
@Table(name = "venta_detalles")
public class DetalleVenta {

    /** Identificador único del detalle de venta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Referencia a la transacción de venta general. Se oculta en el JSON para evitar ciclos. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnore
    private Venta venta;

    /** Producto físico del catálogo que el cliente está adquiriendo. */
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /** Número de unidades compradas de este producto. */
    @Column(nullable = false)
    private Integer cantidad;

    /** Precio de venta al público fijado en el momento exacto de la transacción. */
    @Column(nullable = false)
    private Double precioUnitario;

    /** Subtotal calculado para esta línea (precioUnitario * cantidad). */
    @Column(nullable = false)
    private Double subtotal;

    public DetalleVenta() {
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}