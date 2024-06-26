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

    public double getSubtotal() {
        return this.cantidad * super.getPrecio();
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
