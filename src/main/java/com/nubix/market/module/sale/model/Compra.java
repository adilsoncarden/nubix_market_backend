package com.nubix.market.module.sale.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.nubix.market.module.supplier.model.Proveedor;

/**
 * Entidad JPA que representa una compra de mercadería a un proveedor.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "compras")
public class Compra {

    /** Identificador único de la compra. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Número de factura del proveedor. */
    @Column(nullable = false)
    private String numeroFactura;

    /** Fecha y hora de registro de la compra. */
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

    /** Monto total de la compra. */
    @Column(nullable = false)
    private Double total;

    /** Proveedor que emitió la factura. */
    @ManyToOne
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    /** Líneas de producto incluidas en la compra. */
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL)
    private List<DetalleCompra> detalles = new ArrayList<>();

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Compra() {
    }

    /**
     * Obtiene el identificador de la compra.
     *
     * @return id de la compra
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador de la compra.
     *
     * @param id id de la compra
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el número de factura del proveedor.
     *
     * @return número de factura
     */
    public String getNumeroFactura() {
        return numeroFactura;
    }

    /**
     * Establece el número de factura del proveedor.
     *
     * @param numeroFactura número de factura
     */
    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    /**
     * Obtiene la fecha de la compra.
     *
     * @return fecha y hora de registro
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de la compra.
     *
     * @param fecha fecha y hora de registro
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el total de la compra.
     *
     * @return monto total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Establece el total de la compra.
     *
     * @param total monto total
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * Obtiene el proveedor de la compra.
     *
     * @return proveedor asociado
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * Establece el proveedor de la compra.
     *
     * @param proveedor proveedor asociado
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * Obtiene las líneas de la compra.
     *
     * @return lista de detalles
     */
    public List<DetalleCompra> getDetalles() {
        return detalles;
    }

    /**
     * Establece las líneas de la compra.
     *
     * @param detalles lista de detalles
     */
    public void setDetalles(List<DetalleCompra> detalles) {
        this.detalles = detalles;
    }
}
