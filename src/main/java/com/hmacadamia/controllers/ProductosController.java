package com.hmacadamia.controllers;

import com.hmacadamia.pos.ProductoVenta;
import com.hmacadamia.repo.ProductosRepo;
import com.hmacadamia.repo.RepositorioGenerico;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Objects;
import java.util.ResourceBundle;

public class ProductosController implements Initializable {
    @FXML
    private ImageView image;

    @FXML
    private Label lbID;

    @FXML
    private Label lblDescripcion;

    @FXML
    private Label lblCantidadSeleccionada;

    @FXML
    private Label lblPrecio;
    public int modelEmisor;
    private HmPrincipalController principalController;
    private InventarioController inventarioController;

    private final RepositorioGenerico<ProductoVenta> repoProductos = new ProductosRepo();

    // Método para establecer el controlador principal
    public void setPrincipalController(HmPrincipalController controller) {
        this.principalController = controller;
    }

    // Método para establecer el controlador del inventario
    public void setInventarioController(InventarioController controller) {
        this.inventarioController = controller;
    }

    // Método para establecer los datos del producto
    public void setData(ProductoVenta productos) {
        try {
            // Cargar la imagen del producto
            Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(productos.getUrlimg())));
            image.setImage(image1);

            // Ajustes adicionales para la imagen
            image.setFitWidth(200); // Ajusta el ancho de la imagen
            image.setFitHeight(200); // Ajusta la altura de la imagen
            image.setPreserveRatio(true); // Preserva la relación de aspecto
            image.setSmooth(true); // Usa suavizado para la imagen

            // Establecer los valores en las etiquetas
            lbID.setText(String.valueOf(productos.getId()));
            lblDescripcion.setText(productos.getDescripcion());
            lblCantidadSeleccionada.setText(String.valueOf(productos.getCantidad()));
            lblPrecio.setText(formatNumber(String.valueOf(productos.getPrecio())));
        } catch (NullPointerException e) {
            System.err.println("Error: No se encontró la imagen en la ruta especificada.");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Añadir un manejador de eventos para el clic en la imagen
        image.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            long productoId = Long.parseLong(lbID.getText());
            ProductoVenta ss = repoProductos.searchById(HmPrincipalController.productos, productoId);
            modelEmisor = ss.isProduct() ? 0 : 1;
            System.out.println(modelEmisor);

            switch (modelEmisor) {
                case 0:
                    ProductoVenta pv = repoProductos.searchById(HmPrincipalController.productos, productoId);
                    if (pv != null && principalController != null) {
                        // Añadir o actualizar el producto en el controlador principal
                        principalController.addOrUpdateProducto(pv);
                    } else {
                        System.out.println("Producto no encontrado o controlador principal no establecido.");
                    }
                    break;
                case 1:
                    ProductoVenta invpv = repoProductos.searchById(InventarioController.productos, productoId);
                    if (invpv != null && inventarioController != null) {
                        // Añadir o actualizar el producto en el controlador principal
                        System.out.println("Producto seleccionado "+ invpv.getDescripcion());
                    } else {
                        System.out.println("Producto no encontrado o controlador principal no establecido.");
                    }
                    break;
                default:
                    System.out.println("Número no válido");
                    break;
            }

        });
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
}
