package com.nubix.market.module.cart.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.nubix.market.module.user.model.Usuario;

/**
 * Entidad JPA que representa un carrito de compras general en la base de datos.
 * Funciona como el contenedor principal (Cabecera) que agrupa todos los productos 
 * seleccionados por un usuario específico antes de finalizar su compra.
 */
@Entity
@Table(name = "carrito")
public class Carrito {

    /** Identificador único autoincremental del carrito. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Relación con el cliente dueño de este carrito. Puede ser nulo para carritos de invitados. */
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    /** Token único para rastrear carritos de usuarios no autenticados (invitados). */
    @Column(nullable = true, unique = true, length = 64)
    private String sessionToken;

    /** Fecha y hora de la última modificación (útil para limpiar carritos abandonados). */
    @Column(nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    /** * Relación de uno a muchos con los items (productos) dentro del carrito.
     * CascadeType.ALL asegura que al guardar el carrito se guarden sus items, 
     * y orphanRemoval borra de la BD los items eliminados de la lista.
     */
    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoItem> items = new ArrayList<>();

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

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<CarritoItem> getItems() {
        return items;
    }

    public void setItems(List<CarritoItem> items) {
        this.items = items;
    }
}
