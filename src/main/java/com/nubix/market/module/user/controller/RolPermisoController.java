package com.nubix.market.module.user.controller;

import com.nubix.market.module.user.dto.RolPermisoIdsResponse;
import com.nubix.market.module.user.dto.RolPermisoSyncRequest;
import com.nubix.market.module.user.dto.RolRequest;
import com.nubix.market.module.user.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que gestiona los Roles de usuario (ej. ADMIN, EMPLEADO, CLIENTE) 
 * y la asignación masiva de permisos a dichos roles.
 */
@RestController
@RequestMapping("/api/roles")
public class RolPermisoController {

    @Autowired
    private RbacService rbacService;

    /**
     * Devuelve el catálogo de todos los roles configurados en la plataforma.
     */
    @GetMapping
    public ResponseEntity<?> listarRoles() {
        return ResponseEntity.ok(rbacService.listarRoles());
    }

    /**
     * Busca los detalles específicos de un rol por su ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerRol(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(rbacService.obtenerRol(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una nueva agrupación o "Rol" vacío.
     */
    @PostMapping
    public ResponseEntity<?> crearRol(@RequestBody RolRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(rbacService.crearRol(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Modifica el nombre o descripción de un rol existente.
     */
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

    /**
     * Elimina un rol del sistema. Existen reglas de negocio (protegidas en el servicio) 
     * que impiden borrar roles críticos como el "Administrador Supremo" o "Cliente".
     */
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

    /**
     * Obtiene únicamente el listado de los IDs de los permisos asociados a un rol.
     * Muy útil para pre-cargar los "checkboxes" marcados en la interfaz de configuración.
     */
    @GetMapping("/{id}/permisos")
    public ResponseEntity<?> permisosDeRol(@PathVariable Integer id) {
        try {
            RolPermisoIdsResponse response = rbacService.idsPermisosDeRol(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Aplica la tabla de permisos a un rol. Recibe una lista de IDs y sobreescribe 
     * completamente los permisos anteriores del rol con los nuevos seleccionados.
     */
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
