package com.nubix.market.jackson;

import com.nubix.market.common.jackson.JsonViews;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.sale.model.DetalleVenta;
import com.nubix.market.module.sale.model.Venta;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;

class VentaJsonViewSerializationTest {

    @Test
    void detailViewIncludesSaleLineFieldsWhenDefaultViewInclusionIsDisabled() throws Exception {
        JsonMapper mapper = JsonMapper.builder()
                .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
                .build();

        Venta venta = new Venta();
        venta.setId(42);
        venta.setTotal(25.0);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setId(7);
        detalle.setCantidad(2);
        detalle.setPrecioUnitario(10.0);
        detalle.setSubtotal(20.0);

        Producto producto = new Producto();
        producto.setId(3);
        producto.setNombre("Arroz");
        producto.setCodigo("ARZ-01");
        producto.setPrecioVenta(10.0);
        detalle.setProducto(producto);
        venta.getDetalles().add(detalle);

        String json = mapper.writerWithView(JsonViews.Detail.class).writeValueAsString(venta);

        assertThat(json).contains("\"detalles\"");
        assertThat(json).contains("\"cantidad\":2");
        assertThat(json).contains("\"subtotal\":20.0");
        assertThat(json).contains("\"nombre\":\"Arroz\"");
    }
}
