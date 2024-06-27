package com.hmacadamia.pos;

import com.hmacadamia.gastos.Categoria;
import com.hmacadamia.superclass.Producto;

public class ProductoVenta extends Producto {

    private double subtotal;
    private int cantidad;
    private Categoria categoria;



    public ProductoVenta(int id, String descripcion, double precio, String urlimg, double subtotal, int cantidad, Categoria cat) {
        super(id, descripcion, precio, urlimg);
        this.subtotal = subtotal;
        this.cantidad = cantidad;
        this.categoria = cat;
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
