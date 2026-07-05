package com.nubix.market.module.auth.dto;

/**
 * DTO de solicitud para verificar el código de recuperación.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class VerficarCodigoRequest {
    /** Código interno. */
    private String codigo;
    /** Correo electrónico. */
    private String email;

    public VerficarCodigoRequest() {
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
