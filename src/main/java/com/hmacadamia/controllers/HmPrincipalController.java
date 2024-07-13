package com.hmacadamia.controllers;

import com.hmacadamia.pos.Factura;
import com.hmacadamia.pos.ProductoVenta;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.text.NumberFormat;

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
    @FXML
    private TextField txtRecibe;
    @FXML
    private Label LbCambio;
    @FXML
    private Button btnFacturar;
    @FXML
    private ComboBox<String> Cbcategoria;

    protected static List<ProductoVenta> productos;
    private final ObservableList<String> suggestions = FXCollections.observableArrayList();
    private final ObservableList<ProductoVenta> observablePvList = FXCollections.observableArrayList();
    RepositorioGenerico<ProductoVenta> repoPv = new ProductosRepo();
    private double total = 0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSearchFieldListener();
        setupSuggestionsListListener();
        setupCategoryComboBoxListener();
        setupCategoryComboBoxItems();

        productos = new ArrayList<>(data());
        showAllProducts();

        ColumCodigo.setCellValueFactory(new PropertyValueFactory<>("id"));
        ColumDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        ColumCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        ColumPrecioVenta.setCellValueFactory(new PropertyValueFactory<>("precio"));
        ColumSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));

        txtRecibe.textProperty().addListener((_, oldValue, newValue) -> {
            if (!newValue.matches("[\\d,]*")) {
                txtRecibe.setText(oldValue);
                return;
            }

            String formattedValue = formatNumber(newValue);
            if (!newValue.equals(formattedValue)) {
                txtRecibe.setText(formattedValue);
            }

            // Llama al método handleEnterKey() automáticamente después de formatear el texto
            try {
                handleEnterKey();
            } catch (IllegalArgumentException e) {
                // Manejar la excepción, por ejemplo, imprimir un mensaje de error
                System.out.println("Error al manejar la tecla ENTER: " + e.getMessage());
            }
        });

// Verifica el rango antes de eliminar texto
        txtRecibe.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.BACK_SPACE) {
                event.consume(); // Consumir el evento para evitar el comportamiento predeterminado
                int caretPosition = txtRecibe.getCaretPosition();
                if (caretPosition > 0 && caretPosition <= txtRecibe.getLength()) {
                    try {
                        txtRecibe.deleteText(caretPosition - 1, caretPosition);
                    } catch (IllegalArgumentException e) {
                        // Manejar la excepción, por ejemplo, imprimir un mensaje de error
                        System.out.println("Error al eliminar texto: " + e.getMessage());
                    }
                }
            }
        });

        ColumSubtotal.setCellFactory(_ -> new TableCell<ProductoVenta, Double>() {
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

        tableView.setItems(observablePvList);

        btnDelete.setOnAction(_ -> eliminarProductoSeleccionado());
    }

    private String formatNumber(double value) {
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(value);
    }

    private void setupSearchFieldListener() {
        txtBuscadorF.textProperty().addListener((observable, _, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                showAllProducts();
                suggestionsList.setVisible(false);
            } else {
                List<String> filtered = productos.stream()
                        .map(Producto::getDescripcion)
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

    private void setupCategoryComboBoxListener() {
        Cbcategoria.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                showAllProducts();
            } else {
                performCategoryFilter(newValue);
            }
        });
    }

    private void setupCategoryComboBoxItems() {
        List<String> categories = List.of("","CHIPS", "GASEOSAS", "ADICIONES","SALSAS");
        Cbcategoria.setItems(FXCollections.observableArrayList(categories));
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

        suggestionsList.setOnMouseClicked(_ -> {
            txtBuscadorF.setText(suggestionsList.getSelectionModel().getSelectedItem());
            suggestionsList.setVisible(false);
            performSearch(txtBuscadorF.getText());
        });
    }

    private void performSearch(String query) {
        if (query == null || query.isEmpty()) {
            return;
        }

        List<ProductoVenta> filteredProducts = productos.stream()
                .filter(producto -> producto.getDescripcion().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        updateGridPane(filteredProducts);
    }

    private void performCategoryFilter(String category) {
        List<ProductoVenta> filteredProducts = productos.stream()
                .filter(producto -> producto.getCategoria().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        updateGridPane(filteredProducts);
    }

    private void updateGridPane(List<ProductoVenta> products) {
        if (products == null || products.isEmpty()) {
            return;
        }
        GridProductos.getChildren().clear();
        GridProductos.setHgap(70);
        GridProductos.setVgap(10);

        int columns = 0;
        int rows = 1;
        try {
            for (ProductoVenta product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosController productosController = fxmlLoader.getController();
                productosController.setPrincipalController(this);
                productosController.setData(product);

                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }

                GridProductos.add(productosBox, columns++, rows);
                GridPane.setMargin(productosBox, Insets.EMPTY);
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

    public void addOrUpdateProducto(ProductoVenta producto) {
        boolean exists = false;
        for (ProductoVenta existingProducto : observablePvList) {
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
            observablePvList.add(producto);
        }
        tableView.refresh();
        updateTotal();
    }

    @FXML
    private void handleFacturar() {
        List<ProductoVenta> listaFacturar = new ArrayList<>(tableView.getItems());
        Factura fc = new Factura("1", "6/26/2024");

        for (ProductoVenta producto : listaFacturar) {
            long id = producto.getId();
            String descripcion = producto.getDescripcion();
            int cantidad = producto.getCantidad();
            double precio = producto.getPrecio();

            double subtotal = producto.getSubtotal();

            fc.agregarItem((int) id, descripcion, cantidad, precio, subtotal);
        }

        System.out.println(fc.generarFormatoFactura());
    }

    private void updateTotal() {
        total = observablePvList.stream().mapToDouble(ProductoVenta::getSubtotal).sum();
        LbTotal.setText("$ " + formatNumber(total));
    }

    private void eliminarProductoSeleccionado() {
        ProductoVenta selectedProducto = tableView.getSelectionModel().getSelectedItem();
        if (selectedProducto != null) {
            observablePvList.remove(selectedProducto);
            tableView.refresh();
            updateTotal();
        }
    }

    private String formatNumber(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        try {
            text = text.replaceAll(",", "");
            Number number = Double.parseDouble(text);
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.applyPattern("#,###");

            return decimalFormat.format(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return text;
        }
    }

    @FXML
    private void handleEnterKey() {
        String text = txtRecibe.getText().replaceAll(",", "");
        if (text.isEmpty()) {
            // Manejar el caso cuando el campo de texto está vacío
            LbCambio.setText("$ 0.00");
            return;
        }
        try {
            double number = Double.parseDouble(text);
            double vdevuelta = (number - getTotal());
            LbCambio.setText("$ " + formatNumber(vdevuelta));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Invalid number format");
            // Manejar el caso de formato de número inválido
            LbCambio.setText("$ 0.00");
        }
    }


    public double getTotal() {
        return total;
    }
}
