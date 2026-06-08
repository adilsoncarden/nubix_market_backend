package com.nubix.market.module.product.mapper;

import com.nubix.market.module.product.dto.ProductoPublicResponse;
import com.nubix.market.module.product.dto.ProductoResponse;
import com.nubix.market.module.product.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public ProductoResponse toResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecioCompra(),
                producto.getPrecioVenta(),
                producto.getStock(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null);
        response.setUrlImagen(producto.getUrlImagen());
        return response;
    }

    public ProductoPublicResponse toPublicResponse(Producto producto) {
        ProductoPublicResponse response = new ProductoPublicResponse(
                producto.getId(),
                producto.getCodigo(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecioVenta(),
                producto.getStock(),
                producto.getCategoria() != null ? producto.getCategoria().getNombre() : null);
        response.setUrlImagen(producto.getUrlImagen());
        return response;
    }
}
