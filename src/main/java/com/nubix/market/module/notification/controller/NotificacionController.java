package com.nubix.market.module.notification.controller;

import com.nubix.market.module.notification.dto.NotificacionRequest;
import com.nubix.market.module.notification.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que gestiona las notificaciones internas de la aplicación (in-app alerts).
 * Permite a los usuarios consultar sus alertas pendientes, contarlas y marcarlas como leídas.
 */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    /**
     * Obtiene el historial completo de notificaciones del usuario autenticado actualmente.
     *
     * @return Respuesta HTTP 200 (OK) con la lista de notificaciones ordenadas por fecha.
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
     * Consulta rápidamente cuántas notificaciones nuevas o "no leídas" tiene el usuario.
     * Ideal para mostrar el clásico globo rojo con el número sobre el ícono de la campana.
     *
     * @return Respuesta HTTP 200 (OK) con el conteo numérico de alertas pendientes.
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
     * Crea y emite una nueva notificación dentro del sistema.
     * Puede ser utilizado por procesos internos o administradores para alertar a un usuario.
     *
     * @param request DTO con el título, mensaje y el ID del destinatario de la notificación.
     * @return Respuesta HTTP 200 (OK) con la notificación recién creada.
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
     * Actualiza el estado de una notificación específica para marcarla como "leída".
     *
     * @param id El identificador único de la notificación a actualizar.
     * @return Respuesta HTTP 200 (OK) con la notificación actualizada.
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
