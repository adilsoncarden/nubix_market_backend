package com.nubix.market.module.notification.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.module.user.model.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una notificación para un usuario.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "notificaciones")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador único. */
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    /** Usuario propietario. */
    private Usuario usuario;

    @Column(nullable = false, length = 30)
    /** Tipo del recurso. */
    private String tipo; // pedido, stock, pago, promo, recojo

    @Column(nullable = false, length = 500)
    /** Contenido del mensaje. */
    private String mensaje;

    @Column(nullable = false)
    /** Indica si fue leída. */
    private Boolean leido = false;

    @Column(nullable = false)
    /** Fecha y hora del evento. */
    private LocalDateTime fecha = LocalDateTime.now();

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
     * GetTipo.
     * @return resultado de la operación
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * SetTipo.
     * @param tipo Tipo del recurso.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * GetMensaje.
     * @return resultado de la operación
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * SetMensaje.
     * @param mensaje Contenido del mensaje.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * GetLeido.
     * @return resultado de la operación
     */
    public Boolean getLeido() {
        return leido;
    }

    /**
     * SetLeido.
     * @param leido Indica si fue leída.
     */
    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    /**
     * GetFecha.
     * @return resultado de la operación
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * SetFecha.
     * @param fecha Fecha y hora del evento.
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
