package com.hmacadamia.controllers;

import com.hmacadamia.Models.ProductosMostrar;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.MouseEvent;
import java.net.URL;
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

    public void setData(ProductosMostrar productos) {
        try {
            Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(productos.getProductoImage())));
            image.setImage(image1);

            // Ajustes adicionales para la imagen
            image.setFitWidth(200); // Ajusta el ancho de la imagen
            image.setFitHeight(200); // Ajusta la altura de la imagen
            image.setPreserveRatio(true); // Preserva la relación de aspecto
            image.setSmooth(true); // Usa suavizado para la imagen

            lbID.setText(String.valueOf(productos.getId()));
            lblDescripcion.setText(productos.getDescripcion());
            lblCantidadSeleccionada.setText(String.valueOf(productos.getCantidadSeleccionado()));
            lblPrecio.setText(String.valueOf(productos.getPrecio()));
        } catch (NullPointerException e) {
            System.err.println("Error: No se encontró la imagen en la ruta especificada.");
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        image.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            System.out.println("ImageView clicked! " +lbID.getText());
        });
    }
}
