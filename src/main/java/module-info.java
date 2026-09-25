module com.example.nienluancoso_grapheditor {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.nienluancoso_grapheditor to javafx.fxml;
    exports com.example.nienluancoso_grapheditor;
}