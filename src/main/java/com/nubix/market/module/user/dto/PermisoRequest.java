package com.nubix.market.module.user.dto;

/**
 * DTO para la creación y edición de acciones específicas del sistema (Permisos).
 */
public class PermisoRequest {

    /** Nombre de la acción (ej. "EDITAR_PRODUCTO", "VER_REPORTES"). */
    private String nombre;

    /** Explicación de lo que permite hacer esta acción. */
    private String descripcion;

    /** Agrupador lógico para el frontend (ej. "CATALOGO", "VENTAS"). */
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
