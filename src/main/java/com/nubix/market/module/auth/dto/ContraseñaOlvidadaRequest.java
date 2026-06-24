package com.nubix.market.module.auth.dto;

/**
 * DTO de solicitud para iniciar recuperación de contraseña.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class ContraseñaOlvidadaRequest {
    /** Correo electrónico. */
    private String email;

    public ContraseñaOlvidadaRequest() {
    }

    /**
     * Obtiene el correo electrónico.
     * @return resultado de la operación
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico.
     * @param email Correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
