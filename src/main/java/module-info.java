module com.hmacadamia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.hmacadamia to javafx.fxml;
    exports com.hmacadamia;
}