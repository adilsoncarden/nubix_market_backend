package com.nubix.market.module.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO de solicitud para inicio de sesión.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class LoginRequest {
    /** Nombre de usuario. */
    private String username;
    /** Correo electrónico. */
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    /** Contraseña en texto plano (solo solicitudes). */
    private String password;

    /**
     * Obtiene el nombre de usuario.
     * @return resultado de la operación
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     * @param username Nombre de usuario.
     */
    public void setUsername(String username) {
        this.username = username;
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

    /**
     * Obtiene la contraseña.
     * @return resultado de la operación
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña.
     * @param password Contraseña en texto plano (solo solicitudes).
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
