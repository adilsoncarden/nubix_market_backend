package com.nubix.market.controllers;

import com.nubix.market.entities.Usuario;
import com.nubix.market.services.UsuarioService;
import com.nubix.market.dto.UsuarioResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nubix.market.dto.UsuarioRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    private UsuarioResponse mapToResponse(com.nubix.market.entities.Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getUsername(), usuario.getEmail(), usuario.getRol().getNombre());
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<UsuarioResponse>> obtenerClientes() {
        List<UsuarioResponse> clientes = usuarioService.obtenerClientes().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(clientes, HttpStatus.OK);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<UsuarioResponse> obtenerCliente(@PathVariable Integer id) {
        return usuarioService.obtenerPorId(id)
                .map(this::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
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
    
    @GetMapping("/empleados-admins")
    public ResponseEntity<List<UsuarioResponse>> obtenerEmpleadosYAdmins() {
        List<UsuarioResponse> usuarios = usuarioService.obtenerEmpleadosYAdmins().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @PostMapping("/empleados-admins/create")
    public ResponseEntity<?> guardarEmpleado(@RequestBody UsuarioRequest request) {
        try {
            Usuario nuevoEmpleado = usuarioService.guardarEmpleado(request);
            return ResponseEntity.ok(mapToResponse(nuevoEmpleado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/empleados-admins/{id}")
    public ResponseEntity<UsuarioResponse> obtenerEmpleadosAdmins(@PathVariable Integer id){
        return usuarioService.obtenerPorId(id)
                .map(this::mapToResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/empleados-admins/{id}/update")
    public ResponseEntity<?> updateEmpleadoAdmin(@PathVariable Integer id, @RequestBody UsuarioRequest request) {
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

    @DeleteMapping("/empleados-admins/{id}/delete")
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