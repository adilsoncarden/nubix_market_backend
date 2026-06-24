package com.nubix.market.module.auth.model;

import java.time.LocalDateTime;
import com.nubix.market.module.user.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entidad que representa un token de restablecimiento de contraseña.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "password_reset_tokens")
public class ContraseñaResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador único. */
    private Long id;

    @Column(nullable = false, length = 6)
    /** Código interno. */
    private String codigo;

    @Column(nullable = false)
    /** Fecha de expiración del token. */
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    /** Indica si el token fue utilizado. */
    private boolean utilizado = false;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    /** Usuario propietario. */
    private Usuario usuario;

    public ContraseñaResetToken() {
    }

    /**
     * Obtiene el identificador.
     * @return resultado de la operación
     */
    public Long getId() {
        return id;
    }

    /**
     * GetCodigo.
     * @return resultado de la operación
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * SetCodigo.
     * @param codigo Código interno.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * GetFechaExpiracion.
     * @return resultado de la operación
     */
    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    /**
     * SetFechaExpiracion.
     * @param fechaExpiracion Fecha de expiración del token.
     */
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    /**
     * IsUtilizado.
     * @return resultado de la operación
     */
    public boolean isUtilizado() {
        return utilizado;
    }

    /**
     * SetUtilizado.
     * @param utilizado Indica si el token fue utilizado.
     */
    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
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
}
