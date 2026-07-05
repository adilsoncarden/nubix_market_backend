package com.nubix.market.module.auth.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO de respuesta para operaciones de autenticación y registro.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class AuthResponse {
    /** Indica si la operación fue exitosa. */
    private boolean success;
    /** Mensaje descriptivo del resultado. */
    private String message;
    /** Identificador único. */
    private Integer id;
    /** Nombre de usuario. */
    private String username;
    /** Token JWT de autenticación. */
    private String token;
    /** Nombre del rol asignado. */
    private String rol;
    /** Lista de permisos del usuario. */
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

    /**
     * Indica si la operación fue exitosa.
     * @return resultado de la operación
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * Establece el indicador de éxito.
     * @param success Indica si la operación fue exitosa.
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * Obtiene el mensaje.
     * @return resultado de la operación
     */
    public String getMessage() {
        return message;
    }

    /**
     * Establece el mensaje.
     * @param message Mensaje descriptivo del resultado.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Obtiene el identificador.
     * @return resultado de la operación
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador.
     * @param id Identificador único.
     */
    public void setId(Integer id) {
        this.id = id;
    }

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
     * Obtiene el token JWT.
     * @return resultado de la operación
     */
    public String getToken() {
        return token;
    }

    /**
     * Establece el token JWT.
     * @param token Token JWT de autenticación.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Obtiene el rol.
     * @return resultado de la operación
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol.
     * @param rol Nombre del rol asignado.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Obtiene los permisos.
     * @return resultado de la operación
     */
    public List<String> getPermisos() {
        return permisos;
    }

    /**
     * Establece los permisos.
     * @param permisos Lista de permisos del usuario.
     */
    public void setPermisos(List<String> permisos) {
        this.permisos = permisos != null ? permisos : new ArrayList<>();
    }
}