package com.nubix.market.module.identidad.dto;

/**
 * DTO de respuesta con datos obtenidos de la consulta de identidad.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class IdentidadConsultaResponse {

    /** Número de documento (DNI o RUC). */
    private String documento;
    /** Tipo del recurso. */
    private String tipo;
    /** Nombre o razón social. */
    private String nombreRazonSocial;
    /** Departamento (consulta RUC). */
    private String departamento;
    /** Provincia (consulta RUC). */
    private String provincia;
    /** Distrito (consulta RUC). */
    private String distrito;
    /** Dirección fiscal o de entrega. */
    private String direccion;

    /**
     * GetDocumento.
     * @return resultado de la operación
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * SetDocumento.
     * @param documento Número de documento (DNI o RUC).
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * GetTipo.
     * @return resultado de la operación
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * SetTipo.
     * @param tipo Tipo del recurso.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * GetNombreRazonSocial.
     * @return resultado de la operación
     */
    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    /**
     * SetNombreRazonSocial.
     * @param nombreRazonSocial Nombre o razón social.
     */
    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }

    /**
     * GetDepartamento.
     * @return resultado de la operación
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * SetDepartamento.
     * @param departamento Departamento (consulta RUC).
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    /**
     * GetProvincia.
     * @return resultado de la operación
     */
    public String getProvincia() {
        return provincia;
    }

    /**
     * SetProvincia.
     * @param provincia Provincia (consulta RUC).
     */
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    /**
     * GetDistrito.
     * @return resultado de la operación
     */
    public String getDistrito() {
        return distrito;
    }

    /**
     * SetDistrito.
     * @param distrito Distrito (consulta RUC).
     */
    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    /**
     * GetDireccion.
     * @return resultado de la operación
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * SetDireccion.
     * @param direccion Dirección fiscal o de entrega.
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
