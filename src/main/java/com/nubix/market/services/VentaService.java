package com.nubix.market.services;

import com.nubix.market.dto.VentaRequest;
import com.nubix.market.entities.DetalleVenta;
import com.nubix.market.entities.Producto;
import com.nubix.market.entities.Usuario;
import com.nubix.market.entities.Venta;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.repositories.VentaRepository;
import com.nubix.market.repositories.UsuarioRepository;
import com.nubix.market.repositories.ProductoRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaService {
    @Autowired
    private VentaRepository ventaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;

    public List<Venta> obtenerTodasLasVentas() {
        return ventaRepository.findAll();
    }

    //Metodo para crear venta por mientras se desarrolla el proceso de compra
    public Venta crearVenta(VentaRequest request) {
        Usuario cliente = usuarioRepository.findById(request.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        Usuario vendedor = usuarioRepository.findById(request.getVendedorId())
                .orElseThrow(() -> new RuntimeException("Vendedor no encontrado"));

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setVendedor(vendedor);
        venta.setMetodoPago(request.getMetodoPago());
        venta.setTipoEntrega(request.getTipoEntrega());

        //Logica de EstadoPago
        if (request.getMetodoPago() == MetodoPago.CREDITO) {
            venta.setEstadoPago(EstadoPago.PENDIENTE);
        } else {
            venta.setEstadoPago(EstadoPago.PAGADO);
        }

        //Logica de Entrega
        if (request.getTipoEntrega() == TipoEntrega.DELIVERY) {
            venta.setDireccionEntrega(request.getDireccionEntrega());
        } else {
            venta.setCodigoRecojo(UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }

        double total = 0.0;

        //Procesar detalles de venta y descontar stock
        for (VentaRequest.DetalleVentaRequest item : request.getDetalles()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
            
            //Verificar stock
            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            //Descontar stock
            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecioVenta());

            double subtotal = item.getCantidad() * producto.getPrecioVenta();
            detalle.setSubtotal(subtotal);
            total += subtotal;

            venta.getDetalles().add(detalle);
        }
        
        venta.setTotal(total);
        return ventaRepository.save(venta);
    }

    public Venta actualizarEstadoPedido(Integer ventaId, EstadoPedido nuevoEstado) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + ventaId));
        venta.setEstadoPedido(nuevoEstado);
        return ventaRepository.save(venta);
    }

    public Venta RegistrarCredito(Integer ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        
        if (venta.getMetodoPago() != MetodoPago.CREDITO) {
            throw new RuntimeException("La venta no es a crédito");
        }
        if (venta.getEstadoPago() == EstadoPago.PAGADO) {
            throw new RuntimeException("La venta ya ha sido pagada");
        }
        
        venta.setEstadoPago(EstadoPago.PAGADO);
        return ventaRepository.save(venta);
    }
}
