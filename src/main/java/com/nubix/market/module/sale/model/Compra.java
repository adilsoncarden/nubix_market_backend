package com.nubix.market.module.sale.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.nubix.market.module.supplier.model.Proveedor;

/**
 * Entidad JPA que representa una transacción de abastecimiento o ingreso de mercadería.
 * Registra la compra de productos al por mayor a un proveedor específico para aumentar el stock.
 */
@Entity
@Table(name = "compras")
public class Compra {

    /** Identificador único autoincremental de la orden de compra. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Número de factura física o electrónica entregada por el proveedor. */
    @Column(nullable = false)
    private String numeroFactura;

    /** Fecha y hora exacta en la que se registró el ingreso de mercadería. */
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    /** Costo total de la orden de compra. */
    @Column(nullable = false)
    private Double total;

    /** Relación con la entidad Proveedor que suministró los productos. */
    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    /** * Lista de los productos específicos adquiridos en esta compra. 
     * CascadeType.ALL asegura que al guardar la compra, se guarden automáticamente sus detalles.
     */
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<DetalleCompra> detalles = new ArrayList<>();

    public Compra() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }
}
