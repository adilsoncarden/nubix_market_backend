package com.nubix.market.module.notification.dto;

import java.util.ArrayList;
import java.util.List;

public class EmailConfirmacionContext {

    private String email;
    private String numero;
    private String tipoComprobante;
    private String codigoRecojo;
    private Double subtotal;
    private Double igv;
    private Double costoEnvio;
    private Double total;
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
