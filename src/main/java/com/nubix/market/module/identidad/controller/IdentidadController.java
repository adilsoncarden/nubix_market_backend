package com.nubix.market.module.identidad.controller;

import com.nubix.market.module.identidad.dto.IdentidadConsultaResponse;
import com.nubix.market.module.identidad.service.IdentidadConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST encargado de exponer los servicios de consulta de identidad (DNI/RUC).
 * Permite al frontend validar y autocompletar datos de clientes o proveedores 
 * consumiendo una API gubernamental externa.
 */
@RestController
@RequestMapping("/api/identidad")
public class IdentidadController {

    @Autowired
    private IdentidadConsultaService identidadConsultaService;

    /**
     * Endpoint para consultar la información asociada a un número de documento peruano.
     * El sistema detecta automáticamente si se trata de un DNI (8 dígitos) o un RUC (11 dígitos).
     *
     * @param documento El número de documento a consultar enviado en la URL.
     * @return Respuesta HTTP 200 (OK) con los datos encontrados (Nombre, Dirección, etc.), 
     * o un error HTTP procesado por el manejador global si el documento no existe o es inválido.
     */
    @GetMapping("/consultar/{documento}")
    public ResponseEntity<IdentidadConsultaResponse> consultar(@PathVariable String documento) {
        return ResponseEntity.ok(identidadConsultaService.consultar(documento));
    }
}
