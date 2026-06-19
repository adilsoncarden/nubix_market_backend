package com.nubix.market.module.cart.service;

import com.nubix.market.module.cart.dto.CarritoItemRequest;
import com.nubix.market.module.cart.model.Carrito;
import com.nubix.market.module.cart.model.CarritoItem;
import com.nubix.market.module.cart.repository.CarritoRepository;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import com.nubix.market.module.user.model.Usuario;
import com.nubix.market.module.user.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * Servicio central del módulo de carrito de compras.
 * Aplica reglas de negocio como la validación estricta de stock disponible,
 * el manejo de cantidades y la persistencia de las selecciones del usuario.
 */
@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Recupera el carrito activo del usuario. Si el usuario no tiene un carrito, 
     * se le crea uno vacío automáticamente.
     *
     * @param usuarioId El ID del cliente logueado.
     * @return La entidad Carrito poblada.
     */
    public Carrito obtenerCarritoUsuario(Integer usuarioId) {
        return carritoRepository.findByUsuarioIdWithItems(usuarioId)
                .orElseGet(() -> crearCarrito(usuarioId));
    }

    /**
     * Añade un nuevo producto al carrito o incrementa la cantidad si ya existía.
     * Valida de forma rigurosa que el producto exista y que haya suficiente stock en tienda.
     *
     * @param usuarioId El ID del cliente logueado.
     * @param request DTO con el ID del producto y la cantidad deseada.
     * @return El carrito actualizado.
     * @throws RuntimeException Si el producto está agotado o no hay suficiente stock.
     */
    @Transactional
    public Carrito agregarItem(Integer usuarioId, CarritoItemRequest request) {
        if (request.getProductoId() == null || request.getCantidad() == null || request.getCantidad() < 1) {
            throw new RuntimeException("Producto y cantidad son obligatorios");
        }
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (producto.getStock() <= 0) {
            throw new RuntimeException("Producto agotado");
        }
        if (producto.getStock() < request.getCantidad()) {
            throw new RuntimeException(
                    "Solo hay " + producto.getStock() + " unidades disponibles");
        }

        Carrito carrito = obtenerCarritoUsuario(usuarioId);
        CarritoItem existente = carrito.getItems().stream()
                .filter(i -> i.getProducto().getId().equals(producto.getId()))
                .findFirst()
                .orElse(null);

        if (existente != null) {
            int nuevaCantidad = existente.getCantidad() + request.getCantidad();
            if (producto.getStock() < nuevaCantidad) {
                throw new RuntimeException(
                        "Solo hay " + producto.getStock() + " unidades disponibles");
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

    /**
     * Ajusta la cantidad exacta de un producto en el carrito (ej. usando botones +/- del frontend).
     * Si la cantidad baja a 0, el producto se elimina del carrito.
     * Vuelve a validar la disponibilidad de stock por si este cambió entre el agregado inicial y ahora.
     *
     * @param usuarioId El ID del cliente logueado.
     * @param productoId El ID del producto a modificar.
     * @param cantidad La nueva cantidad final deseada.
     * @return El carrito actualizado.
     * @throws RuntimeException Si no hay suficiente stock para cubrir la nueva cantidad.
     */
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
            if (item.getProducto().getStock() <= 0) {
                throw new RuntimeException("Producto agotado");
            }
            if (item.getProducto().getStock() < cantidad) {
                throw new RuntimeException(
                        "Solo hay " + item.getProducto().getStock() + " unidades disponibles");
            }
            item.setCantidad(cantidad);
        }

        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    /**
     * Remueve completamente un ítem del carrito de compras.
     *
     * @param usuarioId El ID del cliente logueado.
     * @param productoId El ID del producto a retirar.
     * @return El carrito actualizado.
     */
    @Transactional
    public Carrito eliminarItem(Integer usuarioId, Integer productoId) {
        Carrito carrito = obtenerCarritoUsuario(usuarioId);
        carrito.getItems().removeIf(i -> i.getProducto().getId().equals(productoId));
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    /**
     * Limpia completamente el carrito del usuario dejándolo vacío.
     * Se llama típicamente después de procesar una venta exitosa o cuando el usuario aborta su compra.
     *
     * @param usuarioId El ID del cliente logueado.
     */
    @Transactional
    public void vaciarCarrito(Integer usuarioId) {
        Carrito carrito = obtenerCarritoUsuario(usuarioId);
        carrito.getItems().clear();
        carrito.setFechaActualizacion(LocalDateTime.now());
        carritoRepository.save(carrito);
    }

    /**
     * Método privado de utilidad para inicializar un carrito nuevo en la BD para un usuario existente.
     */
    private Carrito crearCarrito(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Carrito carrito = new Carrito();
        carrito.setUsuario(usuario);
        carrito.setFechaActualizacion(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }
}
