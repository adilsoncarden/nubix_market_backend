package com.nubix.market.module.product.dao;

import com.nubix.market.module.product.model.Producto;
import java.util.List;

/**
 * Interfaz Data Access Object (DAO) para consultas complejas de productos.
 * Complementa al repositorio estándar de Spring Data JPA (ProductoRepository) 
 * ofreciendo métodos especializados para reportes y alertas gerenciales, 
 * como el análisis de inventario.
 */
public interface ProductoDAO {

    /**
     * Recupera una lista de productos cuyo stock actual haya caído por debajo de un umbral crítico.
     * Permite opcionalmente filtrar la alerta por una categoría específica.
     *
     * @param umbral      El límite mínimo de stock aceptable (ej. 5 o 10 unidades).
     * @param categoriaId (Opcional) Filtro para evaluar solo los productos de una categoría concreta.
     * @return Lista de productos en riesgo de agotamiento, listos para generar reportes.
     */
    List<Producto> buscarConStockBajo(int umbral, Integer categoriaId);
}
