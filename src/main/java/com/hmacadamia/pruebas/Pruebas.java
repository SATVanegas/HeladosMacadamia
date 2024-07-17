package com.hmacadamia.pruebas;

import com.hmacadamia.Factura.ImpresoraTermica;
import com.hmacadamia.repo.ProductosRepo;
import com.hmacadamia.repo.RepositorioGenerico;
import com.hmacadamia.superclass.Producto;
import com.hmacadamia.util.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;

public class Pruebas {
    public static void main(String[] args) throws SQLException {
        try(Connection conn = ConexionBD.getInstance()){
            RepositorioGenerico<Producto> repoclientes = new ProductosRepo();

            repoclientes.findall().forEach(System.out::println);
            ImpresoraTermica imp = new ImpresoraTermica();
            System.out.println("hi");


        }

    }
}
