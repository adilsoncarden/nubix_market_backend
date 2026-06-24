package com.nubix.market.module.auth.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO de solicitud para establecer una nueva contraseña.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class NuevaContraseñaRequest {
    /** Correo electrónico. */
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#-])[A-Za-z\\d@$!%*?&._#-]{8,}$", message = "La contraseña debe tener al menos una mayúscula, una minúscula, un número y un carácter especial")

    /** Nueva contraseña a establecer. */
    private String nuevaContraseña;
    /** Código interno. */
    private String codigo;

    public NuevaContraseñaRequest() {
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

    /**
     * GetNuevaContraseña.
     * @return resultado de la operación
     */
    public String getNuevaContraseña() {
        return nuevaContraseña;
    }

    /**
     * SetNuevaContraseña.
     * @param nuevaContraseña Nueva contraseña a establecer.
     */
    public void setNuevaContraseña(String nuevaContraseña) {
        this.nuevaContraseña = nuevaContraseña;
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
}
