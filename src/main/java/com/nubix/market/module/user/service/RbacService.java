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

/**
 * Servicio de control de acceso basado en roles (RBAC).
 * <p>
 * Administra el ciclo de vida de permisos y roles, así como la asignación
 * de permisos a roles mediante sincronización de identificadores.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Service
public class RbacService {

    private final PermisoRepository permisoRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     * Construye el servicio RBAC con sus dependencias de persistencia.
     *
     * @param permisoRepository repositorio de permisos
     * @param rolRepository     repositorio de roles
     * @param usuarioRepository repositorio de usuarios
     */
    public RbacService(
            PermisoRepository permisoRepository,
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository) {
        this.permisoRepository = permisoRepository;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // ─── Permisos CRUD ───

    /**
     * Lista los nombres de módulos distintos en los que están agrupados los permisos.
     *
     * @return lista ordenada de nombres de módulo
     */
    public List<String> listarModulosPermisos() {
        return permisoRepository.findDistinctModulos();
    }

    /**
     * Lista permisos del sistema, opcionalmente filtrados por módulo.
     *
     * @param modulo nombre del módulo; si es {@code null} o vacío se listan todos
     * @return lista de DTOs {@link PermisoResponse} ordenados por módulo y nombre
     */
    public List<PermisoResponse> listarPermisos(String modulo) {
        List<Permiso> lista;
        if (modulo == null || modulo.isBlank()) {
            lista = permisoRepository.findAllByOrderByModuloAscNombreAsc();
        } else {
            lista = permisoRepository.findByModuloOrderByNombreAsc(modulo.trim());
        }
        return lista.stream().map(this::mapPermiso).collect(Collectors.toList());
    }

    /**
     * Obtiene un permiso por su identificador.
     *
     * @param id identificador del permiso
     * @return DTO {@link PermisoResponse} con los datos del permiso
     * @throws RuntimeException si el permiso no existe
     */
    public PermisoResponse obtenerPermiso(Integer id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
        return mapPermiso(permiso);
    }

    /**
     * Crea un nuevo permiso en el sistema.
     *
     * @param request datos del permiso (nombre, descripción y módulo obligatorios)
     * @return DTO {@link PermisoResponse} del permiso creado
     * @throws RuntimeException si faltan campos obligatorios o ya existe un permiso con el mismo nombre
     */
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

    /**
     * Actualiza un permiso existente.
     *
     * @param id      identificador del permiso a modificar
     * @param request nuevos datos del permiso
     * @return DTO {@link PermisoResponse} del permiso actualizado
     * @throws RuntimeException si el permiso no existe, faltan campos obligatorios
     *                          o el nombre ya está en uso por otro permiso
     */
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

    /**
     * Elimina un permiso por su identificador.
     *
     * @param id identificador del permiso a eliminar
     * @throws RuntimeException si el permiso no existe
     */
    @Transactional
    public void eliminarPermiso(Integer id) {
        if (!permisoRepository.existsById(id)) {
            throw new RuntimeException("Permiso no encontrado");
        }
        permisoRepository.deleteById(id);
    }

    // ─── Roles CRUD ───

    /**
     * Lista todos los roles del sistema ordenados por nombre.
     *
     * @return lista de DTOs {@link RolResponse}
     */
    public List<RolResponse> listarRoles() {
        return rolRepository.findAllByOrderByNombreAsc().stream()
                .map(this::mapRol)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un rol por su identificador.
     *
     * @param id identificador del rol
     * @return DTO {@link RolResponse} con los datos del rol
     * @throws RuntimeException si el rol no existe
     */
    public RolResponse obtenerRol(Integer id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        return mapRol(rol);
    }

    /**
     * Crea un nuevo rol en el sistema.
     * <p>
     * El nombre se normaliza a mayúsculas antes de persistir.
     * </p>
     *
     * @param request datos del rol (nombre obligatorio, descripción opcional)
     * @return DTO {@link RolResponse} del rol creado
     * @throws RuntimeException si falta el nombre o ya existe un rol con el mismo nombre
     */
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

    /**
     * Actualiza un rol existente.
     *
     * @param id      identificador del rol a modificar
     * @param request nuevos datos del rol
     * @return DTO {@link RolResponse} del rol actualizado
     * @throws RuntimeException si el rol no existe, falta el nombre o el nombre ya está en uso
     */
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

    /**
     * Elimina un rol del sistema.
     * <p>
     * No permite eliminar el Administrador Supremo, el rol base CLIENTE ni roles
     * con usuarios asignados.
     * </p>
     *
     * @param id identificador del rol a eliminar
     * @throws RuntimeException si el rol no existe, es protegido o tiene usuarios asignados
     */
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

    /**
     * Obtiene los identificadores de permisos asignados a un rol.
     *
     * @param rolId identificador del rol
     * @return DTO {@link RolPermisoIdsResponse} con la lista ordenada de ids de permiso
     * @throws RuntimeException si el rol no existe
     */
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

    /**
     * Sincroniza los permisos asignados a un rol con la lista proporcionada.
     * <p>
     * Reemplaza por completo la asignación actual. Una lista vacía o {@code null}
     * deja el rol sin permisos.
     * </p>
     *
     * @param rolId   identificador del rol a actualizar
     * @param request solicitud con la lista de ids de permiso deseada
     * @return DTO {@link RolPermisoIdsResponse} con los ids efectivamente asignados
     * @throws RuntimeException si el rol no existe o algún id de permiso no es válido
     */
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

    /**
     * Valida que la solicitud de permiso contenga nombre, descripción y módulo no vacíos.
     *
     * @param request solicitud a validar
     * @throws RuntimeException si algún campo obligatorio falta o está en blanco
     */
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

    /**
     * Valida que la solicitud de rol contenga un nombre no vacío.
     *
     * @param request solicitud a validar
     * @throws RuntimeException si el nombre del rol falta o está en blanco
     */
    private void validarRolRequest(RolRequest request) {
        if (request == null || request.getNombre() == null || request.getNombre().isBlank()) {
            throw new RuntimeException("El nombre del rol es obligatorio");
        }
    }

    /**
     * Convierte una entidad {@link Permiso} a su DTO de respuesta.
     *
     * @param permiso entidad de permiso
     * @return DTO {@link PermisoResponse} con los datos del permiso
     */
    private PermisoResponse mapPermiso(Permiso permiso) {
        return new PermisoResponse(
                permiso.getId(),
                permiso.getNombre(),
                permiso.getDescripcion(),
                permiso.getModulo());
    }

    /**
     * Convierte una entidad {@link Rol} a su DTO de respuesta.
     *
     * @param rol entidad de rol
     * @return DTO {@link RolResponse} con los datos del rol
     */
    private RolResponse mapRol(Rol rol) {
        return new RolResponse(rol.getId(), rol.getNombre(), rol.getDescripcion());
    }

    /**
     * Determina si el rol corresponde al Administrador Supremo del sistema.
     *
     * @param rol rol a evaluar
     * @return {@code true} si es el Administrador Supremo; {@code false} en caso contrario
     */
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
