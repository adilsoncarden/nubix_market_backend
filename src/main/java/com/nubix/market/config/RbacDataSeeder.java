package com.nubix.market.config;

import com.nubix.market.module.user.model.Permiso;
import com.nubix.market.module.user.model.Rol;
import com.nubix.market.module.user.repository.PermisoRepository;
import com.nubix.market.module.user.repository.RolRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


/**
 * Sembrador de datos (Data Seeder) que se ejecuta al iniciar la aplicación.
 * Garantiza que la base de datos contenga los permisos, roles base (ADMIN, CLIENTE) 
 * y las asignaciones iniciales necesarias para que el sistema de autorización (RBAC) 
 * funcione correctamente desde el primer arranque.
 */
@Component
@Order(50)
public class RbacDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(RbacDataSeeder.class);

    private final PermisoRepository permisoRepository;
    private final RolRepository rolRepository;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param permisoRepository Repositorio para gestionar los permisos en base de datos.
     * @param rolRepository     Repositorio para gestionar los roles en base de datos.
     */
    public RbacDataSeeder(PermisoRepository permisoRepository, RolRepository rolRepository) {
        this.permisoRepository = permisoRepository;
        this.rolRepository = rolRepository;
    }

    /**
     * Diccionario estático que almacena los permisos iniciales requeridos por el sistema.
     * Estructura: Llave (Nombre del permiso) -> Valor (Arreglo con Descripción y Módulo).
     */
    private static final Map<String, String[]> PERMISOS_INICIALES = new LinkedHashMap<>();

    static {
        seedEntry("ver:dashboard", "Ver métricas y reportes del panel administrativo", "Dashboard");
        seedEntry("ver:productos", "Listar y consultar productos del catálogo", "Productos");
        seedEntry("crear:productos", "Registrar nuevos productos en el catálogo", "Productos");
        seedEntry("editar:productos", "Modificar productos e imágenes existentes", "Productos");
        seedEntry("eliminar:productos", "Eliminar productos del catálogo", "Productos");
        seedEntry("ver:categorias", "Listar y consultar categorías", "Categorías");
        seedEntry("crear:categorias", "Registrar nuevas categorías", "Categorías");
        seedEntry("editar:categorias", "Modificar categorías existentes", "Categorías");
        seedEntry("eliminar:categorias", "Eliminar categorías del sistema", "Categorías");
        seedEntry("ver:proveedores", "Listar y consultar proveedores", "Proveedores");
        seedEntry("crear:proveedores", "Registrar nuevos proveedores", "Proveedores");
        seedEntry("editar:proveedores", "Modificar datos de proveedores", "Proveedores");
        seedEntry("eliminar:proveedores", "Eliminar proveedores del sistema", "Proveedores");
        seedEntry("ver:ventas", "Consultar pedidos y ventas", "Ventas");
        seedEntry("crear:ventas", "Registrar ventas desde el panel administrativo", "Ventas");
        seedEntry("editar:ventas", "Editar datos de ventas y registrar créditos", "Ventas");
        seedEntry("actualizar:estado_ventas", "Actualizar el estado logístico de pedidos", "Ventas");
        seedEntry("gestionar:usuarios", "Administrar clientes y empleados del sistema", "Seguridad");
        seedEntry("gestionar:seguridad", "Gestionar roles, permisos y políticas de acceso", "Seguridad");
    }

    /**
     * Método auxiliar para registrar un permiso en el diccionario inicial.
     *
     * @param nombre      Nombre identificador del permiso (ej. "ver:ventas").
     * @param descripcion Descripción legible para el usuario.
     * @param modulo      Módulo al que pertenece (ej. "Ventas").
     */
    private static void seedEntry(String nombre, String descripcion, String modulo) {
        PERMISOS_INICIALES.put(nombre, new String[] { descripcion, modulo });
    }

    /**
     * Punto de entrada principal del componente. 
     * Ejecuta la inserción o actualización de permisos y roles dentro de una transacción.
     *
     * @param args Argumentos de inicialización de Spring Boot.
     */
    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedPermisos();
        seedRolesBase();
    }

    /**
     * Itera sobre los permisos iniciales definidos estáticamente e inserta los faltantes 
     * en la base de datos o actualiza los existentes si carecen de módulo o descripción.
     */
    private void seedPermisos() {
        for (Map.Entry<String, String[]> entry : PERMISOS_INICIALES.entrySet()) {
            String nombre = entry.getKey();
            String descripcion = entry.getValue()[0];
            String modulo = entry.getValue()[1];

            permisoRepository.findByNombre(nombre).map(existing -> {
                boolean changed = false;
                if (needsModuloUpdate(existing.getModulo())) {
                    existing.setModulo(modulo);
                    changed = true;
                }
                if (existing.getDescripcion() == null || existing.getDescripcion().isBlank()) {
                    existing.setDescripcion(descripcion);
                    changed = true;
                }
                return changed ? permisoRepository.save(existing) : existing;
            }).orElseGet(() -> {
                Permiso nuevo = new Permiso(nombre, descripcion, modulo);
                nuevo.setModulo(modulo);
                return permisoRepository.save(nuevo);
            });
        }
        backfillModulosPendientes();
        log.info("RBAC: {} permisos base verificados (con módulo)", PERMISOS_INICIALES.size());
    }

    /**
     * Evalúa si el módulo de un permiso requiere ser actualizado.
     *
     * @param modulo El módulo actual a evaluar.
     * @return {@code true} si está vacío, es nulo o tiene el valor por defecto "General".
     */
    private static boolean needsModuloUpdate(String modulo) {
        return modulo == null
                || modulo.isBlank()
                || "General".equalsIgnoreCase(modulo.trim());
    }

    /**
     * Actualiza las filas heredadas (legacy) en la base de datos que quedaron 
     * con el módulo "General" tras el parche del esquema, infiriendo el módulo correcto.
     */
    private void backfillModulosPendientes() {
        for (Permiso permiso : permisoRepository.findAll()) {
            if (!needsModuloUpdate(permiso.getModulo())) {
                continue;
            }
            String inferido = PERMISOS_INICIALES.containsKey(permiso.getNombre())
                    ? PERMISOS_INICIALES.get(permiso.getNombre())[1]
                    : inferirModuloDesdeNombre(permiso.getNombre());
            permiso.setModulo(inferido);
            permisoRepository.save(permiso);
        }
    }

    /**
     * Infiere la categoría o módulo de un permiso basándose en su nombre utilizando reglas simples.
     *
     * @param nombre El nombre del permiso.
     * @return El nombre del módulo inferido.
     */
    private static String inferirModuloDesdeNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "General";
        }
        if ("ver:dashboard".equals(nombre)) {
            return "Dashboard";
        }
        if (nombre.contains("productos")) {
            return "Productos";
        }
        if (nombre.contains("categorias")) {
            return "Categorías";
        }
        if (nombre.contains("proveedores")) {
            return "Proveedores";
        }
        if (nombre.contains("ventas")) {
            return "Ventas";
        }
        if (nombre.startsWith("gestionar:")) {
            return "Seguridad";
        }
        return "General";
    }

    /**
     * Asegura la creación de los roles fundamentales del sistema y sincroniza
     * sus permisos asociados.
     */
    private void seedRolesBase() {
        Rol admin = ensureRol(
                "ADMIN",
                "Administrador del sistema con acceso total al panel y la configuración de seguridad");
        Rol cliente = ensureRol(
                "CLIENTE",
                "Usuario de la tienda web pública sin acceso al panel administrativo");

        sincronizarPermisosAdmin();

        Rol loadedCliente = rolRepository.findByIdWithPermisos(cliente.getId()).orElse(cliente);
        if (loadedCliente.getPermisos() == null || loadedCliente.getPermisos().isEmpty()) {
            loadedCliente.setPermisos(new HashSet<>());
            rolRepository.save(loadedCliente);
        }

        log.info("RBAC: roles base ADMIN y CLIENTE verificados");
    }

    /**
     * Busca un rol por su nombre y lo crea si no existe. 
     * Si existe pero carece de descripción, la actualiza.
     *
     * @param nombre      El nombre del rol a verificar.
     * @param descripcion La descripción correspondiente a ese rol.
     * @return La entidad Rol actualizada o persistida.
     */
    private Rol ensureRol(String nombre, String descripcion) {
        return rolRepository.findByNombre(nombre).map(existing -> {
            if (existing.getDescripcion() == null || existing.getDescripcion().isBlank()) {
                existing.setDescripcion(descripcion);
                return rolRepository.save(existing);
            }
            return existing;
        }).orElseGet(() -> rolRepository.save(new Rol(nombre, descripcion)));
    }

    /**
     * Garantiza que el rol 'ADMIN' mantenga siempre un acceso absoluto.
     * Compara los permisos actuales del administrador con la totalidad de permisos 
     * existentes en la base de datos y le asigna los faltantes.
     */
    private void sincronizarPermisosAdmin() {
        rolRepository.findByNombre("ADMIN").ifPresent(admin -> {
            Set<Permiso> todos = new HashSet<>(permisoRepository.findAll());
            Rol loaded = rolRepository.findByIdWithPermisos(admin.getId()).orElse(admin);
            Set<Permiso> actuales = loaded.getPermisos() != null
                    ? loaded.getPermisos()
                    : Set.of();

            boolean faltaAlguno = todos.stream()
                    .anyMatch(p -> actuales.stream().noneMatch(a -> a.getId().equals(p.getId())));

            if (actuales.isEmpty() || faltaAlguno) {
                loaded.setPermisos(todos);
                rolRepository.save(loaded);
                log.info("RBAC: rol ADMIN sincronizado con {} permisos", todos.size());
            }
        });
    }
}
