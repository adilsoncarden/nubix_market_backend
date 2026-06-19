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
 * Interceptor global de excepciones para toda la API REST.
 * Actúa como un escudo que captura los errores lanzados en cualquier controlador 
 * o servicio, y los transforma en respuestas HTTP con formato JSON estandarizado 
 * para el frontend.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Captura los errores de validación de los DTOs (cuando fallan las anotaciones como @NotNull o @NotBlank).
     *
     * @param ex La excepción lanzada automáticamente por Spring Boot al fallar la validación.
     * @return Un mapa asociando el nombre del campo que falló con su respectivo mensaje de error.
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
     * Captura los intentos de acceso a rutas protegidas por usuarios sin los permisos adecuados (RBAC).
     *
     * @param ex La excepción de seguridad lanzada por Spring Security.
     * @return Respuesta HTTP 403 (Forbidden) con un formato JSON estructurado.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Acceso denegado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiErrorResponse.forbidden());
    }

    /**
     * Captura los errores específicos de inicio de sesión (usuario incorrecto o contraseña inválida).
     *
     * @param ex La excepción de autenticación específica.
     * @return Respuesta HTTP 401 (Unauthorized) ocultando el motivo exacto por seguridad.
     */
    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleBadCredentials(AuthenticationException ex) {
        log.warn("Autenticación fallida: {}", ex.getClass().getSimpleName());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", AuthMessages.CREDENCIALES_INVALIDAS));
    }

    /**
     * Captura cualquier otro error general relacionado con el proceso de autenticación de Spring Security.
     *
     * @param ex La excepción genérica de autenticación.
     * @return Respuesta HTTP 401 (Unauthorized).
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleAuthentication(AuthenticationException ex) {
        log.warn("Autenticación fallida: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", AuthMessages.CREDENCIALES_INVALIDAS));
    }

    /**
     * Captura problemas de inicialización perezosa (Lazy) de Hibernate, comúnmente causados 
     * por intentar acceder a relaciones de base de datos fuera de una transacción activa.
     *
     * @param ex La excepción lanzada por Hibernate.
     * @return Respuesta HTTP 500 (Internal Server Error) con un mensaje amigable.
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
     * Captura errores de lógica de negocio relacionados con el flujo de recuperación de contraseñas.
     *
     * @param ex La excepción personalizada que contiene el código de error.
     * @return Respuesta HTTP 400 (Bad Request) con detalles del fallo.
     */
    @ExceptionHandler(PasswordResetCodeException.class)
    public ResponseEntity<Map<String, String>> handlePasswordResetCode(
            PasswordResetCodeException ex) {
        log.warn("Validación de recuperación de contraseña: {}", ex.getErrorCode());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("code", ex.getErrorCode(), "message", ex.getMessage()));
    }

    /**
     * Captura errores de tiempo de ejecución (RuntimeException), generalmente utilizados 
     * para lanzar validaciones de negocio personalizadas a lo largo del servicio.
     *
     * @param ex La excepción de negocio.
     * @return Respuesta HTTP 400 (Bad Request) propagando el mensaje exacto de la excepción.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        log.warn("Error de negocio: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of("message", ex.getMessage()));
    }

    /**
     * Captura final para cualquier error no previsto o crítico (NullPointer, caídas de base de datos, etc.).
     * Previene la fuga de información sensible al cliente (StackTraces).
     *
     * @param ex La excepción inesperada.
     * @return Respuesta HTTP 500 (Internal Server Error) con un mensaje genérico de disculpas.
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
