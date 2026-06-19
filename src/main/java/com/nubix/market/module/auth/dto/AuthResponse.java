package com.nubix.market.module.auth.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de transferencia de datos (DTO) que encapsula la respuesta del servidor 
 * tras un intento de inicio de sesión o registro.
 * Se encarga de enviar al frontend el resultado de la operación, el token JWT 
 * y la información de la sesión del usuario (roles y permisos).
 */
public class AuthResponse {

    /** Indica si la operación de autenticación fue exitosa. */
    private boolean success;

    /** Mensaje descriptivo sobre el resultado (ej. "Bienvenido", "Credenciales inválidas"). */
    private String message;

    /** Identificador único del usuario en la base de datos. */
    private Integer id;

    /** Nombre de usuario o correo electrónico del usuario autenticado. */
    private String username;

    /** Token de seguridad JWT generado para la sesión. */
    private String token;

    /** Rol principal asignado al usuario (ej. "ADMIN", "CLIENTE"). */
    private String rol;

    /** Lista de permisos específicos (RBAC) concedidos al usuario. */
    private List<String> permisos = new ArrayList<>();

    public AuthResponse(boolean success, String message, String username) {
        this(success, message, null, username, null, null);
    }

    public AuthResponse(boolean success, String message, String username, String token, String rol) {
        this(success, message, null, username, token, rol);
    }

    public AuthResponse(boolean success, String message, Integer id, String username, String token, String rol) {
        this.success = success;
        this.message = message;
        this.id = id;
        this.username = username;
        this.token = token;
        this.rol = rol;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<String> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<String> permisos) {
        this.permisos = permisos != null ? permisos : new ArrayList<>();
    }
}