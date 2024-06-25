package com.hmacadamia.Models;

public class ProductosMostrar {
    private String ProductoImage;
    private String id;
    private double precio;
    private int cantidadSeleccionado;
    private int cantidadRestante;

    public String getProductoImage() {
        return ProductoImage;
    }

    public void setProductoImage(String productoImage) {
        ProductoImage = productoImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidadSeleccionado() {
        return cantidadSeleccionado;
    }

    public void setCantidadSeleccionado(int cantidadSeleccionado) {
        this.cantidadSeleccionado = cantidadSeleccionado;
    }

    public int getCantidadRestante() {
        return cantidadRestante;
    }

    public void setCantidadRestante(int cantidadRestante) {
        this.cantidadRestante = cantidadRestante;
    }
}
