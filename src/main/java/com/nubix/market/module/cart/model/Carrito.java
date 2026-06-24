package com.nubix.market.module.cart.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.nubix.market.module.user.model.Usuario;

/**
 * Entidad que representa el carrito de compras de un usuario o sesión.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "carrito")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador único. */
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    /** Usuario propietario. */
    private Usuario usuario;

    @Column(nullable = true, unique = true, length = 64)
    /** Token de sesión anónima. */
    private String sessionToken;

    @Column(nullable = false)
    /** Última modificación del carrito. */
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    /** Ítems del carrito. */
    private List<CarritoItem> items = new ArrayList<>();

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
     * GetSessionToken.
     * @return resultado de la operación
     */
    public String getSessionToken() {
        return sessionToken;
    }

    /**
     * SetSessionToken.
     * @param sessionToken Token de sesión anónima.
     */
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * GetFechaActualizacion.
     * @return resultado de la operación
     */
    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    /**
     * SetFechaActualizacion.
     * @param fechaActualizacion Última modificación del carrito.
     */
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    /**
     * GetItems.
     * @return resultado de la operación
     */
    public List<CarritoItem> getItems() {
        return items;
    }

    /**
     * SetItems.
     * @param items Ítems del carrito.
     */
    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }
}
