package com.nubix.market.module.sale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.nubix.market.common.jackson.JsonViews;
import com.nubix.market.module.product.model.Producto;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa una línea de producto dentro de una venta.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@JsonView(JsonViews.Detail.class)
@Entity
@Table(name = "venta_detalles")
public class DetalleVenta {

    /** Identificador único del detalle. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Venta a la que pertenece esta línea. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnore
    private Venta venta;

    /** Producto vendido en esta línea. */
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /** Cantidad de unidades vendidas. */
    @Column(nullable = false)
    private Integer cantidad;

    /** Precio unitario aplicado al momento de la venta. */
    @Column(nullable = false)
    private Double precioUnitario;

    /** Subtotal de la línea (cantidad × precio unitario). */
    @Column(nullable = false)
    private Double subtotal;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public DetalleVenta() {
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
     * Obtiene la venta asociada.
     *
     * @return venta del detalle
     */
    public Venta getVenta() {
        return venta;
    }

    /**
     * Establece la venta asociada.
     *
     * @param venta venta del detalle
     */
    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    /**
     * Obtiene el producto de la línea.
     *
     * @return producto vendido
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Establece el producto de la línea.
     *
     * @param producto producto vendido
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene la cantidad vendida.
     *
     * @return cantidad de unidades
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad vendida.
     *
     * @param cantidad cantidad de unidades
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el precio unitario.
     *
     * @return precio por unidad
     */
    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Establece el precio unitario.
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
