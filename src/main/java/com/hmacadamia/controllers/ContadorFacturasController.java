package com.hmacadamia.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ContadorFacturasController {

    private static final String FILE_PATH = "src/main/resources/com/ContadorFacturas/facturasNum.txt";


    protected void incrementarFactura() {
        try {
            int numeroActual = leerNumeroFactura();
            numeroActual++;
            escribirNumeroFactura(numeroActual);
            mostrarAlerta("Número de Factura Incrementado", "El nuevo número de factura es: " + numeroActual);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo incrementar el número de factura.");
        }
    }

    @FXML
    protected int consultarFactura() {
        int numeroActual = 0;
        try {
            numeroActual = leerNumeroFactura();
            mostrarAlerta("Consultar Número de Factura", "El número actual de factura es: " + numeroActual);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo consultar el número de factura.");
        }
        return numeroActual;
    }

    private int leerNumeroFactura() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
        String linea = reader.readLine();
        reader.close();
        if (linea == null || linea.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(linea);
    }

    private void escribirNumeroFactura(int numero) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH));
        writer.write(String.valueOf(numero));
        writer.close();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

