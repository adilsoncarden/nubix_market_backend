package com.nubix.market.module.auth.dto;

import com.nubix.market.module.notification.dto.EmailProductoLinea;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO utilizado para transportar todos los datos necesarios para construir 
 * y enviar un correo electrónico de confirmación de compra al cliente.
 * Agrupa la información de contacto, los detalles financieros de la venta y 
 * la lista de productos adquiridos.
 */
public class EmailConfirmacionRequest {

    private String email;
    private Integer ventaId;
    private String numero;
    private String tipo;
    private String codigoRecojo;

    /** Subtotal de la compra antes de aplicar impuestos. */
    private Double subtotal;

    /** Monto correspondiente al Impuesto General a las Ventas (IGV). */
    private Double igv;

    /** Costo adicional en caso de que el tipo de entrega sea por Delivery. */
    private Double costoEnvio;

    /** Monto final total pagado por el cliente. */
    private Double total;
    
    /** Lista de productos que componen el pedido. */
    private List<EmailProductoLinea> productos = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getVentaId() {
        return ventaId;
    }

    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCodigoRecojo() {
        return codigoRecojo;
    }

    public void setCodigoRecojo(String codigoRecojo) {
        this.codigoRecojo = codigoRecojo;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public Double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public List<EmailProductoLinea> getProductos() {
        return productos;
    }

    public void setProductos(List<EmailProductoLinea> productos) {
        this.productos = productos != null ? productos : new ArrayList<>();
    }
}
