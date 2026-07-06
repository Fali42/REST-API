module com.restapi {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.restapi to javafx.fxml;
    exports com.restapi;
}
