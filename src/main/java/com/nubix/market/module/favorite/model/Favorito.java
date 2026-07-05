package com.nubix.market.module.favorite.model;

import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.user.model.Usuario;
import jakarta.persistence.*;

/**
 * Entidad que asocia un usuario con un producto favorito.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "favoritos", uniqueConstraints = @UniqueConstraint(columnNames = { "usuario_id", "producto_id" }))
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador único. */
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    /** Usuario propietario. */
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    /** Producto asociado. */
    private Producto producto;

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
     * GetUsuario.
     * @return resultado de la operación
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * SetUsuario.
     * @param usuario Usuario propietario.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * GetProducto.
     * @return resultado de la operación
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * SetProducto.
     * @param producto Producto asociado.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
