package com.nubix.market.module.user.service;

import com.nubix.market.module.user.dto.UsuarioRequest;
import com.nubix.market.module.user.model.Rol;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.RolRepository;
import com.nubix.market.module.user.repository.UsuarioRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RolRepository rolRepository;

    public List<Usuario> obtenerClientes() {
        return usuarioRepository.findByRol_Nombre("CLIENTE");
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario actualizar(Integer id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getUsername().equals(request.getUsername())
                && usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if (!usuario.getEmail().equals(request.getEmail()) && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo electrónico ya existe");
        }

        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    /** Personal interno: todos los usuarios excepto clientes de la tienda. */
    public List<Usuario> obtenerEmpleadosYAdmins() {
        return usuarioRepository.findByRol_NombreNot("CLIENTE");
    }

    public Usuario guardarEmpleado(UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre del usuario ya está en uso");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está en uso");
        }

        Rol rol = resolverRolAsignable(request);
        Usuario usuario = new Usuario();
        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(rol);

        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarEmpleado(Integer id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getUsername().equals(request.getUsername())
                && usuarioRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }
        if (!usuario.getEmail().equals(request.getEmail())
                && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo electrónico ya existe");
        }

        usuario.setUsername(request.getUsername());
        usuario.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (esRolAdministradorSupremo(usuario.getRol())) {
            throw new RuntimeException("No se puede modificar el rol del Administrador Supremo");
        }

        usuario.setRol(resolverRolAsignable(request));
        return usuarioRepository.save(usuario);
    }

    public Usuario eliminar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (esRolAdministradorSupremo(usuario.getRol())) {
            throw new RuntimeException("No se puede dar de baja al Administrador Supremo");
        }
        usuarioRepository.delete(usuario);
        return usuario;
    }

    private Rol resolverRolAsignable(UsuarioRequest request) {
        Rol rol;
        if (request.getRolId() != null) {
            rol = rolRepository.findById(request.getRolId())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        } else if (request.getRolNombre() != null && !request.getRolNombre().isBlank()) {
            rol = rolRepository.findByNombre(request.getRolNombre().trim())
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        } else {
            rol = rolRepository.findByNombre("EMPLEADO")
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        }
        validarRolAsignable(rol);
        return rol;
    }

    private void validarRolAsignable(Rol rol) {
        if (esRolAdministradorSupremo(rol)) {
            throw new RuntimeException(
                    "El rol de Administrador no puede asignarse a empleados");
        }
        if (rol.getNombre() != null && "CLIENTE".equalsIgnoreCase(rol.getNombre().trim())) {
            throw new RuntimeException("El rol CLIENTE no puede asignarse desde personal interno");
        }
    }

    private boolean esRolAdministradorSupremo(Rol rol) {
        if (rol == null || rol.getNombre() == null) {
            return false;
        }
        if (rol.getId() != null && rol.getId() == 1) {
            return true;
        }
        String nombre = rol.getNombre().trim().toUpperCase();
        return "ADMIN".equals(nombre) || "ADMINISTRADOR".equals(nombre);
    }
}
