package com.nubix.market.module.sale.controller;

import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.module.sale.dto.CheckoutRequest;
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

@RestController
@RequestMapping("/api/ventas")
public class VentaWebController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/checkout")
    public ResponseEntity<Venta> checkout(@Valid @RequestBody CheckoutRequest request) {
        if (request.getTipoComprobante() == null) {
            request.setTipoComprobante(TipoComprobante.BOLETA);
        }
        Venta venta = ventaService.checkoutWeb(request);
        return ResponseEntity.ok(venta);
    }

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
