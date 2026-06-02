package com.nubix.market.module.sale.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.module.sale.model.Venta;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class VentaDAOImpl implements VentaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Venta> buscarVentasEntreFechas(LocalDate desde, LocalDate hasta) {
        return buscarConFiltros(desde, hasta, null, null, null, null);
    }

    @Override
    public List<Venta> buscarConFiltros(
            LocalDate desde,
            LocalDate hasta,
            TipoEntrega tipoEntrega,
            Integer clienteId,
            EstadoPedido estadoPedido,
            EstadoPago estadoPago) {
        StringBuilder jpql = new StringBuilder("SELECT v FROM Venta v WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (desde != null) {
            jpql.append(" AND v.fecha >= :desde");
            params.put("desde", desde);
        }
        if (hasta != null) {
            jpql.append(" AND v.fecha <= :hasta");
            params.put("hasta", hasta);
        }
        if (tipoEntrega != null) {
            jpql.append(" AND v.tipoEntrega = :tipoEntrega");
            params.put("tipoEntrega", tipoEntrega);
        }
        if (clienteId != null) {
            jpql.append(" AND v.cliente.id = :clienteId");
            params.put("clienteId", clienteId);
        }
        if (estadoPedido != null) {
            jpql.append(" AND v.estadoPedido = :estadoPedido");
            params.put("estadoPedido", estadoPedido);
        }
        if (estadoPago != null) {
            jpql.append(" AND v.estadoPago = :estadoPago");
            params.put("estadoPago", estadoPago);
        }

        jpql.append(" ORDER BY v.id DESC");

        TypedQuery<Venta> query = entityManager.createQuery(jpql.toString(), Venta.class);
        params.forEach(query::setParameter);
        return query.getResultList();
    }
}
