package com.nubix.market.repositories;

import com.nubix.market.entities.Producto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
   boolean existsByCodigo(String codigo);

   Optional<Producto> findByCodigo(String codigoProducto);
}