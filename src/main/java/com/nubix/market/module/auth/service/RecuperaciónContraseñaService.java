package com.nubix.market.module.auth.service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nubix.market.module.auth.PasswordResetErrorCodes;
import com.nubix.market.module.auth.exception.PasswordResetCodeException;
import com.nubix.market.module.auth.model.ContraseñaResetToken;
import com.nubix.market.module.auth.repository.ReseteoContraseñaRepository;
import com.nubix.market.module.notification.service.EmailService;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;

@Service
public class RecuperaciónContraseñaService {

    public static final int CODE_VALIDITY_MINUTES = 5;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReseteoContraseñaRepository reseteoContraseñaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public void contraseñaOlvidada(String email) {
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

    public void verificarCodigo(String email, String codigo) {
        ContraseñaResetToken token = resolveTokenForValidation(email);
        assertTokenNotExpired(token);
        assertCodeMatches(token, codigo);
    }

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
