package com.nubix.market.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categorias")

public class Categoria {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Relación: Una categoría tiene muchos
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, fetch= FetchType.LAZY)
    @JsonIgnore
    private List<Producto> productos;

    public Categoria() {}

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
    public String getDescripcion(){ 
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion; 
    }
    public List<Producto> getProductos() {
        return productos;
    }
    public void setProductos(List<Producto> productos) {
        this.productos = productos; 
    }
}
