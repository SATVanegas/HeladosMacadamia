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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSearchFieldListener();
        setupSuggestionsListListener();

        productos = new ArrayList<>(data());
        showAllProducts();
    }

    private void setupSearchFieldListener() {
        txtBuscadorF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
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
                showAllProducts();
                event.consume();
            }
        });
    }

    private void showAllProducts() {
        updateGridPane(productos);
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
        List<ProductosMostrar> filteredProducts = productos.stream()
                .filter(producto -> producto.getId().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        updateGridPane(filteredProducts);
    }

    private void updateGridPane(List<ProductosMostrar> products) {
        GridProductos.getChildren().clear();
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosController productosController = fxmlLoader.getController();
                productosController.setData(products.get(i));

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
            throw new RuntimeException(e);
        }
    }

    private List<ProductosMostrar> data() {
        List<ProductosMostrar> ls = new ArrayList<>();

        ls.add(createProduct("1223", "/com/ImagenesProductos/1.jpg", 2000.0, 2));
        ls.add(createProduct("23", "/com/ImagenesProductos/1.jpg", 2100.0, 2));
        ls.add(createProduct("1", "/com/ImagenesProductos/1.jpg", 2200.0, 2));
        ls.add(createProduct("3", "/com/ImagenesProductos/1.jpg", 2300.0, 2));

        return ls;
    }

    private ProductosMostrar createProduct(String id, String imagePath, double price, int cantidadSeleccionado) {
        ProductosMostrar producto = new ProductosMostrar();
        producto.setId(id);
        producto.setProductoImage(imagePath);
        producto.setPrecio(price);
        producto.setCantidadSeleccionado(cantidadSeleccionado);
        return producto;
    }
}
