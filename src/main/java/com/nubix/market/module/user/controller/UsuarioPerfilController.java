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

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioPerfilController {

    @Autowired
    private UsuarioPerfilService usuarioPerfilService;

    @GetMapping("/perfil")
    public ResponseEntity<PerfilResponse> obtenerPerfil() {
        return ResponseEntity.ok(usuarioPerfilService.obtenerPerfilActual());
    }

    @PutMapping("/perfil")
    public ResponseEntity<PerfilResponse> actualizarPerfil(@RequestBody PerfilUpdateRequest request) {
        return ResponseEntity.ok(usuarioPerfilService.actualizarPerfilActual(request));
    }
}
