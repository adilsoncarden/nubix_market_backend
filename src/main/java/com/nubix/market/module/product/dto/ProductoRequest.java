package com.nubix.market.module.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductoRequest {

    @JsonProperty("categoria_id")
    private Integer categoriaId; // solo pedimos el ID de la categoría
    private String codigo;
    private String nombre;
    private String descripcion;
    @JsonProperty("precio_compra")
    private Double precioCompra;
    @JsonProperty("precio_venta")
    private Double precioVenta;
    private Integer stock;
    @JsonProperty("id_imagen")
    private Integer imagenId;

    // getters and setters

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

    public Integer getImagenId() {
        return imagenId;
    }

    public void setImagenId(Integer imagenId) {
        this.imagenId = imagenId;
    }
}
