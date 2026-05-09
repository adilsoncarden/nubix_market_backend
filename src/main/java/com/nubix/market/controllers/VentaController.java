package com.nubix.market.controllers;

import com.nubix.market.entities.Venta;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.services.VentaService;
import com.nubix.market.dto.VentaRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/ventas/create")
    public ResponseEntity<?> crearVenta(@RequestBody VentaRequest request) {
        try {
            return ResponseEntity.ok(ventaService.crearVenta(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/ventas/{id}")
    public ResponseEntity<?> actualizarEstadoPedido(@PathVariable Integer id, @RequestParam EstadoPedido estado) {
        try {
            return ResponseEntity.ok(ventaService.actualizarEstadoPedido(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/ventas/{id}/credito")
    public ResponseEntity<?> registrarCredito(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ventaService.RegistrarCredito(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
