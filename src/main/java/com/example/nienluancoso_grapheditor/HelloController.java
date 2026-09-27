package com.example.nienluancoso_grapheditor;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HelloController {
    @FXML
    private HBox leftContainer;
    @FXML
    private HBox rightContainer;
    @FXML
    private Pane graphPane;
    @FXML
    private Button leftToggleButton;
    @FXML
    private Button rightToggleButton;

    @FXML
    public void leftPanelToggleHandle(){
        leftContainer.setVisible(false);
        leftContainer.setManaged(false);
        leftToggleButton.setText("▶");
    }

    @FXML
    public void rightPanelToggleHandle(){
        rightContainer.setVisible(false);
        rightContainer.setManaged(false);
        rightToggleButton.setText("◀");
    }

}
