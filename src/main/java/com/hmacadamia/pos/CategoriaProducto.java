package com.hmacadamia.pos;

public enum CategoriaProducto {
    CHIPS("CHIPS"),
    GASEOSAS("GASEOSAS"),
    ADICIONES("ADICIONES"),
    SALSAS("SALSAS");

    private final String CategoriaP;

    CategoriaProducto(String categoriaP){
        this.CategoriaP = categoriaP;
    }

    public String getCategoriaP() {
        return CategoriaP;
    }
}
