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

@Service
public class SecurityAuthorityService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;

    public SecurityAuthorityService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            PermisoRepository permisoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.permisoRepository = permisoRepository;
    }

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

    private void cargarPermisosDelRol(Integer rolId, Set<String> destino) {
        rolRepository.findByIdWithPermisos(rolId).ifPresent(rol -> {
            for (Permiso p : rol.getPermisos()) {
                agregarPermiso(destino, p);
            }
        });
    }

    private void agregarPermiso(Set<String> destino, Permiso permiso) {
        if (permiso != null && permiso.getNombre() != null && !permiso.getNombre().isBlank()) {
            destino.add(permiso.getNombre().trim());
        }
    }

    private boolean isAdminRole(String rolNombre) {
        return rolNombre != null && "ADMIN".equalsIgnoreCase(rolNombre.trim());
    }
}
