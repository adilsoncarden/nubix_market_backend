package com.nubix.market.module.sale.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.nubix.market.common.jackson.JsonViews;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.module.sale.dto.VentaRequest;
import com.nubix.market.module.sale.model.Venta;
import com.nubix.market.module.sale.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Controlador REST de administración para la gestión de ventas presenciales.
 * Expone operaciones de consulta, creación, actualización de estado y registro de crédito.
 *
 * @author Grupo de Desarrollo Nubix Market
 * @version 1.0.0 (2026)
 */
@RestController
@RequestMapping("/api/admin")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    /**
     * Lista todas las ventas registradas en el sistema.
     *
     * @return respuesta HTTP 200 con la lista de ventas
     */
    @JsonView(JsonViews.List.class)
    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        return ResponseEntity.ok(ventaService.obtenerTodasLasVentas());
    }

    /**
     * Obtiene el detalle de una venta por su identificador.
     *
     * @param id identificador de la venta
     * @return respuesta HTTP 200 con la venta o 404 si no existe
     */
    @JsonView(JsonViews.Detail.class)
    @GetMapping("/ventas/{id}")
    public ResponseEntity<?> obtenerVenta(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ventaService.obtenerPorId(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Crea una nueva venta presencial desde el panel de administración.
     *
     * @param request datos de la venta a registrar
     * @return respuesta HTTP 200 con la venta creada o 400 si la validación falla
     */
    @PostMapping("/ventas/create")
    public ResponseEntity<?> crearVenta(@RequestBody VentaRequest request) {
        try {
            if (request.getCanal() == null) {
                request.setCanal(com.nubix.market.enums.CanalVenta.PRESENCIAL);
            }
            if (request.getTipoComprobante() == null) {
                request.setTipoComprobante(com.nubix.market.enums.TipoComprobante.TICKET);
            }
            return ResponseEntity.ok(ventaService.crearVenta(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Actualiza el estado de un pedido existente.
     *
     * @param id     identificador de la venta
     * @param estado nuevo estado del pedido
     * @return respuesta HTTP 200 con la venta actualizada o 400 si la transición no es válida
     */
    @PostMapping("/ventas/{id}")
    public ResponseEntity<?> actualizarEstadoPedido(
            @PathVariable Integer id,
            @RequestParam EstadoPedido estado) {
        try {
            return ResponseEntity.ok(ventaService.actualizarEstadoPedido(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Registra el pago de una venta realizada a crédito.
     *
     * @param id identificador de la venta a crédito
     * @return respuesta HTTP 200 con la venta actualizada o 400 si no aplica el crédito
     */
    @PostMapping("/ventas/{id}/credito")
    public ResponseEntity<?> registrarCredito(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(ventaService.registrarCredito(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
