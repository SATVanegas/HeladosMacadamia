package com.hmacadamia;

import com.hmacadamia.Models.ProductosMostrar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
                fxmlLoader.setLocation(getClass().getResource("/com/hmacadamia/Productos.fxml"));

                VBox productosBox = fxmlLoader.load();
                ProductosController productosController = fxmlLoader.getController();
                productosController.setData(productos.get(i));

                if (columns == 3) {
                    columns = 0;
                    ++rows;
                }

                GridProductos.add(productosBox, columns++, rows);
                GridPane.setMargin(productosBox, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private List<ProductosMostrar> data() {
        List<ProductosMostrar> ls = new ArrayList<>();

        ProductosMostrar pm = new ProductosMostrar();
        pm.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
        pm.setId("123");
        pm.setPrecio(2000.0);
        pm.setCantidadRestante(2);
        pm.setCantidadSeleccionado(2);
        ls.add(pm);
        ProductosMostrar kj = new ProductosMostrar();
        kj.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
        kj.setId("123");
        kj.setPrecio(2000.0);
        kj.setCantidadRestante(2);
        kj.setCantidadSeleccionado(2);
        ls.add(kj);

        ProductosMostrar lk = new ProductosMostrar();
        lk.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
        lk.setId("123");
        lk.setPrecio(2000.0);
        lk.setCantidadRestante(2);
        lk.setCantidadSeleccionado(2);
        ls.add(lk);

        ProductosMostrar jkdsj = new ProductosMostrar();
        jkdsj.setProductoImage("/com/ImagenesProductos/1.jpg"); // Ajusta la ruta aquí
        jkdsj.setId("123");
        jkdsj.setPrecio(2000.0);
        jkdsj.setCantidadRestante(2);
        jkdsj.setCantidadSeleccionado(2);
        ls.add(jkdsj);



        return ls;
    }
}
