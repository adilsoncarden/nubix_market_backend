package com.nubix.market.module.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

/**
 * Entidad JPA que representa un usuario del sistema Nubix Market.
 * <p>
 * Almacena credenciales de acceso, rol asignado, datos de contacto, ubicación
 * geográfica y datos de facturación (DNI/RUC y razón social).
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Entity
@Table(name = "usuario")
public class Usuario {
    
    /** Identificador único del usuario (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** Nombre de usuario único para autenticación (máx. 50 caracteres). */
    @Column(nullable = false, unique = true, length = 50)
    private String username;


    /** Correo electrónico único del usuario (máx. 100 caracteres). */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /** Contraseña codificada del usuario; excluida de serialización JSON. */
    @Column(nullable = false)
    @JsonIgnore
    private String password;


    /** Rol asignado al usuario; determina permisos y tipo de cuenta. */
    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    /** Número de teléfono de contacto (máx. 20 caracteres). */
    @Column(length = 20)
    private String telefono;

    /** Dirección textual de entrega o residencia. */
    @Column(length = 255)
    private String direccion;

    /** Departamento de la ubicación (Perú). */
    @Column(length = 80)
    private String departamento;

    /** Provincia de la ubicación (Perú). */
    @Column(length = 80)
    private String provincia;

    /** Distrito de la ubicación (Perú). */
    @Column(length = 80)
    private String distrito;

    /** Referencia adicional para ubicar la dirección. */
    @Column(length = 255)
    private String referencia;

    /** Latitud geográfica de la dirección del usuario. */
    @Column
    private Double latitud;

    /** Longitud geográfica de la dirección del usuario. */
    @Column
    private Double longitud;

    /** Identificador de lugar de Google Maps asociado a la dirección. */
    @Column(name = "google_place_id", length = 255)
    private String googlePlaceId;

    /** Documento de identidad o RUC para facturación (máx. 11 caracteres). */
    @Column(name = "dni_ruc", length = 11)
    private String dniRuc;

    /** Nombre o razón social para comprobantes de pago. */
    @Column(name = "nombre_razon_social", length = 255)
    private String nombreRazonSocial;

    /** Constructor por defecto requerido por JPA. */
    public Usuario() {
    }
    
    /**
     * Crea un usuario con credenciales básicas (sin rol asignado en este constructor).
     *
     * @param username nombre de usuario
     * @param email    correo electrónico
     * @param password contraseña en texto plano (debe codificarse antes de persistir)
     */
    public Usuario(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

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
