package com.nubix.market.module.sale.util;

import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;

import java.util.List;
import java.util.Map;

public final class OrderStatusFlow {

    private static final Map<TipoEntrega, List<EstadoPedido>> FLOW_BY_TIPO = Map.of(
            TipoEntrega.FAST_LANE,
            List.of(
                    EstadoPedido.PENDIENTE,
                    EstadoPedido.EN_PROCESO,
                    EstadoPedido.LISTO_PARA_RECOJO,
                    EstadoPedido.ENTREGADO),
            TipoEntrega.DELIVERY,
            List.of(
                    EstadoPedido.PENDIENTE,
                    EstadoPedido.EN_PROCESO,
                    EstadoPedido.EN_CAMINO,
                    EstadoPedido.ENTREGADO),
            TipoEntrega.PRESENCIAL,
            List.of(
                    EstadoPedido.PENDIENTE,
                    EstadoPedido.EN_PROCESO,
                    EstadoPedido.ENTREGADO));

    private OrderStatusFlow() {
    }

    public static List<EstadoPedido> flowFor(TipoEntrega tipoEntrega) {
        if (tipoEntrega == null) {
            return FLOW_BY_TIPO.get(TipoEntrega.DELIVERY);
        }
        return FLOW_BY_TIPO.getOrDefault(tipoEntrega, FLOW_BY_TIPO.get(TipoEntrega.DELIVERY));
    }

    public static void validateTransition(
            TipoEntrega tipoEntrega,
            EstadoPedido estadoActual,
            EstadoPedido estadoNuevo) {
        if (estadoActual == null || estadoNuevo == null) {
            throw new RuntimeException("El estado del pedido es obligatorio");
        }

        if (estadoActual == EstadoPedido.ENTREGADO) {
            throw new RuntimeException(
                    "El pedido ya fue entregado y no puede modificarse");
        }

        List<EstadoPedido> flow = flowFor(tipoEntrega);
        int actualIndex = flow.indexOf(estadoActual);
        int nuevoIndex = flow.indexOf(estadoNuevo);

        if (actualIndex < 0) {
            throw new RuntimeException(
                    "El estado actual no es válido para el tipo de entrega "
                            + tipoEntrega);
        }

        if (nuevoIndex < 0) {
            throw new RuntimeException(
                    "El estado " + estadoNuevo + " no aplica al tipo de entrega "
                            + tipoEntrega);
        }

        if (nuevoIndex < actualIndex) {
            throw new RuntimeException("No se puede retroceder el estado del pedido");
        }
    }
}
