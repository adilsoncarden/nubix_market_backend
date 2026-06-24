package com.nubix.market.module.user.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un permiso granular del sistema RBAC.
 * <p>
 * Cada permiso pertenece a un módulo funcional y puede asignarse a uno o más roles.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "permisos")
public class Permiso {

    /** Identificador único del permiso (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nombre único del permiso (máx. 120 caracteres). */
    @Column(nullable = false, unique = true, length = 120)
    private String nombre;

    /** Descripción del alcance o acción que autoriza el permiso. */
    @Column(nullable = false, length = 255)
    private String descripcion;

    /** Módulo funcional al que pertenece el permiso; por defecto {@code General}. */
    @Column(
            name = "modulo",
            length = 80,
            nullable = false,
            columnDefinition = "varchar(80) not null default 'General'")
    private String modulo = "General";

    /** Fecha y hora de creación del registro; no actualizable. */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Fecha y hora de la última modificación del registro. */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Callback JPA ejecutado antes de persistir: establece marcas de tiempo
     * y normaliza el módulo por defecto.
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (modulo == null || modulo.isBlank()) {
            modulo = "General";
        }
    }

    /**
     * Callback JPA ejecutado antes de actualizar: refresca la marca de tiempo de modificación.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /** Constructor por defecto requerido por JPA. */
    public Permiso() {
    }

    /**
     * Crea un permiso con nombre, descripción y módulo.
     *
     * @param nombre      nombre único del permiso
     * @param descripcion descripción del permiso
     * @param modulo      módulo funcional al que pertenece
     */
    public Permiso(String nombre, String descripcion, String modulo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.modulo = modulo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
