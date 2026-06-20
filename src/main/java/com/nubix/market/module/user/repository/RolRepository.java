package com.nubix.market.module.user.repository;

import com.nubix.market.module.user.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Spring Data JPA para la entidad Rol.
 * Permite buscar roles y optimizar la carga de la matriz de permisos asociada.
 */
@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    /** Busca un rol por su nombre único (ej. "ADMIN"). */
    Optional<Rol> findByNombre(String nombre);

    /** Obtiene el catálogo completo de roles ordenado de la A a la Z. */
    List<Rol> findAllByOrderByNombreAsc();

    /**
     * Recupera un rol junto con todos sus permisos asignados en una sola consulta a la base de datos.
     * El uso de 'LEFT JOIN FETCH' evita el problema de N+1 consultas que ocurriría por 
     * culpa del FetchType.LAZY declarado en la entidad Rol.
     */
    @Query("SELECT DISTINCT r FROM Rol r LEFT JOIN FETCH r.permisos WHERE r.id = :id")
    Optional<Rol> findByIdWithPermisos(@Param("id") Integer id);
}