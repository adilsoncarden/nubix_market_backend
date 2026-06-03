package com.nubix.market.module.user.controller;

import com.nubix.market.module.user.dto.PermisoRequest;
import com.nubix.market.module.user.dto.PermisoResponse;
import com.nubix.market.module.user.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permisos")
public class PermisoController {

    @Autowired
    private RbacService rbacService;

    @GetMapping("/modulos")
    public ResponseEntity<?> listarModulos() {
        return ResponseEntity.ok(rbacService.listarModulosPermisos());
    }

    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String modulo) {
        return ResponseEntity.ok(rbacService.listarPermisos(modulo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtener(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(rbacService.obtenerPermiso(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody PermisoRequest request) {
        try {
            PermisoResponse created = rbacService.crearPermiso(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

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
