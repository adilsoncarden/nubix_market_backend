package com.nubix.market.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
public class ContraseñaResetToken {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private boolean utilizado = false;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    public ContraseñaResetToken() {
    }

    // Getters y Setters
    public Long getId() {return id;}
    public String getCodigo() {return codigo;}
    public void setCodigo(String codigo) {this.codigo = codigo;}
    public LocalDateTime getFechaExpiracion() {return fechaExpiracion;}
    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {this.fechaExpiracion = fechaExpiracion;}
    public boolean isUtilizado() {return utilizado;}
    public void setUtilizado(boolean utilizado) {this.utilizado = utilizado;}
    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) {this.usuario = usuario;}
}
