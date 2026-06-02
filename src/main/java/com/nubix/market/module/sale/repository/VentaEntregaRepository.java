package com.nubix.market.module.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.sale.model.VentaEntrega;

@Repository
public interface VentaEntregaRepository extends JpaRepository<VentaEntrega, Integer> {
}
