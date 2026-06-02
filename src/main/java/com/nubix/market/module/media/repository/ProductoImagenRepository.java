package com.nubix.market.module.media.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.nubix.market.module.media.model.ProductoImagen;

@Repository
public interface ProductoImagenRepository extends JpaRepository<ProductoImagen, Integer> {

}
