package com.hmacadamia.controllers;
import com.hmacadamia.pos.Factura;
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
    private Label lblCambio;
    @FXML
    private Button btnFacturar;

    protected static List<ProductoVenta> productos;
    private final ObservableList<String> suggestions = FXCollections.observableArrayList();
    private final ObservableList<ProductoVenta> observablePvList = FXCollections.observableArrayList();
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

        // Add listener to format text while typing
        txtRecibe.textProperty().addListener((observable, oldValue, newValue) -> {
            // Allow only digits and commas
            if (!newValue.matches("[\\d,]*")) {
                txtRecibe.setText(oldValue);
                return;
            }

            // Format the number
            txtRecibe.setText(formatNumber(newValue));
        });
        // Add listener for Enter key press
        txtRecibe.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case ENTER:
                    handleEnterKey();
                    break;
                default:
                    break;
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

        tableView.setItems(observablePvList); // Inicializar la tabla con la lista observable

        //manejador de evento para boton delete
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
            return; // No hacer nada si la lista de productos está vacía o es nula
        }

        List<ProductoVenta> filteredProducts = productos.stream()
                .filter(producto -> Integer.toString(producto.getId()).toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        updateGridPane(filteredProducts);
    }

    private void updateGridPane(List<ProductoVenta> products) {
        if (products == null || products.isEmpty()) {
            return; // No hacer nada si la lista de productos está vacía o es nula
        }
        GridProductos.getChildren().clear();
        int columns = 0;
        int rows = 1;
        try {
            for (ProductoVenta product : products) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosController productosController = fxmlLoader.getController();
                productosController.setPrincipalController(this); // Pasar referencia al controlador principal
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
            //noinspection CallToPrintStackTrace
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
        updateTotal(); // Actualizar el total después de agregar o actualizar un producto
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

        // Aquí puedes proceder con la lógica adicional de facturación si es necesario
        System.out.println(fc.generarFormatoFactura());
    }


    private void updateTotal() {
        // Variable para almacenar el total de los subtotales
        double total = observablePvList.stream().mapToDouble(ProductoVenta::getSubtotal).sum();
        LbTotal.setText("$ " + formatNumber(total)); // Puedes reemplazar esto con la lógica que necesites para mostrar el total en tu UI
    }


    //Elimar registro seleccionado de la tabla
    private void eliminarProductoSeleccionado() {
        ProductoVenta selectedProducto = tableView.getSelectionModel().getSelectedItem();
        if (selectedProducto != null) {
            observablePvList.remove(selectedProducto);
            tableView.refresh();
            updateTotal(); // Actualizar el total después de eliminar un producto
        }
    }

    //Formatear el dinero que ingrese con unidades de 1000
    private String formatNumber(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        try {
            // Remove existing commas
            text = text.replaceAll(",", "");

            // Convert to number
            Number number = Double.parseDouble(text);

            // Format number with commas
            NumberFormat numberFormat = NumberFormat.getNumberInstance();
            DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
            decimalFormat.applyPattern("#,###");

            return decimalFormat.format(number);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return text;
        }
    }

    //funcion para manejar el evento cuando se presione enter sobre el texfield recibe
    private void handleEnterKey() {

        String text = txtRecibe.getText().replaceAll(",", "");
        try {
            double recibe = Double.parseDouble(text);
            double cambio = recibe -

        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Invalid number format");
        }
    }
}
