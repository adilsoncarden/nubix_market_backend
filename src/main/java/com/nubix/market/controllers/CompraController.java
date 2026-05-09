package com.nubix.market.controllers;

import com.nubix.market.dto.CompraRequest;
import com.nubix.market.entities.Compra;
import com.nubix.market.services.CompraService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class CompraController {
    @Autowired
    private CompraService compraService;

    @GetMapping("/compras")
    public ResponseEntity<List<Compra>> obtenerTodas() {
        return ResponseEntity.ok(compraService.obtenerTodas());
    }

    @PostMapping("/compras/crear")
    public ResponseEntity<?> crearCompra(@RequestBody CompraRequest request) {
        try {
            return ResponseEntity.ok(compraService.crearCompra(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
