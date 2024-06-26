package com.hmacadamia.controllers;

import com.hmacadamia.Models.ProductosMostrar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
        //Palabras predefinidas
        predefinedWords.add("apple");
        predefinedWords.add("banana");
        predefinedWords.add("cherry");
        predefinedWords.add("date");
        predefinedWords.add("elderberry");
        predefinedWords.add("fig");
        predefinedWords.add("grape");

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
                suggestionsList.setVisible(false);
            } else {
                List<String> filtered = predefinedWords.stream()
                        .filter(word -> word.toLowerCase().startsWith(newValue.toLowerCase()))
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
            }
        });
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

        // Aquí se agregan los resultados al GridPane
        // Este es un ejemplo simple, ajusta esto según tus necesidades
        for (int i = 0; i < predefinedWords.size(); i++) {
            if (predefinedWords.get(i).toLowerCase().contains(query.toLowerCase())) {
                Label label = new Label(predefinedWords.get(i));
                GridProductos.add(label, 0, i);
            }
        }
    }


    private List<ProductosMostrar> data() {
        List<ProductosMostrar> ls = new ArrayList<>();

        ProductosMostrar pm = new ProductosMostrar();
        pm.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
        pm.setId("123");
        pm.setPrecio(2000.0);
        pm.setCantidadRestante(2);
        pm.setCantidadSeleccionado(2);
        ls.add(pm);
        ProductosMostrar kj = new ProductosMostrar();
        kj.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
        kj.setId("123");
        kj.setPrecio(2000.0);
        kj.setCantidadRestante(2);
        kj.setCantidadSeleccionado(2);
        ls.add(kj);

        ProductosMostrar lk = new ProductosMostrar();
        lk.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
        lk.setId("123");
        lk.setPrecio(2000.0);
        lk.setCantidadRestante(2);
        lk.setCantidadSeleccionado(2);
        ls.add(lk);

        ProductosMostrar jkdsj = new ProductosMostrar();
        jkdsj.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
        jkdsj.setId("123");
        jkdsj.setPrecio(2000.0);
        jkdsj.setCantidadRestante(2);
        jkdsj.setCantidadSeleccionado(2);
        ls.add(jkdsj);



        return ls;
    }
}
