package com.nubix.market.module.user.repository;

import com.nubix.market.module.user.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la gestión de la entidad Permiso.
 * Proporciona métodos derivados para organizar la lista de acciones disponibles en el sistema.
 */
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

    /** Busca un permiso exacto por su nombre identificador (ej. "CREAR_USUARIO"). */
    Optional<Permiso> findByNombre(String nombre);

    /** * Retorna todos los permisos ordenados alfabéticamente primero por su módulo 
     * y luego por su nombre. Ideal para mostrar agrupaciones en la interfaz de usuario.
     */
    List<Permiso> findAllByOrderByModuloAscNombreAsc();

    /** * Obtiene los permisos que pertenecen exclusivamente a un módulo específico 
     * (ej. todos los permisos del módulo "VENTAS").
     */
    List<Permiso> findByModuloOrderByNombreAsc(String modulo);

    /**
     * Consulta personalizada (JPQL) que extrae una lista única (sin duplicados) 
     * de los nombres de los módulos que están registrados actualmente.
     * Útil para construir menús desplegables o pestañas de configuración.
     */
    @Query("SELECT DISTINCT p.modulo FROM Permiso p ORDER BY p.modulo ASC")
    List<String> findDistinctModulos();
}
