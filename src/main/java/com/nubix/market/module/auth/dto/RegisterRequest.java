package com.nubix.market.module.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO que transporta los datos enviados por un cliente nuevo al momento de crear su cuenta.
 * Incorpora validaciones estrictas de Spring para asegurar que el formato del correo 
 * y la fortaleza de la contraseña cumplan con las políticas de seguridad antes de 
 * procesar el registro en la base de datos.
 */ 
public class RegisterRequest {

    /** Nombre de usuario elegido para la nueva cuenta. */
    @NotBlank(message = "El username es obligatorio")
    private String username;

    /** Correo electrónico del usuario, validado tanto en presencia como en formato. */
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    /** * Contraseña en texto plano para la nueva cuenta.
     * Se valida su longitud mínima y su complejidad (mayúsculas, minúsculas, números y símbolos).
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")

    // Se corrigió el regex: se eliminó el doble ^^ al inicio
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#-])[A-Za-z\\d@$!%*?&._#-]{8,}$", message = "La contraseña debe tener al menos una mayúscula, una minúscula, un número y un carácter especial")
    private String password;

    // Getters y Setters con formato limpio (mejor mantenibilidad)
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