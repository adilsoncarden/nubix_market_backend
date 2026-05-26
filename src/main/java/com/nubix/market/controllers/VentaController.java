package com.nubix.market.controllers;

import com.nubix.market.dto.VentaRequest;
import com.nubix.market.entities.Venta;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.services.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        return ResponseEntity.ok(ventaService.obtenerTodasLasVentas());
    }

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
