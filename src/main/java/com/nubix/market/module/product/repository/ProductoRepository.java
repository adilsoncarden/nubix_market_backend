package com.nubix.market.module.product.repository;

import java.util.List;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.nubix.market.module.product.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    boolean existsByCodigo(String codigo);

    Optional<Producto> findByCodigo(String codigoProducto);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Producto p WHERE p.id = :id")
    Optional<Producto> findByIdForUpdate(@Param("id") Integer id);

    @Query("SELECT DISTINCT p FROM Producto p LEFT JOIN FETCH p.imagen LEFT JOIN FETCH p.categoria")
    List<Producto> findAllWithImagen();
}