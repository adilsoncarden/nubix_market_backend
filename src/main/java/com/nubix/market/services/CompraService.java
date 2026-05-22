package com.nubix.market.services;

import com.nubix.market.dto.CompraRequest;
import com.nubix.market.entities.Compra;
import com.nubix.market.entities.DetalleCompra;
import com.nubix.market.entities.Categoria;
import com.nubix.market.entities.Producto;
import com.nubix.market.entities.Proveedor;
import com.nubix.market.repositories.CompraRepository;
import com.nubix.market.repositories.ProductoRepository;
import com.nubix.market.repositories.ProveedorRepository;
import com.nubix.market.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CompraService {
    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Compra> obtenerTodas() {
        return compraRepository.findAll();
    }

    @Transactional
    public Compra crearCompra(CompraRequest request) {
        Proveedor proveedor = proveedorRepository.findById(request.getProveedorId())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
                
        Compra compra = new Compra();
        compra.setNumeroFactura(request.getNumeroFactura());
        compra.setProveedor(proveedor);

        double total = 0.0;
        for(CompraRequest.DetalleCompraRequest item : request.getDetalles()){
            // Verificar si el producto existe, si no, crear uno nuevo
            Producto producto = productoRepository.findByCodigo(item.getCodigoProducto())
                    .orElseGet(() -> {
                        Categoria categoria = categoriaRepository.findById(item.getCategoriaId())
                                .orElseThrow(() -> new RuntimeException("Categoría necesaria para producto nuevo"));
                        
                        Producto nuevoProducto = new Producto();
                        nuevoProducto.setCodigo(item.getCodigoProducto());
                        nuevoProducto.setNombre(item.getNombreProducto());
                        nuevoProducto.setDescripcion(item.getDescripcion());
                        nuevoProducto.setStock(0); // Stock inicial en 0, se actualizará después
                        nuevoProducto.setPrecioVenta(item.getPrecioCompra() * 1.3); 
                        nuevoProducto.setCategoria(categoria);

                        return nuevoProducto;
                    });
        // Actualizar stock del producto
            producto.setStock(producto.getStock() + item.getCantidad());
            producto.setPrecioCompra(item.getPrecioCompra());
            productoRepository.save(producto);

        //Crear detalle de compra
            DetalleCompra detalle = new DetalleCompra();
            detalle.setCompra(compra);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioCompra());   
            
            double subtotal = item.getCantidad() * item.getPrecioCompra();
            detalle.setSubtotal(subtotal);
            total += subtotal;

            compra.getDetalles().add(detalle);
        }

        compra.setTotal(total);
        return compraRepository.save(compra);
    }
}
