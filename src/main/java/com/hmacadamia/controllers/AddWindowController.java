package com.hmacadamia.controllers;

import com.hmacadamia.pos.CategoriaProducto;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;

public class AddWindowController implements Initializable {

    @FXML
    private CheckBox checkProducto;

    @FXML
    private ComboBox<String> comboCategoria;

    @FXML
    private Label lblURL;

    @FXML
    private Button saveButton;

    @FXML
    private Button selectImageButton;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtProveedor;

    private static final String IMAGE_DIRECTORY = "src/main/resources/images/";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupCategoryComboBoxItems();
        selectImageButton.setOnAction(event -> selectImage());
        saveButton.setOnAction(event -> saveAndClose());
    }

    public void selectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos de imagen", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            Path sourcePath = selectedFile.toPath();
            Path targetPath = Paths.get("src/main/resources/com/images/" + selectedFile.getName());


            try {
                System.out.println("Source Path: " + sourcePath);
                System.out.println("Target Path: " + targetPath);

                if (Files.notExists(sourcePath)) {
                    System.out.println("El archivo de origen no existe: " + sourcePath);
                } else {
                    Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Archivo copiado con éxito.");
                    lblURL.setText(String.valueOf(targetPath));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al copiar el archivo: " + e.getMessage());
            }
        }
    }


    private void saveAndClose() {
        try {
            // Obtener datos del formulario
            double precio = Double.parseDouble(txtPrecio.getText());
            String descripcion = txtDescripcion.getText();
            String proveedor = txtProveedor.getText();
            String imagenUrl = lblURL.getText();

            // Lógica para guardar los datos en la base de datos
            System.out.println("Descripción: " + descripcion + ", Precio: " + precio + ", Proveedor: " + proveedor + ", Imagen: " + imagenUrl);

            // Cerrar la ventana
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            System.err.println("Error: No se pudo convertir el texto a un número. " + e.getMessage());
        }
    }

    private void setupCategoryComboBoxItems() {
        List<String> categories = List.of("INVENTARIO","CHIPS", "GASEOSAS", "ADICIONES","SALSAS");
        comboCategoria.setItems(FXCollections.observableArrayList(categories));
    }

    private CategoriaProducto setCategoria(String cat){

        return switch (cat) {
            case "CHIPS" -> CategoriaProducto.CHIPS;
            case "GASEOSAS" -> CategoriaProducto.GASEOSAS;
            case "ADICIONES" -> CategoriaProducto.ADICIONES;
            case "SALSAS" -> CategoriaProducto.SALSAS;
            case "INVENTARIO" -> CategoriaProducto.INVENTARIO;
            default -> null;
        };
    }
}
