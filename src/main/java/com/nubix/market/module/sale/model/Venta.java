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

/**
 * Entidad JPA que representa una venta o pedido en Nubix Market.
 * Agrupa información financiera, comprobante fiscal, entrega, pago y líneas de producto.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@JsonView(JsonViews.List.class)
@Entity
@Table(name = "ventas")
public class Venta {

    /** Identificador único de la venta. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Fecha de registro de la venta. */
    @Column(nullable = false)
    private LocalDate fecha = LocalDate.now();

    /** Monto total de la venta (subtotal + IGV + envío). */
    @Column(nullable = false)
    private Double total;

    /** Suma de precios base sin IGV. */
    @Column(nullable = true)
    private Double subtotal = 0.0;

    /** Impuesto general a las ventas calculado. */
    @Column(nullable = true)
    private Double igv = 0.0;

    /** Canal por el que se realizó la venta (presencial, web, etc.). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalVenta canal = CanalVenta.PRESENCIAL;

    /** Tipo de comprobante fiscal emitido. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoComprobante tipoComprobante = TipoComprobante.TICKET;

    /** Método de pago utilizado. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    /** Modalidad de entrega del pedido. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEntrega tipoEntrega;

    /** Estado operativo del pedido. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estadoPedido = EstadoPedido.PENDIENTE;

    /** Estado del pago asociado a la venta. */
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false, length = 20)
    private EstadoPago estadoPago;

    /** Código de recojo para pedidos Fast Lane. */
    @Column(nullable = true)
    private String codigoRecojo;

    /** Dirección de entrega desnormalizada para consultas rápidas. */
    @Column(nullable = true)
    private String direccionEntrega;

    /** Costo de envío aplicado a la venta. */
    @Column(nullable = true)
    private Double costoEnvio = 0.0;

    /** Nombre del titular del comprobante. */
    @Column(nullable = true, length = 150)
    private String nombreComprobante;

    /** DNI del titular para boleta. */
    @Column(nullable = true, length = 8)
    private String dni;

    /** RUC del titular para factura. */
    @Column(nullable = true, length = 11)
    private String ruc;

    /** Razón social para factura. */
    @Column(nullable = true, length = 200)
    private String razonSocial;

    /** Correo para envío del comprobante. */
    @Column(nullable = true, length = 100)
    private String emailComprobante;

    /** Dirección fiscal para factura. */
    @Column(nullable = true, length = 255)
    private String direccionFiscal;

    /** Cliente que realizó la compra, si está registrado. */
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = true)
    @JsonIgnoreProperties({ "venta", "hibernateLazyInitializer", "handler", "password", "rol" })
    private Usuario cliente;

    /** Empleado o cajero que registró la venta presencial. */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    @JsonIgnoreProperties({ "venta", "hibernateLazyInitializer", "handler", "password", "rol" })
    private Usuario vendedor;

    /** Líneas de producto incluidas en la venta. */
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "venta" })
    @JsonIgnore
    private List<DetalleVenta> detalles = new ArrayList<>();

    /** Información detallada de entrega asociada. */
    @OneToOne(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "venta" })
    private VentaEntrega entrega;

    /** Registro de pago asociado a la venta. */
    @OneToOne(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({ "venta" })
    private Pago pago;

    /**
     * Constructor por defecto requerido por JPA.
     */
    public Venta() {
    }

    /**
     * Obtiene el identificador de la venta.
     *
     * @return id de la venta
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador de la venta.
     *
     * @param id id de la venta
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la fecha de la venta.
     *
     * @return fecha de registro
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha de la venta.
     *
     * @param fecha fecha de registro
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el total de la venta.
     *
     * @return monto total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Establece el total de la venta.
     *
     * @param total monto total
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    /**
     * Obtiene el subtotal sin IGV.
     *
     * @return subtotal base
     */
    public Double getSubtotal() {
        return subtotal;
    }

    /**
     * Establece el subtotal sin IGV.
     *
     * @param subtotal subtotal base
     */
    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    /**
     * Obtiene el IGV calculado.
     *
     * @return monto de IGV
     */
    public Double getIgv() {
        return igv;
    }

    /**
     * Establece el IGV calculado.
     *
     * @param igv monto de IGV
     */
    public void setIgv(Double igv) {
        this.igv = igv;
    }

    /**
     * Obtiene el canal de venta.
     *
     * @return canal de la venta
     */
    public CanalVenta getCanal() {
        return canal;
    }

    /**
     * Establece el canal de venta.
     *
     * @param canal canal de la venta
     */
    public void setCanal(CanalVenta canal) {
        this.canal = canal;
    }

    /**
     * Obtiene el tipo de comprobante.
     *
     * @return tipo de comprobante fiscal
     */
    public TipoComprobante getTipoComprobante() {
        return tipoComprobante;
    }

    /**
     * Establece el tipo de comprobante.
     *
     * @param tipoComprobante tipo de comprobante fiscal
     */
    public void setTipoComprobante(TipoComprobante tipoComprobante) {
        this.tipoComprobante = tipoComprobante;
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
     * Obtiene el estado del pedido.
     *
     * @return estado operativo del pedido
     */
    public EstadoPedido getEstadoPedido() {
        return estadoPedido;
    }

    /**
     * Establece el estado del pedido.
     *
     * @param estadoPedido estado operativo del pedido
     */
    public void setEstadoPedido(EstadoPedido estadoPedido) {
        this.estadoPedido = estadoPedido;
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

    /**
     * Obtiene la dirección de entrega.
     *
     * @return dirección de entrega
     */
    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    /**
     * Establece la dirección de entrega.
     *
     * @param direccionEntrega dirección de entrega
     */
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    /**
     * Obtiene el costo de envío.
     *
     * @return costo de envío aplicado
     */
    public Double getCostoEnvio() {
        return costoEnvio;
    }

    /**
     * Establece el costo de envío.
     *
     * @param costoEnvio costo de envío aplicado
     */
    public void setCostoEnvio(Double costoEnvio) {
        this.costoEnvio = costoEnvio;
    }

    /**
     * Obtiene el nombre del comprobante.
     *
     * @return nombre del titular
     */
    public String getNombreComprobante() {
        return nombreComprobante;
    }

    /**
     * Establece el nombre del comprobante.
     *
     * @param nombreComprobante nombre del titular
     */
    public void setNombreComprobante(String nombreComprobante) {
        this.nombreComprobante = nombreComprobante;
    }

    /**
     * Obtiene el DNI del titular.
     *
     * @return DNI
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del titular.
     *
     * @param dni DNI
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el RUC del titular.
     *
     * @return RUC
     */
    public String getRuc() {
        return ruc;
    }

    /**
     * Establece el RUC del titular.
     *
     * @param ruc RUC
     */
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    /**
     * Obtiene la razón social.
     *
     * @return razón social
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Establece la razón social.
     *
     * @param razonSocial razón social
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * Obtiene el email del comprobante.
     *
     * @return correo electrónico
     */
    public String getEmailComprobante() {
        return emailComprobante;
    }

    /**
     * Establece el email del comprobante.
     *
     * @param emailComprobante correo electrónico
     */
    public void setEmailComprobante(String emailComprobante) {
        this.emailComprobante = emailComprobante;
    }

    /**
     * Obtiene la dirección fiscal.
     *
     * @return dirección fiscal
     */
    public String getDireccionFiscal() {
        return direccionFiscal;
    }

    /**
     * Establece la dirección fiscal.
     *
     * @param direccionFiscal dirección fiscal
     */
    public void setDireccionFiscal(String direccionFiscal) {
        this.direccionFiscal = direccionFiscal;
    }

    /**
     * Obtiene el cliente de la venta.
     *
     * @return cliente registrado o {@code null}
     */
    public Usuario getCliente() {
        return cliente;
    }

    /**
     * Establece el cliente de la venta.
     *
     * @param cliente cliente registrado
     */
    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }

    /**
     * Obtiene el vendedor que registró la venta.
     *
     * @return vendedor o {@code null} en ventas web
     */
    public Usuario getVendedor() {
        return vendedor;
    }

    /**
     * Establece el vendedor que registró la venta.
     *
     * @param vendedor vendedor
     */
    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    /**
     * Obtiene las líneas de producto de la venta.
     * Expuesto en la vista de detalle JSON.
     *
     * @return lista de detalles de venta
     */
    @JsonProperty("detalles")
    @JsonView(JsonViews.Detail.class)
    public List<DetalleVenta> getDetalles() {
        return detalles;
    }

    /**
     * Establece las líneas de producto de la venta.
     *
     * @param detalles lista de detalles de venta
     */
    public void setDetalles(List<DetalleVenta> detalles) {
        this.detalles = detalles;
    }

    /**
     * Obtiene la información de entrega asociada.
     *
     * @return entrega de la venta
     */
    public VentaEntrega getEntrega() {
        return entrega;
    }

    /**
     * Establece la información de entrega asociada.
     *
     * @param entrega entrega de la venta
     */
    public void setEntrega(VentaEntrega entrega) {
        this.entrega = entrega;
    }

    /**
     * Obtiene el pago asociado a la venta.
     *
     * @return registro de pago
     */
    public Pago getPago() {
        return pago;
    }

    /**
     * Establece el pago asociado a la venta.
     *
     * @param pago registro de pago
     */
    public void setPago(Pago pago) {
        this.pago = pago;
    }
}
