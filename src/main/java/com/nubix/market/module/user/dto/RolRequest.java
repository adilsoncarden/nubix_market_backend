package com.nubix.market.module.user.dto;

/**
 * DTO de solicitud para crear o actualizar un rol RBAC.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class RolRequest {

    /** Nombre del rol (obligatorio); se normaliza a mayúsculas al persistir. */
    private String nombre;

    /** Descripción opcional del propósito del rol. */
    private String descripcion;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
