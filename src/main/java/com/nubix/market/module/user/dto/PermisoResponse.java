package com.nubix.market.module.user.dto;

public class PermisoResponse {

    private Integer id;
    private String nombre;
    private String descripcion;
    private String modulo;

    public PermisoResponse() {
    }

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
