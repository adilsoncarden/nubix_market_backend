package com.nubix.market.module.category.model;

import jakarta.persistence.*;

/**
 * Entidad que representa una categoría de productos.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "categorias")

public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador único. */
    private Integer id;

    @Column(nullable = false, length = 100)
    /** Nombre descriptivo. */
    private String nombre;

    @Column(length = 500)
    /** Descripción detallada. */
    private String descripcion;

    public Categoria() {
    }

    // getter and setter

    /**
     * Obtiene el identificador.
     * @return resultado de la operación
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador.
     * @param id Identificador único.
     */
    public void setId(Integer id) {
        this.id = id;
    }

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
