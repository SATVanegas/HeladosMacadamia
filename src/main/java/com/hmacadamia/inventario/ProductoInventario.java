package com.hmacadamia.inventario;

import com.hmacadamia.superclass.Producto;

import java.util.Date;

public class ProductoInventario extends Producto {
    private int cantidadInventario;
    private Date ultimaFechadeActualizacion;

    public ProductoInventario(int id, String descripcion, double precio, String urlimg, int cantidadInventario, Date ultimaFechadeActualizacion) {
        super(id, descripcion, precio, urlimg);
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
