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
 * Controlador REST para consulta de documentos de identidad (DNI/RUC).
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/identidad")
public class IdentidadController {

    @Autowired
    private IdentidadConsultaService identidadConsultaService;

    /**
     * Consulta identidad por número de documento.
     * @param documento Número de documento (DNI o RUC).
     * @return resultado de la operación
     */
    @GetMapping("/consultar/{documento}")
    public ResponseEntity<IdentidadConsultaResponse> consultar(@PathVariable String documento) {
        return ResponseEntity.ok(identidadConsultaService.consultar(documento));
    }
}
