package com.hmacadamia.pos;

import com.hmacadamia.superclass.Producto;

public class ProductoVenta extends Producto {

    private double subtotal;
    private int cantidad;

    public ProductoVenta(int id, String descripcion, double precio, String urlimg, double subtotal, int cantidad) {
        super(id, descripcion, precio, urlimg);
        this.subtotal = subtotal;
        this.cantidad = cantidad;
    }

    public ProductoVenta(int id, String descripcion, double precio, int cantidad) {
        super(id, descripcion, precio);
        this.cantidad = cantidad;
    }

    public ProductoVenta() {

    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad() {
        this.cantidad++;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
