package com.nubix.market.module.sale.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.nubix.market.enums.CanalVenta;
import com.nubix.market.module.sale.model.Venta;
import java.time.LocalDate;
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
            LEFT JOIN FETCH v.cliente
            LEFT JOIN FETCH v.vendedor
            ORDER BY v.id DESC
            """)
    List<Venta> findAllForList();

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

    @Query("""
            SELECT DISTINCT v FROM Venta v
            LEFT JOIN FETCH v.entrega
            LEFT JOIN FETCH v.pago
            WHERE v.cliente.id = :clienteId AND v.canal = :canal
            ORDER BY v.id DESC
            """)
    List<Venta> findByClienteIdAndCanalOrderByIdDesc(
            @Param("clienteId") Integer clienteId,
            @Param("canal") CanalVenta canal);

    @Query("""
            SELECT DISTINCT v FROM Venta v
            LEFT JOIN FETCH v.entrega
            LEFT JOIN FETCH v.pago
            WHERE v.cliente.id = :clienteId
            AND v.canal = :canal
            AND v.fecha >= :fechaInicio
            AND v.fecha <= :fechaFin
            ORDER BY v.id DESC
            """)
    List<Venta> findByClienteIdAndCanalAndFechaBetweenOrderByIdDesc(
            @Param("clienteId") Integer clienteId,
            @Param("canal") CanalVenta canal,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}
