package com.hmacadamia.Factura;

import com.hmacadamia.pos.Factura;
import com.hmacadamia.superclass.Producto;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.UnitValue;
import java.io.*;
import java.text.NumberFormat;
import java.util.Locale;

public class ImpresoraTermica {

    private final int tamanoCostante = 95;
    private final String EMAILINTFINITY = "soluciones@intfinity.co";


    public void generarPDFTer(Factura factura) {
        String dest = "src/main/resources/com/PDF/factura #-" +factura.getNumeroFactura() +".pdf";
        String imagePath = "src/main/resources/com/Logos/intfinity.png";
        int numeroitems = factura.getCantidadItems();

        try {

            PdfWriter writer = new PdfWriter(new FileOutputStream(dest));
            PdfDocument pdf = new PdfDocument(writer);

            // Configurar el tamaño de la página (58 mm de ancho)
            float width = 58 * 2.83465f; // 1 mm = 2.83465 pt
            float height = (tamanoCostante + (numeroitems * 5)) * 2.83465f; // Altura inicial (será ajustada)
            pdf.setDefaultPageSize(new com.itextpdf.kernel.geom.PageSize(width, height));

            Document document = new Document(pdf);
            document.setMargins(5, 5, 5, 5);
            // Márgenes pequeños

            // Añadir el contenido al PDF con una fuente más pequeña
            document.add(new Paragraph("Empresa: Chips Love").setFontSize(12).setFixedLeading(5));
            document.add(new Paragraph("Nit: *******").setFontSize(8).setFixedLeading(5));
            document.add(new Paragraph("Factura No: " + factura.getNumeroFactura()).setFontSize(8).setFixedLeading(5));
            document.add(new Paragraph("Fecha: " + factura.getFecha()).setFontSize(8).setFixedLeading(5));
            document.add(new Paragraph("Direccion: Copacabana-Antioquia").setFontSize(8).setFixedLeading(5));
            document.add(new Paragraph("----------------------------------------").setFontSize(8).setFixedLeading(10));


            // Tabla para los artículos
            float[] columnWidths = {3, 1, 2, 2}; // Ajustar anchos de columna
            Table table = new Table(UnitValue.createPercentArray(columnWidths)).useAllAvailableWidth();
            table.addHeaderCell(new Paragraph("Descripcion").setFontSize(8));
            table.addHeaderCell(new Paragraph("Cant").setFontSize(8));
            table.addHeaderCell(new Paragraph("Precio").setFontSize(8));
            table.addHeaderCell(new Paragraph("Subtotal").setFontSize(8));

            // Configuración del formato numérico para precio y subtotal
            NumberFormat numberFormatter = NumberFormat.getNumberInstance(new Locale("es", "CO"));

            for (Producto item : factura.getItems()) {
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
            document.add(new Paragraph("----------------------------------------").setFontSize(8).setFixedLeading(5));
            document.add(new Paragraph("Total: " + currencyFormatter.format(factura.getTotal())).setFontSize(14).setFixedLeading(5));
            document.add(new Paragraph("----------------------------------------").setFontSize(8).setFixedLeading(5));
            document.add(new Paragraph(factura.getFecha()).setFontSize(8).setFixedLeading(5));
            document.add(new Paragraph("Impreso por software administrativo: Intfinity Brick").setFontSize(6).setFixedLeading(5));
            document.add(new Paragraph("Email: " + EMAILINTFINITY).setFontSize(8).setFixedLeading(10));

            ImageData imageData = ImageDataFactory.create(imagePath);
            Image image = new Image(imageData);
            image.setWidth(50);
            image.setHorizontalAlignment(HorizontalAlignment.CENTER);// Ajusta el tamaño de la imagen según sea necesario
            document.add(image);


            // Cerrar el documento para obtener la altura total del contenido
            document.close();

            System.out.println("PDF generado en: " + dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

