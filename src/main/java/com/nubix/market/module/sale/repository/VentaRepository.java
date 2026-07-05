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

/**
 * Repositorio Spring Data JPA para la entidad {@link Venta}.
 * Incluye consultas con carga ansiosa de relaciones para listados y detalle.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

    /**
     * Obtiene todas las ventas con detalles, productos, cliente, vendedor, entrega y pago.
     *
     * @return lista completa de ventas con relaciones cargadas
     */
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

    /**
     * Obtiene todas las ventas con cliente y vendedor para vistas de listado.
     *
     * @return lista de ventas optimizada para listado
     */
    @Query("""
            SELECT DISTINCT v FROM Venta v
            LEFT JOIN FETCH v.cliente
            LEFT JOIN FETCH v.vendedor
            ORDER BY v.id DESC
            """)
    List<Venta> findAllForList();

    /**
     * Busca una venta por id cargando todas sus relaciones.
     *
     * @param id identificador de la venta
     * @return venta encontrada con relaciones, o vacío si no existe
     */
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

    /**
     * Lista pedidos de un cliente en un canal determinado, ordenados por id descendente.
     *
     * @param clienteId identificador del cliente
     * @param canal     canal de venta (p. ej. web)
     * @return lista de ventas del cliente en ese canal
     */
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

    /**
     * Lista pedidos de un cliente en un canal dentro de un rango de fechas.
     *
     * @param clienteId   identificador del cliente
     * @param canal       canal de venta
     * @param fechaInicio fecha inicial del rango (inclusive)
     * @param fechaFin    fecha final del rango (inclusive)
     * @return lista de ventas del cliente en el período indicado
     */
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
