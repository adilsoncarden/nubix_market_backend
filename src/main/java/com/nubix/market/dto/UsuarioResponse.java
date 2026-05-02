package com.nubix.market.dto;

public class UsuarioResponse {
    private Integer id;
    private String username;
    private String email;
    private String rolNombre; // Devolvemos el nombre del rol (ej. "ADMIN") para que el frontend lo lea fácil

    // Constructores, Getters y Setters
    public UsuarioResponse() {}

    public UsuarioResponse(Integer id, String username, String email, String rolNombre) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rolNombre = rolNombre;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }
}