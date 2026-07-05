package com.nubix.market.module.notification.controller;

import com.nubix.market.module.auth.dto.EmailConfirmacionRequest;
import com.nubix.market.module.notification.service.EmailConfirmacionBuilder;
import com.nubix.market.module.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para envío de correos transaccionales.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailConfirmacionBuilder emailConfirmacionBuilder;

    /**
     * Envía correo de confirmación de compra.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    @PostMapping("/confirmacion")
    public ResponseEntity<?> confirmacion(@RequestBody EmailConfirmacionRequest request) {
        try {
            if (request.getEmail() == null || request.getEmail().isBlank()) {
                throw new RuntimeException("Email requerido");
            }
            emailService.enviarConfirmacionCompra(emailConfirmacionBuilder.build(request));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
