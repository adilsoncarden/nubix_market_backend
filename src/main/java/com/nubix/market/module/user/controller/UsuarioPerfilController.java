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
 * Controlador REST del perfil del usuario autenticado.
 * <p>
 * Expone endpoints bajo {@code /api/usuarios} para consultar y actualizar
 * los datos de perfil del usuario en sesión.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioPerfilController {

    @Autowired
    private UsuarioPerfilService usuarioPerfilService;

    /**
     * Obtiene el perfil del usuario autenticado.
     *
     * @return respuesta HTTP 200 con los datos del perfil
     */
    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponse> obtenerPerfil() {
        return ResponseEntity.ok(usuarioPerfilService.obtenerPerfilActual());
    }

    /**
     * Actualiza parcialmente el perfil del usuario autenticado.
     *
     * @param request campos de perfil a actualizar (todos opcionales)
     * @return respuesta HTTP 200 con el perfil actualizado
     */
    @PutMapping("/perfil")
    public ResponseEntity<PerfilResponse> actualizarPerfil(@RequestBody PerfilUpdateRequest request) {
        return ResponseEntity.ok(usuarioPerfilService.actualizarPerfilActual(request));
    }
}
