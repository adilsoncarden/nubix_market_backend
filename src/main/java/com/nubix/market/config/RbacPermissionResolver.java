package com.nubix.market.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class RbacPermissionResolver {

    public String resolve(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (uri.startsWith("/api/permisos") || uri.startsWith("/api/roles")) {
            return "gestionar:seguridad";
        }

        if (!uri.startsWith("/api/admin")) {
            return null;
        }

        if (uri.startsWith("/api/admin/reportes")) {
            return "ver:dashboard";
        }

        if (uri.startsWith("/api/admin/productos")) {
            if (HttpMethod.GET.matches(method)) {
                return "ver:productos";
            }
            if (uri.contains("/create")) {
                return "crear:productos";
            }
            if (uri.contains("/update") || uri.contains("/imagen")) {
                return "editar:productos";
            }
            if (uri.contains("/delete")) {
                return "eliminar:productos";
            }
            return "ver:productos";
        }

        if (uri.startsWith("/api/admin/categorias")) {
            if (HttpMethod.GET.matches(method)) {
                return "ver:categorias";
            }
            if (uri.contains("/create")) {
                return "crear:categorias";
            }
            if (uri.contains("/update")) {
                return "editar:categorias";
            }
            if (uri.contains("/delete")) {
                return "eliminar:categorias";
            }
            return "ver:categorias";
        }

        if (uri.startsWith("/api/admin/proveedores")) {
            if (HttpMethod.GET.matches(method)) {
                return "ver:proveedores";
            }
            if (uri.contains("/create")) {
                return "crear:proveedores";
            }
            if (uri.contains("/update")) {
                return "editar:proveedores";
            }
            if (uri.contains("/delete")) {
                return "eliminar:proveedores";
            }
            return "ver:proveedores";
        }

        if (uri.startsWith("/api/admin/ventas")) {
            if (HttpMethod.GET.matches(method)) {
                return "ver:ventas";
            }
            if (uri.contains("/create")) {
                return "crear:ventas";
            }
            if (uri.contains("/credito")) {
                return "editar:ventas";
            }
            if (HttpMethod.POST.matches(method)
                    && uri.matches(".*/ventas/\\d+")) {
                return "actualizar:estado_ventas";
            }
            return "ver:ventas";
        }

        if (uri.startsWith("/api/admin/clientes") || uri.startsWith("/api/admin/empleados")) {
            return "gestionar:usuarios";
        }

        if (HttpMethod.GET.matches(method)) {
            return "ver:dashboard";
        }

        return null;
    }
}
