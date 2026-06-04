package com.nubix.market.module.auth.dto;

import java.util.ArrayList;
import java.util.List;

public class AuthResponse {
    private boolean success;
    private String message;
    private Integer id;
    private String username;
    private String token;
    private String rol;
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