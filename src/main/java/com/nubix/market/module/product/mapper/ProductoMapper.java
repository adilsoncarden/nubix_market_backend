package com.nubix.market.module.product.mapper;

import com.nubix.market.module.product.dto.ProductoPublicResponse;
import com.nubix.market.module.product.dto.ProductoResponse;
import com.nubix.market.module.product.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Mapper entre entidades de producto y DTOs de respuesta.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Component
public class ProductoMapper {

    /**
     * Convierte Producto a ProductoResponse.
     * @param producto Producto asociado.
     * @return resultado de la operación
     */
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

    /**
     * Convierte Producto a ProductoPublicResponse.
     * @param producto Producto asociado.
     * @return resultado de la operación
     */
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
