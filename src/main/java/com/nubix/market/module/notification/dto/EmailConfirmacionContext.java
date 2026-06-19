package com.nubix.market.module.notification.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de contexto (DTO) utilizado para agrupar todas las variables necesarias 
 * para poblar la plantilla HTML del correo electrónico de confirmación de compra.
 * Contiene el resumen financiero, el código de recojo (Fast Lane) y los datos del cliente.
 */
public class EmailConfirmacionContext {

    /** Correo electrónico del cliente destinatario. */
    private String email;

    /** Número identificador único de la venta o pedido. */
    private String numero;

    /** Tipo de comprobante emitido (ej. BOLETA, FACTURA, TICKET). */
    private String tipoComprobante;

    /** Código de seguridad generado para el recojo rápido en tienda (Fast Lane). */
    private String codigoRecojo;

    /** Monto subtotal de la compra sin impuestos. */
    private Double subtotal;

    /** Monto calculado de impuestos (IGV). */
    private Double igv;

    /** Costo adicional por servicio de delivery (si aplica). */
    private Double costoEnvio;

    /** Monto total final pagado por el cliente. */
    private Double total;

    /** Lista detallada de los productos comprados para mostrar en la tabla del correo. */
    private List<EmailProductoLinea> productos = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
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
