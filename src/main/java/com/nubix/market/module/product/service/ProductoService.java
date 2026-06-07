package com.nubix.market.module.product.service;

import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.repository.CategoriaRepository;
import com.nubix.market.module.media.model.ProductoImagen;
import com.nubix.market.module.media.repository.ProductoImagenRepository;
import com.nubix.market.module.product.dto.ProductoRequest;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoService.class);

    private static final String UPLOAD_SUBDIR = "productos";

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProductoImagenRepository imagenRepository;

    public List<Producto> obtenerTodos() {
        return productoRepository.findAllWithImagen();
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

        if (request.getImagenId() != null) {
            ProductoImagen imagen = imagenRepository.findById(request.getImagenId())
                    .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
            producto.setImagen(imagen);
        }

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

        log.debug("Actualizando producto id={} a código={}", id, detalles.getCodigo());
        return productoRepository.save(producto);
    }

    public void eliminar(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ProductoImagen imagen = producto.getImagen();

        if (imagen != null) {
            producto.setImagen(null);
            productoRepository.saveAndFlush(producto);
        }

        log.info("Eliminando producto id={}", id);
        productoRepository.delete(producto);

        if (imagen != null) {
            eliminarImagenFisicaYRegistro(imagen);
        }
    }

    public Producto subirImagenProducto(Integer productoId, MultipartFile archivo) {
        if (archivo == null || archivo.isEmpty()) {
            throw new RuntimeException("El archivo es obligatorio");
        }
        if (archivo.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("La imagen no puede superar 5 MB");
        }
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("Solo se permiten archivos de imagen");
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        try {
            String nombreArchivo = UUID.randomUUID() + "_" + sanitizarNombreArchivo(archivo.getOriginalFilename());
            String rutaRelativa = UPLOAD_SUBDIR + "/" + nombreArchivo;

            Path directorio = obtenerDirectorioUpload();
            Files.copy(
                    archivo.getInputStream(),
                    directorio.resolve(nombreArchivo),
                    StandardCopyOption.REPLACE_EXISTING);

            ProductoImagen imagenAnterior = producto.getImagen();

            ProductoImagen nuevaImagen = new ProductoImagen();
            nuevaImagen.setArchivo(rutaRelativa);
            ProductoImagen imagenGuardada = imagenRepository.save(nuevaImagen);

            producto.setImagen(imagenGuardada);
            Producto productoActualizado = productoRepository.save(producto);

            if (imagenAnterior != null) {
                eliminarImagenFisicaYRegistro(imagenAnterior);
            }

            log.info("Imagen subida para producto id={}", productoId);
            return productoActualizado;
        } catch (IOException e) {
            log.error("Error al subir imagen de producto id={}: {}", productoId, e.getMessage(), e);
            throw new RuntimeException("Error al subir la imagen: " + e.getMessage());
        }
    }

    public Producto eliminarImagenProducto(Integer productoId) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        ProductoImagen imagen = producto.getImagen();
        if (imagen != null) {
            eliminarImagenFisicaYRegistro(imagen);
            producto.setImagen(null);
            return productoRepository.save(producto);
        }

        return producto;
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

    private String sanitizarNombreArchivo(String original) {
        if (original == null || original.isBlank()) {
            return "imagen";
        }
        return original.replaceAll("[^a-zA-Z0-9._-]", "_");
    }

    private Path obtenerDirectorioUpload() throws IOException {
        Path directorio = Path.of(System.getProperty("user.dir"), "uploads", UPLOAD_SUBDIR);
        if (!Files.exists(directorio)) {
            Files.createDirectories(directorio);
        }
        return directorio;
    }

    private void eliminarImagenFisicaYRegistro(ProductoImagen imagen) {
        try {
            Path rutaImagen = Path.of(System.getProperty("user.dir"), "uploads", imagen.getArchivo());
            Files.deleteIfExists(rutaImagen);
            imagenRepository.delete(imagen);
        } catch (Exception e) {
            log.error("Error eliminando imagen id={}: {}", imagen.getId(), e.getMessage(), e);
            throw new RuntimeException("Error al eliminar la imagen: " + e.getMessage());
        }
    }
}
