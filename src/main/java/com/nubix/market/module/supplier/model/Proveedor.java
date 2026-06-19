package com.nubix.market.module.supplier.model;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla 'proveedores' en la base de datos.
 * Almacena el directorio corporativo de las empresas que abastecen de productos al negocio.
 */
@Entity
@Table(name = "proveedores")
public class Proveedor {

    /** Identificador único autoincremental del proveedor. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Registro Único de Contribuyente (11 dígitos). Es único por empresa. */
    @Column(nullable = false)
    private String ruc;

    /** Nombre comercial de la distribuidora. */
    @Column(nullable = false)
    private String nombre;

    /** Teléfono principal del vendedor o contacto en la empresa. */
    @Column(nullable = false)
    private String telefono;

    /** Correo electrónico formal del proveedor. */
    @Column(nullable = false)
    private String email;

    public Proveedor() {
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}