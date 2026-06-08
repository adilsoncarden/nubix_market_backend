package com.nubix.market.module.report.service;

import com.google.common.collect.ImmutableList;
import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.repository.CategoriaRepository;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import com.nubix.market.module.supplier.model.Proveedor;
import com.nubix.market.module.supplier.repository.ProveedorRepository;
import com.nubix.market.enums.EstadoPago;
import com.nubix.market.enums.EstadoPedido;
import com.nubix.market.enums.TipoEntrega;
import com.nubix.market.enums.TipoComprobante;
import com.nubix.market.module.sale.dao.VentaDAO;
import com.nubix.market.module.sale.model.Venta;
import com.nubix.market.module.user.model.Usuario;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class ReporteExportService {

    private static final Logger log = LoggerFactory.getLogger(ReporteExportService.class);

    private static final ImmutableList<String> HEADERS_PRODUCTOS = ImmutableList.of(
            "ID", "Código", "Nombre", "Categoría", "Stock", "Precio compra", "Precio venta");

    private static final ImmutableList<String> HEADERS_CATEGORIAS = ImmutableList.of("ID", "Nombre", "Descripción");

    private static final ImmutableList<String> HEADERS_PROVEEDORES = ImmutableList.of(
            "ID", "RUC", "Nombre", "Teléfono", "Email");

    private static final ImmutableList<String> HEADERS_VENTAS = ImmutableList.of(
            "ID", "Fecha", "Cliente", "Total", "Subtotal", "IGV", "Estado pedido", "Estado pago",
            "Método pago", "Tipo comprobante", "Tipo entrega", "Código recojo");

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private VentaDAO ventaDAO;

    public byte[] exportarProductosExcel(
            Integer categoriaId,
            Boolean stockBajo,
            Double precioMin,
            Double precioMax) {
        List<Producto> productos = productoRepository.findAllWithCategoria();
        if (categoriaId != null) {
            productos = productos.stream()
                    .filter(p -> p.getCategoria() != null
                            && Objects.equals(p.getCategoria().getId(), categoriaId))
                    .toList();
        }
        if (Boolean.TRUE.equals(stockBajo)) {
            productos = productos.stream()
                    .filter(p -> p.getStock() != null && p.getStock() < 10)
                    .toList();
        }
        if (precioMin != null) {
            productos = productos.stream()
                    .filter(p -> p.getPrecioVenta() != null && p.getPrecioVenta() >= precioMin)
                    .toList();
        }
        if (precioMax != null) {
            productos = productos.stream()
                    .filter(p -> p.getPrecioVenta() != null && p.getPrecioVenta() <= precioMax)
                    .toList();
        }
        log.info("Exportando productos a Excel ({} registros)", productos.size());

        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Productos");
            Row header = sheet.createRow(0);

            int c = 0;
            for (String h : HEADERS_PRODUCTOS) {
                header.createCell(c++).setCellValue(h);
            }

            int rowIdx = 1;
            for (Producto p : productos) {
                Row row = sheet.createRow(rowIdx++);
                int col = 0;

                // Uso de Objects.requireNonNullElse para limpiar el código y quitar las
                // advertencias
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getId(), 0));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getCodigo(), ""));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getNombre(), ""));

                String nombreCategoria = (p.getCategoria() != null) ? p.getCategoria().getNombre() : "";
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(nombreCategoria, ""));

                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getStock(), 0));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getPrecioCompra(), 0.0));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getPrecioVenta(), 0.0));
            }

            for (int i = 0; i < HEADERS_PRODUCTOS.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error generando Excel de productos", e);
            throw new RuntimeException("No se pudo generar el archivo Excel", e);
        }
    }

    public byte[] exportarCategoriasExcel() {
        List<Categoria> categorias = categoriaRepository.findAll();
        log.info("Exportando categorías a Excel ({} registros)", categorias.size());

        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Categorías");
            Row header = sheet.createRow(0);
            int c = 0;
            for (String h : HEADERS_CATEGORIAS) {
                header.createCell(c++).setCellValue(h);
            }

            int rowIdx = 1;
            for (Categoria cat : categorias) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(Objects.requireNonNullElse(cat.getId(), 0));
                row.createCell(1).setCellValue(Objects.requireNonNullElse(cat.getNombre(), ""));
                row.createCell(2).setCellValue(Objects.requireNonNullElse(cat.getDescripcion(), ""));
            }

            for (int i = 0; i < HEADERS_CATEGORIAS.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error generando Excel de categorías", e);
            throw new RuntimeException("No se pudo generar el archivo Excel", e);
        }
    }

    public byte[] exportarProveedoresExcel() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        log.info("Exportando proveedores a Excel ({} registros)", proveedores.size());

        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Proveedores");
            Row header = sheet.createRow(0);
            int c = 0;
            for (String h : HEADERS_PROVEEDORES) {
                header.createCell(c++).setCellValue(h);
            }

            int rowIdx = 1;
            for (Proveedor p : proveedores) {
                Row row = sheet.createRow(rowIdx++);
                int col = 0;
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getId(), 0));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getRuc(), ""));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getNombre(), ""));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getTelefono(), ""));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(p.getEmail(), ""));
            }

            for (int i = 0; i < HEADERS_PROVEEDORES.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error generando Excel de proveedores", e);
            throw new RuntimeException("No se pudo generar el archivo Excel", e);
        }
    }

    public byte[] exportarVentasExcel(
            LocalDate desde,
            LocalDate hasta,
            TipoEntrega tipoEntrega,
            Integer clienteId,
            EstadoPedido estadoPedido,
            EstadoPago estadoPago) {
        List<Venta> ventas = ventaDAO.buscarConFiltros(
                desde, hasta, tipoEntrega, clienteId, estadoPedido, estadoPago);
        log.info("Exportando ventas a Excel ({} registros, {} → {})", ventas.size(), desde, hasta);

        try (XSSFWorkbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = wb.createSheet("Ventas");
            Row header = sheet.createRow(0);
            int hc = 0;
            for (String h : HEADERS_VENTAS) {
                header.createCell(hc++).setCellValue(h);
            }

            int rowIdx = 1;
            for (Venta v : ventas) {
                Row row = sheet.createRow(rowIdx++);
                int col = 0;
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(v.getId(), 0));
                row.createCell(col++).setCellValue(v.getFecha() != null ? v.getFecha().toString() : "");
                row.createCell(col++).setCellValue(nombreClienteVenta(v));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(v.getTotal(), 0.0));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(v.getSubtotal(), 0.0));
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(v.getIgv(), 0.0));
                row.createCell(col++).setCellValue(v.getEstadoPedido() != null ? v.getEstadoPedido().name() : "");
                row.createCell(col++).setCellValue(v.getEstadoPago() != null ? v.getEstadoPago().name() : "");
                row.createCell(col++).setCellValue(v.getMetodoPago() != null ? v.getMetodoPago().name() : "");
                row.createCell(col++).setCellValue(v.getTipoComprobante() != null ? v.getTipoComprobante().name() : "");
                row.createCell(col++).setCellValue(v.getTipoEntrega() != null ? v.getTipoEntrega().name() : "");
                row.createCell(col++).setCellValue(Objects.requireNonNullElse(v.getCodigoRecojo(), ""));
            }

            for (int i = 0; i < HEADERS_VENTAS.size(); i++) {
                sheet.autoSizeColumn(i);
            }
            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error generando Excel de ventas", e);
            throw new RuntimeException("No se pudo generar el archivo Excel", e);
        }
    }

    private static String nombreClienteVenta(Venta v) {
        if (v.getTipoComprobante() == TipoComprobante.TICKET) {
            return "Consumidor Final";
        }
        Usuario cliente = v.getCliente();
        if (cliente != null && cliente.getUsername() != null) {
            return cliente.getUsername();
        }
        if (v.getTipoComprobante() == TipoComprobante.FACTURA) {
            if (v.getRazonSocial() != null) {
                return v.getRazonSocial();
            }
            if (v.getRuc() != null) {
                return "RUC " + v.getRuc();
            }
        }
        if (v.getNombreComprobante() != null) {
            return v.getNombreComprobante();
        }
        if (v.getDni() != null) {
            return "DNI " + v.getDni();
        }
        return "Consumidor Final";
    }
}