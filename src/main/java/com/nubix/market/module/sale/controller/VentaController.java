package com.nubix.market.module.sale.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nubix.market.common.jackson.JsonViews;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.module.sale.dto.VentaRequest;
import com.nubix.market.module.sale.model.Venta;
import com.nubix.market.module.sale.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @JsonView(JsonViews.List.class)
    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        return ResponseEntity.ok(ventaService.obtenerTodasLasVentas());
    }

    @JsonView(JsonViews.Detail.class)
    @GetMapping("/ventas/{id}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ventaService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/ventas/create")
    public ResponseEntity<?> crearVenta(@RequestBody VentaRequest request) {
        try {
            if (request.getCanal() == null) {
                request.setCanal(com.nubix.market.enums.CanalVenta.PRESENCIAL);
            }
            if (request.getTipoComprobante() == null) {
                request.setTipoComprobante(com.nubix.market.enums.TipoComprobante.TICKET);
            }
            return ResponseEntity.ok(ventaService.crearVenta(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/ventas/{id}")
    public ResponseEntity<?> actualizarEstadoPedido(
            @PathVariable Integer id,
            @RequestParam EstadoPedido estado) {
        try {
            return ResponseEntity.ok(ventaService.actualizarEstadoPedido(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/ventas/{id}/credito")
    public ResponseEntity<?> registrarCredito(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ventaService.registrarCredito(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
