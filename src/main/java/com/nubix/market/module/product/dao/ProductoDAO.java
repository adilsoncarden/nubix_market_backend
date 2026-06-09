package com.nubix.market.module.product.dao;

import com.nubix.market.module.product.model.Producto;
import java.util.List;

/**
 * DAO de consultas compuestas para productos.
 * Complementa {@code ProductoRepository} en escenarios de reportes y alertas de stock.
 */
public interface ProductoDAO {

    /**
     * Productos con stock menor al umbral, opcionalmente filtrados por categoría.
     */
    List<Producto> buscarConStockBajo(int umbral, Integer categoriaId);
}
