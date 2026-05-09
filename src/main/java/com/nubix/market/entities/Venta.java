package com.nubix.market.entities;

import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoEntrega;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "ventas")
public class Venta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    @Column(nullable = false)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEntrega tipoEntrega;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago;

    @Column(nullable = true)
    private String codigoRecojo;

    @Column(nullable = true)
    private String direccionEntrega;

    // Relación con Usuario (Cliente) rol CLIENTE (3)
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    // Relación con Usuario (Vendedor) rol VENDEDOR (1 o 2)
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario vendedor;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles = new ArrayList<>();

    public Venta() {
    }

    // Getters y Setters
    public Integer getId() {return id;}
    public void setId(Integer id) {this.id = id;}
    public LocalDate getFecha() {return fecha;}
    public void setFecha(LocalDate fecha) {this.fecha = fecha;}
    public Double getTotal() {return total;}
    public void setTotal(Double total) {this.total = total;}
    public MetodoPago getMetodoPago() {return metodoPago;}
    public void setMetodoPago(MetodoPago metodoPago) {this.metodoPago = metodoPago;}
    public TipoEntrega getTipoEntrega() {return tipoEntrega;}
    public void setTipoEntrega(TipoEntrega tipoEntrega) {this.tipoEntrega = tipoEntrega;}
    public EstadoPedido getEstadoPedido() {return estadoPedido;}
    public void setEstadoPedido(EstadoPedido estadoPedido) {this.estadoPedido = estadoPedido;}
    public EstadoPago getEstadoPago() {return estadoPago;}
    public void setEstadoPago(EstadoPago estadoPago) {this.estadoPago = estadoPago;}
    public String getCodigoRecojo() {return codigoRecojo;}
    public void setCodigoRecojo(String codigoRecojo) {this.codigoRecojo = codigoRecojo;}
    public String getDireccionEntrega() {return direccionEntrega;}
    public void setDireccionEntrega(String direccionEntrega) {this.direccionEntrega = direccionEntrega;}
    public Usuario getCliente() {return cliente;}
    public void setCliente(Usuario cliente) {this.cliente = cliente;}
    public Usuario getVendedor() {return vendedor;}
    public void setVendedor(Usuario vendedor) {this.vendedor = vendedor;}
    public List<DetalleVenta> getDetalles() {return detalles;}
    public void setDetalles(List<DetalleVenta> detalles) {this.detalles = detalles;}
}
