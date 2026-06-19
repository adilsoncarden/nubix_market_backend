package com.nubix.market.config;

import com.nubix.market.module.user.model.Permiso;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.PermisoRepository;
import com.nubix.market.module.user.repository.RolRepository;
import com.nubix.market.module.user.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Servicio de seguridad encargado de construir las autoridades (roles y permisos) 
 * de un usuario autenticado para el contexto de Spring Security.
 */
@Service
public class SecurityAuthorityService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;

    /**
     * Constructor para la inyección de repositorios necesarios para consultar 
     * la seguridad del usuario en la base de datos.
     *
     * @param usuarioRepository Repositorio para acceder a los datos del usuario.
     * @param rolRepository     Repositorio para acceder a los datos de los roles.
     * @param permisoRepository Repositorio para acceder al catálogo completo de permisos.
     */
    public SecurityAuthorityService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PermisoRepository permisoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
    }

    /**
     * Carga y estructura todos los privilegios (roles y permisos) que posee un usuario.
     * Si el usuario es administrador, se le otorgan automáticamente todos los permisos del sistema.
     *
     * @param username El nombre de usuario cuyo acceso se está validando.
     * @param rolClaim El rol extraído preliminarmente (ej. desde el token JWT).
     * @return Una lista de {@link SimpleGrantedAuthority} que Spring Security utilizará para autorizar peticiones.
     */
    @Transactional(readOnly = true)
    public List<SimpleGrantedAuthority> loadAuthorities(String username, String rolClaim) {
        Set<String> permisoNames = new LinkedHashSet<>();
        String rolNombre = rolClaim;

        var usuarioOpt = usuarioRepository.findByUsernameWithRol(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getRol() != null && usuario.getRol().getId() != null) {
                if (usuario.getRol().getNombre() != null) {
                    rolNombre = usuario.getRol().getNombre().trim();
                }
                cargarPermisosDelRol(usuario.getRol().getId(), permisoNames);
            }
        }

        if (isAdminRole(rolNombre)) {
            permisoRepository.findAll().forEach(p -> agregarPermiso(permisoNames, p));
            permisoNames.add("gestionar:permisos");
            permisoNames.add("gestionar:seguridad");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (rolNombre != null && !rolNombre.isBlank()) {
            authorities.add(
                    new SimpleGrantedAuthority("ROLE_" + rolNombre.trim().toUpperCase()));
        }
        for (String nombre : permisoNames) {
            authorities.add(new SimpleGrantedAuthority(nombre));
        }
        return authorities;
    }

    /**
     * Busca los permisos asociados a un rol específico en la base de datos y los añade al conjunto proporcionado.
     *
     * @param rolId   El identificador único del rol a consultar.
     * @param destino El conjunto (Set) donde se irán acumulando los nombres de los permisos encontrados.
     */
    private void cargarPermisosDelRol(Integer rolId, Set<String> destino) {
        rolRepository.findByIdWithPermisos(rolId).ifPresent(rol -> {
            for (Permiso p : rol.getPermisos()) {
                agregarPermiso(destino, p);
            }
        });
    }

    /**
     * Valida y añade un permiso individual a la colección destino.
     * Evita la inserción de permisos nulos o con nombres vacíos.
     *
     * @param destino El conjunto (Set) donde se almacenará el permiso.
     * @param permiso El objeto Permiso a evaluar y extraer.
     */
    private void agregarPermiso(Set<String> destino, Permiso permiso) {
        if (permiso != null && permiso.getNombre() != null && !permiso.getNombre().isBlank()) {
            destino.add(permiso.getNombre().trim());
        }
    }

    /**
     * Verifica si el nombre de un rol corresponde a los privilegios de administrador absoluto.
     *
     * @param rolNombre El nombre del rol a evaluar.
     * @return {@code true} si el rol es "ADMIN", {@code false} en caso contrario.
     */
    private boolean isAdminRole(String rolNombre) {
        return rolNombre != null && "ADMIN".equalsIgnoreCase(rolNombre.trim());
    }
}
