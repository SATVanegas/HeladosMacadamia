package com.hmacadamia;

import com.hmacadamia.ControllerProductos.ProductosController;
import com.hmacadamia.Models.ProductosMostrar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HmPrincipalController implements Initializable {
    @FXML
    private GridPane GridProductos;
    private List<ProductosMostrar> productos;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productos = new ArrayList<>(data());

        int columns = 0;
        int rows = 1;
        try {
            for (int i = 0; i < productos.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("Productos.fxml"));

                VBox productosBox = fxmlLoader.load();

                ProductosController productosController = fxmlLoader.getController();
                productosController.setData(productos.get(i));

                if(columns == 3){
                    columns = 0;
                    ++rows;
                }

                GridProductos.add(productosBox, columns++, rows);
                GridPane.setMargin(productosBox, new Insets(10));
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        private List<ProductosMostrar> data(){
            List<ProductosMostrar> ls = new ArrayList<>();

            ProductosMostrar pm = new ProductosMostrar();
            pm.setProductoImage("@../ImagenesProductos/1.jpg");
            pm.setId("123");
            pm.setPrecio(2000.0);
            pm.setCantidadRestante(2);
            pm.setCantidadSeleccionado(2);
            ls.add(pm);

            return ls;
        }
}