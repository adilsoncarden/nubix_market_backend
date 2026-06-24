package com.nubix.market.module.user.repository;

import com.nubix.market.module.user.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Rol}.
 * <p>
 * Proporciona consultas por nombre, listado ordenado y carga eager de permisos
 * asociados a un rol.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /**
     * Busca un rol por su nombre exacto.
     *
     * @param nombre nombre del rol
     * @return {@link Optional} con el rol si existe
     */
    Optional<Rol> findByNombre(String nombre);

    /**
     * Lista todos los roles ordenados alfabéticamente por nombre.
     *
     * @return lista de roles ordenada por nombre ascendente
     */
    List<Rol> findAllByOrderByNombreAsc();

    /**
     * Obtiene un rol por id cargando su colección de permisos.
     *
     * @param id identificador del rol
     * @return {@link Optional} con el rol y sus permisos si existe
     */
    @Query("SELECT DISTINCT r FROM Rol r LEFT JOIN FETCH r.permisos WHERE r.id = :id")
    Optional<Rol> findByIdWithPermisos(@Param("id") Integer id);
}
