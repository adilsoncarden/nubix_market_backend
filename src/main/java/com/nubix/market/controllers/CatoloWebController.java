package com.nubix.market.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/catalogo")
public class CatoloWebController {
    
    // Pagina de catalogo, se muestra el catalogo de productos, con su nombre, precio y una imagen, ademas de un boton para agregar al carrito
    // Antes de agregar un producto al carrito, se debe verificar que el producto tenga stock disponible
    // Además el usuario debe estar autenticado para poder agregar productos al carrito, si no lo está, se le redirige a la página de login
    // Tambien tendra un buscador para buscar productos por nombre o categoria, y un filtro para filtrar por precio o categoria
}
