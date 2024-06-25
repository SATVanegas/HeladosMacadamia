package com.hmacadamia.ControllerProductos;

import com.hmacadamia.Models.ProductosMostrar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ProductosController {
    @FXML
    private ImageView image;

    @FXML
    private Label lbID;

    @FXML
    private Label lblCantidadRestante;

    @FXML
    private Label lblCantidadSeleccionada;

    @FXML
    private Label lblPrecio;

    public void setData(ProductosMostrar productos){
        Image image1 = new Image(getClass().getResourceAsStream(productos.getProductoImage()));
        image.setImage(image1);

        lbID.setText(productos.getId());
        lblCantidadRestante.setText(String.valueOf(productos.getCantidadRestante()));
        lblCantidadSeleccionada.setText(String.valueOf(productos.getCantidadSeleccionado()));
        lblPrecio.setText(String.valueOf(productos.getPrecio()));
    }
}
