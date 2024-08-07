package com.hmacadamia.controllers;
import com.hmacadamia.repo.ProductosRepo;
import com.hmacadamia.repo.RepositorioGenerico;
import com.hmacadamia.superclass.Producto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class InventarioController implements Initializable {
    @FXML
    private GridPane gridInventario;
    @FXML
    private TextField txtSearchInventario;
    @FXML
    private ListView<String> suggestionsList;

    @FXML
    private Button btnAgregar;

    protected static List<Producto> productos;
    private final RepositorioGenerico<Producto> repoPv = new ProductosRepo();
    private final ObservableList<String> suggestions = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productos = new ArrayList<>(data());
        setupSearchFieldListener();
        setupSuggestionsListListener();
        showAllProducts();
        btnAgregar.setOnAction(event -> openWindow());
    }

    private List<Producto> data() {
        return repoPv.findall();
    }

    //ejecutar los productos dentro del grid
    private void showAllProducts() {
        updateGridPane(productos);
    }
    //poner los productos dentro del gridpane
    private void updateGridPane(List<Producto> products) {
        if (products == null || products.isEmpty()) {
            return; // No hacer nada si la lista de productos está vacía o es nula
        }
        gridInventario.getChildren().clear();
        gridInventario.setHgap(70); // Espacio horizontal entre columnas
        gridInventario.setVgap(10); // Espacio vertical entre filas

        int columns = 0;
        int rows = 1;
        try {
            List<Producto> productosFiltrados = products.stream().filter(productoVenta -> !productoVenta.isProduct()).toList();
            for (Producto product : productosFiltrados) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosVistaController productosVistaController = fxmlLoader.getController();
                productosVistaController.setInventarioController(this); // Pasar referencia al controlador principal
                productosVistaController.setData(product);

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

    private void setupSearchFieldListener() {
        txtSearchInventario.textProperty().addListener((observable, _, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                showAllProducts();
                suggestionsList.setVisible(false);
            } else {
                List<String> filtered = productos.stream()
                        .map(Producto::getDescripcion)  // Convertir ID a String
                        .filter(id -> id.toLowerCase().startsWith(newValue.toLowerCase()))
                        .collect(Collectors.toList());
                suggestions.setAll(filtered);
                suggestionsList.setVisible(!filtered.isEmpty());
            }
        });

        txtSearchInventario.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if ((event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) && !suggestions.isEmpty()) {
                String selectedSuggestion = suggestionsList.getSelectionModel().getSelectedItem();
                if (selectedSuggestion != null) {
                    txtSearchInventario.setText(selectedSuggestion);
                } else {
                    txtSearchInventario.setText(suggestions.getFirst());
                }
                suggestionsList.setVisible(false);
                if (event.getCode() == KeyCode.ENTER) {
                    performSearch(txtSearchInventario.getText());
                }
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER && txtSearchInventario.getText().isEmpty()) {
                showAllProducts();
                event.consume();
            }
        });
    }

    private void setupSuggestionsListListener() {
        suggestionsList.setItems(suggestions);

        suggestionsList.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                txtSearchInventario.setText(suggestionsList.getSelectionModel().getSelectedItem());
                suggestionsList.setVisible(false);
                performSearch(txtSearchInventario.getText());
            }
        });

        suggestionsList.setOnMouseClicked(_ -> {
            txtSearchInventario.setText(suggestionsList.getSelectionModel().getSelectedItem());
            suggestionsList.setVisible(false);
            performSearch(txtSearchInventario.getText());
        });
    }

    private void performSearch(String query) {
        if (query == null || query.isEmpty()) {
            return; // No hacer nada si la lista de productos está vacía o es nula
        }

        List<Producto> filteredProducts = productos.stream()
                .filter(producto -> (producto.getDescripcion()).toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        updateGridPane(filteredProducts);
    }

    private void openWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/hmacadamia/addWindow.fxml"));
            Parent addWindowRoot = loader.load();
            Scene addWindowScene = new Scene(addWindowRoot);

            Stage addWindow = new Stage();
            addWindow.initModality(Modality.APPLICATION_MODAL);
            addWindow.setTitle("Añadir Producto");
            addWindow.setScene(addWindowScene);
            addWindow.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
