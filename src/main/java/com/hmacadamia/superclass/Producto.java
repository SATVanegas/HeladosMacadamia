package com.hmacadamia.superclass;

public class Producto {
    private int id;
    private String descripcion;
    private double precio;
    private String urlimg;
    private boolean isProduct;

    public Producto(int id, String descripcion, double precio, String urlimg, boolean isProduct) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
        this.urlimg = urlimg;
        this.isProduct = isProduct;
    }

    public Producto() {
    }

    public Producto(int id, String descripcion, double precio) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    public boolean isProduct() {
        return isProduct;
    }

    public void setProduct(boolean product) {
        isProduct = product;
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
