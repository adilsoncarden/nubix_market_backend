package com.nubix.market.services;

import com.nubix.market.dto.ProductoRequest;
import com.nubix.market.entities.Categoria;
import com.nubix.market.entities.Producto;
import com.nubix.market.entities.ProductoImagen;
import com.nubix.market.repositories.CategoriaRepository;
import com.nubix.market.repositories.ProductoImagenRepository;
import com.nubix.market.repositories.ProductoRepository;
import com.nubix.market.config.CryptoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.crypto.SecretKey;


@Service
public class ProductoService {
 
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProductoImagenRepository ImagenRepository;

    public List<Producto> obtenerTodos(){
        return productoRepository.findAll();
    }
    public Optional<Producto> obtenerPorId(Integer id) {
        return productoRepository.findById(id);
    }

    // OBTENER TODAS LAS IMÁGENES
    public List<ProductoImagen> obtenerImagenes() {
        return ImagenRepository.findAll();
    }

    public Producto guardar(ProductoRequest request) {
        if (productoRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("El código del producto ya existe");
        }
    
        Categoria categoria = categoriaRepository.findById(request.getCategoriaId())
             .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto producto = new Producto();
        producto.setCodigo(request.getCodigo());
        producto.setNombre(request.getNombre());
        producto.setPrecioCompra(request.getPrecioCompra());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setStock(request.getStock());
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }


    // ACTUALIZAR Y ELIMINAR

     public Producto actualizar(Integer id, ProductoRequest detalles) {
        Producto producto = productoRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        //validar si el código cambió y tambien si existe
        if (!producto.getCodigo().equals(detalles.getCodigo()) &&
            productoRepository.existsByCodigo(detalles.getCodigo())) {
                throw new RuntimeException("El nuevo código de producto ya está en uso");
            }

            Categoria categoria = categoriaRepository.findById(detalles.getCategoriaId())
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

            producto.setCodigo(detalles.getCodigo());
            producto.setNombre(detalles.getNombre());
            producto.setPrecioCompra(detalles.getPrecioCompra());
            producto.setPrecioVenta(detalles.getPrecioVenta());
            producto.setStock(detalles.getStock());
            producto.setCategoria(categoria);

            return productoRepository.save(producto);
     }

     public void eliminar(Integer id) {
        if (!productoRepository.existsById(id)){
            throw new RuntimeException("Producto no encontrado");
        }
        // gracias al "cascade = CascadeType.ALL" --> en la entidad Producto.
        // eliminar el producto borrará automaticamente todas sus imagenes de la base de datos 
        productoRepository.deleteById(id);
     }



    // LÓGICA DE IMAGENES
    public ProductoImagen subirImagen(Integer productoId, MultipartFile archivo) {
        try {
            String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            Path rutaArchivo = Path.of(System.getProperty("user.dir"), "uploads");
            if (!Files.exists(rutaArchivo)) {
                Files.createDirectories(rutaArchivo);
            }

            byte[] contenido = archivo.getBytes();
            SecretKey clave = CryptoUtil.generarClave();
            byte[] datosEncriptados = CryptoUtil.encriptar(contenido, clave);
            Files.write(rutaArchivo.resolve(nombreArchivo), datosEncriptados);

            /*Files.copy(
                archivo.getInputStream(),
                rutaArchivo.resolve(nombreArchivo), 
                StandardCopyOption.REPLACE_EXISTING
            );*/
            
            ProductoImagen nuevaImagen = new ProductoImagen();
            nuevaImagen.setArchivo(nombreArchivo);

            ProductoImagen imagenGuardada = ImagenRepository.save(nuevaImagen);
            return imagenGuardada;
        } catch (Exception e) {
            throw new RuntimeException("Error al subir la imagen: " + e.getMessage());
        }
    }

    public Producto asignarImagenProducto(Integer productoId,Integer imagenId

    ) {
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        ProductoImagen nuevaImagen = ImagenRepository.findById(imagenId)
                .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        // OBTENER LA IMAGEN ANTERIOR
        ProductoImagen imagenAnterior = producto.getImagen();

        // ASIGNAR NUEVA IMAGEN 
        producto.setImagen(nuevaImagen);

        // ELIMINAR IMAGEN ANTERIOR
        if (imagenAnterior != null) {
            try {
                Path rutaImagenAnterior = Path.of(
                    System.getProperty("user.dir"),"uploads", imagenAnterior.getArchivo());

                //  eliminar archivo físico
                Files.deleteIfExists(rutaImagenAnterior);

                // eliminar registro BD
                ImagenRepository.delete(imagenAnterior);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return productoRepository.save(producto);
    }
}
