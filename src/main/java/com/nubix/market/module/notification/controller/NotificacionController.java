package com.nubix.market.module.notification.controller;

import com.nubix.market.module.notification.dto.NotificacionRequest;
import com.nubix.market.module.notification.service.NotificacionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<?> obtenerMisNotificaciones() {
        try {
            return ResponseEntity.ok(notificacionService.obtenerMisNotificaciones());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/count-no-leidas")
    public ResponseEntity<?> contarNoLeidas() {
        try {
            return ResponseEntity.ok(notificacionService.contarNoLeidas());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody NotificacionRequest request) {
        try {
            return ResponseEntity.ok(notificacionService.crear(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<?> marcarLeida(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(notificacionService.marcarLeida(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
