package com.nubix.market.module.sale.dao;

import java.time.LocalDate;
import java.util.List;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.module.sale.model.Venta;

/**
 * DAO de ejemplo: consultas personalizadas para reportes.
 * Los repositorios Spring Data ({@code JpaRepository}) son la implementación
 * estándar de acceso a datos;
 * este contrato aísla consultas específicas (p. ej. filtros por rango de
 * fechas) sin sustituir al repositorio.
 */
public interface VentaDAO {

    List<Venta> buscarVentasEntreFechas(LocalDate desde, LocalDate hasta);

    List<Venta> buscarConFiltros(
            LocalDate desde,
            LocalDate hasta,
            TipoEntrega tipoEntrega,
            Integer clienteId,
            EstadoPedido estadoPedido,
            EstadoPago estadoPago);
}
