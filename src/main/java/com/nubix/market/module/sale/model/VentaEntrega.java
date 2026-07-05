package com.nubix.market.module.sale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.enums.TipoEntrega;
import jakarta.persistence.*;

/**
 * Entidad JPA con los datos de entrega asociados a una venta.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "ventas_entrega")
public class VentaEntrega {

    /** Identificador único del registro de entrega. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Venta a la que pertenece esta entrega. */
    @OneToOne
    @JoinColumn(name = "venta_id", nullable = false, unique = true)
    @JsonIgnore
    private Venta venta;

    /** Modalidad de entrega del pedido. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEntrega tipoEntrega;

    /** Dirección de entrega para pedidos delivery. */
    @Column(nullable = true)
    private String direccion;

    /** Distrito de entrega. */
    @Column(nullable = true)
    private String distrito;

    /** Referencia adicional de la dirección. */
    @Column(nullable = true)
    private String referencia;

    /** Código de recojo para pedidos Fast Lane. */
    @Column(nullable = true)
    private String codigoRecojo;

    /**
     * Obtiene el identificador del registro de entrega.
     *
     * @return id de la entrega
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador del registro de entrega.
     *
     * @param id id de la entrega
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la venta asociada.
     *
     * @return venta de la entrega
     */
    public Venta getVenta() {
        return venta;
    }

    /**
     * Establece la venta asociada.
     *
     * @param venta venta de la entrega
     */
    public void setVenta(Venta venta) {
        this.venta = venta;
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
     * @return dirección
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de entrega.
     *
     * @param direccion dirección
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
}
