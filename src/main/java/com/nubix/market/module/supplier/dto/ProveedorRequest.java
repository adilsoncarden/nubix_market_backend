package com.nubix.market.module.supplier.dto;

/**
 * DTO de solicitud para crear o actualizar un proveedor.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class ProveedorRequest {
    /** Número de RUC. */
    private String ruc;
    /** Nombre descriptivo. */
    private String nombre;
    /** Teléfono de contacto. */
    private String telefono;
    /** Correo electrónico. */
    private String email;

    // Getters and Setters
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
