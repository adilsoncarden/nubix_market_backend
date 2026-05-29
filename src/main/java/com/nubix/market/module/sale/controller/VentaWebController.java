package com.nubix.market.module.sale.controller;

import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.module.sale.dto.CheckoutRequest;
import com.nubix.market.module.sale.model.Venta;
import com.nubix.market.module.sale.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaWebController {

    @Autowired
    private VentaService ventaService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest request) {
        try {
            if (request.getTipoComprobante() == null) {
                request.setTipoComprobante(TipoComprobante.BOLETA);
            }
            Venta venta = ventaService.checkoutWeb(request);
            return ResponseEntity.ok(venta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
