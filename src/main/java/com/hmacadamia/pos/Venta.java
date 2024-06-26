package com.hmacadamia.pos;
import com.google.gson.Gson;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Venta {

    private Integer id;
    private Date fecha;
    private double total;
    private String productoVentasJson;

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

    public void setProductoVentasJson(List<ProductoVenta> productoVentas) {
        List<Integer> productoIds = productoVentas.stream()
                .map(ProductoVenta::getId)
                .collect(Collectors.toList());
        Gson gson = new Gson();
        this.productoVentasJson = gson.toJson(productoIds);
        this.total = productoVentas.stream()
                .mapToDouble(p -> p.getPrecio() * p.getCantidad())
                .sum();
    }
}
