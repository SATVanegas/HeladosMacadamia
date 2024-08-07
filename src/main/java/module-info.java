module com.hmacadamia {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires com.google.gson;
    requires io;
    requires kernel;
    requires layout;
    requires commons;

    exports com.hmacadamia.controllers;
    opens com.hmacadamia.controllers to javafx.fxml;
    exports com.hmacadamia.pos;
    opens com.hmacadamia.pos to javafx.fxml;
    opens com.hmacadamia.superclass to javafx.fxml;
    exports com.hmacadamia.superclass;
}
