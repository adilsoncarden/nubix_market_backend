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
 * Controlador REST privado diseñado para la gestión de ventas desde el panel administrativo.
 * Permite a los empleados crear ventas presenciales (Punto de Venta/POS), registrar créditos, 
 * y avanzar el estado de los pedidos web (ej. de "Pendiente" a "En Camino" o "Entregado").
 */
@RestController
@RequestMapping("/api/admin")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    /**
     * Lista el historial completo de todas las ventas del negocio (tanto web como presenciales).
     * Utiliza @JsonView para enviar una versión resumida sin sobrecargar la red con detalles de productos.
     */
    @JsonView(JsonViews.List.class)
    @GetMapping("/ventas")
    public ResponseEntity<List<Venta>> obtenerTodasLasVentas() {
        return ResponseEntity.ok(ventaService.obtenerTodasLasVentas());
    }

    /**
     * Devuelve el detalle completo de una venta específica, incluyendo todos los productos 
     * comprados, cálculos de IGV y datos del cliente.
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
     * Crea una nueva venta desde el panel de administración (ej. un cliente comprando en tienda física).
     * Asigna valores por defecto de Canal (PRESENCIAL) y Tipo de Comprobante (TICKET) si no se especifican.
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
     * Actualiza la etapa logística de un pedido.
     * Ejemplo: Un empleado cambia el pedido #102 de "PREPARANDO" a "LISTO_PARA_RECOJO".
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
     * Registra que una venta (probablemente presencial o corporativa) ha sido dada "A crédito" 
     * y cambia su estado de pago consecuentemente.
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
