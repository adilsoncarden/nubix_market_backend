package com.nubix.market.module.report.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.module.report.service.ReporteExportService;
import java.time.LocalDate;

/**
 * Controlador REST diseñado para facilitar la descarga de reportes gerenciales 
 * desde el panel de administración.
 * Expone endpoints que construyen y devuelven archivos binarios en formato Excel (.xlsx).
 */
@RestController
@RequestMapping("/api/admin/reportes")
public class ReporteController {

    private final ReporteExportService reporteExportService;

    public ReporteController(ReporteExportService reporteExportService) {
        this.reporteExportService = reporteExportService;
    }

    /**
     * Exporta el catálogo de productos a un archivo Excel, permitiendo aplicar 
     * filtros dinámicos como categoría, alertas de stock bajo o rangos de precios.
     *
     * @return Archivo Excel (bytes) con las cabeceras HTTP configuradas para forzar la descarga.
     */
    @GetMapping(value = "/productos", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportarProductos(
            @RequestParam(required = false) Integer categoriaId,
            @RequestParam(required = false) Boolean stockBajo,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax) {
        byte[] body = reporteExportService.exportarProductosExcel(
                categoriaId, stockBajo, precioMin, precioMax);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"productos.xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(body);
    }

    /**
     * Exporta la lista completa de categorías registradas en el sistema a un archivo Excel.
     */
    @GetMapping(value = "/categorias", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportarCategorias() {
        byte[] body = reporteExportService.exportarCategoriasExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"categorias.xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(body);
    }

    /**
     * Exporta el directorio de proveedores a un archivo Excel.
     */
    @GetMapping(value = "/proveedores", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportarProveedores() {
        byte[] body = reporteExportService.exportarProveedoresExcel();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"proveedores.xlsx\"")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(body);
    }
    
    /**
     * Exporta el historial de ventas dentro de un rango de fechas obligatorio, 
     * permitiendo añadir filtros avanzados por estado, tipo de entrega o cliente.
     * El nombre del archivo descargable incluye dinámicamente las fechas seleccionadas.
     *
     * @param desde Fecha de inicio del reporte (Obligatoria).
     * @param hasta Fecha de fin del reporte (Obligatoria).
     */
    @GetMapping(value = "/ventas", produces = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
    public ResponseEntity<byte[]> exportarVentas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(required = false) TipoEntrega tipoEntrega,
            @RequestParam(required = false) Integer clienteId,
            @RequestParam(required = false) EstadoPedido estadoPedido,
            @RequestParam(required = false) EstadoPago estadoPago) {
        if (desde == null || hasta == null) {
            return ResponseEntity.badRequest().build();
        }
        if (hasta.isBefore(desde)) {
            return ResponseEntity.badRequest().build();
        }
        byte[] body = reporteExportService.exportarVentasExcel(
                desde, hasta, tipoEntrega, clienteId, estadoPedido, estadoPago);
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
