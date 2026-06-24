package com.nubix.market.module.auth.controller;

import jakarta.validation.Valid;
import com.nubix.market.module.auth.dto.AuthResponse;
import com.nubix.market.module.auth.dto.ContraseñaOlvidadaRequest;
import com.nubix.market.module.auth.dto.LoginRequest;
import com.nubix.market.module.auth.dto.NuevaContraseñaRequest;
import com.nubix.market.module.auth.dto.RegisterRequest;
import com.nubix.market.module.auth.dto.VerficarCodigoRequest;
import com.nubix.market.module.auth.service.AuthService;
import com.nubix.market.module.auth.service.RecuperaciónContraseñaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para registro, autenticación y recuperación de contraseña.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RecuperaciónContraseñaService recuperaciónContraseñaService;

    /**
     * Registra un nuevo usuario cliente.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    /**
     * Autentica un usuario y devuelve JWT.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Autentica un usuario con acceso al panel administrativo.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    @PostMapping("/admin-login")
    public ResponseEntity<AuthResponse> adminLogin(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.adminLogin(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    /**
     * Obtiene permisos de la sesión administrativa actual.
     * @return resultado de la operación
     */
    @GetMapping("/admin-permisos")
    public ResponseEntity<List<String>> adminPermisosSesion() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || "anonymousUser".equals(auth.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<String> permisos = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a != null && !a.startsWith("ROLE_"))
                .sorted()
                .collect(Collectors.toList());
        return ResponseEntity.ok(permisos);
    }

    /**
     * Inicia recuperación de contraseña enviando código por email.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> contraseñaOlvidada(@RequestBody ContraseñaOlvidadaRequest request) {
        recuperaciónContraseñaService.contraseñaOlvidada(request.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * Verifica el código de recuperación de contraseña.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    @PostMapping("/verify-code")
    public ResponseEntity<?> verificarCodigo(@RequestBody VerficarCodigoRequest request) {
        recuperaciónContraseñaService.verificarCodigo(request.getEmail(), request.getCodigo());
        return ResponseEntity.ok(java.util.Map.of("message", "Código verificado exitosamente"));
    }

    /**
     * Establece nueva contraseña tras validar el código.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody NuevaContraseñaRequest request) {
        recuperaciónContraseñaService.resetearContraseña(
                request.getEmail(),
                request.getNuevaContraseña(),
                request.getCodigo());
        return ResponseEntity.ok(java.util.Map.of("message", "Contraseña actualizada exitosamente"));
    }
}
