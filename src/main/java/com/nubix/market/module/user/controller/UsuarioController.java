package com.nubix.market.module.user.controller;

import com.nubix.market.module.user.dto.UsuarioRequest;
import com.nubix.market.module.user.dto.UsuarioResponse;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST de administración de usuarios.
 * <p>
 * Expone endpoints bajo {@code /api/admin} para gestionar clientes de la tienda
 * y personal interno (empleados y administradores).
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/admin")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    /**
     * Convierte una entidad {@link Usuario} a su DTO de respuesta para la API.
     *
     * @param usuario entidad de usuario con rol cargado
     * @return DTO {@link UsuarioResponse} con id, credenciales y nombre de rol
     */
    private UsuarioResponse mapToResponse(com.nubix.market.module.user.model.Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getUsername(), usuario.getEmail(),
                usuario.getRol().getNombre());
    }

    /**
     * Lista todos los clientes de la tienda.
     *
     * @return respuesta HTTP 200 con la lista de clientes
     */
    @GetMapping("/clientes")
    public ResponseEntity<List<UsuarioResponse>> obtenerClientes() {
        List<UsuarioResponse> clientes = usuarioService.obtenerClientes().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    /**
     * Obtiene un cliente por su identificador.
     *
     * @param id identificador del cliente
     * @return respuesta HTTP 200 con el cliente, o 404 si no existe
     */
    @GetMapping("/clientes/{id}")
    public ResponseEntity<UsuarioResponse> obtenerCliente(@PathVariable Integer id) {
        return usuarioService.obtenerPorId(id)
                .map(this::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id      identificador del cliente
     * @param request datos actualizados del cliente
     * @return respuesta HTTP 200 con el cliente actualizado, 404 si no existe,
     *         o 400 con mensaje de error de validación
     */
    @PostMapping("/clientes/{id}/update")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody UsuarioRequest request) {
        try {
            Usuario actualizado = usuarioService.actualizar(id, request);
            return ResponseEntity.ok(mapToResponse(actualizado));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Usuario no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Lista el personal interno (empleados y administradores).
     *
     * @return respuesta HTTP 200 con la lista de usuarios internos
     */
    @GetMapping("/empleados")
    public ResponseEntity<List<UsuarioResponse>> obtenerEmpleadosYAdmins() {
        List<UsuarioResponse> usuarios = usuarioService.obtenerEmpleadosYAdmins().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    /**
     * Crea un nuevo empleado o administrador asignable.
     *
     * @param request datos del nuevo usuario interno
     * @return respuesta HTTP 200 con el usuario creado, o 400 con mensaje de error
     */
    @PostMapping("/empleados/create")
    public ResponseEntity<?> guardarEmpleado(@RequestBody UsuarioRequest request) {
        try {
            Usuario nuevoEmpleado = usuarioService.guardarEmpleado(request);
            return ResponseEntity.ok(mapToResponse(nuevoEmpleado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtiene un empleado o administrador por su identificador.
     *
     * @param id identificador del usuario interno
     * @return respuesta HTTP 200 con el usuario, o 404 si no existe
     */
    @GetMapping("/empleados/{id}")
    public ResponseEntity<UsuarioResponse> obtenerEmpleadosAdmins(@PathVariable Integer id) {
        return usuarioService.obtenerPorId(id)
                .map(this::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza los datos de un empleado o administrador.
     *
     * @param id      identificador del usuario interno
     * @param request datos actualizados
     * @return respuesta HTTP 200 con el usuario actualizado, 404 si no existe,
     *         o 400 con mensaje de error de validación
     */
    @PostMapping("/empleados/{id}/update")
    public ResponseEntity<?> updateEmpleadoAdmin(@PathVariable Integer id, @RequestBody UsuarioRequest request) {
        try {
            Usuario actualizado = usuarioService.actualizarEmpleado(id, request);
            return ResponseEntity.ok(mapToResponse(actualizado));
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Usuario no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Elimina un empleado o administrador por su identificador.
     *
     * @param id identificador del usuario a eliminar
     * @return respuesta HTTP 200 si se eliminó correctamente, 404 si no existe,
     *         o 400 con mensaje de error (p. ej. Administrador Supremo)
     */
    @DeleteMapping("/empleados/{id}/delete")
    public ResponseEntity<?> deleteEmpleadoAdmin(@PathVariable Integer id) {
        try {
            usuarioService.eliminar(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().equals("Usuario no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}