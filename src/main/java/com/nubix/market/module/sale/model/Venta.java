package com.nubix.market.module.sale.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.nubix.market.common.jackson.JsonViews;
import com.nubix.market.enums.CanalVenta;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.module.user.model.Usuario;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JsonView(JsonViews.List.class)
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

    @Column(nullable = true)
    private Double subtotal = 0.0;

    @Column(nullable = true)
    private Double igv = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalVenta canal = CanalVenta.PRESENCIAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoComprobante tipoComprobante = TipoComprobante.TICKET;

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
    @Column(name = "estado_pago", nullable = false, length = 20)
    private EstadoPago estadoPago;

    @Column(nullable = true)
    private String codigoRecojo;

    @Column(nullable = true)
    private String direccionEntrega;

    @Column(nullable = true)
    private Double costoEnvio = 0.0;

    @Column(nullable = true, length = 150)
    private String nombreComprobante;

    @Column(nullable = true, length = 8)
    private String dni;

    @Column(nullable = true, length = 11)
    private String ruc;

    @Column(nullable = true, length = 200)
    private String razonSocial;

    @Column(nullable = true, length = 100)
    private String emailComprobante;

    @Column(nullable = true, length = 255)
    private String direccionFiscal;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = true)
    @JsonIgnoreProperties({ "venta", "hibernateLazyInitializer", "handler", "password", "rol" })
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    @JsonIgnoreProperties({ "venta", "hibernateLazyInitializer", "handler", "password", "rol" })
    private Usuario vendedor;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "venta" })
    @JsonIgnore
    private List<DetalleVenta> detalles = new ArrayList<>();

    @OneToOne(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "venta" })
    private VentaEntrega entrega;

    @OneToOne(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "venta" })
    private Pago pago;

    public Venta() {
    }

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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getIgv() {
        return igv;
    }

    public void setIgv(Double igv) {
        this.igv = igv;
    }

    public CanalVenta getCanal() {
        return canal;
    }

    public void setCanal(CanalVenta canal) {
        this.canal = canal;
    }

    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }

    public void setTipoComprobante(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public TipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    public void setTipoEntrega(TipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public EstadoPago getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPago estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getCodigoRecojo() {
        return codigoRecojo;
    }

    public void setCodigoRecojo(String codigoRecojo) {
        this.codigoRecojo = codigoRecojo;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public Double getCostoEnvio() {
        return costoEnvio;
    }

    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    public String getNombreComprobante() {
        return nombreComprobante;
    }

    public void setNombreComprobante(String nombreComprobante) {
        this.nombreComprobante = nombreComprobante;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getEmailComprobante() {
        return emailComprobante;
    }

    public void setEmailComprobante(String emailComprobante) {
        this.emailComprobante = emailComprobante;
    }

    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }

    public Usuario getCliente() {
        return cliente;
    }

    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    @JsonProperty("detalles")
    @JsonView(JsonViews.Detail.class)
    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    public VentaEntrega getEntrega() {
        return entrega;
    }

    public void setEntrega(VentaEntrega entrega) {
        this.entrega = entrega;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }
}
