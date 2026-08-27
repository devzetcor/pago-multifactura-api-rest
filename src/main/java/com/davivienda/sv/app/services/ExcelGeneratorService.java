package com.davivienda.sv.app.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.davivienda.sv.app.entities.db2.TransaccionDTO;
import com.davivienda.sv.app.entities.db2.FacturaTransaccion; // Asegúrate de importar la clase correcta

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@Service
public class ExcelGeneratorService {
    private static final Logger LOGGER = LogManager.getLogger(ExcelGeneratorService.class);
    public String generarExcelBase64ConLogo(TransaccionDTO transaccionDTO) {
        if (transaccionDTO == null) {
            throw new IllegalArgumentException("El DTO de transacción no puede ser nulo");
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Comprobante");

            // Insertar logo (ruta local o recurso)
            try (InputStream logoStream = new ClassPathResource("static/logo_davivienda.png").getInputStream()) {
                byte[] logoBytes = IOUtils.toByteArray(logoStream);
                int pictureIdx = workbook.addPicture(logoBytes, Workbook.PICTURE_TYPE_PNG);

                CreationHelper helper = workbook.getCreationHelper();
                Drawing<?> drawing = sheet.createDrawingPatriarch();
                ClientAnchor anchor = helper.createClientAnchor();
                anchor.setCol1(0);
                anchor.setRow1(0);
                Picture pict = drawing.createPicture(anchor, pictureIdx);
                pict.resize(2.0); // Ajusta el tamaño del logo
            } catch (Exception e) {
                // Opcional: Manejar si el logo no se encuentra para que no detenga la generación del Excel
            	LOGGER.error("Advertencia: No se pudo cargar el logo", e);
            }

            // Estilos
            CellStyle boldStyle = workbook.createCellStyle();
            Font boldFont = workbook.createFont();
            boldFont.setBold(true);
            boldStyle.setFont(boldFont);

            int rowNum = 5; // Dejar espacio para el logo

            // Encabezado horizontal
            Row headerRow = sheet.createRow(rowNum++);
            String[] encabezados = {"ID Transacción", "Estado", "Monto Transacción($)", "Monto Aplicado($)", "Nombre Colector", "Cuenta Cargo"};
            for (int i = 0; i < encabezados.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(encabezados[i]);
                cell.setCellStyle(boldStyle);
            }

            // Calcular monto aplicado verificando nulos
            double montoAplicado = 0.0;
            List<FacturaTransaccion> facturas = transaccionDTO.getFacturas();
            if (facturas != null) {
                montoAplicado = facturas.stream()
                        .filter(f -> "PAGADA".equalsIgnoreCase(f.getEstado()))
                        .filter(f -> f.getMonto() != null) // Evitar NullPointerException si el monto viene null
                        .mapToDouble(f -> f.getMonto().doubleValue())
                        .sum();
            }

            // Valores del encabezado principal usando el método de seguridad para nulos
            Row dataRow = sheet.createRow(rowNum++);
            Object[] valores = {
                    obtenerValorSeguro(transaccionDTO.getIdTransaccion()),
                    obtenerValorSeguro(transaccionDTO.getEstado()),
                    obtenerValorSeguro(transaccionDTO.getMontoTotal()),
                    montoAplicado, // Este ya es un primitivo calculado, no será null
                    obtenerValorSeguro(transaccionDTO.getNombreColector()),
                    obtenerValorSeguro(transaccionDTO.getCuentaCargo())
            };

            for (int i = 0; i < valores.length; i++) {
                dataRow.createCell(i).setCellValue(String.valueOf(valores[i]));
            }

            // Espacio y título del detalle
            rowNum++;
            Row tituloDetalle = sheet.createRow(rowNum++);
            Cell tituloCell = tituloDetalle.createCell(0);
            tituloCell.setCellValue("Detalle de Facturas");
            tituloCell.setCellStyle(boldStyle);

            // Encabezado del detalle
            String[] columnasDetalle = {"Nº Factura", "Cliente", "Monto", "Estado","Descripción Estado","Fecha Pago","Confirmación Pago", "Fecha Vencimiento", "NPE" };
            Row detalleHeader = sheet.createRow(rowNum++);
            for (int i = 0; i < columnasDetalle.length; i++) {
                Cell cell = detalleHeader.createCell(i);
                cell.setCellValue(columnasDetalle[i]);
                cell.setCellStyle(boldStyle);
            }

            // Filas del detalle
            if (facturas != null) {
                for (FacturaTransaccion factura : facturas) {
                    Row fila = sheet.createRow(rowNum++);
                    // NOTA: Asumo los nombres de los getters de FacturaTransaccion basados en las llaves de tu Map original.
                    // Si tus getters se llaman diferente, ajústalos aquí.
                    fila.createCell(0).setCellValue(obtenerValorSeguro(factura.getNumeroFactura()));
                    fila.createCell(1).setCellValue(obtenerValorSeguro(factura.getNombreCliente()));
                    fila.createCell(2).setCellValue(obtenerValorSeguro(factura.getMonto()));
                    fila.createCell(3).setCellValue(obtenerValorSeguro(factura.getEstado()));
                    fila.createCell(4).setCellValue(obtenerValorSeguro(factura.getDescripcionError()));
                    fila.createCell(5).setCellValue(obtenerValorSeguro(transaccionDTO.getFechaAprobacion()));
                    fila.createCell(6).setCellValue(obtenerValorSeguro(factura.getReferencia()));
                    fila.createCell(7).setCellValue(obtenerValorSeguro(factura.getFechaVencimiento()));
                    fila.createCell(8).setCellValue(obtenerValorSeguro(factura.getNpe()));
                }
            }

            // Ajustar columnas
            for (int i = 0; i < columnasDetalle.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Convertir a Base64
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            return Base64.getEncoder().encodeToString(bos.toByteArray());

        } catch (Exception e) {
        	LOGGER.error("Error generando Excel con logo", e);
            throw new RuntimeException("Error generando Excel con logo", e);
        }
    }

    /**
     * Método auxiliar para manejar valores nulos.
     * Si el objeto es null, retorna un string vacío. Si no, retorna su representación en String.
     */
    private String obtenerValorSeguro(Object valor) {
        return valor != null ? valor.toString() : "";
    }
}
