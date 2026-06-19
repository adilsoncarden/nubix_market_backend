package com.nubix.market.module.category.dto;

/**
 * Objeto de Transferencia de Datos (DTO) utilizado para enviar la información 
 * de una categoría desde el backend hacia el frontend.
 * Filtra y expone únicamente los datos necesarios y seguros para la vista.
 */
public class CategoriaResponse {

    /** Identificador único de la categoría en la base de datos. */
    private Integer id;

    /** Nombre representativo de la categoría. */
    private String nombre;
    
    /** Descripción detallada de la categoría. */
    private String descripcion;

    public CategoriaResponse(Integer id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
