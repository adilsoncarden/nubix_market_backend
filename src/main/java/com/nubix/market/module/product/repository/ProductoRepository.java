package com.nubix.market.module.product.repository;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.product.model.Producto;

/**
 * Repositorio JPA para productos.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    /**
     * Verifica existencia por código de producto.
     * @param codigo Código interno.
     * @return resultado de la operación
     */
    boolean existsByCodigo(String codigo);

    /**
     * Busca producto por código.
     * @param codigoProducto valor del parámetro
     * @return resultado de la operación
     */
    Optional<Producto> findByCodigo(String codigoProducto);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Optional<Producto> findByIdForUpdate(@Param("id") Integer id);

    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT p FROM Producto p ORDER BY p.id ASC")
    /**
     * Lista productos con categoría.
     * @return resultado de la operación
     */
    List<Producto> findAllWithCategoria();

    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Optional<Producto> findByIdWithRelations(@Param("id") Integer id);
}
