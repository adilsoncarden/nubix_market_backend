package com.nubix.market.module.sale.controller;

import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.module.sale.dto.CheckoutRequest;
import com.nubix.market.module.sale.model.Venta;
import com.nubix.market.module.sale.service.VentaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
