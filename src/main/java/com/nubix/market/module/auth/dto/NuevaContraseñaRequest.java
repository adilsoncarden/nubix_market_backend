package com.nubix.market.module.auth.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO utilizado en el último paso del flujo de "Olvidé mi contraseña".
 * Transporta la nueva credencial del usuario junto con el código de verificación para autorizar el cambio.
 * Aplica reglas estrictas de seguridad sobre la complejidad de la contraseña.
 */
public class NuevaContraseñaRequest {

    /** Correo electrónico asociado a la cuenta que se está recuperando. */
    private String email;

    /**
     * La nueva contraseña propuesta por el usuario.
     * Pasa por un filtro estricto de validaciones automáticas:
     * - No puede estar vacía.
     * - Debe tener un mínimo de 8 caracteres.
     * - Debe cumplir con la expresión regular (al menos una mayúscula, una minúscula, un número y un símbolo).
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&._#-])[A-Za-z\\d@$!%*?&._#-]{8,}$", message = "La contraseña debe tener al menos una mayúscula, una minúscula, un número y un carácter especial")

    /** Código de 6 dígitos enviado previamente al correo del usuario. */
    private String nuevaContraseña;
    private String codigo;

    public NuevaContraseñaRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNuevaContraseña() {
        return nuevaContraseña;
    }

    public void setNuevaContraseña(String nuevaContraseña) {
        this.nuevaContraseña = nuevaContraseña;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
