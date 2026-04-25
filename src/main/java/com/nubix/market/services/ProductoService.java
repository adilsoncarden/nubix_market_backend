package com.nubix.market.services;

import com.nubix.market.dto.ProductoRequest;
import com.nubix.market.entities.Categoria;
import com.nubix.market.entities.Producto;
import com.nubix.market.entities.ProductoImagen;
import com.nubix.market.repositories.CategoriaRepository;
import com.nubix.market.repositories.ProductoImagenRepository;
import com.nubix.market.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


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

    public Producto guardar(ProductoRequest request) {
        if (productoRepository.existsByCodigo(request.getCodigo())) {
            throw new RuntimeException("El código del producto ya existe");
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
            producto.setDescripcionCorta(detalles.getDescripcionCorta());
            producto.setDescripcionLarga(detalles.getDescripcionLarga());
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
        Producto producto = productoRepository.findById(productoId)
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    // lógica de guardado simulada (en producción se usaria AWS o una carpeta local estática)
    String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
    String rutaFalsa = "http://localhost:8080/uploads/" + nombreArchivo;

    ProductoImagen nuevaImagen = new ProductoImagen(rutaFalsa, producto);
    return ImagenRepository.save(nuevaImagen);
    }


    // ACTUALIZAR IMAGEN
   
    public ProductoImagen actualizarImagen(Integer imagenId, MultipartFile nuevoArchivo) {
        ProductoImagen imagenExistente = imagenRepository.findById(imagenId)
        .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));

        // en producción, aqui deberiamos incluir el código borrar
        // el archivo físico anterior del server o de AWS para no acumular basura.

        //Simulamos el guardado del nuevo archivo
        String nombreArchivo = java.util.UUID.randomUUID().toString() + "_" + nuevoArchivo.getOriginalFilename();
        String nuevaRuta = "http://localhost:8080/uploads/" + nombreArchivo;

        imagenExistente.setUrl(nuevaRuta);
        return imagenRepository.save(imagenExistente);
    }



    public void eliminarImagen(Integer imagenId) {
        if (!imagenRepository.existsById(imagenId)) {
            throw new RuntimeException("Imagen no encontrada");
        }
        imagenRepository.deleteById(imagenId);
    }
}
