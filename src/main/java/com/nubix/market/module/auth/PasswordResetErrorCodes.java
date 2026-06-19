package com.nubix.market.module.auth;

/**
 * Clase de utilidad que define constantes para los códigos de error internos 
 * utilizados específicamente durante el flujo de recuperación de contraseñas.
 * Permiten que el frontend identifique el tipo de fallo de forma programática.
 */
public final class PasswordResetErrorCodes {

    /** Código emitido cuando el token de recuperación ha superado su límite de tiempo. */
    public static final String CODE_EXPIRED = "CODE_EXPIRED";

    /** Código emitido cuando el token ingresado no coincide con el registro en la base de datos. */
    public static final String INVALID_CODE = "INVALID_CODE";

    private PasswordResetErrorCodes() {
    }
}
