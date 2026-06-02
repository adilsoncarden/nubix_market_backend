package com.nubix.market.module.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.sale.model.Venta;
import java.util.List;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    @Query("""
            SELECT DISTINCT v FROM Venta v
            LEFT JOIN FETCH v.detalles d
            LEFT JOIN FETCH d.producto
            LEFT JOIN FETCH v.cliente
            LEFT JOIN FETCH v.vendedor
            LEFT JOIN FETCH v.entrega
            LEFT JOIN FETCH v.pago
            ORDER BY v.id DESC
            """)
    List<Venta> findAllWithRelations();

    @Query("""
            SELECT DISTINCT v FROM Venta v
            LEFT JOIN FETCH v.detalles d
            LEFT JOIN FETCH d.producto
            LEFT JOIN FETCH v.cliente
            LEFT JOIN FETCH v.vendedor
            LEFT JOIN FETCH v.entrega
            LEFT JOIN FETCH v.pago
            WHERE v.id = :id
            """)
    Optional<Venta> findByIdWithRelations(Integer id);
}
