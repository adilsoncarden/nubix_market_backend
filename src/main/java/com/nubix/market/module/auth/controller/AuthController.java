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
 * Controlador REST encargado de gestionar todos los procesos de autenticación 
 * y gestión de credenciales (Inicio de sesión, Registro y Recuperación de cuenta).
 * Sirve como el principal punto de entrada (Endpoints) para el frontend en estos flujos.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RecuperaciónContraseñaService recuperaciónContraseñaService;

    /**
     * Endpoint para registrar un nuevo usuario (generalmente clientes).
     *
     * @param request El DTO que contiene los datos del nuevo usuario (nombre, email, contraseña, etc.).
     * @return Una respuesta HTTP 201 (Created) si el registro fue exitoso, o 400 (Bad Request) 
     * si ocurrió algún error (ej. el correo ya existe).
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
     * Endpoint para iniciar sesión en la tienda pública.
     * Valida las credenciales y devuelve un token JWT.
     *
     * @param request El DTO con las credenciales (email/username y contraseña).
     * @return Respuesta HTTP 200 (OK) con el token JWT si es exitoso, o 401 (Unauthorized) si falla.
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
     * Endpoint específico para el inicio de sesión del panel administrativo.
     * Similar al login normal, pero incluye validaciones extras de permisos y roles (RBAC).
     *
     * @param request El DTO con las credenciales administrativas.
     * @return Respuesta HTTP 200 (OK) si está autorizado para el panel, o 401 (Unauthorized) si no lo está.
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
     * Endpoint que devuelve la lista de permisos exactos que posee el usuario autenticado actualmente.
     * Es consumido por el frontend (React) para mostrar u ocultar botones y secciones 
     * del panel administrativo dinámicamente.
     *
     * @return Una lista de Strings con los nombres de los permisos (ej. ["ver:productos", "crear:ventas"]).
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
     * Primer paso para recuperar la cuenta. 
     * Recibe un correo y solicita al servicio que genere y envíe un código de verificación de 6 dígitos.
     *
     * @param request El DTO que contiene únicamente el email del usuario.
     * @return Respuesta HTTP 200 indicando que el proceso inició (sin revelar si el correo existe o no por seguridad).
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<?> contraseñaOlvidada(@RequestBody ContraseñaOlvidadaRequest request) {
        recuperaciónContraseñaService.contraseñaOlvidada(request.getEmail());
        return ResponseEntity.ok().build();
    }

    /**
     * Segundo paso para recuperar la cuenta.
     * Valida que el código de 6 dígitos ingresado por el usuario coincida con el enviado por correo 
     * y no haya expirado.
     *
     * @param request El DTO con el email y el código de verificación ingresado.
     * @return Respuesta HTTP 200 confirmando que el código es correcto.
     */
    @PostMapping("/verify-code")
    public ResponseEntity<?> verificarCodigo(@RequestBody VerficarCodigoRequest request) {
        recuperaciónContraseñaService.verificarCodigo(request.getEmail(), request.getCodigo());
        return ResponseEntity.ok(java.util.Map.of("message", "Código verificado exitosamente"));
    }

    /**
     * Tercer y último paso para recuperar la cuenta.
     * Recibe la nueva contraseña, valida nuevamente el código por seguridad, 
     * y actualiza las credenciales en la base de datos.
     *
     * @param request El DTO con el email, el código y la nueva contraseña.
     * @return Respuesta HTTP 200 confirmando el cambio exitoso.
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
