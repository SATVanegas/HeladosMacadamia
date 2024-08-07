package com.hmacadamia.controllers;

import com.hmacadamia.Factura.ImpresoraTermica;
import com.hmacadamia.format.Formatos;
import com.hmacadamia.pos.Factura;
import com.hmacadamia.repo.ProductosRepo;
import com.hmacadamia.repo.RepositorioGenerico;
import com.hmacadamia.superclass.Producto;
import com.hmacadamia.util.FechaFormato;
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

public class FacturacionController implements Initializable {

    @FXML
    private TableView<Producto> tableView;
    @FXML
    private TableColumn<Producto, Long> ColumCodigo;
    @FXML
    private TableColumn<Producto, String> ColumDescripcion;
    @FXML
    private TableColumn<Producto, Integer> ColumCantidad;
    @FXML
    private TableColumn<Producto, Double> ColumPrecioVenta;
    @FXML
    private TableColumn<Producto, Double> ColumSubtotal;

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
    private ComboBox<String> Cbcategoria;

    protected static List<Producto> productsCopyLocal;
    private final ObservableList<String> suggestions = FXCollections.observableArrayList();
    private final ObservableList<Producto> observablePvList = FXCollections.observableArrayList();
    RepositorioGenerico<Producto> repoProduct = new ProductosRepo();
    private double totalGeneral = 0.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupSearchFieldListener();
        setupSuggestionsListListener();
        setupCategoryComboBoxListener();
        setupCategoryComboBoxItems();
        productsCopyLocal = new ArrayList<>(data());
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

            String formattedValue = Formatos.formatNumber(newValue);
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
        ColumSubtotal.setCellFactory(_ -> new TableCell<>() {
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
        txtBuscadorF.textProperty().addListener((_, _, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                showAllProducts();
                suggestionsList.setVisible(false);
            } else {
                List<String> filtered = productsCopyLocal.stream()
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
                    txtBuscadorF.setText(suggestions.getFirst());
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
        Cbcategoria.valueProperty().addListener((_, _, newValue) -> {
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
        updateGridPane(productsCopyLocal);
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

        List<Producto> filteredProducts = productsCopyLocal.stream()
                .filter(product -> product.getDescripcion().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        updateGridPane(filteredProducts);
    }

    private void performCategoryFilter(String category) {
        List<Producto> filteredProducts = productsCopyLocal.stream()
                .filter(product -> product.getCategoria().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        updateGridPane(filteredProducts);
    }

    private void updateGridPane(List<Producto> products) {
        if (products == null || products.isEmpty()) {
            return;
        }
        GridProductos.getChildren().clear();
        GridProductos.setHgap(70);
        GridProductos.setVgap(10);

        int columns = 0;
        int rows = 1;
        try {
            List<Producto> productosFiltrados = products.stream().filter(Producto::isProduct).toList();
            for (Producto product : productosFiltrados) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosVistaController productosVistaController = fxmlLoader.getController();
                productosVistaController.setPrincipalController(this);
                productosVistaController.setData(product);

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
    private List<Producto> data() {
        return repoProduct.findall();
    }
    public void addOrUpdateProducto(Producto producto) {
        boolean exists = false;
        for (Producto existingProducto : observablePvList) {
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
            handleEnterKey();
        }
        tableView.refresh();
        updateTotal();
    }
    @FXML
    private void handleFacturar() {
        List<Producto> listaFacturar = new ArrayList<>(tableView.getItems());
        ContadorFacturasController contadorFactura = new ContadorFacturasController();
        FechaFormato fm = new FechaFormato();
        String fechaFormateada = fm.getFechaFormateada();
        String NumeroFactura = fechaFormateada.concat("-").concat(String.valueOf(contadorFactura.consultarFactura()));
        contadorFactura.incrementarFactura();
        Factura fc = new Factura(NumeroFactura, "6/26/2024");

        for (Producto producto : listaFacturar) {
            long id = producto.getId();
            String descripcion = producto.getDescripcion();
            int cantidad = producto.getCantidad();
            double precio = producto.getPrecio();
            double subtotal = producto.getSubtotal();
            fc.agregarItem((int) id, descripcion, cantidad, precio, subtotal);
        }
        ImpresoraTermica imp = new ImpresoraTermica();
        imp.generarPDFTer(fc);
    }

    private void updateTotal() {
        totalGeneral = observablePvList.stream().mapToDouble(Producto::getSubtotal).sum();
        LbTotal.setText("$ " + formatNumber(totalGeneral));
    }

    private void eliminarProductoSeleccionado() {
        Producto selectedProducto = tableView.getSelectionModel().getSelectedItem();
        if (selectedProducto != null) {
            observablePvList.remove(selectedProducto);
            tableView.refresh();
            updateTotal();
        }
    }

    @FXML
    protected void handleEnterKey() {
        String text = txtRecibe.getText().replaceAll(",", "");
        if (text.isEmpty()) {
            // Manejar el caso cuando el campo de texto está vacío
            LbCambio.setText("$ 0.00");
            return;
        }
        try {
            double number = Double.parseDouble(text);
            double vdevuelta = (number - getTotalGeneral());
            LbCambio.setText("$ " + formatNumber(vdevuelta));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Invalid number format");
            // Manejar el caso de formato de número inválido
            LbCambio.setText("$ 0.00");
        }
    }

    public double getTotalGeneral() {
        return totalGeneral;
    }
}

