package com.hmacadamia.pos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hmacadamia.inventario.ProductoInventario;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Venta {

    private Integer id;
    private Date fecha;
    private double total;
    private String productoVentasJson;

    // Constructor vacío
    public Venta() {}

    // Constructor que recibe una lista de ProductoInventario
    public Venta(Integer id, Date fecha, List<ProductoVenta> productoVenta) {
        this.id = id;
        this.fecha = fecha;
        this.setProductoInventarios(productoVenta);
    }

    // Constructor que recibe un JSON de productos
    public Venta(Integer id, Date fecha, String productoVentasJson, double total) {
        this.id = id;
        this.fecha = fecha;
        this.productoVentasJson = productoVentasJson;
        this.total = total;
    }

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

    public String getProductoVentasJson() {
        return productoVentasJson;
    }

    public void setProductoInventarios(List<ProductoVenta> productoVenta) {
        List<Integer> productoIds = productoVenta.stream()
                .map(ProductoVenta::getId)
                .collect(Collectors.toList());
        Gson gson = new Gson();
        this.productoVentasJson = gson.toJson(productoIds);
        this.total = calculateTotal(productoVenta);
    }

    public void setProductoInventarios(String productoVentasJson) {
        this.productoVentasJson = productoVentasJson;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<ProductoVenta>>() {}.getType();
        List<ProductoVenta> productoInventarios = gson.fromJson(productoVentasJson, listType);
        this.total = calculateTotal(productoInventarios);
    }

    private double calculateTotal(List<ProductoVenta> productoVentas) {
        return productoVentas.stream()
                .mapToDouble(p -> p.getPrecio() * p.getCantidad())
                .sum();
    }
}
