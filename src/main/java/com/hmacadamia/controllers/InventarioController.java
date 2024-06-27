package com.hmacadamia.controllers;
import com.hmacadamia.pos.ProductoVenta;
import com.hmacadamia.repo.ProductosRepo;
import com.hmacadamia.repo.RepositorioGenerico;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InventarioController implements Initializable {
    @FXML
    private GridPane gridInventario;
    @FXML
    private TextField txtSearchInventario;

    protected static List<ProductoVenta> productos;
    RepositorioGenerico<ProductoVenta> repoPv = new ProductosRepo();
    private final ObservableList<String> suggestions = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productos = new ArrayList<>(data());
        showAllProducts();
    }

    private List<ProductoVenta> data() {
        return repoPv.findall();
    }
    private void showAllProducts() {
        updateGridPane(productos);
    }

    private void updateGridPane(List<ProductoVenta> products) {
        if (products == null || products.isEmpty()) {
            return; // No hacer nada si la lista de productos está vacía o es nula
        }
        gridInventario.getChildren().clear();
        gridInventario.setHgap(70); // Espacio horizontal entre columnas
        gridInventario.setVgap(10); // Espacio vertical entre filas

        int columns = 0;
        int rows = 1;
        try {
            for (ProductoVenta product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosController productosController = fxmlLoader.getController();
                productosController.setInventarioController(this); // Pasar referencia al controlador principal
                productosController.setData(product);

                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }

                gridInventario.add(productosBox, columns++, rows);
                // Asegúrate de que no haya márgenes alrededor de cada VBox
                GridPane.setMargin(productosBox, Insets.EMPTY);
                GridPane.setHalignment(productosBox, HPos.CENTER);
                GridPane.setValignment(productosBox, VPos.CENTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
