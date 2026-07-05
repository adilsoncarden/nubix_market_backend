package com.nubix.market.module.user.controller;

import com.nubix.market.module.user.dto.PermisoRequest;
import com.nubix.market.module.user.dto.PermisoResponse;
import com.nubix.market.module.user.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST de permisos del sistema RBAC.
 * <p>
 * Expone endpoints bajo {@code /api/permisos} para listar módulos,
 * consultar permisos y realizar operaciones CRUD sobre permisos.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/permisos")
public class PermisoController {

    @Autowired
    private RbacService rbacService;

    /**
     * Lista los nombres de módulos distintos de permisos.
     *
     * @return respuesta HTTP 200 con la lista de módulos
     */
    @GetMapping("/modulos")
    public ResponseEntity<?> listarModulos() {
        return ResponseEntity.ok(rbacService.listarModulosPermisos());
    }

    /**
     * Lista permisos, opcionalmente filtrados por módulo.
     *
     * @param modulo nombre del módulo (opcional)
     * @return respuesta HTTP 200 con la lista de permisos
     */
    @GetMapping
    public ResponseEntity<?> listar(
            @RequestParam(required = false) String modulo) {
        return ResponseEntity.ok(rbacService.listarPermisos(modulo));
    }

    /**
     * Obtiene un permiso por su identificador.
     *
     * @param id identificador del permiso
     * @return respuesta HTTP 200 con el permiso, o 404 si no existe
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
     * Crea un nuevo permiso.
     *
     * @param request datos del permiso a crear
     * @return respuesta HTTP 201 con el permiso creado, o 400 con mensaje de error
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
     * Actualiza un permiso existente.
     *
     * @param id      identificador del permiso
     * @param request nuevos datos del permiso
     * @return respuesta HTTP 200 con el permiso actualizado, 404 si no existe,
     *         o 400 con mensaje de error
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
     * Elimina un permiso por su identificador.
     *
     * @param id identificador del permiso a eliminar
     * @return respuesta HTTP 200 si se eliminó, 404 si no existe, o 400 con mensaje de error
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
