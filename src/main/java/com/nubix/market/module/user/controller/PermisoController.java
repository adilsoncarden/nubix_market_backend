package com.nubix.market.module.user.controller;

import com.nubix.market.module.user.dto.PermisoRequest;
import com.nubix.market.module.user.dto.PermisoResponse;
import com.nubix.market.module.user.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST encargado de la gestión de los permisos individuales del sistema.
 * Permite listar, crear, editar y eliminar las "acciones" específicas que luego 
 * serán agrupadas y asignadas a los roles.
 */
@RestController
@RequestMapping("/api/permisos")
public class PermisoController {

    @Autowired
    private RbacService rbacService;

    /**
     * Devuelve una lista de todos los "módulos" registrados (ej. VENTAS, PRODUCTOS, REPORTES) 
     * para facilitar la agrupación visual de los permisos en el frontend.
     */
    @GetMapping("/modulos")
    public ResponseEntity<?> listarModulos() {
        return ResponseEntity.ok(rbacService.listarModulosPermisos());
    }

    /**
     * Lista todos los permisos del sistema. Opcionalmente se puede filtrar por un módulo específico.
     */
    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String modulo) {
        return ResponseEntity.ok(rbacService.listarPermisos(modulo));
    }

    /**
     * Obtiene el detalle de un permiso específico por su identificador.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(rbacService.obtenerPermiso(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Registra un nuevo permiso en el sistema.
     */
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PermisoRequest request) {
        try {
            PermisoResponse created = rbacService.crearPermiso(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Modifica los datos descriptivos de un permiso existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Integer id,
            @RequestBody PermisoRequest request) {
        try {
            return ResponseEntity.ok(rbacService.actualizarPermiso(id, request));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina un permiso del sistema, retirándolo automáticamente de cualquier 
     * rol que lo tuviera asignado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            rbacService.eliminarPermiso(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
