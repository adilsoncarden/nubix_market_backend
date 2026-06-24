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
 * Servicio de negocio para productos favoritos del usuario.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Obtiene productos favoritos de un usuario.
     * @param usuarioId Id del usuario destino.
     * @return resultado de la operación
     */
    public List<Producto> listarFavoritos(Integer usuarioId) {
        return favoritoRepository.findAllByUsuario_Id(usuarioId).stream()
                .map(Favorito::getProducto)
                .collect(Collectors.toList());
    }

    /**
     * Agrega o quita un producto de favoritos.
     * @param usuario Usuario propietario.
     * @param productoId valor del parámetro
     * @return resultado de la operación
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
     * Elimina un producto de favoritos.
     * @param usuarioId Id del usuario destino.
     * @param productoId valor del parámetro
     */
    @Transactional
    public void eliminarFavorito(Integer usuarioId, Integer productoId) {
        favoritoRepository.deleteByUsuario_IdAndProducto_Id(usuarioId, productoId);
    }
}
