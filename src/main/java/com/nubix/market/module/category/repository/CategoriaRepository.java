package com.nubix.market.module.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.category.model.Categoria;

/**
 * Repositorio de Spring Data JPA para la entidad Categoria.
 * Facilita la comunicación con la base de datos para realizar operaciones CRUD 
 * sobre las categorías del catálogo.
 */
@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    /**
     * Verifica si ya existe una categoría registrada con un nombre específico.
     * Útil para validaciones de negocio y evitar duplicados en el catálogo.
     *
     * @param nombre El nombre exacto de la categoría a buscar.
     * @return {@code true} si ya existe una categoría con ese nombre, {@code false} en caso contrario.
     */
    boolean existsByNombre(String nombre);
}
