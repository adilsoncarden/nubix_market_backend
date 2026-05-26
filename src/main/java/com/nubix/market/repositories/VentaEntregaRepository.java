package com.nubix.market.repositories;

import com.nubix.market.entities.VentaEntrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaEntregaRepository extends JpaRepository<VentaEntrega, Integer> {
}
