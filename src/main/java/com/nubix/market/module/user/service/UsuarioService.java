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

/**
 * Servicio de gestión de usuarios del sistema Nubix Market.
 * <p>
 * Proporciona operaciones CRUD para clientes de la tienda y personal interno
 * (empleados y administradores), incluyendo validación de unicidad de credenciales
 * y restricciones sobre roles protegidos del sistema.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RolRepository rolRepository;

    /**
     * Obtiene todos los usuarios con rol {@code CLIENTE}.
     *
     * @return lista de usuarios clientes de la tienda
     */
    public List<Usuario> obtenerClientes() {
        return usuarioRepository.findByRol_Nombre("CLIENTE");
    }

    /**
     * Busca un usuario por su identificador.
     *
     * @param id identificador único del usuario
     * @return {@link Optional} con el usuario si existe, o vacío en caso contrario
     */
    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    /**
     * Actualiza los datos de un cliente existente.
     * <p>
     * Valida unicidad de nombre de usuario y correo electrónico. La contraseña
     * solo se modifica si se proporciona un valor no vacío en la solicitud.
     * </p>
     *
     * @param id      identificador del usuario a actualizar
     * @param request datos actualizados del cliente
     * @return entidad {@link Usuario} persistida con los cambios aplicados
     * @throws RuntimeException si el usuario no existe, o si el username o email ya están en uso
     */
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

    /**
     * Obtiene el personal interno: todos los usuarios cuyo rol no es {@code CLIENTE}.
     *
     * @return lista de empleados y administradores del sistema
     */
    public List<Usuario> obtenerEmpleadosYAdmins() {
        return usuarioRepository.findByRol_NombreNot("CLIENTE");
    }

    /**
     * Crea un nuevo usuario de personal interno (empleado o administrador asignable).
     * <p>
     * Codifica la contraseña y asigna el rol indicado en la solicitud, o {@code EMPLEADO}
     * por defecto si no se especifica rol.
     * </p>
     *
     * @param request datos del nuevo empleado, incluyendo credenciales y rol opcional
     * @return entidad {@link Usuario} persistida
     * @throws RuntimeException si el username o email ya existen, el rol no se encuentra,
     *                          o el rol no es asignable (ADMIN o CLIENTE)
     */
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

    /**
     * Actualiza los datos de un empleado o administrador existente.
     * <p>
     * No permite modificar el rol del Administrador Supremo ni reasignar roles protegidos.
     * </p>
     *
     * @param id      identificador del usuario a actualizar
     * @param request datos actualizados, incluyendo rol opcional
     * @return entidad {@link Usuario} persistida con los cambios aplicados
     * @throws RuntimeException si el usuario no existe, las credenciales duplican otro usuario,
     *                          el rol no se encuentra, el rol no es asignable, o se intenta
     *                          modificar el rol del Administrador Supremo
     */
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

    /**
     * Elimina un usuario de personal interno por su identificador.
     *
     * @param id identificador del usuario a eliminar
     * @return entidad {@link Usuario} eliminada
     * @throws RuntimeException si el usuario no existe o es el Administrador Supremo
     */
    public Usuario eliminar(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (esRolAdministradorSupremo(usuario.getRol())) {
            throw new RuntimeException("No se puede dar de baja al Administrador Supremo");
        }
        usuarioRepository.delete(usuario);
        return usuario;
    }

    /**
     * Resuelve el rol a asignar a un empleado a partir de la solicitud.
     * <p>
     * Prioridad: {@code rolId}, luego {@code rolNombre}, y por defecto {@code EMPLEADO}.
     * </p>
     *
     * @param request solicitud con identificador o nombre de rol opcional
     * @return entidad {@link Rol} validada y asignable
     * @throws RuntimeException si el rol no existe o no es asignable
     */
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

    /**
     * Valida que el rol pueda asignarse a personal interno.
     *
     * @param rol rol candidato a asignación
     * @throws RuntimeException si el rol es Administrador Supremo o CLIENTE
     */
    private void validarRolAsignable(Rol rol) {
        if (esRolAdministradorSupremo(rol)) {
            throw new RuntimeException(
                    "El rol de Administrador no puede asignarse a empleados");
        }
        if (rol.getNombre() != null && "CLIENTE".equalsIgnoreCase(rol.getNombre().trim())) {
            throw new RuntimeException("El rol CLIENTE no puede asignarse desde personal interno");
        }
    }

    /**
     * Determina si el rol corresponde al Administrador Supremo del sistema.
     * <p>
     * Se considera supremo si su id es {@code 1} o su nombre es {@code ADMIN}
     * o {@code ADMINISTRADOR}.
     * </p>
     *
     * @param rol rol a evaluar
     * @return {@code true} si es el Administrador Supremo; {@code false} en caso contrario
     */
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
