package com.nubix.market.module.product.mapper;

import com.nubix.market.module.product.dto.ProductoPublicResponse;
import com.nubix.market.module.product.dto.ProductoResponse;
import com.nubix.market.module.product.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Componente utilitario encargado de transformar (mapear) la entidad de base de datos 'Producto' 
 * hacia sus respectivos Objetos de Transferencia de Datos (DTOs).
 * Centraliza la lógica de conversión para mantener los controladores limpios.
 */
@Component
public class ProductoMapper {

    /**
     * Convierte la entidad a un DTO administrativo con todos los detalles financieros expuestos.
     *
     * @param producto La entidad Producto obtenida de la BD.
     * @return El DTO completo (ProductoResponse).
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
     * Convierte la entidad a un DTO público seguro para la vista de los clientes.
     * Oculta los márgenes de ganancia y costos internos.
     *
     * @param producto La entidad Producto obtenida de la BD.
     * @return El DTO filtrado (ProductoPublicResponse).
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
