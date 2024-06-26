package com.hmacadamia.gastos;

public enum Categoria {
    FIJOS(1),
    OTROS(2),
    NOMINA(3),
    MATERIAP(4);


    private final int valor;

    Categoria(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }

    @Override
    public String toString() {
        switch(this) {
            case FIJOS: return "Lunes";
            case OTROS: return "Martes";
            case NOMINA: return "Miércoles";
            case MATERIAP: return "Jueves";
            default: throw new IllegalArgumentException();
        }
    }
}
