package com.nubix.market.services;

import com.nubix.market.dto.AuthResponse;
import com.nubix.market.dto.LoginRequest;
import com.nubix.market.dto.RegisterRequest;
import com.nubix.market.entities.Usuario;
import com.nubix.market.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import com.nubix.market.config.JwtUtils;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse(false, "El nombre de usuario ya existe", null);
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "El correo electronico ya existe", null);
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setEmail(request.getEmail());

        // ¡Magia aquí! Encriptamos la contraseña antes de guardarla
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));

        usuarioRepository.save(nuevoUsuario);

        return new AuthResponse(true, "Usuario registrado exitosamente", nuevoUsuario.getUsername());
    }

    public AuthResponse login(LoginRequest request) {
        Optional<Usuario> usuarioOpt = Optional.empty();

        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            usuarioOpt = usuarioRepository.findByUsername(request.getUsername());
        }
        if (usuarioOpt.isEmpty() && request.getEmail() != null && !request.getEmail().isBlank()) {
            usuarioOpt = usuarioRepository.findByEmail(request.getEmail());
        }

        if (usuarioOpt.isEmpty()) {
            return new AuthResponse(false, "Usuario no encontrado", null);
        }

        Usuario usuario = usuarioOpt.get();

        // Validamos la contraseña usando BCrypt
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return new AuthResponse(false, "Contraseña incorrecta", null);
        }

        // ¡Login exitoso! Generamos el JWT
        String token = jwtUtils.generateToken(usuario.getUsername());
        return new AuthResponse(true, "Login exitoso", usuario.getUsername(), token);
    }
}