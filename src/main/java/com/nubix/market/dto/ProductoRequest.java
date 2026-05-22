package com.nubix.market.dto;


public class ProductoRequest {

private Integer categoriaId;  // solo pedimos el ID de la categoría
private String codigo;
private String nombre;
private Double precioCompra; 
private Double precioVenta;
private Integer stock;



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
}
