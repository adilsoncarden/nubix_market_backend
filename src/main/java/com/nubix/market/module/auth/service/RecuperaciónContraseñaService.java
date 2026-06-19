package com.nubix.market.module.auth.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nubix.market.module.auth.PasswordResetErrorCodes;
import com.nubix.market.module.auth.exception.PasswordResetCodeException;
import com.nubix.market.module.auth.model.ContraseñaResetToken;
import com.nubix.market.module.auth.repository.ReseteoContraseñaRepository;
import com.nubix.market.module.notification.service.EmailService;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;

/**
 * Servicio encargado de gestionar el flujo completo de recuperación de contraseñas.
 * Maneja la generación de códigos seguros, la validación de su vigencia, 
 * el envío de notificaciones por correo y la actualización final de la credencial.
 */
@Service
public class RecuperaciónContraseñaService {

    /** Tiempo de vigencia (en minutos) de un código de recuperación antes de expirar. */
    public static final int CODE_VALIDITY_MINUTES = 5;

    /** Generador de números aleatorios criptográficamente seguro para los códigos. */
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final ReseteoContraseñaRepository reseteoContraseñaRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public RecuperaciónContraseñaService(
            UsuarioRepository usuarioRepository,
            ReseteoContraseñaRepository reseteoContraseñaRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.reseteoContraseñaRepository = reseteoContraseñaRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    /**
     * Inicia el proceso de recuperación.
     * Si el correo existe, genera un código de 6 dígitos, lo guarda en la base de datos 
     * asociado al usuario y programa su envío por correo electrónico.
     * Silenciosamente ignora los correos no registrados para evitar filtraciones de seguridad.
     *
     * @param email Correo electrónico de la cuenta a recuperar.
     */
    public void contraseñaOlvidada(String email) {
        if (StringUtils.isBlank(email)) {
            return;
        }
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return;
        }

        Usuario usuario = usuarioOpt.get();

        String codigo = String.format("%06d", SECURE_RANDOM.nextInt(1_000_000));

        ContraseñaResetToken resetCodigo = new ContraseñaResetToken();
        resetCodigo.setUsuario(usuario);
        resetCodigo.setCodigo(codigo);
        resetCodigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(CODE_VALIDITY_MINUTES));
        resetCodigo.setUtilizado(false);

        reseteoContraseñaRepository.save(resetCodigo);

        emailService.enviarCodigoRecuperacion(email, codigo);
    }

    /**
     * Valida un código ingresado por el usuario contra el último código generado en el sistema.
     * Confirma que coincidan y que no haya expirado el tiempo límite.
     *
     * @param email Correo del usuario.
     * @param codigo Código de 6 dígitos ingresado.
     * @throws PasswordResetCodeException Si el código es inválido o ha expirado.
     */
    public void verificarCodigo(String email, String codigo) {
        ContraseñaResetToken token = resolveTokenForValidation(email);
        assertTokenNotExpired(token);
        assertCodeMatches(token, codigo);
    }

    /**
     * Paso final: actualiza la contraseña del usuario tras validar nuevamente el código de seguridad.
     * Al finalizar, marca el token como utilizado para evitar que vuelva a ser canjeado.
     *
     * @param email Correo del usuario.
     * @param nuevaContraseña La nueva credencial elegida.
     * @param codigo El código de seguridad para autorizar la acción.
     */
    public void resetearContraseña(String email, String nuevaContraseña, String codigo) {
        ContraseñaResetToken token = resolveTokenForValidation(email);
        assertTokenNotExpired(token);
        assertCodeMatches(token, codigo);
        
        Usuario usuario = token.getUsuario();
        usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
        usuarioRepository.save(usuario);

        token.setUtilizado(true);
        reseteoContraseñaRepository.save(token);
    }

    private ContraseñaResetToken resolveTokenForValidation(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            throw invalidCodeException();
        }

        return reseteoContraseñaRepository
                .findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(usuarioOpt.get())
                .orElseThrow(this::invalidCodeException);
    }

    private void assertTokenNotExpired(ContraseñaResetToken token) {
        if (token.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            token.setUtilizado(true);
            reseteoContraseñaRepository.save(token);
            throw new PasswordResetCodeException(
                    PasswordResetErrorCodes.CODE_EXPIRED,
                    "El código ha expirado. Solicita una nueva recuperación de contraseña.");
        }
    }

    private void assertCodeMatches(ContraseñaResetToken token, String codigo) {
        if (codigo == null
                || token.getCodigo() == null
                || !codigo.trim().equals(token.getCodigo().trim())) {
            throw invalidCodeException();
        }
    }

    private PasswordResetCodeException invalidCodeException() {
        return new PasswordResetCodeException(
                PasswordResetErrorCodes.INVALID_CODE,
                "El código ingresado no es correcto.");
    }
}
