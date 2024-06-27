package com.hmacadamia.pos;

public enum CategoriaProducto {
    INFANTILES("Infantiles"),
    BATIDOS("Batidos"),
    CONOS("Conos"),
    CUCURUCHOS("Cucuruchos");

    private final String CategoriaP;

    CategoriaProducto(String categoriaP){
        this.CategoriaP = categoriaP;
    }

    public String getCategoriaP() {
        return CategoriaP;
    }
}
