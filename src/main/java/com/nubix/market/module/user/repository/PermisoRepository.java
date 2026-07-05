package com.nubix.market.module.user.repository;

import com.nubix.market.module.user.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Permiso}.
 * <p>
 * Proporciona consultas por nombre, módulo y listado de módulos distintos
 * para la administración RBAC.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {

    /**
     * Busca un permiso por su nombre exacto.
     *
     * @param nombre nombre único del permiso
     * @return {@link Optional} con el permiso si existe
     */
    Optional<Permiso> findByNombre(String nombre);

    /**
     * Lista todos los permisos ordenados por módulo y nombre.
     *
     * @return lista de permisos ordenada por módulo y nombre ascendente
     */
    List<Permiso> findAllByOrderByModuloAscNombreAsc();

    /**
     * Lista permisos de un módulo ordenados por nombre.
     *
     * @param modulo nombre del módulo
     * @return lista de permisos del módulo indicado
     */
    List<Permiso> findByModuloOrderByNombreAsc(String modulo);

    /**
     * Obtiene los nombres de módulo distintos presentes en los permisos.
     *
     * @return lista ordenada de nombres de módulo
     */
    @Query("SELECT DISTINCT p.modulo FROM Permiso p ORDER BY p.modulo ASC")
    List<String> findDistinctModulos();
}
