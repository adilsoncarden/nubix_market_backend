package com.nubix.market.module.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.sale.model.Pago;

/**
 * Repositorio Spring Data JPA para la entidad {@link Pago}.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
}
