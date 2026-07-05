package com.nubix.market.module.auth.exception;

/**
 * Excepción lanzada cuando falla la validación del código de recuperación.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public class PasswordResetCodeException extends RuntimeException {

    private final String errorCode;

    public PasswordResetCodeException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Obtiene el código de error.
     * @return resultado de la operación
     */
    public String getErrorCode() {
        return errorCode;
    }
}
