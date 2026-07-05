package com.nubix.market.module.sale.dao;

import java.time.LocalDate;
import java.util.List;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.module.sale.model.Venta;

/**
 * Contrato de acceso a datos para consultas personalizadas de ventas orientadas a reportes.
 * Complementa a los repositorios Spring Data aislando filtros por rango de fechas y criterios
 * adicionales sin sustituir al repositorio estándar.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public interface VentaDAO {

    /**
     * Busca ventas registradas dentro de un rango de fechas.
     *
     * @param desde fecha inicial del rango (inclusive)
     * @param hasta fecha final del rango (inclusive)
     * @return lista de ventas que caen en el período indicado
     */
    List<Venta> buscarVentasEntreFechas(LocalDate desde, LocalDate hasta);

    /**
     * Busca ventas aplicando filtros opcionales de fecha, entrega, cliente y estados.
     *
     * @param desde        fecha inicial del rango (inclusive); {@code null} para omitir
     * @param hasta        fecha final del rango (inclusive); {@code null} para omitir
     * @param tipoEntrega  tipo de entrega a filtrar; {@code null} para omitir
     * @param clienteId    identificador del cliente; {@code null} para omitir
     * @param estadoPedido estado del pedido; {@code null} para omitir
     * @param estadoPago   estado del pago; {@code null} para omitir
     * @return lista de ventas que cumplen los criterios, ordenadas por id descendente
     */
    List<Venta> buscarConFiltros(
            LocalDate desde,
            LocalDate hasta,
            TipoEntrega tipoEntrega,
            Integer clienteId,
            EstadoPedido estadoPedido,
            EstadoPago estadoPago);
}
