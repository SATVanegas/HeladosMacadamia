module com.hmacadamia {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hmacadamia to javafx.fxml;
    exports com.hmacadamia;
}