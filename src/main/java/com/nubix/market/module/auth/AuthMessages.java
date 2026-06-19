package com.nubix.market.module.auth;

/**
 * Clase de utilidad que centraliza los mensajes de texto estáticos 
 * relacionados con la autenticación. 
 * Facilita la consistencia en las respuestas de la API y agiliza futuras traducciones o modificaciones.
 */
public final class AuthMessages {

    /** Mensaje de seguridad estándar y ambiguo para rechazar credenciales inválidas. */
    public static final String CREDENCIALES_INVALIDAS =
            "Correo electrónico o contraseña incorrectos. Por favor, inténtelo de nuevo.";

    private AuthMessages() {
    }
}
