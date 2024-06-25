package com.hmacadamia.inventario;

import java.util.Date;

public class ProductoInventario {
    private int id;
    private String nombre;
    private double precio;
    private int cantidadInventario;
    private Date ultimaFechadeActualizacion;

    public ProductoInventario(int id, String nombre, double precio, int cantidad, Date ultimaFechadeActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidadInventario = cantidad;
        this.ultimaFechadeActualizacion = ultimaFechadeActualizacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidadInventario() {
        return cantidadInventario;
    }

    public void setCantidadInventario(int cantidadInventario) {
        this.cantidadInventario = cantidadInventario;
    }

    public Date getUltimaFechadeActualizacion() {
        return ultimaFechadeActualizacion;
    }

    public void setUltimaFechadeActualizacion(Date ultimaFechadeActualizacion) {
        this.ultimaFechadeActualizacion = ultimaFechadeActualizacion;
    }
}
