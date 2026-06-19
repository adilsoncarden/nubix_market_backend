package com.nubix.market.module.category.dto;

/**
 * Objeto de Transferencia de Datos (DTO) utilizado para recibir la información 
 * desde el frontend cuando un administrador desea crear o actualizar una categoría.
 */
public class CategoriaRequest {

    /** El nombre de la categoría (ej. "Abarrotes", "Lácteos"). */
    private String nombre;

    /** Una breve descripción explicativa sobre los productos que agrupa esta categoría. */
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
