package com.nubix.market.module.sale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.MetodoPago;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa el pago asociado a una venta.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "pagos")
public class Pago {

    /** Identificador único del pago. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Venta a la que pertenece este pago. */
    @OneToOne
    @JoinColumn(name = "venta_id", nullable = false, unique = true)
    @JsonIgnore
    private Venta venta;

    /** Método de pago utilizado. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    /** Estado actual del pago. */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false, length = 20)
    private EstadoPago estadoPago;

    /** Monto pagado o pendiente de cobro. */
    @Column(nullable = false)
    private Double monto;

    /** Fecha y hora en que se registró el pago. */
    @Column(nullable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    /**
     * Obtiene el identificador del pago.
     *
     * @return id del pago
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del pago.
     *
     * @param id id del pago
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la venta asociada.
     *
     * @return venta del pago
     */
    public Venta getVenta() {
        return venta;
    }

    /**
     * Establece la venta asociada.
     *
     * @param venta venta del pago
     */
    public void setVenta(Venta venta) {
        this.venta = venta;
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
     * Obtiene el monto del pago.
     *
     * @return monto pagado o pendiente
     */
    public Double getMonto() {
        return monto;
    }

    /**
     * Establece el monto del pago.
     *
     * @param monto monto pagado o pendiente
     */
    public void setMonto(Double monto) {
        this.monto = monto;
    }

    /**
     * Obtiene la fecha de registro del pago.
     *
     * @return fecha y hora del pago
     */
    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    /**
     * Establece la fecha de registro del pago.
     *
     * @param fechaPago fecha y hora del pago
     */
    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }
}
