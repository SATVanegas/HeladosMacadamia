package com.hmacadamia.Models;

public class ProductosMostrar {
    private String ProductoImage;
    private String id;

    private String Descripcion;

    private double precio;
    private int cantidadSeleccionado;


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

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

}
