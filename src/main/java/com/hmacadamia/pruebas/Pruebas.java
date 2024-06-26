package com.hmacadamia.pruebas;

import com.hmacadamia.Factura.ImpresoraTermica;
import com.hmacadamia.pos.Factura;

public class Pruebas {
    public static void main(String[] args) {
        Factura factura = new Factura("1223","6/25/2024 12:17 PM");

        factura.agregarItem(1,"mazana",2,1709.8);
        factura.agregarItem(2,"limon",2,2709.8);
        factura.agregarItem(3,"pina",2,42709.8);
        factura.agregarItem(4,"helado",3,4709.8);
        factura.agregarItem(5,"uva",1,3709.8);
        factura.agregarItem(6,"banasplit",7,21709.8);

        System.out.println(factura.generarFormatoFactura());
        String textoFactura = factura.generarFormatoFactura();
        ImpresoraTermica impresora = new ImpresoraTermica();
        impresora.imprimir(textoFactura);
    }
}
