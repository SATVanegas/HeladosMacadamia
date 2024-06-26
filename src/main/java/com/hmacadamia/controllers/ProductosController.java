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
    private Label lblCantidadSeleccionada;

    @FXML
    private Label lblPrecio;

    public void setData(ProductosMostrar productos) {
        try {
            Image image1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream(productos.getProductoImage())));
            image.setImage(image1);

            lbID.setText(productos.getId());
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
