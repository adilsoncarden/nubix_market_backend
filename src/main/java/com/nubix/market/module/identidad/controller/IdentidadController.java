package com.nubix.market.module.identidad.controller;

import com.nubix.market.module.identidad.dto.IdentidadConsultaResponse;
import com.nubix.market.module.identidad.service.IdentidadConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/identidad")
public class IdentidadController {

    @Autowired
    private IdentidadConsultaService identidadConsultaService;

    @GetMapping("/consultar/{documento}")
    public ResponseEntity<IdentidadConsultaResponse> consultar(@PathVariable String documento) {
        return ResponseEntity.ok(identidadConsultaService.consultar(documento));
    }
}
