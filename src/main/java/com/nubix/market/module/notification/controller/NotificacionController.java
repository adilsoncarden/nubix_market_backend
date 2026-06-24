package com.nubix.market.module.notification.controller;

import com.nubix.market.module.notification.dto.NotificacionRequest;
import com.nubix.market.module.notification.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para notificaciones del usuario autenticado.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    /**
     * Obtiene notificaciones del usuario autenticado.
     * @return resultado de la operación
     */
    @GetMapping
    public ResponseEntity<?> obtenerMisNotificaciones() {
        try {
            return ResponseEntity.ok(notificacionService.obtenerMisNotificaciones());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Cuenta notificaciones no leídas.
     * @return resultado de la operación
     */
    @GetMapping("/count-no-leidas")
    public ResponseEntity<?> contarNoLeidas() {
        try {
            return ResponseEntity.ok(notificacionService.contarNoLeidas());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Crea una nueva notificación.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody NotificacionRequest request) {
        try {
            return ResponseEntity.ok(notificacionService.crear(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Marca notificación como leída.
     * @param id Identificador único.
     * @return resultado de la operación
     */
    @PutMapping("/{id}/leer")
    public ResponseEntity<?> marcarLeida(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(notificacionService.marcarLeida(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
