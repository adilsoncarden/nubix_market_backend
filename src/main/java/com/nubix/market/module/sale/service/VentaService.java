package com.nubix.market.module.sale.service;

import com.nubix.market.enums.CanalVenta;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.module.cart.service.CarritoService;
import com.nubix.market.module.notification.service.NotificacionService;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import com.nubix.market.module.sale.dto.CheckoutRequest;
import com.nubix.market.module.sale.dto.MisPedidoResponse;
import com.nubix.market.module.sale.dto.VentaRequest;
import com.nubix.market.module.sale.model.DetalleVenta;
import com.nubix.market.module.sale.model.Pago;
import com.nubix.market.module.sale.model.Venta;
import com.nubix.market.module.sale.model.VentaEntrega;
import com.nubix.market.module.sale.repository.VentaRepository;
import com.nubix.market.module.sale.util.OrderStatusFlow;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio de dominio para la gestión de ventas presenciales y pedidos web.
 * Orquesta validaciones, cálculo de totales, control de stock, entrega, pago y notificaciones.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Service
public class VentaService {
    private static final double IGV_RATE = 0.13;
    private static final double ENVIO_GRATIS_DESDE = 100.0;
    private static final double COSTO_ENVIO_DEFAULT = 8.0;

    private static final Logger log = LoggerFactory.getLogger(VentaService.class);

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final NotificacionService notificacionService;
    private final CarritoService carritoService;

    /**
     * Crea el servicio con las dependencias necesarias para ventas.
     *
     * @param ventaRepository      repositorio de ventas
     * @param usuarioRepository    repositorio de usuarios
     * @param productoRepository   repositorio de productos
     * @param notificacionService  servicio de notificaciones internas
     * @param carritoService       servicio de carrito web
     */
    public VentaService(
            VentaRepository ventaRepository,
            UsuarioRepository usuarioRepository,
            ProductoRepository productoRepository,
            NotificacionService notificacionService,
            CarritoService carritoService) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.notificacionService = notificacionService;
        this.carritoService = carritoService;
    }

    /**
     * Obtiene todas las ventas para listados administrativos.
     *
     * @return lista de ventas con cliente y vendedor cargados
     */
    @Transactional(readOnly = true)
    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAllForList();
    }

    /**
     * Busca una venta por identificador con todas sus relaciones.
     *
     * @param id identificador de la venta
     * @return venta encontrada con detalles, entrega y pago
     * @throws IllegalArgumentException si el id es nulo o no positivo
     * @throws RuntimeException         si la venta no existe
     */
    @Transactional(readOnly = true)
    public Venta obtenerPorId(Integer id) {
        Preconditions.checkArgument(id != null && id > 0, "El id de la venta es obligatorio");
        return ventaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    /**
     * Registra una venta presencial desde caja, descontando stock y emitiendo notificaciones.
     *
     * @param request datos de la venta presencial
     * @return venta persistida con estado entregado
     * @throws RuntimeException si la validación, el stock o el comprobante no son válidos
     */
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
        aplicarTotalesFinancieros(venta, subtotal, request.getTipoEntrega());
        configurarEntrega(venta, request.getTipoEntrega(), request.getDireccionEntrega(),
                request.getDistrito(), request.getReferencia());
        configurarPago(venta, request.getMetodoPago(), venta.getTotal());
        // Venta presencial de cajero se considera entregada por defecto
        venta.setEstadoPedido(EstadoPedido.ENTREGADO);
        Venta saved = ventaRepository.save(venta);
        notificacionService.crearInterna(
                destinatarioNotificacion(saved),
                "pedido",
                "Nueva venta presencial registrada (ID #" + saved.getId() + ").");
        if (saved.getEstadoPago() == EstadoPago.APROBADO) {
            notificacionService.crearInterna(
                    destinatarioNotificacion(saved),
                    "pago",
                    "Pago confirmado para la venta #" + saved.getId() + ".");
        }
        return saved;
    }

    /**
     * Procesa el checkout de un pedido web, vacía el carrito y genera notificaciones.
     *
     * @param request datos del checkout web
     * @return venta web creada en estado pendiente
     * @throws RuntimeException si el carrito está vacío o falla alguna validación de negocio
     */
    @Transactional
    public Venta checkoutWeb(CheckoutRequest request) {
        Usuario usuarioActual = obtenerUsuarioActual();
        if ("CLIENTE".equals(usuarioActual.getRol().getNombre())) {
            request.setClienteId(usuarioActual.getId());
        }
        validarCheckout(request);
        VentaRequest ventaRequest = mapearCheckout(request);
        Venta venta = construirVentaBase(ventaRequest);
        venta.setCanal(CanalVenta.WEB);
        venta.setVendedor(null);
        asignarClienteSiCorresponde(venta, request.getClienteId(), request.getTipoComprobante());
        aplicarComprobante(venta, request.getTipoComprobante(), request.getNombreComprobante(),
                request.getDni(), request.getRuc(), request.getRazonSocial(),
                request.getEmailComprobante(), request.getDireccionFiscal());
        double subtotal = procesarDetallesYStock(venta, request.getDetalles());
        aplicarTotalesFinancieros(venta, subtotal, request.getTipoEntrega());
        configurarEntrega(venta, request.getTipoEntrega(), request.getDireccionEntrega(),
                request.getDistrito(), request.getReferencia());
        configurarPago(venta, request.getMetodoPago(), venta.getTotal());
        Venta saved = ventaRepository.save(venta);
        notificacionService.crearInterna(
                destinatarioNotificacion(saved),
                "pedido",
                "Nuevo pedido web creado (ID #" + saved.getId() + ").");
        if (saved.getEstadoPago() == EstadoPago.APROBADO) {
            notificacionService.crearInterna(
                    destinatarioNotificacion(saved),
                    "pago",
                    "Pago confirmado para el pedido #" + saved.getId() + ".");
        }
        if (saved.getTipoEntrega() == TipoEntrega.FAST_LANE && saved.getCodigoRecojo() != null) {
            notificacionService.crearInterna(
                    destinatarioNotificacion(saved),
                    "recojo",
                    "Pedido Fast Lane #" + saved.getId() + " registrado. Código de recojo: " + saved.getCodigoRecojo());
        }
        carritoService.vaciarCarrito(usuarioActual.getId());
        return saved;
    }

    /**
     * Lista los pedidos web del cliente autenticado con filtro opcional por período.
     *
     * @param mes          período predefinido ({@code actual}, {@code todos}, {@code trimestre}, etc.)
     * @param fechaInicio  fecha inicial del rango personalizado
     * @param fechaFin     fecha final del rango personalizado
     * @return lista resumida de pedidos del cliente en canal web
     */
    @Transactional(readOnly = true)
    public List<MisPedidoResponse> listarMisPedidosWeb(
            String mes, LocalDate fechaInicio, LocalDate fechaFin) {
        Usuario usuario = obtenerUsuarioActual();
        Integer clienteId = usuario.getId();
        CanalVenta canal = CanalVenta.WEB;

        LocalDate[] rango = resolverRangoMisPedidos(mes, fechaInicio, fechaFin);
        List<Venta> ventas;
        if (rango == null) {
            ventas = ventaRepository.findByClienteIdAndCanalOrderByIdDesc(clienteId, canal);
        } else {
            ventas = ventaRepository.findByClienteIdAndCanalAndFechaBetweenOrderByIdDesc(
                    clienteId, canal, rango[0], rango[1]);
        }

        return ventas.stream().map(this::toMisPedidoResponse).collect(Collectors.toList());
    }

    /**
     * Resuelve el rango de fechas para filtrar los pedidos del cliente.
     * Sin parámetros o con {@code mes=actual}: mes en curso.
     * {@code mes=todos} / {@code all}: sin filtro de fecha.
     *
     * @param mes          alias de período predefinido
     * @param fechaInicio  fecha inicial explícita
     * @param fechaFin     fecha final explícita
     * @return arreglo con inicio y fin inclusive, o {@code null} si no hay filtro de fecha
     */
    private LocalDate[] resolverRangoMisPedidos(String mes, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null) {
            return new LocalDate[] {fechaInicio, fechaFin};
        }

        LocalDate hoy = LocalDate.now();
        String periodo = (mes == null || mes.isBlank()) ? "actual" : mes.trim().toLowerCase(Locale.ROOT);

        return switch (periodo) {
            case "todos", "all", "completo" -> null;
            case "trimestre", "quarter" -> new LocalDate[] {hoy.withDayOfMonth(1).minusMonths(2), hoy};
            case "anio", "year" -> new LocalDate[] {
                LocalDate.of(hoy.getYear(), 1, 1), LocalDate.of(hoy.getYear(), 12, 31)
            };
            case "actual", "month", "mes" -> new LocalDate[] {
                hoy.withDayOfMonth(1), hoy.withDayOfMonth(hoy.lengthOfMonth())
            };
            default -> new LocalDate[] {hoy.withDayOfMonth(1), hoy.withDayOfMonth(hoy.lengthOfMonth())};
        };
    }

    /**
     * Convierte una entidad {@link Venta} al DTO de respuesta para mis pedidos.
     *
     * @param venta entidad de venta
     * @return DTO resumido del pedido
     */
    private MisPedidoResponse toMisPedidoResponse(Venta venta) {
        MisPedidoResponse dto = new MisPedidoResponse();
        dto.setId(venta.getId());
        dto.setFecha(venta.getFecha());
        dto.setEstado(venta.getEstadoPedido());
        dto.setEstadoPago(venta.getEstadoPago());
        dto.setTotal(venta.getTotal());
        dto.setTipoEntrega(venta.getTipoEntrega());
        dto.setCodigoRecojo(venta.getCodigoRecojo());
        dto.setCanal(venta.getCanal());
        return dto;
    }

    /**
     * Actualiza el estado de un pedido validando la transición según su tipo de entrega.
     *
     * @param ventaId     identificador de la venta
     * @param nuevoEstado estado destino del pedido
     * @return venta actualizada
     * @throws RuntimeException si la venta no existe o la transición no es válida
     */
    @Transactional
    public Venta actualizarEstadoPedido(Integer ventaId, EstadoPedido nuevoEstado) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + ventaId));
        OrderStatusFlow.validateTransition(
                venta.getTipoEntrega(),
                venta.getEstadoPedido(),
                nuevoEstado);
        venta.setEstadoPedido(nuevoEstado);
        Venta saved = ventaRepository.save(venta);
        notificacionService.crearInterna(
                destinatarioNotificacion(saved),
                "pedido",
                "Estado del pedido #" + saved.getId() + " actualizado a " + nuevoEstado + ".");
        return saved;
    }

    /**
     * Marca como aprobado el pago de una venta realizada a crédito.
     *
     * @param ventaId identificador de la venta a crédito
     * @return venta con pago aprobado
     * @throws RuntimeException si la venta no existe, no es a crédito o ya fue pagada
     */
    @Transactional
    public Venta registrarCredito(Integer ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        if (venta.getMetodoPago() != MetodoPago.CREDITO) {
            throw new RuntimeException("La venta no es a crédito");
        }
        if (venta.getEstadoPago() == EstadoPago.APROBADO) {
            throw new RuntimeException("La venta ya ha sido pagada");
        }

        venta.setEstadoPago(EstadoPago.APROBADO);
        if (venta.getPago() != null) {
            venta.getPago().setEstadoPago(EstadoPago.APROBADO);
        }
        Venta saved = ventaRepository.save(venta);
        notificacionService.crearInterna(
                destinatarioNotificacion(saved),
                "pago",
                "Pago aprobado para la venta #" + saved.getId() + ".");
        return saved;
    }

    /**
     * Construye una venta base con valores iniciales de comprobante, entrega y pago.
     *
     * @param request solicitud con datos de la venta
     * @return entidad venta sin persistir
     */
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

    /**
     * Determina el estado inicial del pago según el método seleccionado.
     *
     * @param metodoPago método de pago de la venta
     * @return {@link EstadoPago#PENDIENTE} para crédito; {@link EstadoPago#APROBADO} en otros casos
     */
    private EstadoPago resolverEstadoPagoInicial(MetodoPago metodoPago) {
        if (metodoPago == MetodoPago.CREDITO) {
            return EstadoPago.PENDIENTE;
        }
        return EstadoPago.APROBADO;
    }

    /**
     * Valida los datos mínimos de una venta presencial.
     *
     * @param request solicitud de venta presencial
     * @throws RuntimeException si faltan productos, datos de comprobante o dirección de delivery
     */
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

    /**
     * Valida y normaliza los datos del checkout web.
     *
     * @param request solicitud de checkout
     * @throws RuntimeException si el carrito está vacío o faltan datos obligatorios
     */
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

    /**
     * Valida los datos requeridos según el tipo de comprobante fiscal.
     *
     * @param tipo             tipo de comprobante
     * @param clienteId        id del cliente registrado, si existe
     * @param nombre           nombre del titular
     * @param dni              DNI del titular
     * @param ruc              RUC del titular
     * @param razonSocial      razón social
     * @param direccionFiscal  dirección fiscal
     * @throws RuntimeException si faltan datos obligatorios para el comprobante
     */
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

    /**
     * Obtiene el usuario autenticado en el contexto de seguridad actual.
     *
     * @return usuario autenticado
     * @throws RuntimeException si no hay sesión válida
     */
    private Usuario obtenerUsuarioActual() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    /**
     * Obtiene un usuario del sistema para usar como destinatario de notificaciones internas.
     *
     * @return primer usuario ADMIN o EMPLEADO disponible
     * @throws RuntimeException si no existe un vendedor del sistema configurado
     */
    private Usuario obtenerVendedorSistema() {
        return usuarioRepository.findFirstByRol_Nombre("ADMIN")
                .or(() -> usuarioRepository.findFirstByRol_Nombre("EMPLEADO"))
                .orElseThrow(() -> new RuntimeException("No hay vendedor del sistema configurado"));
    }

    /**
     * Determina el destinatario de notificaciones internas para una venta.
     *
     * @param venta venta que originó la notificación
     * @return vendedor de la venta o, si no existe, un usuario del sistema
     */
    private Usuario destinatarioNotificacion(Venta venta) {
        return venta.getVendedor() != null ? venta.getVendedor() : obtenerVendedorSistema();
    }

    /**
     * Asocia el cliente a la venta cuando el tipo de comprobante lo requiere.
     *
     * @param venta     venta en construcción
     * @param clienteId identificador del cliente, si aplica
     * @param tipo      tipo de comprobante
     * @throws RuntimeException si el cliente indicado no existe
     */
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

    /**
     * Copia los datos fiscales del comprobante a la entidad venta.
     *
     * @param venta            venta en construcción
     * @param tipo             tipo de comprobante
     * @param nombre           nombre del titular
     * @param dni              DNI del titular
     * @param ruc              RUC del titular
     * @param razonSocial      razón social
     * @param email            correo del comprobante
     * @param direccionFiscal  dirección fiscal
     */
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

    /**
     * Procesa las líneas de venta, descuenta stock y calcula el subtotal base.
     *
     * @param venta entidad venta en construcción
     * @param items líneas solicitadas
     * @return subtotal base sin IGV ni envío
     * @throws RuntimeException si un producto no existe, la cantidad es inválida o no hay stock
     */
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
                        destinatarioNotificacion(venta),
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

    /**
     * Redondea un valor monetario a dos decimales.
     *
     * @param value valor a redondear
     * @return valor redondeado
     */
    private static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    /**
     * Calcula y asigna subtotal, IGV, costo de envío y total a la venta.
     * Subtotal = suma de precios base (sin IGV). Total = subtotal + IGV + envío.
     *
     * @param venta         venta en construcción
     * @param subtotalBase  subtotal sin impuestos ni envío
     * @param tipoEntrega   modalidad de entrega para calcular envío
     */
    private void aplicarTotalesFinancieros(Venta venta, double subtotalBase, TipoEntrega tipoEntrega) {
        double base = round2(subtotalBase);
        double envio = calcularCostoEnvio(base, tipoEntrega);
        double igv = round2(base * IGV_RATE);
        double total = round2(base + igv + envio);
        venta.setSubtotal(base);
        venta.setIgv(igv);
        venta.setCostoEnvio(envio);
        venta.setTotal(total);
    }

    /**
     * Calcula el costo de envío según el subtotal y el tipo de entrega.
     *
     * @param subtotal    subtotal base de la venta
     * @param tipoEntrega modalidad de entrega
     * @return costo de envío; cero si no aplica o califica para envío gratis
     */
    private double calcularCostoEnvio(double subtotal, TipoEntrega tipoEntrega) {
        if (tipoEntrega != TipoEntrega.DELIVERY) {
            return 0.0;
        }
        if (subtotal >= ENVIO_GRATIS_DESDE) {
            return 0.0;
        }
        return COSTO_ENVIO_DEFAULT;
    }

    /**
     * Configura la entidad de entrega y los campos desnormalizados de la venta.
     *
     * @param venta      venta en construcción
     * @param tipo       modalidad de entrega
     * @param direccion  dirección para delivery
     * @param distrito   distrito para delivery
     * @param referencia referencia de entrega
     */
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

    /**
     * Crea y asocia el registro de pago a la venta.
     *
     * @param venta      venta en construcción
     * @param metodoPago método de pago seleccionado
     * @param monto      monto total a registrar en el pago
     */
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

    /**
     * Convierte un {@link CheckoutRequest} en {@link VentaRequest} para reutilizar el flujo común.
     *
     * @param checkout solicitud de checkout web
     * @return solicitud de venta equivalente
     */
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
