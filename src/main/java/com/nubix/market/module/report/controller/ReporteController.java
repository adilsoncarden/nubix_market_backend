package com.nubix.market.module.report.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nubix.market.module.report.service.ReporteExportService;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteExportService reporteExportService;

    @GetMapping(value = "/productos", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportarProductos(
            @RequestParam(required = false) Integer categoriaId) {
        byte[] body = reporteExportService.exportarProductosExcel(categoriaId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"productos.xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(body);
    }

    @GetMapping(value = "/categorias", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportarCategorias() {
        byte[] body = reporteExportService.exportarCategoriasExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"categorias.xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(body);
    }

    @GetMapping(value = "/ventas", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportarVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        if (desde == null || hasta == null) {
            return ResponseEntity.badRequest().build();
        }
        if (hasta.isBefore(desde)) {
            return ResponseEntity.badRequest().build();
        }
        byte[] body = reporteExportService.exportarVentasExcel(desde, hasta);
        String safeDesde = StringUtils.replaceChars(desde.toString(), '/', '-');
        String safeHasta = StringUtils.replaceChars(hasta.toString(), '/', '-');
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"ventas_" + safeDesde + "_" + safeHasta + ".xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(body);
    }
}
