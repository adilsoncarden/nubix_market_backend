package com.nubix.market.module.supplier.dto;

/**
 * Objeto de Transferencia de Datos (DTO) que encapsula la información 
 * enviada desde el frontend para registrar o actualizar un proveedor.
 */
public class ProveedorRequest {

    /** Número de RUC de 11 dígitos. */
    private String ruc;

    /** Razón social o nombre comercial del distribuidor. */
    private String nombre;

    /** Número de celular o teléfono de contacto directo (9 dígitos). */
    private String telefono;

    /** Correo electrónico para envío de órdenes de compra. */
    private String email;

    // Getters and Setters
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
