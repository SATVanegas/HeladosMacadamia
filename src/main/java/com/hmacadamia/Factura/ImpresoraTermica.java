package com.hmacadamia.Factura;

import com.hmacadamia.pos.Factura;
import com.hmacadamia.pos.ProductoVenta;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Locale;

public class ImpresoraTermica {

    public void imprimir(String texto) {
        try {
            // Asegúrate de que el texto esté formateado adecuadamente
            InputStream is = new ByteArrayInputStream(texto.getBytes(StandardCharsets.UTF_8));
            DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            PrintService[] printService = PrintServiceLookup.lookupPrintServices(flavor, pras);
            PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();

            if (printService.length == 0) {
                if (defaultService == null) {
                    System.err.println("No se encontraron impresoras.");
                    return;
                } else {
                    defaultService.createPrintJob().print(new SimpleDoc(is, flavor, null), null);
                }
            } else {
                PrintService service = ServiceUI.printDialog(null, 200, 200, printService, defaultService, flavor, pras);
                if (service != null) {
                    service.createPrintJob().print(new SimpleDoc(is, flavor, null), null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarPDF(String texto) {
        String destino = "src/main/resources/com/PDF/factura.pdf";
        try {
            File file = new File(destino);
            file.getParentFile().mkdirs(); // Crear directorios si no existen

            PdfWriter writer = new PdfWriter(destino);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Agregar el texto al documento PDF
            document.add(new Paragraph(texto)
                    .setFont(PdfFontFactory.createFont(StandardFonts.HELVETICA))
                    .setFontSize(12)
                    .setFontColor(new DeviceRgb(0, 0, 0))); // Texto en color negro

            document.close();
            System.out.println("PDF generado en: " + destino);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generarPDFTer(Factura factura) {
        String dest = "src/main/resources/com/PDF/factura.pdf";

        try {
            PdfWriter writer = new PdfWriter(new FileOutputStream(dest));
            PdfDocument pdf = new PdfDocument(writer);

            // Configurar el tamaño de la página (58 mm de ancho)
            float width = 58 * 2.83465f; // 1 mm = 2.83465 pt
            float height = 297 * 2.83465f; // Altura inicial (será ajustada)
            pdf.setDefaultPageSize(new com.itextpdf.kernel.geom.PageSize(width, height));

            Document document = new Document(pdf);
            document.setMargins(5, 5, 5, 5); // Márgenes pequeños

            // Añadir el contenido al PDF con una fuente más pequeña
            document.add(new Paragraph("Empresa: Chips Love").setFontSize(12));
            document.add(new Paragraph("Nit: *******").setFontSize(8));
            document.add(new Paragraph("Factura No: " + factura.getNumeroFactura()).setFontSize(8));
            document.add(new Paragraph("Fecha: " + factura.getFecha()).setFontSize(8));
            document.add(new Paragraph("Direccion: Copacabana\nAntioquia").setFontSize(8));
            document.add(new Paragraph("----------------------------------------").setFontSize(8));

            // Tabla para los artículos
            float[] columnWidths = {3, 1, 2, 2}; // Ajustar anchos de columna
            Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
            table.addHeaderCell(new Paragraph("Descripcion").setFontSize(8));
            table.addHeaderCell(new Paragraph("Cant").setFontSize(8));
            table.addHeaderCell(new Paragraph("Precio").setFontSize(8));
            table.addHeaderCell(new Paragraph("Subtotal").setFontSize(8));

            // Configuración del formato numérico para precio y subtotal
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(new Locale("es", "CO"));

            for (ProductoVenta item : factura.getItems()) {
                double subtotal = item.getPrecio() * item.getCantidad();
                table.addCell(new Paragraph(item.getDescripcion()).setFontSize(8));
                table.addCell(new Paragraph(String.valueOf(item.getCantidad())).setFontSize(8));
                table.addCell(new Paragraph(numberFormatter.format(item.getPrecio())).setFontSize(8));
                table.addCell(new Paragraph(numberFormatter.format(subtotal)).setFontSize(8));
            }

            document.add(table);

            // Configuración del formato de moneda para el total
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));

            // Subtotal y Total
            document.add(new Paragraph("----------------------------------------").setFontSize(8));
            document.add(new Paragraph("Total: " + currencyFormatter.format(factura.getTotal())).setFontSize(14));
            document.add(new Paragraph("----------------------------------------").setFontSize(8));
            document.add(new Paragraph(factura.getFecha()).setFontSize(8));

            // Cerrar el documento para obtener la altura total del contenido
            document.close();

            System.out.println("PDF generado en: " + dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

