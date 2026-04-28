package com.nubix.market.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "producto_imagenes" )

public class ProductoImagen {
    
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String url;   // guardamos la ruta local o la URL de AWS

    // relación: Muchas imagenes pertenecen a un Producto
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnore // evita los bucles infinitos
    private Producto producto;

    public ProductoImagen() {}

    public ProductoImagen(String url, Producto producto) {
        this.url = url;
        this.producto = producto;
    }

    // getters and setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }
}
