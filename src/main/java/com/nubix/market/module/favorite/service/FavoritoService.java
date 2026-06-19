package com.nubix.market.module.favorite.service;

import com.nubix.market.module.favorite.model.Favorito;
import com.nubix.market.module.favorite.repository.FavoritoRepository;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import com.nubix.market.module.user.model.Usuario;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio encargado de la lógica de negocio de la lista de deseos o favoritos.
 * Permite listar los productos guardados y procesar la acción del botón de favoritos 
 * desde la tienda web.
 */
@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Consulta la tabla de favoritos y extrae únicamente la información de los productos 
     * asociados a un usuario para enviarlos a la vista.
     *
     * @param usuarioId El ID del cliente.
     * @return Lista de entidades Producto guardadas por el usuario.
     */
    public List<Producto> listarFavoritos(Integer usuarioId) {
        return favoritoRepository.findAllByUsuario_Id(usuarioId).stream()
                .map(Favorito::getProducto)
                .collect(Collectors.toList());
    }

    /**
     * Gestiona la lógica del botón "Agregar/Quitar a favoritos" (Toggle).
     * Revisa si la relación ya existe en BD: si existe la borra, si no existe la crea.
     *
     * @param usuario El objeto usuario completo.
     * @param productoId El identificador del producto en el catálogo.
     * @return {@code true} si el producto se agregó a la lista, {@code false} si fue removido.
     * @throws RuntimeException Si se intenta agregar un producto que no existe.
     */
    @Transactional
    public boolean toggleFavorito(Usuario usuario, Integer productoId) {
        Integer usuarioId = usuario.getId();
        if (favoritoRepository.existsByUsuario_IdAndProducto_Id(usuarioId, productoId)) {
            favoritoRepository.deleteByUsuario_IdAndProducto_Id(usuarioId, productoId);
            return false;
        }

        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Favorito f = new Favorito();
        f.setUsuario(usuario);
        f.setProducto(producto);
        favoritoRepository.save(f);
        return true;
    }

    /**
     * Remueve de manera directa un producto de la lista de favoritos.
     *
     * @param usuarioId El ID del usuario.
     * @param productoId El ID del producto a eliminar.
     */
    @Transactional
    public void eliminarFavorito(Integer usuarioId, Integer productoId) {
        favoritoRepository.deleteByUsuario_IdAndProducto_Id(usuarioId, productoId);
    }
}
