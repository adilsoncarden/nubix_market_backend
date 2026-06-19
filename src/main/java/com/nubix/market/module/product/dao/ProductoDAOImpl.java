package com.nubix.market.module.product.dao;

import com.nubix.market.module.product.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * Implementación concreta del DAO de productos utilizando la API nativa de JPA (EntityManager).
 * Se utiliza para construir consultas dinámicas avanzadas (JPQL) que serían muy complejas 
 * de escribir utilizando únicamente los métodos automáticos de Spring Data.
 */
@Repository
public class ProductoDAOImpl implements ProductoDAO {

    /** Gestor de entidades inyectado por Spring para interactuar directamente con la base de datos. */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Ejecuta una consulta dinámica en JPQL para encontrar productos con bajo stock.
     * Si se proporciona una categoría, añade dinámicamente esa cláusula WHERE a la consulta 
     * antes de ejecutarla. Optimiza el rendimiento usando FETCH JOIN para cargar la categoría 
     * en la misma llamada.
     */
    @Override
    public List<Producto> buscarConStockBajo(int umbral, Integer categoriaId) {
        StringBuilder jpql = new StringBuilder(
                "SELECT p FROM Producto p JOIN FETCH p.categoria c WHERE p.stock IS NOT NULL AND p.stock < :umbral");
        Map<String, Object> params = new HashMap<>();
        params.put("umbral", umbral);

        if (categoriaId != null) {
            jpql.append(" AND c.id = :categoriaId");
            params.put("categoriaId", categoriaId);
        }

        jpql.append(" ORDER BY p.stock ASC, p.nombre ASC");

        TypedQuery<Producto> query = entityManager.createQuery(jpql.toString(), Producto.class);
        params.forEach(query::setParameter);
        return query.getResultList();
    }
}
