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

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarFavoritos(Integer usuarioId) {
        return favoritoRepository.findAllByUsuario_Id(usuarioId).stream()
                .map(Favorito::getProducto)
                .collect(Collectors.toList());
    }

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

    @Transactional
    public void eliminarFavorito(Integer usuarioId, Integer productoId) {
        favoritoRepository.deleteByUsuario_IdAndProducto_Id(usuarioId, productoId);
    }
}
