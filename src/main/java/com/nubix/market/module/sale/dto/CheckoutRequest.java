package com.nubix.market.module.sale.dto;

import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.module.sale.dto.VentaRequest.DetalleVentaRequest;
import java.util.List;

/**
 * DTO de solicitud para el checkout de un pedido web.
 * Agrupa datos de comprobante, entrega, pago y líneas de producto del carrito.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class CheckoutRequest {

    /** Identificador del cliente; se asigna automáticamente si el usuario autenticado es CLIENTE. */
    private Integer clienteId;

    /** Tipo de comprobante fiscal (boleta, factura, etc.). */
    private TipoComprobante tipoComprobante;

    /** Método de pago seleccionado en el checkout. */
    private MetodoPago metodoPago;

    /** Modalidad de entrega del pedido (Fast Lane, delivery, presencial). */
    private TipoEntrega tipoEntrega;

    /** Nombre del titular del comprobante para ventas sin cliente registrado. */
    private String nombreComprobante;

    /** DNI de 8 dígitos requerido para boleta sin cliente registrado. */
    private String dni;

    /** RUC de 11 dígitos requerido para factura. */
    private String ruc;

    /** Razón social del titular para factura. */
    private String razonSocial;

    /** Correo electrónico para envío del comprobante. */
    private String emailComprobante;

    /** Dirección fiscal del titular para factura. */
    private String direccionFiscal;

    /** Dirección de entrega obligatoria cuando el tipo es delivery. */
    private String direccionEntrega;

    /** Distrito de entrega para pedidos delivery. */
    private String distrito;

    /** Referencia adicional de la dirección de entrega. */
    private String referencia;

    /** Costo de envío informado (el servicio puede recalcularlo). */
    private Double costoEnvio;

    /** Líneas de producto incluidas en el checkout. */
    private List<DetalleVentaRequest> detalles;

    /**
     * Obtiene el identificador del cliente.
     *
     * @return id del cliente o {@code null} si no está definido
     */
    public Integer getClienteId() {
        return clienteId;
    }

    /**
     * Establece el identificador del cliente.
     *
     * @param clienteId id del cliente
     */
    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    /**
     * Obtiene el tipo de comprobante solicitado.
     *
     * @return tipo de comprobante
     */
    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Establece el tipo de comprobante solicitado.
     *
     * @param tipoComprobante tipo de comprobante
     */
    public void setTipoComprobante(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    /**
     * Obtiene el método de pago.
     *
     * @return método de pago seleccionado
     */
    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    /**
     * Establece el método de pago.
     *
     * @param metodoPago método de pago
     */
    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    /**
     * Obtiene el tipo de entrega.
     *
     * @return modalidad de entrega
     */
    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    /**
     * Establece el tipo de entrega.
     *
     * @param tipoEntrega modalidad de entrega
     */
    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    /**
     * Obtiene el nombre del titular del comprobante.
     *
     * @return nombre del comprobante
     */
    public String getNombreComprobante() {
        return nombreComprobante;
    }

    /**
     * Establece el nombre del titular del comprobante.
     *
     * @param nombreComprobante nombre del comprobante
     */
    public void setNombreComprobante(String nombreComprobante) {
        this.nombreComprobante = nombreComprobante;
    }

    /**
     * Obtiene el DNI del titular.
     *
     * @return DNI de 8 dígitos
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del titular.
     *
     * @param dni DNI de 8 dígitos
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el RUC del titular.
     *
     * @return RUC de 11 dígitos
     */
    public String getRuc() {
        return ruc;
    }

    /**
     * Establece el RUC del titular.
     *
     * @param ruc RUC de 11 dígitos
     */
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    /**
     * Obtiene la razón social del titular.
     *
     * @return razón social
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Establece la razón social del titular.
     *
     * @param razonSocial razón social
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * Obtiene el correo para el comprobante.
     *
     * @return email del comprobante
     */
    public String getEmailComprobante() {
        return emailComprobante;
    }

    /**
     * Establece el correo para el comprobante.
     *
     * @param emailComprobante email del comprobante
     */
    public void setEmailComprobante(String emailComprobante) {
        this.emailComprobante = emailComprobante;
    }

    /**
     * Obtiene la dirección fiscal.
     *
     * @return dirección fiscal
     */
    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    /**
     * Establece la dirección fiscal.
     *
     * @param direccionFiscal dirección fiscal
     */
    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }

    /**
     * Obtiene la dirección de entrega.
     *
     * @return dirección de entrega
     */
    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    /**
     * Establece la dirección de entrega.
     *
     * @param direccionEntrega dirección de entrega
     */
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    /**
     * Obtiene el distrito de entrega.
     *
     * @return distrito
     */
    public String getDistrito() {
        return distrito;
    }

    /**
     * Establece el distrito de entrega.
     *
     * @param distrito distrito
     */
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    /**
     * Obtiene la referencia de entrega.
     *
     * @return referencia de la dirección
     */
    public String getReferencia() {
        return referencia;
    }

    /**
     * Establece la referencia de entrega.
     *
     * @param referencia referencia de la dirección
     */
    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    /**
     * Obtiene el costo de envío informado.
     *
     * @return costo de envío
     */
    public Double getCostoEnvio() {
        return costoEnvio;
    }

    /**
     * Establece el costo de envío informado.
     *
     * @param costoEnvio costo de envío
     */
    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    /**
     * Obtiene las líneas de producto del checkout.
     *
     * @return lista de detalles de venta
     */
    public List<DetalleVentaRequest> getDetalles() {
        return detalles;
    }

    /**
     * Establece las líneas de producto del checkout.
     *
     * @param detalles lista de detalles de venta
     */
    public void setDetalles(List<DetalleVentaRequest> detalles) {
        this.detalles = detalles;
    }
}
