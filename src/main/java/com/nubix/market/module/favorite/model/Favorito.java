package com.nubix.market.module.favorite.model;

import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.user.model.Usuario;
import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla intermedia de 'favoritos' en la base de datos.
 * Establece una relación muchos-a-muchos entre Usuarios y Productos.
 * Incluye una restricción de unicidad para garantizar que un usuario no pueda 
 * agregar el mismo producto a favoritos más de una vez.
 */
@Entity
@Table(name = "favoritos", uniqueConstraints = @UniqueConstraint(columnNames = { "usuario_id", "producto_id" }))
public class Favorito {

    /** Identificador único autoincremental del registro de favorito. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** * El usuario que marcó el producto. 
     * FetchType.LAZY retrasa la carga del usuario hasta que sea estrictamente necesario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** * El producto que fue guardado. 
     * FetchType.EAGER fuerza la carga del producto inmediatamente, útil para mostrar la lista en frontend.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
