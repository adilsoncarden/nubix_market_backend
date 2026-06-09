package com.nubix.market.module.product.dao;

import com.nubix.market.module.product.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ProductoDAOImpl implements ProductoDAO {

    @PersistenceContext
    private EntityManager entityManager;

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
