package com.nubix.market.module.notification.service;

import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.module.auth.dto.EmailConfirmacionRequest;
import com.nubix.market.module.notification.dto.EmailConfirmacionContext;
import com.nubix.market.module.notification.dto.EmailProductoLinea;
import com.nubix.market.module.sale.model.DetalleVenta;
import com.nubix.market.module.sale.model.Venta;
import com.nubix.market.module.sale.repository.VentaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmailConfirmacionBuilder {

    @Autowired
    private VentaRepository ventaRepository;

    public EmailConfirmacionContext build(EmailConfirmacionRequest request) {
        EmailConfirmacionContext context = new EmailConfirmacionContext();
        context.setEmail(request.getEmail());

        if (request.getVentaId() != null) {
            Venta venta = ventaRepository.findByIdWithRelations(request.getVentaId())
                    .orElseThrow(() -> new RuntimeException("Pedido no encontrado para enviar confirmación"));
            mapFromVenta(context, venta);
            return context;
        }

        context.setNumero(request.getNumero());
        context.setTipoComprobante(resolveTipoLabel(request.getTipo(), null));
        context.setCodigoRecojo(request.getCodigoRecojo());
        context.setSubtotal(request.getSubtotal());
        context.setIgv(request.getIgv());
        context.setCostoEnvio(request.getCostoEnvio());
        context.setTotal(request.getTotal());
        context.setProductos(mapProductosFromRequest(request));
        return context;
    }

    private void mapFromVenta(EmailConfirmacionContext context, Venta venta) {
        context.setNumero("V-" + String.format("%05d", venta.getId()));
        context.setTipoComprobante(resolveTipoLabel(null, venta.getTipoComprobante()));
        context.setCodigoRecojo(venta.getCodigoRecojo());
        context.setSubtotal(venta.getSubtotal());
        context.setIgv(venta.getIgv());
        context.setCostoEnvio(venta.getCostoEnvio());
        context.setTotal(venta.getTotal());
        context.setProductos(mapProductosFromVenta(venta));
    }

    private List<EmailProductoLinea> mapProductosFromVenta(Venta venta) {
        List<EmailProductoLinea> lineas = new ArrayList<>();
        if (venta.getDetalles() == null) {
            return lineas;
        }
        for (DetalleVenta detalle : venta.getDetalles()) {
            String nombre = detalle.getProducto() != null
                    ? detalle.getProducto().getNombre()
                    : "Producto";
            int cantidad = detalle.getCantidad() != null ? detalle.getCantidad() : 0;
            double subtotal = detalle.getSubtotal() != null
                    ? detalle.getSubtotal()
                    : safeMultiply(detalle.getPrecioUnitario(), cantidad);
            lineas.add(new EmailProductoLinea(nombre, cantidad, subtotal));
        }
        return lineas;
    }

    private List<EmailProductoLinea> mapProductosFromRequest(EmailConfirmacionRequest request) {
        if (request.getProductos() == null || request.getProductos().isEmpty()) {
            return new ArrayList<>();
        }
        return request.getProductos().stream()
                .map(p -> new EmailProductoLinea(
                        p.getNombre(),
                        p.getCantidad(),
                        p.getSubtotal()))
                .toList();
    }

    private String resolveTipoLabel(String tipoRequest, TipoComprobante tipoVenta) {
        if (tipoVenta != null) {
            return switch (tipoVenta) {
                case BOLETA -> "Boleta electrónica";
                case FACTURA -> "Factura electrónica";
                default -> "Comprobante";
            };
        }
        if (StringUtils.isBlank(tipoRequest)) {
            return "Comprobante";
        }
        if ("factura".equalsIgnoreCase(tipoRequest)) {
            return "Factura electrónica";
        }
        if ("boleta".equalsIgnoreCase(tipoRequest)) {
            return "Boleta electrónica";
        }
        return tipoRequest;
    }

    private static double safeMultiply(Double precio, int cantidad) {
        if (precio == null || cantidad <= 0) {
            return 0.0;
        }
        return precio * cantidad;
    }
}
