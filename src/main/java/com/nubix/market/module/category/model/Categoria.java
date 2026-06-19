package com.nubix.market.module.category.model;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla 'categorias' en la base de datos.
 * Sirve para clasificar y agrupar los productos dentro del catálogo 
 * (por ejemplo: "Lácteos", "Abarrotes", "Limpieza"), facilitando su búsqueda y organización.
 */
@Entity
@Table(name = "categorias")

public class Categoria {

    /** Identificador único autoincremental de la categoría. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nombre principal de la categoría. Es obligatorio y tiene un límite de 100 caracteres. */
    @Column(nullable = false, length = 100)
    private String nombre;

    /** Breve descripción opcional sobre el tipo de productos que incluye esta categoría. */
    @Column(length = 500)
    private String descripcion;

    public Categoria() {
    }

    // getter and setter

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
