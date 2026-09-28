package com.example.nienluancoso_grapheditor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PJ_structure.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        String layoutCss = this.getClass().getResource("/com/example/nienluancoso_grapheditor/css/app-layout.css").toExternalForm();
        String graphCSS = this.getClass().getResource("/com/example/nienluancoso_grapheditor/css/graph.css").toExternalForm();
        scene.getStylesheets().addAll(layoutCss, graphCSS);

        stage.setTitle("Graph Editor");
        stage.setScene(scene);
        stage.show();
    }
}
