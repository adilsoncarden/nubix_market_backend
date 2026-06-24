package com.nubix.market.module.category.dto;

/**
 * DTO de solicitud para crear o actualizar una categoría.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class CategoriaRequest {
    /** Nombre descriptivo. */
    private String nombre;
    /** Descripción detallada. */
    private String descripcion;

    /**
     * GetNombre.
     * @return resultado de la operación
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * SetNombre.
     * @param nombre Nombre descriptivo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * GetDescripcion.
     * @return resultado de la operación
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * SetDescripcion.
     * @param descripcion Descripción detallada.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
