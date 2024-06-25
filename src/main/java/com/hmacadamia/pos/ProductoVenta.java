package com.hmacadamia.pos;

import com.hmacadamia.superclass.Producto;

public class ProductoVenta extends Producto {

    private double subtotal;
    private int cantidad;

    public ProductoVenta(int id, String nombre, int cantidad,double precio) {
        super(id, nombre, precio);
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
