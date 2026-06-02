package com.nubix.market.module.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.sale.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {
}
