package com.nubix.market.module.sale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.enums.TipoEntrega;
import jakarta.persistence.*;

/**
 * Entidad JPA que encapsula la información logística de una venta.
 * Almacena dónde y cómo se debe hacer llegar el pedido al cliente.
 */
@Entity
@Table(name = "ventas_entrega")
public class VentaEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Relación uno a uno obligatoria con la venta principal. */
    @OneToOne
    @JoinColumn(name = "venta_id", nullable = false, unique = true)
    @JsonIgnore
    private Venta venta;

    /** Modalidad elegida (ej. DELIVERY_MOTO, DELIVERY_AUTO, RECOJO_EN_TIENDA). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEntrega tipoEntrega;

    @Column(nullable = true)
    private String direccion;

    @Column(nullable = true)
    private String distrito;

    @Column(nullable = true)
    private String referencia;

    /** Código auto-generado que el cliente debe mostrar en caja para retirar su pedido. */
    @Column(nullable = true)
    private String codigoRecojo;

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

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getCodigoRecojo() {
        return codigoRecojo;
    }

    public void setCodigoRecojo(String codigoRecojo) {
        this.codigoRecojo = codigoRecojo;
    }
}
