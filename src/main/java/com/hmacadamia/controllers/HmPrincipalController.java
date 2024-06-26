package com.hmacadamia.controllers;

import com.hmacadamia.Models.ProductosMostrar;
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

    RepositorioGenerico<ProductoVenta> repoProductos = new ProductosRepo();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSearchFieldListener();
        setupSuggestionsListListener();

        productos = new ArrayList<>(loadDataFromRepo());
        showAllProducts();
    }

    private void setupSearchFieldListener() {
        txtBuscadorF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                showAllProducts();
                suggestionsList.setVisible(false);
            } else {
                List<Integer> filtered = productos.stream()
                        .map(ProductosMostrar::getId)
                        .filter(id -> id.toString().toLowerCase().startsWith(newValue.toLowerCase()))
                        .toList();
                suggestions.setAll(String.valueOf(filtered));
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
                .filter(producto -> Integer.toString(producto.getId()).toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        updateGridPane(filteredProducts);
    }


    private void updateGridPane(List<ProductosMostrar> products) {
        GridProductos.getChildren().clear();
        int columns = 0;
        int rows = 1;
        try {
            for (ProductosMostrar product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosController productosController = fxmlLoader.getController();
                productosController.setData(product);

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

    private List<ProductosMostrar> loadDataFromRepo() {
        List<ProductoVenta> productosVenta = repoProductos.findall();
        return productosVenta.stream()
                .map(this::convertToProductosMostrar)
                .collect(Collectors.toList());
    }

    private ProductosMostrar convertToProductosMostrar(ProductoVenta productoVenta) {
        ProductosMostrar producto = new ProductosMostrar();
        producto.setId(productoVenta.getId());
        producto.setProductoImage(productoVenta.getUrlimg());
        producto.setPrecio(productoVenta.getPrecio());
        producto.setCantidadSeleccionado(productoVenta.getCantidad());
        return producto;
    }
}
