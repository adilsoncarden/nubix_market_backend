package com.nubix.market.module.sale.controller;

import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.module.sale.dto.CheckoutRequest;
import com.nubix.market.module.sale.dto.StripeCargoRequest;
import com.nubix.market.module.sale.dto.StripeCargoResponse;
import com.nubix.market.module.sale.dto.MisPedidoResponse;
import com.nubix.market.module.sale.model.Venta;
import com.nubix.market.module.sale.service.VentaService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para el canal web de ventas.
 * Gestiona el checkout de pedidos y la consulta de pedidos del cliente autenticado.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/ventas")
public class VentaWebController {

    @Autowired
    private VentaService ventaService;

    /**
     * Procesa el checkout de un pedido web a partir del carrito del cliente.
     *
     * @param request datos del checkout (productos, comprobante, entrega y pago)
     * @return respuesta HTTP 200 con la venta creada
     */
    @PostMapping("/checkout")
    public ResponseEntity<Venta> checkout(@Valid @RequestBody CheckoutRequest request) {
        if (request.getTipoComprobante() == null) {
            request.setTipoComprobante(TipoComprobante.BOLETA);
        }
        Venta venta = ventaService.checkoutWeb(request);
        return ResponseEntity.ok(venta);
    }

    /**
     * Procesa un cargo con tarjeta vía Stripe y crea el pedido web si el PaymentIntent es exitoso.
     *
     * @param request PaymentMethod Stripe, correo, monto y datos del checkout
     * @return respuesta HTTP 200 con el PaymentIntent y la venta registrada
     */
    @PostMapping("/cargo")
    public ResponseEntity<StripeCargoResponse> procesarCargoStripe(
            @Valid @RequestBody StripeCargoRequest request) {
        if (request.getCheckout().getTipoComprobante() == null) {
            request.getCheckout().setTipoComprobante(TipoComprobante.BOLETA);
        }
        StripeCargoResponse response = ventaService.procesarCargoTarjetaStripe(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista los pedidos web del cliente autenticado, con filtro opcional por período.
     *
     * @param mes          período predefinido ({@code actual}, {@code todos}, {@code trimestre}, etc.)
     * @param fechaInicio  fecha inicial del rango personalizado (inclusive)
     * @param fechaFin     fecha final del rango personalizado (inclusive)
     * @return respuesta HTTP 200 con la lista de pedidos del cliente
     */
    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<MisPedidoResponse>> misPedidos(
            @RequestParam(required = false) String mes,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate fechaFin) {
        return ResponseEntity.ok(ventaService.listarMisPedidosWeb(mes, fechaInicio, fechaFin));
    }
}
