package com.nubix.market.exception;

import com.nubix.market.config.ApiErrorResponse;
import com.nubix.market.module.auth.AuthMessages;
import com.nubix.market.module.auth.exception.PasswordResetCodeException;
import java.util.HashMap;
import java.util.Map;
import org.hibernate.LazyInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de excepciones para la API REST.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja errores de validación de argumentos de entrada.
     *
     * @param ex excepción con los errores de validación por campo
     * @return mapa de campo a mensaje de error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        log.warn("Error de validación: {}", errors);
        return errors;
    }

    /**
     * Maneja intentos de acceso a recursos sin permisos suficientes.
     *
     * @param ex excepción de acceso denegado
     * @return respuesta HTTP 403 con cuerpo de error estandarizado
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiErrorResponse.forbidden());
    }

    /**
     * Maneja credenciales inválidas o usuario inexistente.
     *
     * @param ex excepción de autenticación por credenciales incorrectas
     * @return respuesta HTTP 401 con mensaje genérico de credenciales inválidas
     */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleBadCredentials(AuthenticationException ex) {
        log.warn("Autenticación fallida: {}", ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", AuthMessages.CREDENCIALES_INVALIDAS));
    }

    /**
     * Maneja fallos generales de autenticación.
     *
     * @param ex excepción de autenticación
     * @return respuesta HTTP 401 con mensaje genérico de credenciales inválidas
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(AuthenticationException ex) {
        log.warn("Autenticación fallida: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", AuthMessages.CREDENCIALES_INVALIDAS));
    }

    /**
     * Maneja errores de carga perezosa de entidades Hibernate.
     *
     * @param ex excepción de inicialización perezosa fuera de sesión
     * @return respuesta HTTP 500 con mensaje orientado al usuario
     */
    @ExceptionHandler(LazyInitializationException.class)
    public ResponseEntity<Map<String, String>> handleLazyInitialization(
            LazyInitializationException ex) {
        log.error("LazyInitializationException", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "message",
                        "Error al cargar los datos de la cuenta. Inténtelo de nuevo o contacte al administrador."));
    }

    /**
     * Maneja errores del flujo de recuperación de contraseña.
     *
     * @param ex excepción con código y mensaje de error de negocio
     * @return respuesta HTTP 400 con código y mensaje de error
     */
    @ExceptionHandler(PasswordResetCodeException.class)
    public ResponseEntity<Map<String, String>> handlePasswordResetCode(
            PasswordResetCodeException ex) {
        log.warn("Validación de recuperación de contraseña: {}", ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("code", ex.getErrorCode(), "message", ex.getMessage()));
    }

    /**
     * Maneja excepciones de negocio en tiempo de ejecución.
     *
     * @param ex excepción con mensaje de error de negocio
     * @return respuesta HTTP 400 con el mensaje de la excepción
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        log.warn("Error de negocio: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    /**
     * Maneja excepciones no controladas.
     *
     * @param ex excepción inesperada
     * @return respuesta HTTP 500 con mensaje genérico de error interno
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        log.error("Error no controlado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "message",
                        "Ocurrió un error interno. Por favor, inténtelo de nuevo más tarde."));
    }
}
