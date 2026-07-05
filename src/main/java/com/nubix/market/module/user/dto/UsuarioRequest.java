package com.nubix.market.module.user.dto;

/**
 * DTO de solicitud para crear o actualizar un usuario (cliente o personal interno).
 * <p>
 * El rol puede indicarse por {@code rolId} o {@code rolNombre}; si ninguno se envía
 * al crear empleados, se asigna {@code EMPLEADO} por defecto.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class UsuarioRequest {

    /** Nombre de usuario para autenticación. */
    private String username;

    /** Correo electrónico del usuario. */
    private String email;

    /** Contraseña en texto plano; opcional en actualizaciones si no se desea cambiar. */
    private String password;

    /** Identificador del rol a asignar (prioridad sobre {@code rolNombre}). */
    private Integer rolId;

    /** Nombre del rol a asignar si no se proporciona {@code rolId}. */
    private String rolNombre;

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

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
}
