package com.hmacadamia.superclass;

public class Producto {
    private int id;
    private String descripcion;
    private double precio;
    private String urlimg;

    public Producto(int id, String descripcion, double precio, String urlimg) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
        this.urlimg = urlimg;
    }

    public Producto(int id, String descripcion, double precio) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public String getUrlimg() {
        return urlimg;
    }

    public void setUrlimg(String urlimg) {
        this.urlimg = urlimg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
}
