package com.nubix.market.module.user.dto;

/**
 * DTO de respuesta con datos públicos de un usuario para la API de administración.
 * <p>
 * No incluye contraseña ni datos extendidos de perfil; expone el nombre del rol
 * para consumo directo del frontend.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class UsuarioResponse {

    /** Identificador único del usuario. */
    private Integer id;

    /** Nombre de usuario para autenticación. */
    private String username;

    /** Correo electrónico del usuario. */
    private String email;

    /** Nombre del rol asignado (p. ej. {@code ADMIN}); legible por el frontend. */
    private String rolNombre; // Devolvemos el nombre del rol (ej. "ADMIN") para que el frontend lo lea fácil

    // Constructores, Getters y Setters

    /** Constructor por defecto. */
    public UsuarioResponse() {
    }

    /**
     * Crea una respuesta con todos los campos públicos del usuario.
     *
     * @param id        identificador del usuario
     * @param username  nombre de usuario
     * @param email     correo electrónico
     * @param rolNombre nombre del rol asignado
     */
    public UsuarioResponse(Integer id, String username, String email, String rolNombre) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.rolNombre = rolNombre;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
}
