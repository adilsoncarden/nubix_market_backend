package com.nubix.market.services;

import com.nubix.market.dto.CheckoutRequest;
import com.nubix.market.dto.VentaRequest;
import com.nubix.market.entities.DetalleVenta;
import com.nubix.market.entities.Pago;
import com.nubix.market.entities.Producto;
import com.nubix.market.entities.Usuario;
import com.nubix.market.entities.Venta;
import com.nubix.market.entities.VentaEntrega;
import com.nubix.market.enums.CanalVenta;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.repositories.ProductoRepository;
import com.nubix.market.repositories.UsuarioRepository;
import com.nubix.market.repositories.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAllWithRelations();
    }

    public Venta obtenerPorId(Integer id) {
        return ventaRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    @Transactional
    public Venta crearVenta(VentaRequest request) {
        validarRequestPresencial(request);
        Venta venta = construirVentaBase(request);
        venta.setCanal(request.getCanal() != null ? request.getCanal() : CanalVenta.PRESENCIAL);
        venta.setVendedor(obtenerVendedor(request.getVendedorId()));
        asignarClienteSiCorresponde(venta, request.getClienteId(), request.getTipoComprobante());
        aplicarComprobante(venta, request.getTipoComprobante(), request.getNombreComprobante(),
                request.getDni(), request.getRuc(), request.getRazonSocial(),
                request.getEmailComprobante(), request.getDireccionFiscal());
        double subtotal = procesarDetallesYStock(venta, request.getDetalles());
        double envio = request.getCostoEnvio() != null ? request.getCostoEnvio() : 0.0;
        venta.setCostoEnvio(envio);
        venta.setTotal(subtotal + envio);
        configurarEntrega(venta, request.getTipoEntrega(), request.getDireccionEntrega(),
                request.getDistrito(), request.getReferencia());
        configurarPago(venta, request.getMetodoPago(), venta.getTotal());
        return ventaRepository.save(venta);
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
        venta.setCostoEnvio(envio);
        venta.setTotal(subtotal + envio);
        configurarEntrega(venta, request.getTipoEntrega(), request.getDireccionEntrega(),
                request.getDistrito(), request.getReferencia());
        configurarPago(venta, request.getMetodoPago(), venta.getTotal());
        if (venta.getTipoEntrega() == TipoEntrega.DELIVERY) {
            venta.setEstadoPedido(EstadoPedido.EN_PROCESO);
        } else if (venta.getTipoEntrega() == TipoEntrega.FAST_LANE) {
            venta.setEstadoPedido(EstadoPedido.LISTO_PARA_RECOJO);
        }
        return ventaRepository.save(venta);
    }

    public Venta actualizarEstadoPedido(Integer ventaId, EstadoPedido nuevoEstado) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + ventaId));
        venta.setEstadoPedido(nuevoEstado);
        return ventaRepository.save(venta);
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
        return ventaRepository.save(venta);
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
        if (request.getVendedorId() == null) {
            throw new RuntimeException("El vendedor es obligatorio");
        }
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
            throw new RuntimeException("La venta debe tener al menos un producto");
        }
        TipoComprobante tipo = request.getTipoComprobante() != null
                ? request.getTipoComprobante()
                : TipoComprobante.TICKET;
        validarComprobante(tipo, request.getClienteId(), request.getDni(), request.getRuc());
        if (request.getTipoEntrega() == TipoEntrega.DELIVERY
                && (request.getDireccionEntrega() == null || request.getDireccionEntrega().isBlank())) {
            throw new RuntimeException("La dirección de entrega es obligatoria para delivery");
        }
    }

    private void validarCheckout(CheckoutRequest request) {
        if (request.getDetalles() == null || request.getDetalles().isEmpty()) {
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
        validarComprobante(tipo, request.getClienteId(), request.getDni(), request.getRuc());
        if (request.getTipoEntrega() == TipoEntrega.DELIVERY
                && (request.getDireccionEntrega() == null || request.getDireccionEntrega().isBlank())) {
            throw new RuntimeException("La dirección de entrega es obligatoria para delivery");
        }
    }

    private void validarComprobante(TipoComprobante tipo, Integer clienteId, String dni, String ruc) {
        switch (tipo) {
            case TICKET -> {
                // Sin cliente registrado obligatorio
            }
            case BOLETA -> {
                if ((dni == null || !dni.matches("\\d{8}"))
                        && clienteId == null) {
                    throw new RuntimeException("La boleta requiere DNI de 8 dígitos o cliente registrado");
                }
            }
            case FACTURA -> {
                if (ruc == null || !ruc.matches("\\d{11}")) {
                    throw new RuntimeException("La factura requiere RUC de 11 dígitos");
                }
            }
            default -> throw new RuntimeException("Tipo de comprobante no válido");
        }
    }

    private Usuario obtenerVendedor(Integer vendedorId) {
        return usuarioRepository.findById(vendedorId)
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));
    }

    private Usuario obtenerVendedorSistema() {
        return usuarioRepository.findFirstByRol_Nombre("ADMIN")
                .or(() -> usuarioRepository.findFirstByRol_Nombre("EMPLEADO"))
                .orElseThrow(() -> new RuntimeException("No hay vendedor del sistema configurado"));
    }

    private void asignarClienteSiCorresponde(Venta venta, Integer clienteId, TipoComprobante tipo) {
        if (clienteId != null) {
            Usuario cliente = usuarioRepository.findById(clienteId)
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            venta.setCliente(cliente);
        } else if (tipo != TipoComprobante.TICKET) {
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
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException(
                            "Producto no encontrado: " + item.getProductoId()));

            if (item.getCantidad() == null || item.getCantidad() < 1) {
                throw new RuntimeException("Cantidad inválida para: " + producto.getNombre());
            }
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre()
                        + " (disponible: " + producto.getStock() + ")");
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

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
        } else if (tipoEntrega == TipoEntrega.FAST_LANE || tipoEntrega == TipoEntrega.PRESENCIAL) {
            String codigo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            entrega.setCodigoRecojo(codigo);
            venta.setCodigoRecojo(codigo);
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
