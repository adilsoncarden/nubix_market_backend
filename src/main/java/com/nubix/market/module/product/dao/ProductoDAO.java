package com.nubix.market.module.product.dao;

import com.nubix.market.module.product.model.Producto;
import java.util.List;

/**
 * DAO de consultas compuestas para productos.
 * <p>
 * Complementa {@code ProductoRepository} en escenarios de reportes y alertas de stock.
 * </p>
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
public interface ProductoDAO {

    /**
     * Busca productos con stock menor al umbral, opcionalmente filtrados por categoría.
     *
     * @param umbral      cantidad máxima de stock (exclusivo) para considerar stock bajo
     * @param categoriaId identificador de categoría para filtrar, o {@code null} para todas
     * @return lista de productos con categoría cargada
     */
    List<Producto> buscarConStockBajo(int umbral, Integer categoriaId);
}
