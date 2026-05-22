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

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> obtenerPorId(Integer id){
        return categoriaRepository.findById(id);
    }

    public Categoria guardar(Categoria categoria){
        if (categoriaRepository.existsByNombre(categoria.getNombre())){
            throw new RuntimeException("El nombre de la categoria ya está en uso");
        }
        return categoriaRepository.save(categoria);
    }
    
    public Categoria actualizar(Integer id, Categoria detalles) {
        return categoriaRepository.findById(id).map(categoriaExistente -> {
            
            // validar que el nuevo nombre
            //no choque con otra categoría existente
            if (!categoriaExistente.getNombre().equals(detalles.getNombre()) && 
                categoriaRepository.existsByNombre(detalles.getNombre())) {
                throw new RuntimeException("El nombre de la categoría ya está en uso");
            }
            categoriaExistente.setNombre(detalles.getNombre());

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
