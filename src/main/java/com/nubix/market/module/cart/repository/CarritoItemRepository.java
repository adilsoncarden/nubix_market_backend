package com.nubix.market.module.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.cart.model.CarritoItem;

/**
 * Repositorio JPA para ítems del carrito.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {
}
