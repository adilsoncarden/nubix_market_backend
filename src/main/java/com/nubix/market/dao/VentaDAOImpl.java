package com.nubix.market.dao;

import com.nubix.market.entities.Venta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class VentaDAOImpl implements VentaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Venta> buscarVentasEntreFechas(LocalDate desde, LocalDate hasta) {
        TypedQuery<Venta> query = entityManager.createQuery(
                "SELECT v FROM Venta v WHERE v.fecha BETWEEN :desde AND :hasta ORDER BY v.id DESC",
                Venta.class);
        query.setParameter("desde", desde);
        query.setParameter("hasta", hasta);
        return query.getResultList();
    }
}
