package com.nubix.market.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nubix.market.entities.Usuario;
import com.nubix.market.entities.ContraseñaResetToken;
import com.nubix.market.repositories.ReseteoContraseñaRepository;
import com.nubix.market.repositories.UsuarioRepository;

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

    public void contraseñaOlvidada(String email){
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()){
            return;
        }

        Usuario usuario = usuarioOpt.get();
        String codigo = String.format("%06d",new Random().nextInt(999999));
        ContraseñaResetToken resetCodigo = new ContraseñaResetToken();
        resetCodigo.setUsuario(usuario);
        resetCodigo.setCodigo(codigo);
        resetCodigo.setFechaExpiracion(LocalDateTime.now().plusMinutes(5));
        resetCodigo.setUtilizado(false);
        reseteoContraseñaRepository.save(resetCodigo);

        emailService.enviarCodigoRecuperacion(email, codigo);
    }

    public boolean verificarCodigo(String email, String codigo){
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        Usuario usuario = usuarioOpt.get();

        Optional<ContraseñaResetToken> codigoOpt =
        reseteoContraseñaRepository.findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(usuario);
        if (codigoOpt.isEmpty()) {
            return false;
        }

        ContraseñaResetToken token = codigoOpt.get();

        if (token.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return false;
        }

        if (!codigo.equals(token.getCodigo())) {
            token.setUtilizado(true);
            reseteoContraseñaRepository.save(token);
            return true;
        }

        return false;
    }

    public boolean resetearContraseña(String email, String nuevaContraseña){
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()){
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
        usuarioRepository.save(usuario);
        return true;
    }
}
