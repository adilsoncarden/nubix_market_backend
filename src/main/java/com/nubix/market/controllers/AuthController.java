package com.nubix.market.controllers;

import jakarta.validation.Valid;
import com.nubix.market.dto.AuthResponse;
import com.nubix.market.dto.LoginRequest;
import com.nubix.market.dto.RegisterRequest;
import com.nubix.market.dto.ContraseñaOlvidadaRequest;
import com.nubix.market.dto.VerficarCodigoRequest;
import com.nubix.market.dto.NuevaContraseñaRequest;
import com.nubix.market.services.AuthService;
import com.nubix.market.services.RecuperaciónContraseñaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private RecuperaciónContraseñaService recuperaciónContraseñaService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }   

      @PostMapping("/admin-login")
    public ResponseEntity<AuthResponse> adminLogin(@RequestBody LoginRequest request) {
        AuthResponse response = authService.adminLogin(request);
         if(response.isSuccess()) {
            return ResponseEntity.ok(response);
         } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
         }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> contraseñaOlvidada(@RequestBody ContraseñaOlvidadaRequest request) {
        recuperaciónContraseñaService.contraseñaOlvidada(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verificarCodigo(@RequestBody VerficarCodigoRequest request) {
        boolean valid = recuperaciónContraseñaService.verificarCodigo(request.getEmail(), request.getCodigo());
        if (!valid) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok("Código verificado exitosamente");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody NuevaContraseñaRequest request) {
        //corregido: agrego request.getCodigo()
        boolean success = recuperaciónContraseñaService.resetearContraseña(
            request.getEmail(),
            request.getNuevaContraseña(),
            request.getCodigo()
      );
        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }
}
