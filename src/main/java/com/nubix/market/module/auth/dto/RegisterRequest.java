package com.nubix.market.module.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO de solicitud para registro de un nuevo usuario.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class RegisterRequest {

    @NotBlank(message = "El username es obligatorio")
    /** Nombre de usuario. */
    private String username;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    /** Correo electrónico. */
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    // Se corrigió el regex: se eliminó el doble ^^ al inicio
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#-])[A-Za-z\\d@$!%*?&._#-]{8,}$", message = "La contraseña debe tener al menos una mayúscula, una minúscula, un número y un carácter especial")
    /** Contraseña en texto plano (solo solicitudes). */
    private String password;

    // Getters y Setters con formato limpio (mejor mantenibilidad)
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