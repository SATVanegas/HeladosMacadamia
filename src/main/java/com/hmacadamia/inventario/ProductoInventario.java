package com.hmacadamia.inventario;

import com.hmacadamia.superclass.Producto;

import java.util.Date;

public class ProductoInventario extends Producto {
    private int cantidadInventario;
    private Date ultimaFechadeActualizacion;

    public ProductoInventario(int id, String descripcion, double precio, String urlimg, boolean isProduct, int cantidadInventario, Date ultimaFechadeActualizacion) {
        super(id, descripcion, precio, urlimg, isProduct);
        this.cantidadInventario = cantidadInventario;
        this.ultimaFechadeActualizacion = ultimaFechadeActualizacion;
    }

    public ProductoInventario(int cantidadInventario, Date ultimaFechadeActualizacion) {
        this.cantidadInventario = cantidadInventario;
        this.ultimaFechadeActualizacion = ultimaFechadeActualizacion;
    }

    public ProductoInventario(int id, String descripcion, double precio, int cantidadInventario, Date ultimaFechadeActualizacion) {
        super(id, descripcion, precio);
        this.cantidadInventario = cantidadInventario;
        this.ultimaFechadeActualizacion = ultimaFechadeActualizacion;
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
