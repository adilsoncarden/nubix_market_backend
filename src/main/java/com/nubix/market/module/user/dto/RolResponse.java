package com.nubix.market.module.user.dto;

/**
 * DTO de respuesta con los datos públicos de un rol RBAC.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class RolResponse {

    /** Identificador único del rol. */
    private Integer id;

    /** Nombre del rol. */
    private String nombre;

    /** Descripción del rol. */
    private String descripcion;

    /** Constructor por defecto. */
    public RolResponse() {
    }

    /**
     * Crea una respuesta con los datos del rol.
     *
     * @param id          identificador del rol
     * @param nombre      nombre del rol
     * @param descripcion descripción del rol
     */
    public RolResponse(Integer id, String nombre, String descripcion) {
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
