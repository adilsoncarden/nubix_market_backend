package com.nubix.market.module.auth.dto;

/**
 * DTO utilizado en el paso intermedio de la recuperación de cuenta.
 * Transporta el código de seguridad de 6 dígitos que el usuario recibió en su correo
 * para que el backend valide si es correcto y aún no ha expirado.
 */
public class VerficarCodigoRequest {

    /** El código de verificación alfanumérico ingresado por el usuario. */
    private String codigo;

    /** El correo electrónico asociado al intento de recuperación. */
    private String email;

    public VerficarCodigoRequest() {
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
