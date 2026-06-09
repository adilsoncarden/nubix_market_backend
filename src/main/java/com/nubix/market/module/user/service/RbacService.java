package com.nubix.market.module.user.service;

import com.nubix.market.module.user.dto.PermisoRequest;
import com.nubix.market.module.user.dto.PermisoResponse;
import com.nubix.market.module.user.dto.RolPermisoIdsResponse;
import com.nubix.market.module.user.dto.RolPermisoSyncRequest;
import com.nubix.market.module.user.dto.RolRequest;
import com.nubix.market.module.user.dto.RolResponse;
import com.nubix.market.module.user.model.Permiso;
import com.nubix.market.module.user.model.Rol;
import com.nubix.market.module.user.repository.PermisoRepository;
import com.nubix.market.module.user.repository.RolRepository;
import com.nubix.market.module.user.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RbacService {

    private final PermisoRepository permisoRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public RbacService(
            PermisoRepository permisoRepository,
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository) {
        this.permisoRepository = permisoRepository;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ─── Permisos CRUD ───

    public List<String> listarModulosPermisos() {
        return permisoRepository.findDistinctModulos();
    }

    public List<PermisoResponse> listarPermisos(String modulo) {
        List<Permiso> lista;
        if (modulo == null || modulo.isBlank()) {
            lista = permisoRepository.findAllByOrderByModuloAscNombreAsc();
        } else {
            lista = permisoRepository.findByModuloOrderByNombreAsc(modulo.trim());
        }
        return lista.stream().map(this::mapPermiso).collect(Collectors.toList());
    }

    public PermisoResponse obtenerPermiso(Integer id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        return mapPermiso(permiso);
    }

    @Transactional
    public PermisoResponse crearPermiso(PermisoRequest request) {
        validarPermisoRequest(request);
        if (permisoRepository.findByNombre(request.getNombre().trim()).isPresent()) {
            throw new RuntimeException("Ya existe un permiso con ese nombre");
        }
        Permiso permiso = new Permiso(
                request.getNombre().trim(),
                request.getDescripcion().trim(),
                request.getModulo().trim());
        return mapPermiso(permisoRepository.save(permiso));
    }

    @Transactional
    public PermisoResponse actualizarPermiso(Integer id, PermisoRequest request) {
        validarPermisoRequest(request);
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        permisoRepository.findByNombre(request.getNombre().trim()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new RuntimeException("Ya existe otro permiso con ese nombre");
            }
        });
        permiso.setNombre(request.getNombre().trim());
        permiso.setDescripcion(request.getDescripcion().trim());
        permiso.setModulo(request.getModulo().trim());
        return mapPermiso(permisoRepository.save(permiso));
    }

    @Transactional
    public void eliminarPermiso(Integer id) {
        if (!permisoRepository.existsById(id)) {
            throw new RuntimeException("Permiso no encontrado");
        }
        permisoRepository.deleteById(id);
    }

    // ─── Roles CRUD ───

    public List<RolResponse> listarRoles() {
        return rolRepository.findAllByOrderByNombreAsc().stream()
                .map(this::mapRol)
                .collect(Collectors.toList());
    }

    public RolResponse obtenerRol(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return mapRol(rol);
    }

    @Transactional
    public RolResponse crearRol(RolRequest request) {
        validarRolRequest(request);
        if (rolRepository.findByNombre(request.getNombre().trim().toUpperCase()).isPresent()) {
            throw new RuntimeException("Ya existe un rol con ese nombre");
        }
        Rol rol = new Rol(
                request.getNombre().trim().toUpperCase(),
                request.getDescripcion() != null ? request.getDescripcion().trim() : null);
        return mapRol(rolRepository.save(rol));
    }

    @Transactional
    public RolResponse actualizarRol(Integer id, RolRequest request) {
        validarRolRequest(request);
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        String nombre = request.getNombre().trim().toUpperCase();
        rolRepository.findByNombre(nombre).ifPresent(existing -> {
            if (!existing.getId().equals(id)) {
                throw new RuntimeException("Ya existe otro rol con ese nombre");
            }
        });
        rol.setNombre(nombre);
        rol.setDescripcion(
                request.getDescripcion() != null ? request.getDescripcion().trim() : null);
        return mapRol(rolRepository.save(rol));
    }

    @Transactional
    public void eliminarRol(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        if (esRolAdministradorSupremo(rol)) {
            throw new RuntimeException(
                    "El rol de Administrador Supremo no puede ser eliminado");
        }
        if (rol.getNombre() != null && "CLIENTE".equalsIgnoreCase(rol.getNombre().trim())) {
            throw new RuntimeException("No se puede eliminar un rol base del sistema");
        }
        if (usuarioRepository.countByRol_Id(id) > 0) {
            throw new RuntimeException("No se puede eliminar: hay usuarios asignados a este rol");
        }
        rolRepository.delete(rol);
    }

    // ─── Asignación permisos ↔ rol ───

    @Transactional(readOnly = true)
    public RolPermisoIdsResponse idsPermisosDeRol(Integer rolId) {
        Rol rol = rolRepository.findByIdWithPermisos(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        List<Integer> ids = rol.getPermisos().stream()
                .map(Permiso::getId)
                .sorted()
                .collect(Collectors.toList());
        return new RolPermisoIdsResponse(ids);
    }

    @Transactional
    public RolPermisoIdsResponse sincronizarPermisosRol(Integer rolId, RolPermisoSyncRequest request) {
        Rol rol = rolRepository.findByIdWithPermisos(rolId)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        List<Integer> ids = request.getPermisoIds();
        if (ids == null || ids.isEmpty()) {
            rol.setPermisos(new HashSet<>());
        } else {
            List<Permiso> encontrados = permisoRepository.findAllById(ids);
            if (encontrados.size() != ids.size()) {
                throw new RuntimeException("Uno o más permisos no existen");
            }
            rol.setPermisos(new HashSet<>(encontrados));
        }
        rolRepository.save(rol);
        return idsPermisosDeRol(rolId);
    }

    private void validarPermisoRequest(PermisoRequest request) {
        if (request == null
                || request.getNombre() == null
                || request.getNombre().isBlank()
                || request.getDescripcion() == null
                || request.getDescripcion().isBlank()
                || request.getModulo() == null
                || request.getModulo().isBlank()) {
            throw new RuntimeException(
                    "Nombre, descripción y módulo del permiso son obligatorios");
        }
    }

    private void validarRolRequest(RolRequest request) {
        if (request == null || request.getNombre() == null || request.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del rol es obligatorio");
        }
    }

    private PermisoResponse mapPermiso(Permiso permiso) {
        return new PermisoResponse(
                permiso.getId(),
                permiso.getNombre(),
                permiso.getDescripcion(),
                permiso.getModulo());
    }

    private RolResponse mapRol(Rol rol) {
        return new RolResponse(rol.getId(), rol.getNombre(), rol.getDescripcion());
    }

    private boolean esRolAdministradorSupremo(Rol rol) {
        if (rol == null) {
            return false;
        }
        if (rol.getId() != null && rol.getId() == 1) {
            return true;
        }
        if (rol.getNombre() == null) {
            return false;
        }
        String nombre = rol.getNombre().trim().toUpperCase();
        return "ADMIN".equals(nombre) || "ADMINISTRADOR".equals(nombre);
    }
}
