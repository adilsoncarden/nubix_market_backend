package com.nubix.market.module.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.sale.model.Compra;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {

}
