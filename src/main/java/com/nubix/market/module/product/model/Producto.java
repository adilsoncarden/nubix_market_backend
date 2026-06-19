package com.nubix.market.module.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.nubix.market.common.jackson.JsonViews;
import com.nubix.market.module.category.model.Categoria;
import jakarta.persistence.*;

/**
 * Entidad JPA principal que representa un ítem comercializable dentro del catálogo de la tienda.
 */
@Entity
@Table(name = "productos")
public class Producto {

    /** Identificador único autoincremental del producto. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Código de barras o SKU identificador interno único del producto. */
    @Column(nullable = false)
    private String codigo;

    /** Nombre comercial del producto. */
    @Column(nullable = false)
    private String nombre;

    /** Descripción detallada o características del producto. */
    @Column(nullable = false)
    private String descripcion;

    /** Costo interno de adquisición (lo que le cuesta a la tienda comprarlo). */
    @Column(nullable = false)
    private Double precioCompra;

    /** Precio final de venta al público (PVP). */
    @Column(nullable = false)
    private Double precioVenta;

    /** Cantidad de unidades físicas disponibles actualmente en el almacén. */
    @Column(nullable = false)
    private Integer stock;

    /** * Relación con la clasificación del producto. 
     * FetchType.LAZY mejora el rendimiento evitando cargar la categoría si no es solicitada.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    /** URL o ruta hacia la fotografía principal del producto. */
    @Column(name = "url_imagen")
    private String urlImagen;

    public Producto() {
    }

    @JsonView(JsonViews.Detail.class)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonView(JsonViews.Detail.class)
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    @JsonView(JsonViews.Detail.class)
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    @JsonView(JsonViews.Detail.class)
    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @JsonIgnore
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
