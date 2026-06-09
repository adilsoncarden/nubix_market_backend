package com.nubix.market.module.sale.util;

import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderStatusFlowTest {

    @Test
    void fastLaneFlow_omitsEnCamino() {
        assertThat(OrderStatusFlow.flowFor(TipoEntrega.FAST_LANE))
                .containsExactly(
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PROCESO,
                        EstadoPedido.LISTO_PARA_RECOJO,
                        EstadoPedido.ENTREGADO);
    }

    @Test
    void deliveryFlow_omitsListoParaRecojo() {
        assertThat(OrderStatusFlow.flowFor(TipoEntrega.DELIVERY))
                .containsExactly(
                        EstadoPedido.PENDIENTE,
                        EstadoPedido.EN_PROCESO,
                        EstadoPedido.EN_CAMINO,
                        EstadoPedido.ENTREGADO);
    }

    @Test
    void validateTransition_rejectsBackwardMove() {
        assertThatThrownBy(() -> OrderStatusFlow.validateTransition(
                TipoEntrega.DELIVERY,
                EstadoPedido.EN_CAMINO,
                EstadoPedido.EN_PROCESO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("retroceder");
    }

    @Test
    void validateTransition_rejectsChangeFromEntregado() {
        assertThatThrownBy(() -> OrderStatusFlow.validateTransition(
                TipoEntrega.FAST_LANE,
                EstadoPedido.ENTREGADO,
                EstadoPedido.EN_PROCESO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("entregado");
    }

    @Test
    void validateTransition_rejectsInvalidStateForTipoEntrega() {
        assertThatThrownBy(() -> OrderStatusFlow.validateTransition(
                TipoEntrega.FAST_LANE,
                EstadoPedido.EN_PROCESO,
                EstadoPedido.EN_CAMINO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no aplica");
    }

    @Test
    void validateTransition_allowsForwardMove() {
        OrderStatusFlow.validateTransition(
                TipoEntrega.DELIVERY,
                EstadoPedido.PENDIENTE,
                EstadoPedido.EN_CAMINO);
    }
}
