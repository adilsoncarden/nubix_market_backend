package com.nubix.market.module.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.category.model.Categoria;

/**
 * Repositorio JPA para categorías.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    /**
     * Verifica existencia por nombre.
     * @param nombre Nombre descriptivo.
     * @return resultado de la operación
     */
    boolean existsByNombre(String nombre);
}
