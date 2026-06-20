package com.nubix.market.module.user.controller;

import com.nubix.market.module.user.dto.PerfilResponse;
import com.nubix.market.module.user.dto.PerfilUpdateRequest;
import com.nubix.market.module.user.service.UsuarioPerfilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que permite a los usuarios gestionar su propia cuenta 
 * de manera autónoma (Autogestión de Perfil).
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioPerfilController {

    @Autowired
    private UsuarioPerfilService usuarioPerfilService;

    /**
     * Consulta y devuelve la información pública de la cuenta logueada actualmente.
     * Evita que el usuario tenga que enviar su ID, extrayéndolo directamente del JWT de seguridad.
     */
    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponse> obtenerPerfil() {
        return ResponseEntity.ok(usuarioPerfilService.obtenerPerfilActual());
    }

    /**
     * Actualiza la información personal del usuario logueado (Nombre de usuario, email 
     * e incluso la contraseña si lo solicita).
     */
    @PutMapping("/perfil")
    public ResponseEntity<PerfilResponse> actualizarPerfil(@RequestBody PerfilUpdateRequest request) {
        return ResponseEntity.ok(usuarioPerfilService.actualizarPerfilActual(request));
    }
}
