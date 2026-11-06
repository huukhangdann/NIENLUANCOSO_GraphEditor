package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;


public class AlgorithmResultController {
    @FXML
    private VBox resultPanel;
    @FXML
    private Label componentCountValueLabel;
    @FXML
    private Label overallResultsLabel;
    @FXML
    private FlowPane componentDetailsPane;
    @FXML
    private HBox detailHeader;
    @FXML
    private SplitPane detailSplitPane;
    @FXML
    private Button toggleDetailButton;
    @FXML
    private SplitPane workspaceSplitPane;
    private boolean detailsVisible;

    public AlgorithmResultController(){
        detailsVisible = true;
        setExecutionTraceVisible(false);
        workspaceSplitPane.getItems().remove(resultPanel);
        updateToggleIcon(true);
    }

    public Label getOverallResultsLabel() {
        return overallResultsLabel;
    }

    public void setResultPanelVisible(boolean visible) {
        if (visible) {
            if (!workspaceSplitPane.getItems().contains(resultPanel)) {
                workspaceSplitPane.getItems().add(resultPanel);
            }

            workspaceSplitPane.setDividerPositions(0.67);
            workspaceSplitPane.setDividerPositions(0.67);
        } else {
            workspaceSplitPane.getItems().remove(resultPanel);
        }
    }

    public void displayComponentDetails(List<List<Vertex>> components) {
        componentDetailsPane.getChildren().clear();
        for (int i = 0; i < components.size(); i++) {
            List<Vertex> component = components.get(i);
            double hue = (i * 137.508) % 360;
            double saturation = 0.23 + (i % 3) * 0.06;
            double brightness = 0.98;
            Color componentColor = Color.hsb(hue, saturation, brightness);
            VBox card = new VBox(10);
            card.setPadding(new Insets(12));
            card.setMinWidth(120);
            card.setMaxWidth(220);
            card.setStyle(
                    "-fx-background-color: white;" +
                            "-fx-border-color: #E4E4E7;" +
                            "-fx-border-radius: 8px;" +
                            "-fx-background-radius: 8px;"
            );
            Label title = new Label("Component " + (i + 1));
            title.setStyle(
                    "-fx-font-size: 13px;" +
                            "-fx-font-weight: bold;" +
                            "-fx-text-fill: #3F3F46;"
            );
            FlowPane verticesPane = new FlowPane();
            verticesPane.setHgap(6);
            verticesPane.setVgap(6);
            String colorHex = String.format(
                    "#%02X%02X%02X",
                    (int) Math.round(componentColor.getRed() * 255),
                    (int) Math.round(componentColor.getGreen() * 255),
                    (int) Math.round(componentColor.getBlue() * 255)
            );

            for (Vertex vertex : component) {
                Label vertexLabel = new Label(vertex.getLabel());
                vertexLabel.setMinSize(30, 30);
                vertexLabel.setPrefSize(30, 30);
                vertexLabel.setAlignment(Pos.CENTER);
                vertexLabel.setStyle(
                        "-fx-background-color: " + colorHex + ";" +
                                "-fx-background-radius: 50%;" +
                                "-fx-text-fill: #27272A;" +
                                "-fx-font-size: 12px;" +
                                "-fx-font-weight: bold;"
                );
                verticesPane.getChildren().add(vertexLabel);
            }
            // Cho FlowPane tự xuống hàng theo chiều rộng card
            verticesPane.setPrefWrapLength(190);

            // Card tự tính chiều cao theo số hàng đỉnh
            card.setMinWidth(220);
            card.setPrefWidth(220);
            card.setMaxWidth(220);

            // Không cố định chiều cao card
            card.setMinHeight(Region.USE_PREF_SIZE);
            card.setMaxHeight(Region.USE_PREF_SIZE);

            // Hiển thị trực tiếp danh sách đỉnh bên trong card
            card.getChildren().setAll(title, verticesPane);
            componentDetailsPane.getChildren().add(card);
        }
    }

    public void handleToggleDetails() {
        detailsVisible = !detailsVisible;
        detailHeader.setVisible(detailsVisible);
        detailHeader.setManaged(detailsVisible);
        detailSplitPane.setVisible(detailsVisible);
        detailSplitPane.setManaged(detailsVisible);
        updateToggleIcon(detailsVisible);
    }

    public void updateToggleIcon(boolean visible) {
        FontIcon icon;
        if (visible) {
            icon = new FontIcon(Feather.CHEVRON_DOWN);
        } else {
            icon = new FontIcon(Feather.CHEVRON_RIGHT);
        }
        icon.setIconSize(14);
        icon.setIconColor(javafx.scene.paint.Color.valueOf("#52525b"));
        toggleDetailButton.setText("");
        toggleDetailButton.setGraphic(icon);
    }

    public void setExecutionTraceVisible(boolean visible) {
        detailHeader.setVisible(visible);
        detailHeader.setManaged(visible);

        detailSplitPane.setVisible(visible);
        detailSplitPane.setManaged(visible);

        detailsVisible = visible;
        updateToggleIcon(visible);
    }

    public void setTextComponentCountValueLabel(String s){
        componentCountValueLabel.setText(s);
    }

}
