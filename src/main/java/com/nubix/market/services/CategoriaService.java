package com.nubix.market.services;

import com.nubix.market.entities.Categoria;
import com.nubix.market.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CategoriaService {

    @Autowired 
    private CategoriaRepository categoriaRepository;

    public List<CategoriaService> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<CategoriaService> obtenerPorId(Integer id){
        return categoriaRepository.findById(id);
    }

    public CategoriaService guardar(CategoriaService categoria){
        if (categoriaRepository.existsByNombre(categoria.getNombre())){
            throw new RuntimeException("El nombre de la categoria ya está en uso");
        }
        if (categoriaRepository.existsBySlug(categoria.getSlug())) {
            throw new RuntimeException("El slug (URL) ya está en uso");
        }
        return categoriaRepository.save(categoria);
    }
    
    public CategoriaService actualizar(Integer id, CategoriaService detalles) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            
            // validar que el nuevo nombre
            //no choque con otra categoría existente
            if (!categoriaExistente.getNombre().equals(detalles.getNombre()) && 
                categoriaRepository.existsByNombre(detalles.getNombre())) {
                throw new RuntimeException("El nombre de la categoría ya está en uso");
            }
            // validar el slug
            if (!categoriaExistente.getSlug().equals(detalles.getSlug()) && 
                categoriaRepository.existsBySlug(detalles.getSlug())) {
                throw new RuntimeException("El slug (URL) ya está en uso");
            }
            categoriaExistente.setNombre(detalles.getNombre());
            categoriaExistente.setSlug(detalles.getSlug());
            categoriaExistente.setDescripcion(detalles.getDescripcion());

            return categoriaRepository.save(categoriaExistente);
        }).orElseThrow(() -> new RuntimeException("Categoria no encontrada"));
}

public void eliminar(Integer id) {
    if (!categoriaRepository.existsById(id)) {
        throw new RuntimeException("Categoría no encontrada");
    }
    categoriaRepository.deleteById(id);
}

}
