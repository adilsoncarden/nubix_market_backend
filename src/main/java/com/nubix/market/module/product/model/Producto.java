package com.nubix.market.module.product.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.nubix.market.common.jackson.JsonViews;
import com.nubix.market.module.category.model.Categoria;
import jakarta.persistence.*;

/**
 * Entidad que representa un producto del catálogo.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador único. */
    private Integer id;

    @Column(nullable = false)
    /** Código interno. */
    private String codigo;

    @Column(nullable = false)
    /** Nombre descriptivo. */
    private String nombre;

    @Column(nullable = false)
    /** Descripción detallada. */
    private String descripcion;

    @Column(nullable = false)
    /** Precio de compra. */
    private Double precioCompra;

    @Column(nullable = false)
    /** Precio de venta. */
    private Double precioVenta;

    @Column(nullable = false)
    /** Cantidad en inventario. */
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    /** Campo categoria. */
    private Categoria categoria;

    @Column(name = "url_imagen")
    /** URL de imagen del producto. */
    private String urlImagen;

    public Producto() {
    }

    /**
     * Obtiene el identificador.
     * @return resultado de la operación
     */
    @JsonView(JsonViews.Detail.class)
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
     * GetCodigo.
     * @return resultado de la operación
     */
    @JsonView(JsonViews.Detail.class)
    public String getCodigo() {
        return codigo;
    }

    /**
     * SetCodigo.
     * @param codigo Código interno.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * GetNombre.
     * @return resultado de la operación
     */
    @JsonView(JsonViews.Detail.class)
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
     * GetDescripcion.
     * @return resultado de la operación
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * SetDescripcion.
     * @param descripcion Descripción detallada.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * GetPrecioCompra.
     * @return resultado de la operación
     */
    public Double getPrecioCompra() {
        return precioCompra;
    }

    /**
     * SetPrecioCompra.
     * @param precioCompra Precio de compra.
     */
    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    /**
     * GetPrecioVenta.
     * @return resultado de la operación
     */
    @JsonView(JsonViews.Detail.class)
    public Double getPrecioVenta() {
        return precioVenta;
    }

    /**
     * SetPrecioVenta.
     * @param precioVenta Precio de venta.
     */
    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    /**
     * GetStock.
     * @return resultado de la operación
     */
    public Integer getStock() {
        return stock;
    }

    /**
     * SetStock.
     * @param stock Cantidad en inventario.
     */
    public void setStock(Integer stock) {
        this.stock = stock;
    }

    /**
     * GetCategoria.
     * @return resultado de la operación
     */
    @JsonIgnore
    public Categoria getCategoria() {
        return categoria;
    }

    /**
     * SetCategoria.
     * @param categoria valor del parámetro
     */
    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    /**
     * GetUrlImagen.
     * @return resultado de la operación
     */
    public String getUrlImagen() {
        return urlImagen;
    }

    /**
     * SetUrlImagen.
     * @param urlImagen URL de imagen del producto.
     */
    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
