package com.nubix.market.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "producto_imagenes" )
public class ProductoImagen {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String archivo;   // guardamos la ruta local o la URL de AWS

    public ProductoImagen() {}

    public ProductoImagen(String archivo, Producto producto) {
        this.archivo = archivo;
    }

    // getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getArchivo() { return archivo; }
    public void setArchivo(String archivo) { this.archivo = archivo; }
}
