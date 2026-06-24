package com.nubix.market.module.supplier.model;

import jakarta.persistence.*;

/**
 * Entidad que representa un proveedor de productos.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "proveedores")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identificador único. */
    private Integer id;

    @Column(nullable = false)
    /** Número de RUC. */
    private String ruc;

    @Column(nullable = false)
    /** Nombre descriptivo. */
    private String nombre;

    @Column(nullable = false)
    /** Teléfono de contacto. */
    private String telefono;

    @Column(nullable = false)
    /** Correo electrónico. */
    private String email;

    public Proveedor() {
    }

    // Getters and Setters
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
     * GetRuc.
     * @return resultado de la operación
     */
    public String getRuc() {
        return ruc;
    }

    /**
     * SetRuc.
     * @param ruc Número de RUC.
     */
    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    /**
     * GetNombre.
     * @return resultado de la operación
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * SetNombre.
     * @param nombre Nombre descriptivo.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * GetTelefono.
     * @return resultado de la operación
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * SetTelefono.
     * @param telefono Teléfono de contacto.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el correo electrónico.
     * @return resultado de la operación
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico.
     * @param email Correo electrónico.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}