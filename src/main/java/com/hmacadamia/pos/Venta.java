package com.hmacadamia.pos;

import com.hmacadamia.inventario.ProductoInventario;

import java.util.Date;
import java.util.List;

public class Venta {

    private int id;
    private Date fecha;
    private double total;
    private List<ProductoInventario> productoInventarios;

}
