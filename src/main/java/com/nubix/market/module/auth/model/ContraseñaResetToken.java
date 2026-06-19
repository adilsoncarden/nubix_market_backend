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
 * Entidad JPA que representa la tabla 'password_reset_tokens' en la base de datos.
 * Almacena los códigos de verificación de 6 dígitos enviados a los usuarios 
 * para autorizar el cambio de su contraseña, controlando su estado y vigencia.
 */
@Entity
@Table(name = "password_reset_tokens")
public class ContraseñaResetToken {

    /** Identificador único autoincremental del registro. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Código de seguridad alfanumérico o numérico de longitud estricta (6 caracteres). */
    @Column(nullable = false, length = 6)
    private String codigo;

    /** Fecha y hora límite en la que el código es válido antes de expirar. */
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    /** Bandera que indica si el código ya fue canjeado exitosamente (evita el reuso). */
    @Column(nullable = false)
    private boolean utilizado = false;

    /** Relación con el usuario propietario de este token de recuperación. */
    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public ContraseñaResetToken() {
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public boolean isUtilizado() {
        return utilizado;
    }

    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
