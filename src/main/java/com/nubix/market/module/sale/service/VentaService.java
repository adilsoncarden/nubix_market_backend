package com.nubix.market.module.sale.service;

import com.nubix.market.enums.CanalVenta;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.module.notification.service.NotificacionService;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import com.nubix.market.module.sale.dto.CheckoutRequest;
import com.nubix.market.module.sale.dto.VentaRequest;
import com.nubix.market.module.sale.model.DetalleVenta;
import com.nubix.market.module.sale.model.Pago;
import com.nubix.market.module.sale.model.Venta;
import com.nubix.market.module.sale.model.VentaEntrega;
import com.nubix.market.module.sale.repository.VentaRepository;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class VentaService {
    private static final double IGV_RATE = 0.13;

    private static final Logger log = LoggerFactory.getLogger(VentaService.class);

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private NotificacionService notificacionService;

    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAllWithRelations();
    }

    public Venta obtenerPorId(Integer id) {
        return ventaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    @Transactional
    public Venta crearVenta(VentaRequest request) {
        // Forzar siempre venta presencial tipo cajero
        request.setCanal(CanalVenta.PRESENCIAL);
        request.setTipoEntrega(TipoEntrega.PRESENCIAL);

        validarRequestPresencial(request);
        log.info("Creando venta presencial (comprobante={}, líneas={})",
                request.getTipoComprobante(),
                request.getDetalles() != null ? request.getDetalles().size() : 0);
        Venta venta = construirVentaBase(request);
        venta.setCanal(CanalVenta.PRESENCIAL);
        venta.setVendedor(obtenerUsuarioActual());
        asignarClienteSiCorresponde(venta, request.getClienteId(), request.getTipoComprobante());
        aplicarComprobante(venta, request.getTipoComprobante(), request.getNombreComprobante(),
                request.getDni(), request.getRuc(), request.getRazonSocial(),
                request.getEmailComprobante(), request.getDireccionFiscal());
        double subtotal = procesarDetallesYStock(venta, request.getDetalles());
        double envio = request.getCostoEnvio() != null ? request.getCostoEnvio() : 0.0;
        double igv = round2(subtotal * IGV_RATE);
        venta.setCostoEnvio(envio);
        venta.setSubtotal(round2(subtotal));
        venta.setIgv(igv);
        venta.setTotal(round2(subtotal + igv + envio));
        configurarEntrega(venta, request.getTipoEntrega(), request.getDireccionEntrega(),
                request.getDistrito(), request.getReferencia());
        configurarPago(venta, request.getMetodoPago(), venta.getTotal());
        // Venta presencial de cajero se considera entregada por defecto
        venta.setEstadoPedido(EstadoPedido.ENTREGADO);
        Venta saved = ventaRepository.save(venta);
        notificacionService.crearInterna(
                saved.getVendedor(),
                "pedido",
                "Nueva venta presencial registrada (ID #" + saved.getId() + ").");
        if (saved.getEstadoPago() == EstadoPago.APROBADO) {
            notificacionService.crearInterna(
                    saved.getVendedor(),
                    "pago",
                    "Pago confirmado para la venta #" + saved.getId() + ".");
        }
        return saved;
    }

    @Transactional
    public Venta checkoutWeb(CheckoutRequest request) {
        validarCheckout(request);
        VentaRequest ventaRequest = mapearCheckout(request);
        Venta venta = construirVentaBase(ventaRequest);
        venta.setCanal(CanalVenta.WEB);
        venta.setVendedor(obtenerVendedorSistema());
        asignarClienteSiCorresponde(venta, request.getClienteId(), request.getTipoComprobante());
        aplicarComprobante(venta, request.getTipoComprobante(), request.getNombreComprobante(),
                request.getDni(), request.getRuc(), request.getRazonSocial(),
                request.getEmailComprobante(), request.getDireccionFiscal());
        double subtotal = procesarDetallesYStock(venta, request.getDetalles());
        double envio = request.getCostoEnvio() != null ? request.getCostoEnvio() : 0.0;
        double igv = round2(subtotal * IGV_RATE);
        venta.setCostoEnvio(envio);
        venta.setSubtotal(round2(subtotal));
        venta.setIgv(igv);
        venta.setTotal(round2(subtotal + igv + envio));
        configurarEntrega(venta, request.getTipoEntrega(), request.getDireccionEntrega(),
                request.getDistrito(), request.getReferencia());
        configurarPago(venta, request.getMetodoPago(), venta.getTotal());
        if (venta.getTipoEntrega() == TipoEntrega.DELIVERY) {
            venta.setEstadoPedido(EstadoPedido.EN_PROCESO);
        } else if (venta.getTipoEntrega() == TipoEntrega.FAST_LANE) {
            venta.setEstadoPedido(EstadoPedido.LISTO_PARA_RECOJO);
        }
        Venta saved = ventaRepository.save(venta);
        notificacionService.crearInterna(
                saved.getVendedor(),
                "pedido",
                "Nuevo pedido web creado (ID #" + saved.getId() + ").");
        if (saved.getEstadoPago() == EstadoPago.APROBADO) {
            notificacionService.crearInterna(
                    saved.getVendedor(),
                    "pago",
                    "Pago confirmado para el pedido #" + saved.getId() + ".");
        }
        if (saved.getTipoEntrega() == TipoEntrega.FAST_LANE && saved.getCodigoRecojo() != null) {
            notificacionService.crearInterna(
                    saved.getVendedor(),
                    "recojo",
                    "Pedido #" + saved.getId() + " listo para recojo. Código: " + saved.getCodigoRecojo());
        }
        return saved;
    }

    public Venta actualizarEstadoPedido(Integer ventaId, EstadoPedido nuevoEstado) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + ventaId));
        venta.setEstadoPedido(nuevoEstado);
        Venta saved = ventaRepository.save(venta);
        notificacionService.crearInterna(
                saved.getVendedor(),
                "pedido",
                "Estado del pedido #" + saved.getId() + " actualizado a " + nuevoEstado + ".");
        return saved;
    }

    public Venta registrarCredito(Integer ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        if (venta.getMetodoPago() != MetodoPago.CREDITO) {
            throw new RuntimeException("La venta no es a crédito");
        }
        if (venta.getEstadoPago() == EstadoPago.PAGADO || venta.getEstadoPago() == EstadoPago.APROBADO) {
            throw new RuntimeException("La venta ya ha sido pagada");
        }

        venta.setEstadoPago(EstadoPago.APROBADO);
        if (venta.getPago() != null) {
            venta.getPago().setEstadoPago(EstadoPago.APROBADO);
        }
        Venta saved = ventaRepository.save(venta);
        notificacionService.crearInterna(
                saved.getVendedor(),
                "pago",
                "Pago aprobado para la venta #" + saved.getId() + ".");
        return saved;
    }

    private Venta construirVentaBase(VentaRequest request) {
        Venta venta = new Venta();
        TipoComprobante comprobante = request.getTipoComprobante() != null
                ? request.getTipoComprobante()
                : TipoComprobante.TICKET;
        venta.setTipoComprobante(comprobante);
        venta.setMetodoPago(request.getMetodoPago());
        TipoEntrega entrega = request.getTipoEntrega() != null
                ? request.getTipoEntrega()
                : TipoEntrega.PRESENCIAL;
        venta.setTipoEntrega(entrega);
        venta.setEstadoPedido(EstadoPedido.PENDIENTE);
        venta.setEstadoPago(resolverEstadoPagoInicial(request.getMetodoPago()));
        return venta;
    }

    private EstadoPago resolverEstadoPagoInicial(MetodoPago metodoPago) {
        if (metodoPago == MetodoPago.CREDITO) {
            return EstadoPago.PENDIENTE;
        }
        return EstadoPago.APROBADO;
    }

    private void validarRequestPresencial(VentaRequest request) {
        if (ObjectUtils.isEmpty(request.getDetalles())) {
            throw new RuntimeException("La venta debe tener al menos un producto");
        }
        TipoComprobante tipo = request.getTipoComprobante() != null
                ? request.getTipoComprobante()
                : TipoComprobante.TICKET;
        validarComprobante(tipo, request.getClienteId(), request.getNombreComprobante(),
                request.getDni(), request.getRuc(), request.getRazonSocial(), request.getDireccionFiscal());
        if (request.getTipoEntrega() == TipoEntrega.DELIVERY
                && StringUtils.isBlank(request.getDireccionEntrega())) {
            throw new RuntimeException("La dirección de entrega es obligatoria para delivery");
        }
    }

    private void validarCheckout(CheckoutRequest request) {
        if (ObjectUtils.isEmpty(request.getDetalles())) {
            throw new RuntimeException("El carrito está vacío");
        }
        if (request.getMetodoPago() == null) {
            request.setMetodoPago(MetodoPago.YAPE);
        }
        if (request.getTipoEntrega() == null) {
            request.setTipoEntrega(TipoEntrega.FAST_LANE);
        }
        TipoComprobante tipo = request.getTipoComprobante() != null
                ? request.getTipoComprobante()
                : TipoComprobante.BOLETA;
        request.setTipoComprobante(tipo);
        validarComprobante(tipo, request.getClienteId(), request.getNombreComprobante(),
                request.getDni(), request.getRuc(), request.getRazonSocial(), request.getDireccionFiscal());
        if (request.getTipoEntrega() == TipoEntrega.DELIVERY
                && StringUtils.isBlank(request.getDireccionEntrega())) {
            throw new RuntimeException("La dirección de entrega es obligatoria para delivery");
        }
    }

    private void validarComprobante(
            TipoComprobante tipo,
            Integer clienteId,
            String nombre,
            String dni,
            String ruc,
            String razonSocial,
            String direccionFiscal) {
        switch (tipo) {
            case TICKET -> {
                // Sin cliente registrado obligatorio
            }
            case BOLETA -> {
                if (clienteId == null) {
                    if (dni == null || !dni.matches("\\d{8}")) {
                        throw new RuntimeException("La boleta requiere DNI de 8 dígitos o cliente registrado");
                    }
                    if (nombre == null || nombre.isBlank()) {
                        throw new RuntimeException("La boleta requiere el nombre del cliente");
                    }
                }
            }
            case FACTURA -> {
                if (ruc == null || !ruc.matches("\\d{11}")) {
                    throw new RuntimeException("La factura requiere RUC de 11 dígitos");
                }
                if (razonSocial == null || razonSocial.isBlank()) {
                    throw new RuntimeException("La factura requiere razón social");
                }
                if (direccionFiscal == null || direccionFiscal.isBlank()) {
                    throw new RuntimeException("La factura requiere dirección fiscal");
                }
            }
            default -> throw new RuntimeException("Tipo de comprobante no válido");
        }
    }

    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    private Usuario obtenerVendedorSistema() {
        return usuarioRepository.findFirstByRol_Nombre("ADMIN")
                .or(() -> usuarioRepository.findFirstByRol_Nombre("EMPLEADO"))
                .orElseThrow(() -> new RuntimeException("No hay vendedor del sistema configurado"));
    }

    private void asignarClienteSiCorresponde(Venta venta, Integer clienteId, TipoComprobante tipo) {
        if (tipo == TipoComprobante.TICKET) {
            venta.setCliente(null);
            return;
        }
        if (clienteId != null) {
            Usuario cliente = usuarioRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            venta.setCliente(cliente);
        } else {
            venta.setCliente(null);
        }
    }

    private void aplicarComprobante(Venta venta, TipoComprobante tipo, String nombre,
            String dni, String ruc, String razonSocial, String email, String direccionFiscal) {
        venta.setTipoComprobante(tipo != null ? tipo : TipoComprobante.TICKET);
        venta.setNombreComprobante(nombre);
        venta.setDni(dni);
        venta.setRuc(ruc);
        venta.setRazonSocial(razonSocial);
        venta.setEmailComprobante(email);
        venta.setDireccionFiscal(direccionFiscal);
    }

    private double procesarDetallesYStock(Venta venta, List<VentaRequest.DetalleVentaRequest> items) {
        double total = 0.0;
        for (VentaRequest.DetalleVentaRequest item : items) {
            Producto producto = productoRepository.findByIdForUpdate(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado: " + item.getProductoId()));

            if (item.getCantidad() == null || item.getCantidad() < 1) {
                throw new RuntimeException("Cantidad inválida para: " + producto.getNombre());
            }
            int stockActual = producto.getStock() != null ? producto.getStock() : 0;
            if (stockActual < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre()
                        + " (disponible: " + stockActual + ")");
            }

            int nuevoStock = stockActual - item.getCantidad();
            if (nuevoStock < 0) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }
            producto.setStock(nuevoStock);
            productoRepository.save(producto);
            if (nuevoStock <= 5) {
                notificacionService.crearInterna(
                        venta.getVendedor(),
                        "stock",
                        "Stock bajo detectado para " + producto.getNombre() + " (restante: " + nuevoStock + ").");
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());
            double subtotal = item.getCantidad() * producto.getPrecioVenta();
            detalle.setSubtotal(subtotal);
            total += subtotal;
            venta.getDetalles().add(detalle);
        }
        return total;
    }

    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private void configurarEntrega(Venta venta, TipoEntrega tipo, String direccion,
            String distrito, String referencia) {
        TipoEntrega tipoEntrega = tipo != null ? tipo : TipoEntrega.PRESENCIAL;
        venta.setTipoEntrega(tipoEntrega);

        VentaEntrega entrega = new VentaEntrega();
        entrega.setVenta(venta);
        entrega.setTipoEntrega(tipoEntrega);

        if (tipoEntrega == TipoEntrega.DELIVERY) {
            entrega.setDireccion(direccion);
            entrega.setDistrito(distrito);
            entrega.setReferencia(referencia);
            venta.setDireccionEntrega(direccion);
            venta.setCodigoRecojo(null);
        } else if (tipoEntrega == TipoEntrega.FAST_LANE) {
            String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            entrega.setCodigoRecojo(codigo);
            venta.setCodigoRecojo(codigo);
            venta.setDireccionEntrega(null);
        } else if (tipoEntrega == TipoEntrega.PRESENCIAL) {
            entrega.setCodigoRecojo(null);
            venta.setCodigoRecojo(null);
            venta.setDireccionEntrega(null);
        }

        venta.setEntrega(entrega);
    }

    private void configurarPago(Venta venta, MetodoPago metodoPago, double monto) {
        EstadoPago estado = resolverEstadoPagoInicial(metodoPago);
        venta.setMetodoPago(metodoPago);
        venta.setEstadoPago(estado);

        Pago pago = new Pago();
        pago.setVenta(venta);
        pago.setMetodoPago(metodoPago);
        pago.setEstadoPago(estado);
        pago.setMonto(monto);
        venta.setPago(pago);
    }

    private VentaRequest mapearCheckout(CheckoutRequest checkout) {
        VentaRequest request = new VentaRequest();
        request.setClienteId(checkout.getClienteId());
        request.setTipoComprobante(checkout.getTipoComprobante());
        request.setMetodoPago(checkout.getMetodoPago());
        request.setTipoEntrega(checkout.getTipoEntrega());
        request.setDireccionEntrega(checkout.getDireccionEntrega());
        request.setDistrito(checkout.getDistrito());
        request.setReferencia(checkout.getReferencia());
        request.setCostoEnvio(checkout.getCostoEnvio());
        request.setNombreComprobante(checkout.getNombreComprobante());
        request.setDni(checkout.getDni());
        request.setRuc(checkout.getRuc());
        request.setRazonSocial(checkout.getRazonSocial());
        request.setEmailComprobante(checkout.getEmailComprobante());
        request.setDireccionFiscal(checkout.getDireccionFiscal());
        request.setDetalles(checkout.getDetalles());
        return request;
    }
}
