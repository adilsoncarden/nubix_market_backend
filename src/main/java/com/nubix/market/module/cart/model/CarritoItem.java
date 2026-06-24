package com.nubix.market.module.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.module.product.model.Producto;
import jakarta.persistence.*;

/**
 * Entidad que representa un producto dentro del carrito.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "carrito_items")
public class CarritoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador único. */
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    @JsonIgnore
    /** Carrito contenedor. */
    private Carrito carrito;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    /** Producto asociado. */
    private Producto producto;

    @Column(nullable = false)
    /** Cantidad de unidades. */
    private Integer cantidad;

    /**
     * Obtiene el identificador.
     * @return resultado de la operación
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador.
     * @param id Identificador único.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * GetCarrito.
     * @return resultado de la operación
     */
    public Carrito getCarrito() {
        return carrito;
    }

    /**
     * SetCarrito.
     * @param carrito Carrito contenedor.
     */
    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
    }

    /**
     * GetProducto.
     * @return resultado de la operación
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * SetProducto.
     * @param producto Producto asociado.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
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
