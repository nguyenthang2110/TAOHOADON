module com.example.product {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.product to javafx.fxml;
    exports com.example.product;
}