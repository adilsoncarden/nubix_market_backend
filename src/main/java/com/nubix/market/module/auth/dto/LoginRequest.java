package com.nubix.market.module.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO que encapsula las credenciales enviadas por el cliente para intentar iniciar sesión.
 * Soporta la autenticación tanto por nombre de usuario como por correo electrónico.
 */
public class LoginRequest {

    /** Nombre de usuario (opcional si se provee el email). */
    private String username;

    /** Correo electrónico del usuario (opcional si se provee el username). */
    private String email;

    /** * Contraseña en texto plano ingresada por el usuario. 
     * La anotación @NotBlank garantiza que el framework rechace la petición si este campo viene vacío.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
