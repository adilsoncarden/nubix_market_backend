package com.nubix.market.module.auth.exception;

/**
 * Excepción de negocio personalizada que se lanza cuando ocurre un error 
 * durante el proceso de recuperación de contraseña (ej. código inválido o expirado).
 */
public class PasswordResetCodeException extends RuntimeException {

    /** Código interno del error para facilitar la gestión en el frontend (ej. "CODIGO_EXPIRADO"). */
    private final String errorCode;

    /**
     * Construye una nueva excepción de código de reseteo.
     *
     * @param errorCode El código identificador del error.
     * @param message   El mensaje descriptivo y legible para el usuario.
     */
    public PasswordResetCodeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Obtiene el código de error específico.
     *
     * @return El código del error como String.
     */
    public String getErrorCode() {
        return errorCode;
    }
}
