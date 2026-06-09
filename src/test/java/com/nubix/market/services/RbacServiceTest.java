package com.nubix.market.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nubix.market.module.user.dto.PermisoRequest;
import com.nubix.market.module.user.model.Permiso;
import com.nubix.market.module.user.repository.PermisoRepository;
import com.nubix.market.module.user.repository.RolRepository;
import com.nubix.market.module.user.repository.UsuarioRepository;
import com.nubix.market.module.user.service.RbacService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RbacServiceTest {

    @Mock
    private PermisoRepository permisoRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private RbacService rbacService;

    @Test
    void crearPermiso_nombreDuplicado_lanzaExcepcion() {
        PermisoRequest request = new PermisoRequest();
        request.setNombre("ver:productos");
        request.setDescripcion("Ver productos");
        request.setModulo("Productos");

        when(permisoRepository.findByNombre("ver:productos")).thenReturn(Optional.of(new Permiso()));

        assertThatThrownBy(() -> rbacService.crearPermiso(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Ya existe");
        verify(permisoRepository, never()).save(any());
    }

    @Test
    void crearPermiso_exito() {
        PermisoRequest request = new PermisoRequest();
        request.setNombre("ver:reportes");
        request.setDescripcion("Ver reportes");
        request.setModulo("Reportes");

        when(permisoRepository.findByNombre("ver:reportes")).thenReturn(Optional.empty());
        when(permisoRepository.save(any(Permiso.class))).thenAnswer(inv -> {
            Permiso p = inv.getArgument(0);
            p.setId(99);
            return p;
        });

        var response = rbacService.crearPermiso(request);

        assertThat(response.getNombre()).isEqualTo("ver:reportes");
        assertThat(response.getModulo()).isEqualTo("Reportes");
    }
}
