package com.nubix.market.module.product.service;

import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.repository.CategoriaRepository;
import com.nubix.market.module.product.dto.ProductoRequest;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAllWithCategoria();
    }

    public Optional<Producto> obtenerPorId(Integer id) {
        return productoRepository.findByIdWithRelations(id);
    }

    public Producto guardar(ProductoRequest request) {
        validarPreciosYStock(request);
        if (StringUtils.isBlank(request.getCodigo()) || StringUtils.isBlank(request.getNombre())) {
            throw new RuntimeException("Código y nombre del producto son obligatorios");
        }
        if (productoRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("El código del producto ya existe");
        }

        if (request.getCategoriaId() == null) {
            throw new RuntimeException("La categoría es obligatoria");
        }
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto producto = new Producto();
        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecioCompra(request.getPrecioCompra());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setStock(request.getStock());
        producto.setCategoria(categoria);
        producto.setUrlImagen(normalizarUrlImagen(request.getUrlImagen()));

        log.info("Creando producto código={} nombre={}", request.getCodigo(), request.getNombre());
        return productoRepository.save(producto);
    }

    public Producto actualizar(Integer id, ProductoRequest detalles) {
        validarPreciosYStock(detalles);
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (StringUtils.isBlank(detalles.getCodigo()) || StringUtils.isBlank(detalles.getNombre())) {
            throw new RuntimeException("Código y nombre del producto son obligatorios");
        }

        if (!producto.getCodigo().equals(detalles.getCodigo()) &&
                productoRepository.existsByCodigo(detalles.getCodigo())) {
            throw new RuntimeException("El nuevo código de producto ya está en uso");
        }

        if (detalles.getCategoriaId() == null) {
            throw new RuntimeException("La categoría es obligatoria");
        }
        Categoria categoria = categoriaRepository.findById(detalles.getCategoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        producto.setCodigo(detalles.getCodigo());
        producto.setNombre(detalles.getNombre());
        producto.setDescripcion(detalles.getDescripcion());
        producto.setPrecioCompra(detalles.getPrecioCompra());
        producto.setPrecioVenta(detalles.getPrecioVenta());
        producto.setStock(detalles.getStock());
        producto.setCategoria(categoria);
        producto.setUrlImagen(normalizarUrlImagen(detalles.getUrlImagen()));

        log.debug("Actualizando producto id={} a código={}", id, detalles.getCodigo());
        return productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        log.info("Eliminando producto id={}", id);
        productoRepository.delete(producto);
    }

    private String normalizarUrlImagen(String urlImagen) {
        return StringUtils.trimToNull(urlImagen);
    }

    private void validarPreciosYStock(ProductoRequest request) {
        if (request.getPrecioCompra() != null && request.getPrecioCompra() < 0) {
            throw new RuntimeException("El precio de compra no puede ser negativo");
        }
        if (request.getPrecioVenta() != null && request.getPrecioVenta() < 0) {
            throw new RuntimeException("El precio de venta no puede ser negativo");
        }
        if (request.getStock() != null && request.getStock() < 0) {
            throw new RuntimeException("El stock no puede ser negativo");
        }
    }
}
