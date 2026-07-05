package com.nubix.market.module.notification.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Contexto con datos para renderizar el email de confirmación de compra.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class EmailConfirmacionContext {

    /** Correo electrónico. */
    private String email;
    /** Número de pedido o comprobante. */
    private String numero;
    /** Etiqueta del tipo de comprobante. */
    private String tipoComprobante;
    /** Código de recojo en tienda. */
    private String codigoRecojo;
    /** Subtotal sin impuestos. */
    private Double subtotal;
    /** Monto del IGV. */
    private Double igv;
    /** Costo de envío. */
    private Double costoEnvio;
    /** Monto total. */
    private Double total;
    /** Productos del pedido. */
    private List<EmailProductoLinea> productos = new ArrayList<>();

    /**
     * Obtiene el correo electrónico.
     * @return resultado de la operación
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico.
     * @param email Correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * GetNumero.
     * @return resultado de la operación
     */
    public String getNumero() {
        return numero;
    }

    /**
     * SetNumero.
     * @param numero Número de pedido o comprobante.
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * GetTipoComprobante.
     * @return resultado de la operación
     */
    public String getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * SetTipoComprobante.
     * @param tipoComprobante Etiqueta del tipo de comprobante.
     */
    public void setTipoComprobante(String tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    /**
     * GetCodigoRecojo.
     * @return resultado de la operación
     */
    public String getCodigoRecojo() {
        return codigoRecojo;
    }

    /**
     * SetCodigoRecojo.
     * @param codigoRecojo Código de recojo en tienda.
     */
    public void setCodigoRecojo(String codigoRecojo) {
        this.codigoRecojo = codigoRecojo;
    }

    /**
     * GetSubtotal.
     * @return resultado de la operación
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * SetSubtotal.
     * @param subtotal Subtotal sin impuestos.
     */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * GetIgv.
     * @return resultado de la operación
     */
    public Double getIgv() {
        return igv;
    }

    /**
     * SetIgv.
     * @param igv Monto del IGV.
     */
    public void setIgv(Double igv) {
        this.igv = igv;
    }

    /**
     * GetCostoEnvio.
     * @return resultado de la operación
     */
    public Double getCostoEnvio() {
        return costoEnvio;
    }

    /**
     * SetCostoEnvio.
     * @param costoEnvio Costo de envío.
     */
    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    /**
     * GetTotal.
     * @return resultado de la operación
     */
    public Double getTotal() {
        return total;
    }

    /**
     * SetTotal.
     * @param total Monto total.
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * GetProductos.
     * @return resultado de la operación
     */
    public List<EmailProductoLinea> getProductos() {
        return productos;
    }

    /**
     * SetProductos.
     * @param productos Productos del pedido.
     */
    public void setProductos(List<EmailProductoLinea> productos) {
        this.productos = productos != null ? productos : new ArrayList<>();
    }
}
