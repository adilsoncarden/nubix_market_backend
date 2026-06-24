package com.nubix.market.module.sale.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.module.product.model.Producto;

/**
 * Entidad JPA que representa una línea de producto dentro de una compra a proveedor.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "detalle_compras")
public class DetalleCompra {

    /** Identificador único del detalle. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Compra a la que pertenece esta línea. */
    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = false)
    @JsonIgnore
    private Compra compra;

    /** Producto adquirido en esta línea. */
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /** Cantidad de unidades adquiridas. */
    @Column(nullable = false)
    private Integer cantidad;

    /** Precio unitario de compra. */
    @Column(nullable = false)
    private Double precioUnitario;

    /** Subtotal de la línea (cantidad × precio unitario). */
    @Column(nullable = false)
    private Double subtotal;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public DetalleCompra() {
    }

    /**
     * Obtiene el identificador del detalle.
     *
     * @return id del detalle
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del detalle.
     *
     * @param id id del detalle
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la compra asociada.
     *
     * @return compra del detalle
     */
    public Compra getCompra() {
        return compra;
    }

    /**
     * Establece la compra asociada.
     *
     * @param compra compra del detalle
     */
    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    /**
     * Obtiene el producto de la línea.
     *
     * @return producto adquirido
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece el producto de la línea.
     *
     * @param producto producto adquirido
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene la cantidad adquirida.
     *
     * @return cantidad de unidades
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad adquirida.
     *
     * @param cantidad cantidad de unidades
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario de compra.
     *
     * @return precio por unidad
     */
    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece el precio unitario de compra.
     *
     * @param precioUnitario precio por unidad
     */
    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * Obtiene el subtotal de la línea.
     *
     * @return subtotal calculado
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal de la línea.
     *
     * @param subtotal subtotal calculado
     */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
