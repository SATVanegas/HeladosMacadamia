package com.hmacadamia.pos;

import com.google.gson.Gson;
import com.hmacadamia.inventario.ProductoInventario;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Venta {

    private Integer id;
    private Date fecha;
    private double total;
    private List<ProductoInventario> productoInventarios;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<ProductoInventario> getProductoInventarios() {
        return productoInventarios;
    }

    public void setProductoInventarios(List<ProductoInventario> productoInventarios) {
        this.productoInventarios = productoInventarios;
    }

    public String getProductoIdsAsJson() {
        List<Integer> productoIds = productoInventarios.stream()
                .map(ProductoInventario::getId)
                .collect(Collectors.toList());
        Gson gson = new Gson();
        return gson.toJson(productoIds);
    }
}
