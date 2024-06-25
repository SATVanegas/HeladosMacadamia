module com.hmacadamia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    exports com.hmacadamia.controllers;
    opens com.hmacadamia.controllers to javafx.fxml;
}