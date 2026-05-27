package com.nubix.market.services;

import com.nubix.market.dto.VentaRequest;
import com.nubix.market.entities.Categoria;
import com.nubix.market.entities.Producto;
import com.nubix.market.entities.Rol;
import com.nubix.market.entities.Usuario;
import com.nubix.market.entities.Venta;
import com.nubix.market.enums.MetodoPago;
import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.repositories.ProductoRepository;
import com.nubix.market.repositories.UsuarioRepository;
import com.nubix.market.repositories.VentaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ProductoRepository productoRepository;
    @Mock
    private NotificacionService notificacionService;

    @InjectMocks
    private VentaService ventaService;

    @BeforeEach
    void setVendorInSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        "vendedor1",
                        "n/a",
                        List.of(new SimpleGrantedAuthority("ROLE_EMPLEADO"))));
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void crearVenta_presencial_ticket_unLinea_exito() {
        Usuario vendedor = new Usuario();
        vendedor.setUsername("vendedor1");
        Rol rol = new Rol("EMPLEADO");
        vendedor.setRol(rol);
        when(usuarioRepository.findByUsername("vendedor1")).thenReturn(Optional.of(vendedor));

        Categoria cat = new Categoria();
        cat.setId(1);
        cat.setNombre("Test");
        Producto producto = new Producto();
        producto.setId(1);
        producto.setNombre("Item");
        producto.setPrecioVenta(10.0);
        producto.setStock(50);
        producto.setCategoria(cat);
        when(productoRepository.findByIdForUpdate(1)).thenReturn(Optional.of(producto));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        when(ventaRepository.save(any(Venta.class))).thenAnswer(inv -> {
            Venta v = inv.getArgument(0);
            v.setId(500);
            return v;
        });

        VentaRequest request = new VentaRequest();
        request.setTipoComprobante(TipoComprobante.TICKET);
        request.setMetodoPago(MetodoPago.EFECTIVO);
        request.setTipoEntrega(TipoEntrega.PRESENCIAL);
        VentaRequest.DetalleVentaRequest linea = new VentaRequest.DetalleVentaRequest();
        linea.setProductoId(1);
        linea.setCantidad(2);
        request.setDetalles(List.of(linea));

        Venta resultado = ventaService.crearVenta(request);

        assertThat(resultado.getId()).isEqualTo(500);
        assertThat(resultado.getTipoComprobante()).isEqualTo(TipoComprobante.TICKET);
        verify(ventaRepository).save(any(Venta.class));
    }
}
