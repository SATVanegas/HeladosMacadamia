package com.hmacadamia.controllers;

import com.hmacadamia.Models.ProductosMostrar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HmPrincipalController implements Initializable {
    @FXML
    private GridPane GridProductos;
    @FXML
    private ListView<String> suggestionsList;
    @FXML
    private TextField txtBuscadorF;

    private List<ProductosMostrar> productos;

    private final ObservableList<String> suggestions = FXCollections.observableArrayList();
    private final List<String> predefinedWords = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSearchFieldListener();
        setupSuggestionsListListener();

        productos = new ArrayList<>(data());
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < productos.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosController productosController = fxmlLoader.getController();
                productosController.setData(productos.get(i));

                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }

                GridProductos.add(productosBox, columns++, rows);
                GridPane.setMargin(productosBox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void setupSearchFieldListener() {
        txtBuscadorF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                // Si el campo de búsqueda está vacío, mostrar todos los productos
                showAllProducts();
                suggestionsList.setVisible(false);
            } else {
                List<String> filtered = productos.stream()
                        .map(ProductosMostrar::getId)
                        .filter(id -> id.toLowerCase().startsWith(newValue.toLowerCase()))
                        .collect(Collectors.toList());
                suggestions.setAll(filtered);
                suggestionsList.setVisible(!filtered.isEmpty());
            }
        });

        txtBuscadorF.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if ((event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) && !suggestions.isEmpty()) {
                String selectedSuggestion = suggestionsList.getSelectionModel().getSelectedItem();
                if (selectedSuggestion != null) {
                    txtBuscadorF.setText(selectedSuggestion);
                } else {
                    txtBuscadorF.setText(suggestions.get(0));
                }
                suggestionsList.setVisible(false);
                if (event.getCode() == KeyCode.ENTER) {
                    performSearch(txtBuscadorF.getText());
                }
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER && txtBuscadorF.getText().isEmpty()) {
                // Si se presiona Enter con el campo de búsqueda vacío, mostrar todos los productos
                showAllProducts();
                event.consume();
            }
        });
    }

    private void showAllProducts() {
        GridProductos.getChildren().clear();
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < productos.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosController productosController = fxmlLoader.getController();
                productosController.setData(productos.get(i));

                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }

                GridProductos.add(productosBox, columns++, rows);
                GridPane.setMargin(productosBox, new Insets(10));

                GridPane.setHalignment(productosBox, HPos.CENTER);
                GridPane.setValignment(productosBox, VPos.CENTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupSuggestionsListListener() {
        suggestionsList.setItems(suggestions);

        suggestionsList.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtBuscadorF.setText(suggestionsList.getSelectionModel().getSelectedItem());
                suggestionsList.setVisible(false);
                performSearch(txtBuscadorF.getText());
            }
        });

        suggestionsList.setOnMouseClicked(event -> {
            txtBuscadorF.setText(suggestionsList.getSelectionModel().getSelectedItem());
            suggestionsList.setVisible(false);
            performSearch(txtBuscadorF.getText());
        });
    }

    private void performSearch(String query) {
        // Limpia el GridPane
        GridProductos.getChildren().clear();

        // Busca en la lista de productos
        List<ProductosMostrar> filteredProducts = productos.stream()
                .filter(producto -> producto.getId().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());

        // Añade los productos filtrados al GridPane
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < filteredProducts.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();

                ProductosController productosController = fxmlLoader.getController();
                productosController.setData(filteredProducts.get(i));

                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }

                GridProductos.add(productosBox, columns++, rows);
                GridPane.setMargin(productosBox, new Insets(10));

                GridPane.setHalignment(productosBox, HPos.CENTER);
                GridPane.setValignment(productosBox, VPos.CENTER);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private List<ProductosMostrar> data() {
        List<ProductosMostrar> ls = new ArrayList<>();

        ProductosMostrar pm = new ProductosMostrar();
        pm.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
<<<<<<< Updated upstream
        pm.setId("1223");
=======
        pm.setId("3");
>>>>>>> Stashed changes
        pm.setPrecio(2000.0);
        pm.setCantidadSeleccionado(2);
        ls.add(pm);
        ProductosMostrar kj = new ProductosMostrar();
        kj.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
<<<<<<< Updated upstream
        kj.setId("23");
        kj.setPrecio(2000.0);
        kj.setCantidadRestante(2);
=======
        kj.setId("1");
        kj.setPrecio(2100.0);
>>>>>>> Stashed changes
        kj.setCantidadSeleccionado(2);
        ls.add(kj);

        ProductosMostrar lk = new ProductosMostrar();
        lk.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
<<<<<<< Updated upstream
        lk.setId("1");
        lk.setPrecio(2000.0);
        lk.setCantidadRestante(2);
=======
        lk.setId("2");
        lk.setPrecio(2200.0);
>>>>>>> Stashed changes
        lk.setCantidadSeleccionado(2);
        ls.add(lk);

        ProductosMostrar jkdsj = new ProductosMostrar();
        jkdsj.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
<<<<<<< Updated upstream
        jkdsj.setId("3");
        jkdsj.setPrecio(2000.0);
        jkdsj.setCantidadRestante(2);
=======
        jkdsj.setId("4");
        jkdsj.setPrecio(2300.0);
>>>>>>> Stashed changes
        jkdsj.setCantidadSeleccionado(2);
        ls.add(jkdsj);



        return ls;
    }
}
