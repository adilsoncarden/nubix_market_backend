package com.nubix.market.module.user.dto;

/**
 * DTO de respuesta con los datos de un permiso RBAC.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class PermisoResponse {

    /** Identificador único del permiso. */
    private Integer id;

    /** Nombre único del permiso. */
    private String nombre;

    /** Descripción del alcance del permiso. */
    private String descripcion;

    /** Módulo funcional al que pertenece el permiso. */
    private String modulo;

    /** Constructor por defecto. */
    public PermisoResponse() {
    }

    /**
     * Crea una respuesta con todos los campos del permiso.
     *
     * @param id          identificador del permiso
     * @param nombre      nombre del permiso
     * @param descripcion descripción del permiso
     * @param modulo      módulo funcional
     */
    public PermisoResponse(Integer id, String nombre, String descripcion, String modulo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.modulo = modulo;
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

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }
}
