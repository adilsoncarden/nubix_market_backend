package com.nubix.market.module.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un rol del sistema RBAC.
 * <p>
 * Agrupa permisos mediante una relación muchos-a-muchos y define el perfil
 * de acceso de los usuarios asignados a dicho rol.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "roles")
public class Rol {

    /** Identificador único del rol (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nombre único del rol (p. ej. {@code ADMIN}, {@code EMPLEADO}, {@code CLIENTE}). */
    @Column(nullable = false, unique = true)
    private String nombre;

    /** Descripción legible del propósito del rol. */
    @Column(length = 500)
    private String descripcion;

    /** Conjunto de permisos asignados a este rol; carga lazy. */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permiso",
            joinColumns = @JoinColumn(
                    name = "role_id",
                    foreignKey = @ForeignKey(
                            foreignKeyDefinition = "FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE")),
            inverseJoinColumns = @JoinColumn(
                    name = "permiso_id",
                    foreignKey = @ForeignKey(
                            foreignKeyDefinition = "FOREIGN KEY (permiso_id) REFERENCES permisos(id) ON DELETE CASCADE")))
    private Set<Permiso> permisos = new HashSet<>();

    /**
     * Crea un rol con el nombre indicado.
     *
     * @param nombre nombre del rol
     */
    public Rol(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Crea un rol con nombre y descripción.
     *
     * @param nombre      nombre del rol
     * @param descripcion descripción del rol
     */
    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    /** Constructor por defecto requerido por JPA. */
    public Rol() {
    }

    // Getters y Setters

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

    @JsonIgnore
    public Set<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(Set<Permiso> permisos) {
        this.permisos = permisos != null ? permisos : new HashSet<>();
    }

}
