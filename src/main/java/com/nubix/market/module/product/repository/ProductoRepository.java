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
 * Repositorio de Spring Data JPA para gestionar la persistencia de los productos.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    /** Verifica la existencia de un código SKU para evitar duplicados. */
    boolean existsByCodigo(String codigo);

    /** Busca un producto utilizando su código SKU en lugar de su ID interno. */
    Optional<Producto> findByCodigo(String codigoProducto);

    /**
     * Busca un producto por su ID aplicando un bloqueo pesimista en la base de datos.
     * Este bloqueo previene condiciones de carrera (concurrency issues) durante el checkout, 
     * asegurando que dos transacciones simultáneas no descuenten el mismo stock al mismo tiempo.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Optional<Producto> findByIdForUpdate(@Param("id") Integer id);

    /**
     * Recupera todos los productos forzando la carga temprana (Eager) de sus categorías.
     * Utiliza un EntityGraph para resolver el problema de N+1 consultas de Hibernate.
     */
    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT p FROM Producto p ORDER BY p.id ASC")
    List<Producto> findAllWithCategoria();

    /**
     * Busca un producto por ID forzando la carga de la categoría asociada en la misma consulta SQL.
     */
    @EntityGraph(attributePaths = {"categoria"})
    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Optional<Producto> findByIdWithRelations(@Param("id") Integer id);
}
