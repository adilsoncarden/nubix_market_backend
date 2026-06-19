package com.nubix.market.module.notification.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nubix.market.module.user.model.Usuario;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa una alerta o notificación interna (in-app) para un usuario.
 * Se utiliza para registrar eventos importantes en la plataforma, como confirmaciones de pago, 
 * actualizaciones de stock o avisos de que un pedido está listo para recoger.
 */
@Entity
@Table(name = "notificaciones")
public class Notificacion {

    /** Identificador único autoincremental de la notificación. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** * Referencia al usuario destinatario de la notificación. 
     * Se ignora en el JSON (JsonIgnore) para evitar ciclos infinitos y ocultar datos del usuario.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    /** Categoría o tipo de notificación (ej. "pedido", "stock", "pago", "promo", "recojo"). */
    @Column(nullable = false, length = 30)
    private String tipo;

    /** Cuerpo principal de la alerta, el texto descriptivo que leerá el usuario. */
    @Column(nullable = false, length = 500)
    private String mensaje;

    /** Bandera que indica si el usuario ya ha abierto o interactuado con esta notificación. */
    @Column(nullable = false)
    private Boolean leido = false;

    /** Fecha y hora exacta en la que se generó la alerta en el sistema. */
    @Column(nullable = false)
    private LocalDateTime fecha = LocalDateTime.now();

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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getLeido() {
        return leido;
    }

    public void setLeido(Boolean leido) {
        this.leido = leido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
