package com.nubix.market.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.repository.CategoriaRepository;
import com.nubix.market.module.product.dao.ProductoDAO;
import com.nubix.market.module.product.dto.ProductoRequest;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import com.nubix.market.module.product.service.ProductoService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private CategoriaRepository categoriaRepository;
    @Mock
    private ProductoDAO productoDAO;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void obtenerConStockBajo_delegaEnProductoDAO() {
        Producto producto = new Producto();
        producto.setId(1);
        producto.setStock(3);
        when(productoDAO.buscarConStockBajo(ProductoService.STOCK_BAJO_UMBRAL, 2))
                .thenReturn(List.of(producto));

        List<Producto> result = productoService.obtenerConStockBajo(2);

        assertThat(result).hasSize(1);
        verify(productoDAO).buscarConStockBajo(10, 2);
    }

    @Test
    void obtenerPorId_idInvalido_lanzaExcepcion() {
        assertThatThrownBy(() -> productoService.obtenerPorId(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void guardar_precioNegativo_lanzaExcepcion() {
        ProductoRequest request = new ProductoRequest();
        request.setCodigo("P001");
        request.setNombre("Arroz");
        request.setPrecioVenta(-1.0);
        request.setCategoriaId(1);

        assertThatThrownBy(() -> productoService.guardar(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("precio de venta");
    }

    @Test
    void guardar_exito() {
        ProductoRequest request = new ProductoRequest();
        request.setCodigo("P001");
        request.setNombre("Arroz");
        request.setPrecioVenta(5.0);
        request.setStock(20);
        request.setCategoriaId(1);

        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNombre("Abarrotes");

        when(productoRepository.existsByCodigo("P001")).thenReturn(false);
        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(productoRepository.save(org.mockito.ArgumentMatchers.any(Producto.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        Producto saved = productoService.guardar(request);

        assertThat(saved.getCodigo()).isEqualTo("P001");
        assertThat(saved.getCategoria()).isEqualTo(categoria);
    }
}
