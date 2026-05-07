package com.nubix.market.services;

import com.nubix.market.dto.AuthResponse;
import com.nubix.market.dto.LoginRequest;
import com.nubix.market.dto.RegisterRequest;
import com.nubix.market.entities.Usuario;
import com.nubix.market.entities.Rol;
import com.nubix.market.repositories.RolRepository;
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
    @Autowired
    private RolRepository rolRepository;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            return new AuthResponse(false, "El nombre de usuario ya existe", null);
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            return new AuthResponse(false, "El correo electronico ya existe", null);
        }

        Rol rol = this.rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Rol CLIENTE no encontrado en la base de datos"));

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(request.getUsername());
        nuevoUsuario.setEmail(request.getEmail());
        nuevoUsuario.setRol(rol);

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
        return new AuthResponse(true, "Login exitoso", usuario.getUsername(), token, usuario.getRol().getNombre());
    }

    public AuthResponse adminLogin(LoginRequest request) {
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

        // Verificamos que el usuario tenga rol de ADMIN o EMPLEADO
        String rolNombre = usuario.getRol().getNombre();
        if (!rolNombre.equals("ADMIN") && !rolNombre.equals("EMPLEADO")) {
            return new AuthResponse(false, "Acceso denegado: no es un usuario administrativo", null);
        }
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            return new AuthResponse(false, "Contraseña incorrecta", null);
        }

        String token = jwtUtils.generateToken(usuario.getUsername());
        return new AuthResponse(true, "Bienvenido al panel de administración", usuario.getUsername(), token, rolNombre);
    }
}