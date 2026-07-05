package com.nubix.market.module.product.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO de solicitud para crear o actualizar un producto.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class ProductoRequest {

    @JsonProperty("categoria_id")
    @JsonAlias("categoriaId")
    /** Identificador de categoría. */
    private Integer categoriaId;
    /** Código interno. */
    private String codigo;
    /** Nombre descriptivo. */
    private String nombre;
    /** Descripción detallada. */
    private String descripcion;
    @JsonProperty("precio_compra")
    @JsonAlias("precioCompra")
    /** Precio de compra. */
    private Double precioCompra;
    @JsonProperty("precio_venta")
    @JsonAlias("precioVenta")
    /** Precio de venta. */
    private Double precioVenta;
    /** Cantidad en inventario. */
    private Integer stock;
    @JsonProperty("url_imagen")
    @JsonAlias("urlImagen")
    /** URL de imagen del producto. */
    private String urlImagen;

    /**
     * GetCategoriaId.
     * @return resultado de la operación
     */
    public Integer getCategoriaId() {
        return categoriaId;
    }

    /**
     * SetCategoriaId.
     * @param categoriaId Identificador de categoría.
     */
    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    /**
     * GetCodigo.
     * @return resultado de la operación
     */
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
