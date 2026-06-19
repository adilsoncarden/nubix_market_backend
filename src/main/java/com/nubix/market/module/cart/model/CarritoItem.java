package com.nubix.market.module.cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.module.product.model.Producto;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa una línea específica o ítem dentro de un carrito de compras.
 * Conecta el carrito general con un producto específico del catálogo e indica la cantidad deseada.
 */
@Entity
@Table(name = "carrito_items")
public class CarritoItem {

    /** Identificador único autoincremental de la línea del carrito. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** * Referencia al carrito padre al que pertenece este ítem. 
     * Se ignora en el JSON de respuesta para evitar ciclos infinitos en el frontend.
     */
    @ManyToOne
    @JoinColumn(name = "carrito_id", nullable = false)
    @JsonIgnore
    private Carrito carrito;

    /** Producto específico del catálogo seleccionado por el cliente. */
    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    /** Cantidad solicitada de este producto en el carrito. */
    @Column(nullable = false)
    private Integer cantidad;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Carrito getCarrito() {
        return carrito;
    }

    public void setCarrito(Carrito carrito) {
        this.carrito = carrito;
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
}
