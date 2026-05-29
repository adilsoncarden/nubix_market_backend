package com.nubix.market.module.auth.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nubix.market.module.auth.model.ContraseñaResetToken;
import com.nubix.market.module.auth.repository.ReseteoContraseñaRepository;
import com.nubix.market.module.notification.service.EmailService;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;

@Service
public class RecuperaciónContraseñaService {

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

        String codigo = String.format("%06d", new Random().nextInt(999999));

        ContraseñaResetToken resetCodigo = new ContraseñaResetToken();
        resetCodigo.setUsuario(usuario);
        resetCodigo.setCodigo(codigo);
        resetCodigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(5));
        resetCodigo.setUtilizado(false);

        reseteoContraseñaRepository.save(resetCodigo);

        emailService.enviarCodigoRecuperacion(email, codigo);
    }

    public boolean verificarCodigo(String email, String codigo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();

        Optional<ContraseñaResetToken> codigoOpt = reseteoContraseñaRepository
                .findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(usuario);

        if (codigoOpt.isEmpty()) {
            return false;
        }

        ContraseñaResetToken token = codigoOpt.get();

        // Validamos expiración
        if (token.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return false;
        }

        // ✔ Solo validamos, NO consumimos el token aquí (flujo correcto)
        if (codigo != null && token.getCodigo() != null &&
                codigo.trim().equals(token.getCodigo().trim())) {
            return true;
        }

        return false;
    }

    public boolean resetearContraseña(String email, String nuevaContraseña, String codigo) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();

        Optional<ContraseñaResetToken> codigoOpt = reseteoContraseñaRepository
                .findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(usuario);

        if (codigoOpt.isEmpty()) {
            return false;
        }

        ContraseñaResetToken token = codigoOpt.get();

        // Validación final (expiración + coincidencia de código)
        if (token.getFechaExpiracion().isBefore(LocalDateTime.now()) ||
                codigo == null ||
                !codigo.trim().equals(token.getCodigo().trim())) {
            return false;
        }

        // ✔ Cambio de contraseña
        usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
        usuarioRepository.save(usuario);

        // ✔ Aquí sí consumimos el token (uso único real)
        token.setUtilizado(true);
        reseteoContraseñaRepository.save(token);

        return true;
    }
}