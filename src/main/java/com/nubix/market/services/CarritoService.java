package com.nubix.market.services;

import com.nubix.market.dto.CarritoItemRequest;
import com.nubix.market.entities.Carrito;
import com.nubix.market.entities.CarritoItem;
import com.nubix.market.entities.Producto;
import com.nubix.market.entities.Usuario;
import com.nubix.market.repositories.CarritoRepository;
import com.nubix.market.repositories.ProductoRepository;
import com.nubix.market.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public Carrito obtenerCarritoUsuario(Integer usuarioId) {
        return carritoRepository.findByUsuarioIdWithItems(usuarioId)
                .orElseGet(() -> crearCarrito(usuarioId));
    }

    @Transactional
    public Carrito agregarItem(Integer usuarioId, CarritoItemRequest request) {
        if (request.getProductoId() == null || request.getCantidad() == null || request.getCantidad() < 1) {
            throw new RuntimeException("Producto y cantidad son obligatorios");
        }
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (producto.getStock() < request.getCantidad()) {
            throw new RuntimeException("Stock insuficiente");
        }

        Carrito carrito = obtenerCarritoUsuario(usuarioId);
        CarritoItem existente = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(producto.getId()))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            int nuevaCantidad = existente.getCantidad() + request.getCantidad();
            if (producto.getStock() < nuevaCantidad) {
                throw new RuntimeException("Stock insuficiente");
            }
            existente.setCantidad(nuevaCantidad);
        } else {
            CarritoItem item = new CarritoItem();
            item.setCarrito(carrito);
            item.setProducto(producto);
            item.setCantidad(request.getCantidad());
            carrito.getItems().add(item);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito actualizarCantidad(Integer usuarioId, Integer productoId, Integer cantidad) {
        Carrito carrito = obtenerCarritoUsuario(usuarioId);
        CarritoItem item = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(productoId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Producto no está en el carrito"));

        if (cantidad == null || cantidad < 1) {
            carrito.getItems().remove(item);
        } else {
            if (item.getProducto().getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente");
            }
            item.setCantidad(cantidad);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    @Transactional
    public Carrito eliminarItem(Integer usuarioId, Integer productoId) {
        Carrito carrito = obtenerCarritoUsuario(usuarioId);
        carrito.getItems().removeIf(i -> i.getProducto().getId().equals(productoId));
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    @Transactional
    public void vaciarCarrito(Integer usuarioId) {
        Carrito carrito = obtenerCarritoUsuario(usuarioId);
        carrito.getItems().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }

    private Carrito crearCarrito(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }
}
