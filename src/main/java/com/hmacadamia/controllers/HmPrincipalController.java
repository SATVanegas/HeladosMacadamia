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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class HmPrincipalController implements Initializable {

    @FXML
    private TableView<ProductoVenta> tableView;
    @FXML
    private TableColumn<ProductoVenta, Long> ColumCodigo;
    @FXML
    private TableColumn<ProductoVenta, String> ColumDescripcion;
    @FXML
    private TableColumn<ProductoVenta, Integer> ColumCantidad;
    @FXML
    private TableColumn<ProductoVenta, Double> ColumPrecioVenta;
    @FXML
    private TableColumn<ProductoVenta, Double> ColumSubtotal;

    @FXML
    private GridPane GridProductos;
    @FXML
    private ListView<String> suggestionsList;
    @FXML
    private TextField txtBuscadorF;
    @FXML
    private Label LbTotal;
    @FXML
    private Button btnDelete;

    protected static List<ProductoVenta> productos;
    private final ObservableList<String> suggestions = FXCollections.observableArrayList();
    private final ObservableList<ProductoVenta> observableSalesList = FXCollections.observableArrayList();
    private double total = 0.0;  // Variable para almacenar el total de los subtotales
    RepositorioGenerico<ProductoVenta> repoPv = new ProductosRepo();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSearchFieldListener();
        setupSuggestionsListListener();

        productos = new ArrayList<>(data());
        showAllProducts();

        ColumCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColumDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        ColumCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        ColumPrecioVenta.setCellValueFactory(new PropertyValueFactory<>("precio"));
        ColumSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        ColumSubtotal.setCellFactory(column -> new TableCell<ProductoVenta, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatNumber(item));
                }
            }
        });

        tableView.setItems(observableSalesList); // Inicializar la tabla con la lista observable

        //manejador de evento para boton delete
        btnDelete.setOnAction(event -> eliminarProductoSeleccionado());
    }

    private String formatNumber(double value) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(value);
    }

    private void setupSearchFieldListener() {
        txtBuscadorF.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                showAllProducts();
                suggestionsList.setVisible(false);
            } else {
                List<String> filtered = productos.stream()
                        .map(producto -> String.valueOf(producto.getId()))  // Convertir ID a String
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
        List<ProductoVenta> filteredProducts = productos.stream()
                .filter(producto -> Integer.toString(producto.getId()).toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        updateGridPane(filteredProducts);
    }

    private void updateGridPane(List<ProductoVenta> products) {
        GridProductos.getChildren().clear();
        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < products.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosController productosController = fxmlLoader.getController();
                productosController.setPrincipalController(this); // Pasar referencia al controlador principal
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

    private List<ProductoVenta> data() {
        return repoPv.findall();
    }

    // Método para agregar o actualizar un producto en el controlador principal
    public void addOrUpdateProducto(ProductoVenta producto) {
        boolean exists = false;
        for (ProductoVenta existingProducto : observableSalesList) {
            if (existingProducto.getId() == producto.getId()) {
                existingProducto.setCantidad(existingProducto.getCantidad() + 1);
                existingProducto.setSubtotal(existingProducto.getSubtotal() + producto.getPrecio());
                exists = true;
                break;
            }
        }
        if (!exists) {
            producto.setCantidad(1);
            producto.setSubtotal(producto.getPrecio());
            observableSalesList.add(producto);
        }
        tableView.refresh();
        updateTotal(); // Actualizar el total después de agregar o actualizar un producto
    }

    private void updateTotal() {
        total = observableSalesList.stream().mapToDouble(ProductoVenta::getSubtotal).sum();
        LbTotal.setText("$ " + formatNumber(total)); // Puedes reemplazar esto con la lógica que necesites para mostrar el total en tu UI
    }

    public double getTotal() {
        return total;
    }

    //Elimar registro seleccionado de la tabla
    private void eliminarProductoSeleccionado() {
        ProductoVenta selectedProducto = tableView.getSelectionModel().getSelectedItem();
        if (selectedProducto != null) {
            observableSalesList.remove(selectedProducto);
            tableView.refresh();
            updateTotal(); // Actualizar el total después de eliminar un producto
        }
}
