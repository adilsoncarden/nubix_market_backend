package com.nubix.market.module.product.service;

import com.google.common.base.Preconditions;
import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.repository.CategoriaRepository;
import com.nubix.market.module.product.dao.ProductoDAO;
import com.nubix.market.module.product.dto.ProductoRequest;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de negocio para productos.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Service
public class ProductoService {

    public static final int STOCK_BAJO_UMBRAL = 10;

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoDAO productoDAO;

    public ProductoService(
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            ProductoDAO productoDAO) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoDAO = productoDAO;
    }

    /**
     * Obtiene todos los registros.
     * @return resultado de la operación
     */
    public List<Producto> obtenerTodos() {
        return productoRepository.findAllWithCategoria();
    }

    /**
     * Obtiene productos con stock bajo.
     * @param categoriaId Identificador de categoría.
     * @return resultado de la operación
     */
    public List<Producto> obtenerConStockBajo(Integer categoriaId) {
        return productoDAO.buscarConStockBajo(STOCK_BAJO_UMBRAL, categoriaId);
    }

    /**
     * Obtiene un recurso por identificador.
     * @param id Identificador único.
     * @return resultado de la operación
     */
    public Optional<Producto> obtenerPorId(Integer id) {
        Preconditions.checkArgument(id != null && id > 0, "El id del producto es obligatorio");
        return productoRepository.findByIdWithRelations(id);
    }

    /**
     * Persiste un nuevo recurso.
     * @param request valor del parámetro
     * @return resultado de la operación
     */
    public Producto guardar(ProductoRequest request) {
        Preconditions.checkNotNull(request, "La solicitud de producto es obligatoria");
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

    /**
     * Actualiza un recurso existente.
     * @param id Identificador único.
     * @param detalles valor del parámetro
     * @return resultado de la operación
     */
    public Producto actualizar(Integer id, ProductoRequest detalles) {
        Preconditions.checkNotNull(id, "El id del producto es obligatorio");
        Preconditions.checkNotNull(detalles, "La solicitud de actualización es obligatoria");
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

    /**
     * Elimina un recurso.
     * @param id Identificador único.
     */
    public void eliminar(Integer id) {
        Preconditions.checkNotNull(id, "El id del producto es obligatorio");
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        log.info("Eliminando producto id={}", id);
        productoRepository.delete(producto);
    }

    private String normalizarUrlImagen(String urlImagen) {
        return StringUtils.trimToNull(urlImagen);
    }

    private void validarPreciosYStock(ProductoRequest request) {
        if (request.getPrecioCompra() != null) {
            Preconditions.checkArgument(
                    request.getPrecioCompra() >= 0,
                    "El precio de compra no puede ser negativo");
        }
        if (request.getPrecioVenta() != null) {
            Preconditions.checkArgument(
                    request.getPrecioVenta() >= 0,
                    "El precio de venta no puede ser negativo");
        }
        if (request.getStock() != null) {
            Preconditions.checkArgument(request.getStock() >= 0, "El stock no puede ser negativo");
        }
    }
}
