package com.nubix.market.module.product.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Objeto de Transferencia de Datos (DTO) utilizado para recibir la información 
 * de creación o actualización de un producto desde el panel administrativo.
 * Utiliza anotaciones de Jackson para mapear correctamente las variables 
 * independientemente de si el frontend las envía en formato snake_case o camelCase.
 */
public class ProductoRequest {

    /** Mapea el ID de la categoría (acepta 'categoria_id' o 'categoriaId' en el JSON). */
    @JsonProperty("categoria_id")
    @JsonAlias("categoriaId")
    private Integer categoriaId;

    private String codigo;
    private String nombre;
    private String descripcion;

    /** Mapea el costo interno del producto. */
    @JsonProperty("precio_compra")
    @JsonAlias("precioCompra")
    private Double precioCompra;

    /** Mapea el precio final al consumidor. */
    @JsonProperty("precio_venta")
    @JsonAlias("precioVenta")
    private Double precioVenta;

    private Integer stock;

    /** Mapea la ruta o URL de la fotografía del producto. */
    @JsonProperty("url_imagen")
    @JsonAlias("urlImagen")
    private String urlImagen;

    public Integer getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Integer categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

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

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }
}
