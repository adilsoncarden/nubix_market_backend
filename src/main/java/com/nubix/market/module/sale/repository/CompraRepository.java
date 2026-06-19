package com.nubix.market.module.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.sale.model.Compra;

/**
 * Repositorio de Spring Data JPA para la gestión de compras a proveedores (ingreso de mercadería).
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

}
