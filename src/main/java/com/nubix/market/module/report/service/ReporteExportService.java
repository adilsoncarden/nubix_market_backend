package com.nubix.market.module.report.service;

import com.google.common.collect.ImmutableList;
import com.nubix.market.module.category.model.Categoria;
import com.nubix.market.module.category.repository.CategoriaRepository;
import com.nubix.market.module.product.model.Producto;
import com.nubix.market.module.product.repository.ProductoRepository;
import com.nubix.market.module.sale.dao.VentaDAO;
import com.nubix.market.module.sale.model.Venta;
import org.apache.commons.lang3.ObjectUtils;
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

    private static final ImmutableList<String> HEADERS_CATEGORIAS = ImmutableList.of("ID", "Nombre");

    private static final ImmutableList<String> HEADERS_VENTAS = ImmutableList.of(
            "ID", "Fecha", "Total", "Subtotal", "IGV", "Estado pedido", "Estado pago",
            "Método pago", "Tipo comprobante", "Tipo entrega");

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private VentaDAO ventaDAO;

    public byte[] exportarProductosExcel(Integer categoriaId) {
        List<Producto> productos = productoRepository.findAllWithImagen();
        if (categoriaId != null) {
            productos = productos.stream()
                    .filter(p -> p.getCategoria() != null
                            && Objects.equals(p.getCategoria().getId(), categoriaId))
                    .toList();
        }
        log.info("Exportando productos a Excel ({} registros, filtro categoría={})",
                productos.size(), categoriaId);

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
                row.createCell(col++).setCellValue(p.getId() != null ? p.getId() : 0);
                row.createCell(col++).setCellValue(ObjectUtils.defaultIfNull(p.getCodigo(), ""));
                row.createCell(col++).setCellValue(ObjectUtils.defaultIfNull(p.getNombre(), ""));
                row.createCell(col++).setCellValue(
                        p.getCategoria() != null ? ObjectUtils.defaultIfNull(p.getCategoria().getNombre(), "") : "");
                row.createCell(col++).setCellValue(p.getStock() != null ? p.getStock() : 0);
                row.createCell(col++).setCellValue(p.getPrecioCompra() != null ? p.getPrecioCompra() : 0);
                row.createCell(col++).setCellValue(p.getPrecioVenta() != null ? p.getPrecioVenta() : 0);
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
                row.createCell(0).setCellValue(cat.getId() != null ? cat.getId() : 0);
                row.createCell(1).setCellValue(ObjectUtils.defaultIfNull(cat.getNombre(), ""));
            }
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            wb.write(out);
            return out.toByteArray();
        } catch (IOException e) {
            log.error("Error generando Excel de categorías", e);
            throw new RuntimeException("No se pudo generar el archivo Excel", e);
        }
    }

    public byte[] exportarVentasExcel(LocalDate desde, LocalDate hasta) {
        List<Venta> ventas = ventaDAO.buscarVentasEntreFechas(desde, hasta);
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
                row.createCell(col++).setCellValue(v.getId() != null ? v.getId() : 0);
                row.createCell(col++).setCellValue(v.getFecha() != null ? v.getFecha().toString() : "");
                row.createCell(col++).setCellValue(v.getTotal() != null ? v.getTotal() : 0);
                row.createCell(col++).setCellValue(v.getSubtotal() != null ? v.getSubtotal() : 0);
                row.createCell(col++).setCellValue(v.getIgv() != null ? v.getIgv() : 0);
                row.createCell(col++).setCellValue(v.getEstadoPedido() != null ? v.getEstadoPedido().name() : "");
                row.createCell(col++).setCellValue(v.getEstadoPago() != null ? v.getEstadoPago().name() : "");
                row.createCell(col++).setCellValue(v.getMetodoPago() != null ? v.getMetodoPago().name() : "");
                row.createCell(col++).setCellValue(
                        v.getTipoComprobante() != null ? v.getTipoComprobante().name() : "");
                row.createCell(col++).setCellValue(v.getTipoEntrega() != null ? v.getTipoEntrega().name() : "");
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
}
