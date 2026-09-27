package com.example.nienluancoso_grapheditor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HelloController {
    @FXML
    private HBox leftContainer;
    @FXML
    private HBox rightContainer;
    @FXML
    private VBox topContainer;
    @FXML
    private HBox botContainer;
    @FXML
    private Pane graphPane;
    @FXML
    private Button leftToggleButton;
    @FXML
    private VBox leftPanel;
    @FXML
    private VBox rightPanel;

    @FXML
    public void leftPanelToggleHandle() {
        boolean visible = leftPanel.isVisible();
        leftPanel.setVisible(!visible);
        leftPanel.setManaged(!visible);
        leftToggleButton.setText(visible ? "▶" : "◀");
    }

    @FXML
    private void initialize(){
        System.out.println();
        Label label = new Label("Create your first vertex here!");
        label.setId("testLayoutText");
        label.layoutXProperty().bind(graphPane.widthProperty().subtract(label.widthProperty()).divide(2));
        label.layoutYProperty().bind(graphPane.heightProperty().subtract(label.heightProperty()).divide(2));
        graphPane.getChildren().add(label);
    }

}
