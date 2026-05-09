package com.nubix.market.dto;

import java.util.List;

public class CompraRequest {
    private String numeroFactura;
    private Integer proveedorId;
    private List<DetalleCompraRequest> detalles;

    //Getters and Setters
    public String getNumeroFactura() {return numeroFactura;}
    public void setNumeroFactura(String numeroFactura) {this.numeroFactura = numeroFactura;}
    public Integer getProveedorId() {return proveedorId;}
    public void setProveedorId(Integer proveedorId) {this.proveedorId = proveedorId;}
    public List<DetalleCompraRequest> getDetalles() {return detalles;}
    public void setDetalles(List<DetalleCompraRequest> detalles) {this.detalles = detalles;}

    public static class DetalleCompraRequest {
        private String codigoProducto;
        private String nombreProducto;
        private String descripcion;
        private Integer categoriaId;
        private Integer cantidad;
        private Double precioCompra;

        //Getters and Setters
        public String getCodigoProducto() {return codigoProducto;}
        public void setCodigoProducto(String codigoProducto) {this.codigoProducto = codigoProducto;}
        public String getNombreProducto() {return nombreProducto;}
        public void setNombreProducto(String nombreProducto) {this.nombreProducto = nombreProducto;}
        public String getDescripcion() {return descripcion;}
        public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
        public Integer getCategoriaId() {return categoriaId;}
        public void setCategoriaId(Integer categoriaId) {this.categoriaId = categoriaId;}
        public Integer getCantidad() {return cantidad;}
        public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}
        public Double getPrecioCompra() {return precioCompra;}
        public void setPrecioCompra(Double precioCompra) {this.precioCompra = precioCompra;}
    }
}
