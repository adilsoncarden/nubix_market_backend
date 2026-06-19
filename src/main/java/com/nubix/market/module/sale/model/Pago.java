package com.nubix.market.module.sale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.MetodoPago;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA encargada de registrar la trazabilidad financiera de una venta.
 * Desacopla la transacción de pago (tarjeta, efectivo, yape) de la transacción logística (la entrega).
 */
@Entity
@Table(name = "pagos")
public class Pago {

    /** Identificador único autoincremental del registro de pago. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Relación uno a uno con la venta. Cada venta tiene un único registro de pago oficial. */
    @OneToOne
    @JoinColumn(name = "venta_id", nullable = false, unique = true)
    @JsonIgnore
    private Venta venta;

    /** Vía por la cual se recibió el dinero (ej. TARJETA, EFECTIVO, YAPE). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    /** Estado actual de la transacción (ej. PAGADO, PENDIENTE, RECHAZADO). */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false, length = 20)
    private EstadoPago estadoPago;

    /** Monto monetario exacto procesado en esta transacción. */
    @Column(nullable = false)
    private Double monto;

    /** Fecha y hora exacta de la confirmación del pago. */
    @Column(nullable = false)
    private LocalDateTime fechaPago = LocalDateTime.now();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }
}
