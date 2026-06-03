package com.nubix.market.module.user.controller;

import com.nubix.market.module.user.dto.RolPermisoIdsResponse;
import com.nubix.market.module.user.dto.RolPermisoSyncRequest;
import com.nubix.market.module.user.dto.RolRequest;
import com.nubix.market.module.user.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class RolPermisoController {

    @Autowired
    private RbacService rbacService;

    @GetMapping
    public ResponseEntity<?> listarRoles() {
        return ResponseEntity.ok(rbacService.listarRoles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRol(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(rbacService.obtenerRol(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> crearRol(@RequestBody RolRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(rbacService.crearRol(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarRol(
            @PathVariable Integer id,
            @RequestBody RolRequest request) {
        try {
            return ResponseEntity.ok(rbacService.actualizarRol(id, request));
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRol(@PathVariable Integer id) {
        try {
            rbacService.eliminarRol(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            if (e.getMessage().contains("Administrador Supremo")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}/permisos")
    public ResponseEntity<?> permisosDeRol(@PathVariable Integer id) {
        try {
            RolPermisoIdsResponse response = rbacService.idsPermisosDeRol(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/permisos")
    public ResponseEntity<?> sincronizarPermisos(
            @PathVariable Integer id,
            @RequestBody RolPermisoSyncRequest request) {
        try {
            return ResponseEntity.ok(rbacService.sincronizarPermisosRol(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
