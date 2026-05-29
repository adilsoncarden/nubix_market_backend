package com.nubix.market.module.auth.dto;

public class AuthResponse {
    private boolean success;
    private String message;
    private String username;
    private String token;
    private String rol;

    public AuthResponse(boolean success, String message, String username) {
        this(success, message, username, null, null);
    }

    public AuthResponse(boolean success, String message, String username, String token, String rol) {
        this.success = success;
        this.message = message;
        this.username = username;
        this.token = token;
        this.rol = rol;
    }

    // Getters y Setters
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
}