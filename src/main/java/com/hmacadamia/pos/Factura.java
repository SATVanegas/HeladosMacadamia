package com.hmacadamia.pos;

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

    public void agregarItem(int id, String descripcion, int cantidad, double precioUnitario) {
        ProductoVenta item = new ProductoVenta(id,descripcion, cantidad, precioUnitario);
        items.add(item);
        total += item.getSubtotal();
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public String getFecha() {
        return fecha;
    }


    public List<ProductoVenta> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    public String generarFormatoFactura() {

        StringBuilder sb = new StringBuilder();
        sb.append("Empresa: ").append("Helados Macadamia").append("\n");
        sb.append("Nit:   ").append("8239878764").append("\n");
        sb.append("Factura No: ").append(numeroFactura).append("\n");
        sb.append("Fecha: ").append(fecha).append("\n");
        sb.append("Direccion: ").append("Copacabana Antioquia").append("\n");
        sb.append("-------------------------------\n");
        sb.append("Descripcion       Cant Precio Subtotal\n");
        sb.append("-------------------------------\n");

        for (ProductoVenta item : items) {
            sb.append(String.format("%-15s %3d %6.2f %7.2f\n",
                    item.getDescripcion(), item.getCantidad(), item.getPrecio(), item.getSubtotal()));
        }

        sb.append("-------------------------------\n");
        sb.append(String.format("Total: %26.2f\n", total));
        sb.append("-------------------------------\n");
        sb.append(" ").append(fecha).append("\n");

        return sb.toString();
    }
}


