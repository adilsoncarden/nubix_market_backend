package com.nubix.market.module.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.sale.model.Compra;

/**
 * Repositorio Spring Data JPA para la entidad {@link Compra}.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

}
