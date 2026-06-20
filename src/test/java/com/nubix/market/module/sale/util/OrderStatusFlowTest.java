package com.nubix.market.module.sale.util;

import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Pruebas unitarias para la máquina de estados (State Machine) de los pedidos.
 * Garantiza que las reglas de negocio sobre el ciclo de vida de una venta 
 * sean inquebrantables.
 */
class OrderStatusFlowTest {

    /** Verifica que un pedido Fast Lane no pase por el estado "EN_CAMINO". */
    @Test
    void fastLaneFlow_omitsEnCamino() {
        assertThat(OrderStatusFlow.flowFor(TipoEntrega.FAST_LANE))
                .containsExactly(
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PROCESO,
                        EstadoPedido.LISTO_PARA_RECOJO,
                        EstadoPedido.ENTREGADO);
    }

    /** Verifica que un pedido por Delivery no pase por el estado "LISTO_PARA_RECOJO". */
    @Test
    void deliveryFlow_omitsListoParaRecojo() {
        assertThat(OrderStatusFlow.flowFor(TipoEntrega.DELIVERY))
                .containsExactly(
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PROCESO,
                        EstadoPedido.EN_CAMINO,
                        EstadoPedido.ENTREGADO);
    }

    /** Asegura que el sistema lance una excepción si se intenta retroceder el estado de un pedido. */
    @Test
    void validateTransition_rejectsBackwardMove() {
        assertThatThrownBy(() -> OrderStatusFlow.validateTransition(
                TipoEntrega.DELIVERY,
                EstadoPedido.EN_CAMINO,
                EstadoPedido.EN_PROCESO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("retroceder");
    }

    /** Asegura que un pedido finalizado (Entregado) quede bloqueado y no pueda ser alterado. */
    @Test
    void validateTransition_rejectsChangeFromEntregado() {
        assertThatThrownBy(() -> OrderStatusFlow.validateTransition(
                TipoEntrega.FAST_LANE,
                EstadoPedido.ENTREGADO,
                EstadoPedido.EN_PROCESO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("entregado");
    }

    /** Verifica que se rechacen estados que no correspondan a la modalidad de entrega. */
    @Test
    void validateTransition_rejectsInvalidStateForTipoEntrega() {
        assertThatThrownBy(() -> OrderStatusFlow.validateTransition(
                TipoEntrega.FAST_LANE,
                EstadoPedido.EN_PROCESO,
                EstadoPedido.EN_CAMINO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no aplica");
    }

    /** Confirma que el flujo normal y lógico de un pedido se apruebe sin lanzar excepciones. */
    @Test
    void validateTransition_allowsForwardMove() {
        OrderStatusFlow.validateTransition(
                TipoEntrega.DELIVERY,
                EstadoPedido.PENDIENTE,
                EstadoPedido.EN_CAMINO);
    }
}
