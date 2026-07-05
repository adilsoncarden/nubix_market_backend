package com.nubix.market.module.sale.dto;

import com.nubix.market.enums.CanalVenta;
import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.enums.TipoEntrega;
import java.util.List;

/**
 * DTO de solicitud para registrar una venta presencial o como plantilla interna de checkout.
 * Contiene datos de cliente, comprobante, entrega, pago y líneas de producto.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class VentaRequest {

    /** Identificador del cliente registrado, si aplica. */
    private Integer clienteId;

    /** Identificador del vendedor que registra la venta. */
    private Integer vendedorId;

    /** Canal por el que se realiza la venta. */
    private CanalVenta canal;

    /** Tipo de comprobante fiscal emitido. */
    private TipoComprobante tipoComprobante;

    /** Método de pago utilizado. */
    private MetodoPago metodoPago;

    /** Modalidad de entrega del pedido. */
    private TipoEntrega tipoEntrega;

    /** Dirección de entrega para pedidos delivery. */
    private String direccionEntrega;

    /** Distrito de entrega. */
    private String distrito;

    /** Referencia adicional de la dirección. */
    private String referencia;

    /** Costo de envío informado. */
    private Double costoEnvio;

    /** Nombre del titular del comprobante. */
    private String nombreComprobante;

    /** DNI del titular para boleta. */
    private String dni;

    /** RUC del titular para factura. */
    private String ruc;

    /** Razón social para factura. */
    private String razonSocial;

    /** Correo para envío del comprobante. */
    private String emailComprobante;

    /** Dirección fiscal para factura. */
    private String direccionFiscal;

    /** Líneas de producto de la venta. */
    private List<DetalleVentaRequest> detalles;

    /**
     * Obtiene el identificador del cliente.
     *
     * @return id del cliente
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
     * Obtiene el identificador del vendedor.
     *
     * @return id del vendedor
     */
    public Integer getVendedorId() {
        return vendedorId;
    }

    /**
     * Establece el identificador del vendedor.
     *
     * @param vendedorId id del vendedor
     */
    public void setVendedorId(Integer vendedorId) {
        this.vendedorId = vendedorId;
    }

    /**
     * Obtiene el canal de venta.
     *
     * @return canal de venta
     */
    public CanalVenta getCanal() {
        return canal;
    }

    /**
     * Establece el canal de venta.
     *
     * @param canal canal de venta
     */
    public void setCanal(CanalVenta canal) {
        this.canal = canal;
    }

    /**
     * Obtiene el tipo de comprobante.
     *
     * @return tipo de comprobante
     */
    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Establece el tipo de comprobante.
     *
     * @param tipoComprobante tipo de comprobante
     */
    public void setTipoComprobante(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    /**
     * Obtiene el método de pago.
     *
     * @return método de pago
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
     * Obtiene el costo de envío.
     *
     * @return costo de envío
     */
    public Double getCostoEnvio() {
        return costoEnvio;
    }

    /**
     * Establece el costo de envío.
     *
     * @param costoEnvio costo de envío
     */
    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    /**
     * Obtiene el nombre del comprobante.
     *
     * @return nombre del titular
     */
    public String getNombreComprobante() {
        return nombreComprobante;
    }

    /**
     * Establece el nombre del comprobante.
     *
     * @param nombreComprobante nombre del titular
     */
    public void setNombreComprobante(String nombreComprobante) {
        this.nombreComprobante = nombreComprobante;
    }

    /**
     * Obtiene el DNI del titular.
     *
     * @return DNI
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del titular.
     *
     * @param dni DNI
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el RUC del titular.
     *
     * @return RUC
     */
    public String getRuc() {
        return ruc;
    }

    /**
     * Establece el RUC del titular.
     *
     * @param ruc RUC
     */
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    /**
     * Obtiene la razón social.
     *
     * @return razón social
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Establece la razón social.
     *
     * @param razonSocial razón social
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * Obtiene el email del comprobante.
     *
     * @return correo electrónico
     */
    public String getEmailComprobante() {
        return emailComprobante;
    }

    /**
     * Establece el email del comprobante.
     *
     * @param emailComprobante correo electrónico
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
     * Obtiene las líneas de producto.
     *
     * @return lista de detalles de venta
     */
    public List<DetalleVentaRequest> getDetalles() {
        return detalles;
    }

    /**
     * Establece las líneas de producto.
     *
     * @param detalles lista de detalles de venta
     */
    public void setDetalles(List<DetalleVentaRequest> detalles) {
        this.detalles = detalles;
    }

    /**
     * Línea de producto incluida en una solicitud de venta.
     *
     * @author Grupo de Desarrollo Nubix Market
     * @version 1.0.0 (2026)
     */
    public static class DetalleVentaRequest {

        /** Identificador del producto vendido. */
        private Integer productoId;

        /** Cantidad de unidades solicitadas. */
        private Integer cantidad;

        /**
         * Obtiene el identificador del producto.
         *
         * @return id del producto
         */
        public Integer getProductoId() {
            return productoId;
        }

        /**
         * Establece el identificador del producto.
         *
         * @param productoId id del producto
         */
        public void setProductoId(Integer productoId) {
            this.productoId = productoId;
        }

        /**
         * Obtiene la cantidad solicitada.
         *
         * @return cantidad de unidades
         */
        public Integer getCantidad() {
            return cantidad;
        }

        /**
         * Establece la cantidad solicitada.
         *
         * @param cantidad cantidad de unidades
         */
        public void setCantidad(Integer cantidad) {
            this.cantidad = cantidad;
        }
    }
}
