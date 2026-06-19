package com.nubix.market.module.sale.dao;

import java.time.LocalDate;
import java.util.List;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.module.sale.model.Venta;

/**
 * Interfaz Data Access Object (DAO) especializada en búsquedas complejas del módulo de ventas.
 * A diferencia del JpaRepository estándar, este contrato está diseñado para manejar 
 * múltiples combinaciones de filtros dinámicos simultáneos, esenciales para la 
 * generación de reportes gerenciales.
 */
public interface VentaDAO {

    /**
     * Busca todas las ventas registradas dentro de un periodo de tiempo específico.
     */
    List<Venta> buscarVentasEntreFechas(LocalDate desde, LocalDate hasta);

    /**
     * Búsqueda avanzada y dinámica. Todos los parámetros son opcionales y se combinarán 
     * mediante la cláusula AND en la consulta final a la base de datos.
     */
    List<Venta> buscarConFiltros(
            LocalDate desde,
            LocalDate hasta,
            TipoEntrega tipoEntrega,
            Integer clienteId,
            EstadoPedido estadoPedido,
            EstadoPago estadoPago);
}
