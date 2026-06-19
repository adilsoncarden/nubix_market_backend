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
 * Controlador REST encargado de exponer endpoints para el disparo manual o programático 
 * de correos electrónicos transaccionales.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailConfirmacionBuilder emailConfirmacionBuilder;

    /**
     * Endpoint para enviar un correo de confirmación de compra a un cliente.
     * Recibe los datos de la venta, construye la plantilla HTML dinámica y la envía.
     *
     * @param request DTO con los detalles financieros y logísticos de la venta.
     * @return Respuesta HTTP 200 (OK) si el correo fue encolado/enviado exitosamente, 
     * o HTTP 400 (Bad Request) si falta el correo del destinatario.
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
