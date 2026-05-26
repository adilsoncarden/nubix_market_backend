package com.nubix.market.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.enums.TipoEntrega;
import jakarta.persistence.*;

@Entity
@Table(name = "ventas_entrega")
public class VentaEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "venta_id", nullable = false, unique = true)
    @JsonIgnore
    private Venta venta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEntrega tipoEntrega;

    @Column(nullable = true)
    private String direccion;

    @Column(nullable = true)
    private String distrito;

    @Column(nullable = true)
    private String referencia;

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
