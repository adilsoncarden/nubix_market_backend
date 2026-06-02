package com.nubix.market.module.product.dto;

public class ProductoPublicResponse {
    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Double precioVenta;
    private Integer stock;
    private String categoriaNombre;
    private ProductoResponse.ImagenDTO imagen;

    public ProductoPublicResponse(Integer id, String codigo, String nombre, String descripcion,
            Double precioVenta, Integer stock, String categoriaNombre) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.categoriaNombre = categoriaNombre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    public ProductoResponse.ImagenDTO getImagen() {
        return imagen;
    }

    public void setImagen(ProductoResponse.ImagenDTO imagen) {
        this.imagen = imagen;
    }
}
