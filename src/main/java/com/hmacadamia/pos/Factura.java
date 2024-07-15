package com.hmacadamia.pos;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private String numeroFactura;
    private String fecha;
    private List<ProductoVenta> items;
    private double total;

    public Factura(String numeroFactura, String fecha) {
        this.numeroFactura = numeroFactura;
        this.fecha = fecha;
        this.items = new ArrayList<>();
        this.total = 0.0;
    }

    public void agregarItem(int id, String descripcion, int cantidad, double precioUnitario, double subtotal) {
        ProductoVenta item = new ProductoVenta(id,descripcion, precioUnitario, cantidad);
        items.add(item);
        total += subtotal;
    }


    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getFecha() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        return ahora.format(formatter);
    }

    public int getCantidadItems(){
       return this.getItems().size();
    }


    public List<ProductoVenta> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public String formatNumber(double value) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(value);
    }

    public String generarFormatoFactura() {
        StringBuilder sb = new StringBuilder();
        sb.append("Empresa: ").append("Chips Love").append("\n");
        sb.append("Nit:   ").append("*******").append("\n");
        sb.append("Factura No: ").append(numeroFactura).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Direccion: ").append("Copacabana Antioquia").append("\n");
        sb.append("-------------------------------\n");
        sb.append("Descripcion       Cant Precio Subtotal\n");
        sb.append("-------------------------------\n");

        for (ProductoVenta item : items) {
            double subtotal = item.getPrecio() * item.getCantidad();
            sb.append(String.format("%-15s %3d %8.2f %9.2f\n",
                    item.getDescripcion(), item.getCantidad(), item.getPrecio(), subtotal));
        }

        sb.append("-------------------------------\n");
        sb.append(String.format("Total: %26s\n", formatNumber(total)));
        sb.append("-------------------------------\n");
        sb.append(" ").append(getFecha()).append("\n");

        return sb.toString();
    }

    private String formatNumber(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        try {
            text = text.replaceAll(",", "");
            Number number = Double.parseDouble(text);
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.applyPattern("#,###");

            return decimalFormat.format(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return text;
        }
    }
}