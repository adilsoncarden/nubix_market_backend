package com.nubix.market.dto;

import java.util.List;

public class ProductoResponse {
    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Double precioCompra;
    private Double precioVenta;
    private Integer stock;
    private String categoriaNombre;
    private List<String> imagenes;

    public ProductoResponse(Integer id, String codigo, String nombre, String descripcion, Double precioCompra, Double precioVenta, Integer stock, String categoriaNombre, List<String> imagenes) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
        this.stock = stock;
        this.categoriaNombre = categoriaNombre;
        this.imagenes = imagenes;
    }

    // Getters y Setters
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}
    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}
    public String getDescripcion() {return descripcion;}   
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public Double getPrecioCompra() {return precioCompra;}
    public void setPrecioCompra(Double precioCompra) {this.precioCompra = precioCompra;}
    public Double getPrecioVenta() {return precioVenta;}
    public void setPrecioVenta(Double precioVenta) {this.precioVenta = precioVenta;}
    public Integer getStock() {return stock;}
    public void setStock(Integer stock) {this.stock = stock;}
    public String getCategoriaNombre() {return categoriaNombre;}
    public void setCategoriaNombre(String categoriaNombre) {this.categoriaNombre = categoriaNombre;}
    public List<String> getImagenes() {return imagenes;}
    public void setImagenes(List<String> imagenes) {this.imagenes = imagenes;}

    // Sub-DTO para imágenes
    public static class ImagenDTO {
        private Integer id;
        private String url;

        public ImagenDTO(Integer id, String url) {
            this.id = id;
            this.url = url;
        }

        public Integer getId() {return id;}
        public String getUrl() {return url;}
    }
}