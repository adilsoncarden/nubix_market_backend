package com.nubix.market.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carrito")
public class CarritoWebController {
    
    // Pagina de carrito, se muestra el carrito de compras del usuario, con el nombre del producto, su precio, la cantidad y el total, ademas de un boton para eliminar el producto del carrito
    // Antes de eliminar un producto del carrito, se debe verificar que el producto exista en el carrito del usuario
    // Además el usuario debe estar autenticado para poder ver su carrito, si no lo está, se le redirige a la página de login
    // Tambien tendra un boton para finalizar la compra, que redirige a la pagina de checkout
}
