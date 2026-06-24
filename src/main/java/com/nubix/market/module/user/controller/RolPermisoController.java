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
 * Controlador REST de roles y asignación de permisos.
 * <p>
 * Expone endpoints bajo {@code /api/roles} para el CRUD de roles y la
 * sincronización de permisos asociados a cada rol.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/roles")
public class RolPermisoController {

    @Autowired
    private RbacService rbacService;

    /**
     * Lista todos los roles del sistema.
     *
     * @return respuesta HTTP 200 con la lista de roles
     */
    @GetMapping
    public ResponseEntity<?> listarRoles() {
        return ResponseEntity.ok(rbacService.listarRoles());
    }

    /**
     * Obtiene un rol por su identificador.
     *
     * @param id identificador del rol
     * @return respuesta HTTP 200 con el rol, o 404 si no existe
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
     * Crea un nuevo rol.
     *
     * @param request datos del rol a crear
     * @return respuesta HTTP 201 con el rol creado, o 400 con mensaje de error
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
     * Actualiza un rol existente.
     *
     * @param id      identificador del rol
     * @param request nuevos datos del rol
     * @return respuesta HTTP 200 con el rol actualizado, 404 si no existe,
     *         o 400 con mensaje de error
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
     * Elimina un rol del sistema.
     *
     * @param id identificador del rol a eliminar
     * @return respuesta HTTP 200 si se eliminó, 404 si no existe, 403 si es rol protegido,
     *         o 400 con mensaje de error
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
     * Obtiene los identificadores de permisos asignados a un rol.
     *
     * @param id identificador del rol
     * @return respuesta HTTP 200 con la lista de ids de permiso, o 404 si el rol no existe
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
     * Sincroniza los permisos asignados a un rol.
     *
     * @param id      identificador del rol
     * @param request lista de ids de permiso deseada
     * @return respuesta HTTP 200 con los ids asignados, o 400 con mensaje de error
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
