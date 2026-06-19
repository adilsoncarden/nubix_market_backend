package com.nubix.market.module.identidad.dto;

/**
 * Objeto de Transferencia de Datos (DTO) que estandariza la respuesta de identidad 
 * enviada al frontend. Sirve como un "molde" limpio que oculta las diferencias y 
 * complejidades del JSON original devuelto por la API externa (Apisperu).
 */
public class IdentidadConsultaResponse {

    /** El número de documento consultado (DNI o RUC). */
    private String documento;

    /** El tipo de documento detectado ("DNI" o "RUC"). */
    private String tipo;

    /** Nombre completo de la persona (para DNI) o Razón Social (para RUC). */
    private String nombreRazonSocial;

    /** Departamento fiscal asociado al RUC (nulo para DNI). */
    private String departamento;

    /** Provincia fiscal asociada al RUC (nulo para DNI). */
    private String provincia;

    /** Distrito fiscal asociado al RUC (nulo para DNI). */
    private String distrito;

    /** Dirección completa o domicilio fiscal (principalmente para RUC). */
    private String direccion;

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
