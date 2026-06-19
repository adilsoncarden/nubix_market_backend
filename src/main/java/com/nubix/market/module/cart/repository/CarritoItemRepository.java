package com.nubix.market.module.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.cart.model.CarritoItem;

/**
 * Repositorio de Spring Data JPA para la entidad CarritoItem.
 * Proporciona operaciones básicas CRUD para gestionar líneas de productos en carritos.
 */
@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {
}
