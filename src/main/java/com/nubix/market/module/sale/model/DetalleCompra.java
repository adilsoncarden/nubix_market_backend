package com.nubix.market.module.sale.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.module.product.model.Producto;

/**
 * Entidad JPA que representa una línea específica dentro de una orden de compra a proveedor.
 * Relaciona el ingreso general de mercadería con un producto individual del catálogo.
 */
@Entity
@Table(name = "detalle_compras")
public class DetalleCompra {

    /** Identificador único del detalle de compra. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** * Referencia a la compra padre. 
     * Se ignora en la serialización JSON para evitar ciclos infinitos al enviar la respuesta al frontend.
     */
    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    @JsonIgnore
    private Compra compra;

    /** Producto específico del catálogo que está siendo reabastecido. */
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /** Cantidad de unidades ingresadas al almacén. */
    @Column(nullable = false)
    private Integer cantidad;

    /** Costo individual de adquisición pactado con el proveedor para este lote. */
    @Column(nullable = false)
    private Double precioUnitario;

    /** Cálculo del subtotal (precioUnitario * cantidad) para esta línea específica. */
    @Column(nullable = false)
    private Double subtotal;

    public DetalleCompra() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
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
