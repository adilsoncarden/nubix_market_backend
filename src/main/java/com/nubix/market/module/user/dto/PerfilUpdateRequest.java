package com.nubix.market.module.user.dto;

/**
 * DTO de solicitud para actualización parcial del perfil del usuario autenticado.
 * <p>
 * Todos los campos son opcionales; solo se actualizan los presentes en la petición.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class PerfilUpdateRequest {

    /** Número de teléfono de contacto. */
    private String telefono;

    /** Dirección textual de entrega o residencia. */
    private String direccion;

    /** Departamento de la ubicación. */
    private String departamento;

    /** Provincia de la ubicación. */
    private String provincia;

    /** Distrito de la ubicación. */
    private String distrito;

    /** Referencia adicional para ubicar la dirección. */
    private String referencia;

    /** Latitud geográfica de la dirección. */
    private Double latitud;

    /** Longitud geográfica de la dirección. */
    private Double longitud;

    /** Identificador de lugar de Google Maps. */
    private String googlePlaceId;

    /** DNI o RUC para facturación. */
    private String dniRuc;

    /** Nombre o razón social para comprobantes. */
    private String nombreRazonSocial;

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public String getGooglePlaceId() {
        return googlePlaceId;
    }

    public void setGooglePlaceId(String googlePlaceId) {
        this.googlePlaceId = googlePlaceId;
    }

    public String getDniRuc() {
        return dniRuc;
    }

    public void setDniRuc(String dniRuc) {
        this.dniRuc = dniRuc;
    }

    public String getNombreRazonSocial() {
        return nombreRazonSocial;
    }

    public void setNombreRazonSocial(String nombreRazonSocial) {
        this.nombreRazonSocial = nombreRazonSocial;
    }
}
