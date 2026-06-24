package com.nubix.market.module.sale.util;

import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;

import java.util.List;
import java.util.Map;

/**
 * Utilidad que define y valida las transiciones permitidas del estado de un pedido
 * según el tipo de entrega (Fast Lane, delivery o presencial).
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
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

    /**
     * Constructor privado para impedir la instanciación de esta clase de utilidad.
     */
    private OrderStatusFlow() {
    }

    /**
     * Obtiene la secuencia de estados válidos para el tipo de entrega indicado.
     *
     * @param tipoEntrega tipo de entrega del pedido; si es {@code null}, se usa delivery
     * @return lista ordenada de estados permitidos para ese tipo de entrega
     */
    public static List<EstadoPedido> flowFor(TipoEntrega tipoEntrega) {
        if (tipoEntrega == null) {
            return FLOW_BY_TIPO.get(TipoEntrega.DELIVERY);
        }
        return FLOW_BY_TIPO.getOrDefault(tipoEntrega, FLOW_BY_TIPO.get(TipoEntrega.DELIVERY));
    }

    /**
     * Valida que la transición del estado actual al nuevo estado sea permitida
     * para el tipo de entrega del pedido.
     *
     * @param tipoEntrega  tipo de entrega asociado al pedido
     * @param estadoActual estado actual del pedido
     * @param estadoNuevo  estado al que se desea cambiar
     * @throws RuntimeException si algún estado es nulo, el pedido ya fue entregado,
     *                          el estado no aplica al tipo de entrega o se intenta retroceder
     */
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
