package com.nubix.market.module.sale.dto;

import com.nubix.market.enums.CanalVenta;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import java.time.LocalDate;

/**
 * Objeto de Transferencia de Datos (DTO) utilizado para renderizar la tabla de 
 * "Historial de Compras" o "Mis Pedidos" en el perfil de un cliente en la tienda web.
 * Contiene un resumen de la transacción sin sobrecargar la red con toda la lista de productos.
 */
public class MisPedidoResponse {

    private Integer id;
    private LocalDate fecha;
    private EstadoPedido estado;
    private EstadoPago estadoPago;
    private Double total;
    private TipoEntrega tipoEntrega;

    /** Código secreto (Fast Lane) para retirar en tienda, si aplica. */
    private String codigoRecojo;
    private CanalVenta canal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido estado) {
        this.estado = estado;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public String getCodigoRecojo() {
        return codigoRecojo;
    }

    public void setCodigoRecojo(String codigoRecojo) {
        this.codigoRecojo = codigoRecojo;
    }

    public CanalVenta getCanal() {
        return canal;
    }

    public void setCanal(CanalVenta canal) {
        this.canal = canal;
    }
}
