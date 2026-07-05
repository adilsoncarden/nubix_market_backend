package com.nubix.market.module.sale.dto;

import com.nubix.market.enums.CanalVenta;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import java.time.LocalDate;

/**
 * DTO de respuesta con el resumen de un pedido del cliente en el canal web.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class MisPedidoResponse {

    /** Identificador del pedido. */
    private Integer id;

    /** Fecha de registro de la venta. */
    private LocalDate fecha;

    /** Estado actual del pedido. */
    private EstadoPedido estado;

    /** Estado del pago asociado. */
    private EstadoPago estadoPago;

    /** Monto total de la venta. */
    private Double total;

    /** Modalidad de entrega del pedido. */
    private TipoEntrega tipoEntrega;

    /** Código de recojo para pedidos Fast Lane. */
    private String codigoRecojo;

    /** Canal por el que se realizó la venta. */
    private CanalVenta canal;

    /**
     * Obtiene el identificador del pedido.
     *
     * @return id del pedido
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del pedido.
     *
     * @param id id del pedido
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la fecha del pedido.
     *
     * @return fecha de la venta
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha del pedido.
     *
     * @param fecha fecha de la venta
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el estado del pedido.
     *
     * @return estado del pedido
     */
    public EstadoPedido getEstado() {
        return estado;
    }

    /**
     * Establece el estado del pedido.
     *
     * @param estado estado del pedido
     */
    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el estado del pago.
     *
     * @return estado del pago
     */
    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    /**
     * Establece el estado del pago.
     *
     * @param estadoPago estado del pago
     */
    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    /**
     * Obtiene el total del pedido.
     *
     * @return monto total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Establece el total del pedido.
     *
     * @param total monto total
     */
    public void setTotal(Double total) {
        this.total = total;
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
     * Obtiene el código de recojo.
     *
     * @return código de recojo o {@code null} si no aplica
     */
    public String getCodigoRecojo() {
        return codigoRecojo;
    }

    /**
     * Establece el código de recojo.
     *
     * @param codigoRecojo código de recojo
     */
    public void setCodigoRecojo(String codigoRecojo) {
        this.codigoRecojo = codigoRecojo;
    }

    /**
     * Obtiene el canal de venta.
     *
     * @return canal de la venta
     */
    public CanalVenta getCanal() {
        return canal;
    }

    /**
     * Establece el canal de venta.
     *
     * @param canal canal de la venta
     */
    public void setCanal(CanalVenta canal) {
        this.canal = canal;
    }
}
