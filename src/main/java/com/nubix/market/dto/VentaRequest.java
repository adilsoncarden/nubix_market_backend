package com.nubix.market.dto;

import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoEntrega;
import java.util.List;

public class VentaRequest {
    private Integer clienteId;
    private Integer vendedorId;
    private MetodoPago metodoPago;
    private TipoEntrega tipoEntrega;
    private String direccionEntrega;
    private List<DetalleVentaRequest> detalles;

    // Getters y Setters
    public Integer getClienteId() {return clienteId;}
    public void setClienteId(Integer clienteId) {this.clienteId = clienteId;}
    public Integer getVendedorId() {return vendedorId;}
    public void setVendedorId(Integer vendedorId) {this.vendedorId = vendedorId;}
    public MetodoPago getMetodoPago() {return metodoPago;}
    public void setMetodoPago(MetodoPago metodoPago) {this.metodoPago = metodoPago;}
    public TipoEntrega getTipoEntrega() {return tipoEntrega;}
    public void setTipoEntrega(TipoEntrega tipoEntrega) {this.tipoEntrega = tipoEntrega;}
    public String getDireccionEntrega() {return direccionEntrega;}
    public void setDireccionEntrega(String direccionEntrega) {this.direccionEntrega = direccionEntrega;}
    public List<DetalleVentaRequest> getDetalles() {return detalles;}
    public void setDetalles(List<DetalleVentaRequest> detalles) {this.detalles = detalles;}

    public static class DetalleVentaRequest {
        private Integer productoId;
        private Integer cantidad;

        // Getters y Setters
        public Integer getProductoId() {return productoId;}
        public void setProductoId(Integer productoId) {this.productoId = productoId;}
        public Integer getCantidad() {return cantidad;}
        public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}
    }
}
