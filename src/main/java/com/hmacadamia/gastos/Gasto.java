package com.hmacadamia.gastos;

import java.util.Date;

public class Gasto {
    private Categoria cat;
    private double costo;
    private Date fechagasto;
    private String descripcion;

    public Gasto(Categoria cat, double costo, Date fechagasto, String descripcion) {
        this.cat = cat;
        this.costo = costo;
        this.fechagasto = fechagasto;
        this.descripcion = descripcion;
    }

    public Categoria getCat() {
        return cat;
    }

    public void setCat(Categoria cat) {
        this.cat = cat;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public Date getFechagasto() {
        return fechagasto;
    }

    public void setFechagasto(Date fechagasto) {
        this.fechagasto = fechagasto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
