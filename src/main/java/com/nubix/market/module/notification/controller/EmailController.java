package com.nubix.market.module.notification.controller;

import com.nubix.market.module.auth.dto.EmailConfirmacionRequest;
import com.nubix.market.module.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/confirmacion")
    public ResponseEntity<?> confirmacion(@RequestBody EmailConfirmacionRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                throw new RuntimeException("Email requerido");
            }
            emailService.enviarConfirmacionCompra(
                    request.getEmail(),
                    request.getNumero(),
                    request.getTipo(),
                    request.getCodigoRecojo(),
                    request.getTotal());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
