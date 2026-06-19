package com.nubix.market.module.sale.util;

import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;

import java.util.List;
import java.util.Map;

/**
 * Clase utilitaria que define y controla el ciclo de vida (State Machine) de los pedidos.
 * Establece las reglas de negocio sobre cómo debe avanzar el estado de una venta 
 * dependiendo de su modalidad de entrega, evitando transiciones inválidas o retrocesos 
 * operativos.
 */
public final class OrderStatusFlow {

    /**
     * Mapa inmutable que define la secuencia lógica e irreversible de estados 
     * para cada tipo de entrega soportado por el negocio.
     */
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

    /**
     * Obtiene la secuencia de estados permitidos para un tipo de entrega específico.
     *
     * @param tipoEntrega La modalidad elegida por el cliente.
     * @return Lista ordenada de estados permitidos. Si es nulo, asume DELIVERY por defecto.
     */

    public static List<EstadoPedido> flowFor(TipoEntrega tipoEntrega) {
        if (tipoEntrega == null) {
            return FLOW_BY_TIPO.get(TipoEntrega.DELIVERY);
        }
        return FLOW_BY_TIPO.getOrDefault(tipoEntrega, FLOW_BY_TIPO.get(TipoEntrega.DELIVERY));
    }

    /**
     * Valida estrictamente si la transición de un estado a otro es permitida por el sistema.
     * Aplica reglas de negocio: no se puede editar un pedido ya entregado, 
     * no se permiten estados ajenos al tipo de entrega y no se puede retroceder en el flujo.
     *
     * @param tipoEntrega  La modalidad de entrega del pedido.
     * @param estadoActual El estado en el que se encuentra la orden actualmente.
     * @param estadoNuevo  El nuevo estado al que se intenta cambiar.
     * @throws RuntimeException Si la transición rompe alguna regla de negocio.
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
