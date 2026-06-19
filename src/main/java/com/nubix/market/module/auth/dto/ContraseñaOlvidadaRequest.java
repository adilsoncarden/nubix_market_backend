package com.nubix.market.module.auth.dto;

/**
 * DTO que representa la solicitud inicial del cliente para recuperar su cuenta.
 * Recibe desde el frontend únicamente el correo electrónico asociado al usuario 
 * que ha olvidado su contraseña.
 */
public class ContraseñaOlvidadaRequest {
    private String email;

    public ContraseñaOlvidadaRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
