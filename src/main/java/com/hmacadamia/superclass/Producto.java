package com.hmacadamia.superclass;

import com.hmacadamia.pos.CategoriaProducto;

public class Producto {
    private int id;
    private String descripcion;
    private double precio;
    private String urlimg;
    private boolean isProduct;
    private double subtotal;
    private int cantidad;
    private CategoriaProducto categoria;

    public Producto(int id, String descripcion, double precio, String urlimg, boolean isProduct, double subtotal, int cantidad, CategoriaProducto categoria) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
        this.urlimg = urlimg;
        this.isProduct = isProduct;
        this.subtotal = subtotal;
        this.cantidad = cantidad;
        this.categoria = categoria;
    }
    
    

    public Producto() {
    }

    public Producto(int id, String descripcion, double precio) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precio;
    }


    public Producto(int id, String descripcion, double precioUnitario, int cantidad) {
        this.id = id;
        this.descripcion = descripcion;
        this.precio = precioUnitario;
        this.cantidad = cantidad;
    }

    public boolean isProduct() {
        return isProduct;
    }

    public void setProduct(boolean product) {
        isProduct = product;
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

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getCategoria() {
        return categoria.getCategoriaP();
    }

    public void setCategoria(CategoriaProducto categoria) {
        this.categoria = categoria;
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
