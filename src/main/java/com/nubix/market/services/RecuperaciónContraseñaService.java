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

        Optional<ContraseñaResetToken> codigoOpt = reseteoContraseñaRepository.findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(usuario);
        if (codigoOpt.isEmpty()) {
            return false;
        }

        ContraseñaResetToken token = codigoOpt.get();

        if (token.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return false;   // el token ha expirado, no se permite ingresar a la siguiente pantalla para ingresar la nueva contraseña
        }

        if (codigo.trim().equals(token.getCodigo().trim())) {
            return true;   // esto está correcto, se deja pasar a la siguiente pantalla para ingresar la nueva contraseña
        }

        return false; // es falso
    }

    public boolean resetearContraseña(String email, String nuevaContraseña, String codigo){
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()){
            return false;
        }
        Usuario usuario = usuarioOpt.get();

        Optional<ContraseñaResetToken> codigoOpt = reseteoContraseñaRepository.findTopByUsuarioAndUtilizadoFalseOrderByIdDesc(usuario);
        if (codigoOpt.isEmpty()) {
            return false;
        }
        ContraseñaResetToken token = codigoOpt.get();
        
        //validamos por última vez por seguridad
        if (token.getFechaExpiracion().isBefore(LocalDateTime.now()) || !codigo.trim().equals(token.getCodigo().trim())) {  
            return false; 
        }

        // éxito, cambiamos la clave
        usuario.setPassword(passwordEncoder.encode(nuevaContraseña));
        usuarioRepository.save(usuario);


        token.setUtilizado(true);
        reseteoContraseñaRepository.save(token);

        return true;
    }
}
    