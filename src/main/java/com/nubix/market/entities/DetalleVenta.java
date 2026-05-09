package com.nubix.market.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_ventas")
public class DetalleVenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double subtotal;

    public DetalleVenta() {
    }

    // Getters y Setters
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public Venta getVenta() {return venta;}
    public void setVenta(Venta venta) {this.venta = venta;}
    public Producto getProducto() {return producto;}
    public void setProducto(Producto producto) {this.producto = producto;}
    public Integer getCantidad() {return cantidad;}
    public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}
    public Double getPrecioUnitario() {return precioUnitario;}
    public void setPrecioUnitario(Double precioUnitario) {this.precioUnitario = precioUnitario;}
    public Double getSubtotal() {return subtotal;}
    public void setSubtotal(Double subtotal) {this.subtotal = subtotal;}    
}