package com.nubix.market.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String numeroFactura;

    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    @Column(nullable = false)
    private Double total;

    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<DetalleCompra> detalles = new ArrayList<>();

    public Compra() {
    }

    //Getters and Setters
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public String getNumeroFactura() {return numeroFactura;}
    public void setNumeroFactura(String numeroFactura) {this.numeroFactura = numeroFactura;}
    public LocalDateTime getFecha() {return fecha;}
    public void setFecha(LocalDateTime fecha) {this.fecha = fecha;}
    public Double getTotal() {return total;}
    public void setTotal(Double total) {this.total = total;}
    public Proveedor getProveedor() {return proveedor;}
    public void setProveedor(Proveedor proveedor) {this.proveedor = proveedor;}
    public List<DetalleCompra> getDetalles() {return detalles;}
    public void setDetalles(List<DetalleCompra> detalles) {this.detalles = detalles;}
}
