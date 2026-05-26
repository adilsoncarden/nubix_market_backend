package com.nubix.market.repositories;

import com.nubix.market.entities.Producto;
import java.util.Optional;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
   boolean existsByCodigo(String codigo);

   Optional<Producto> findByCodigo(String codigoProducto);

   @Lock(LockModeType.PESSIMISTIC_WRITE)
   @Query("SELECT p FROM Producto p WHERE p.id = :id")
   Optional<Producto> findByIdForUpdate(@Param("id") Integer id);
}