package com.nubix.market.module.user.dto;

/**
 * DTO de solicitud para crear o actualizar un permiso RBAC.
 * <p>
 * Los campos nombre, descripción y módulo son obligatorios en el servicio.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class PermisoRequest {

    /** Nombre único del permiso. */
    private String nombre;

    /** Descripción del alcance del permiso. */
    private String descripcion;

    /** Módulo funcional al que pertenece el permiso. */
    private String modulo;

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

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }
}
