package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;


public class AlgorithmResultController {
    private final VBox resultPanel;
    private final Label componentCountValueLabel;
    private final FlowPane componentDetailsPane;
    private final SplitPane workspaceSplitPane;
    private final Label resultMetricTitleLabel;
    private final VBox componentCountPanel;
    private final ScrollPane componentDetailsScrollPane;
    private final TabPane resultTabPane;
    private final Tab overviewTab;
    private final Tab executionTraceTab;

    public AlgorithmResultController(
            VBox resultPanel,
            TabPane resultTabPane,
            Tab overviewTab,
            Tab executionTraceTab,
            Label resultMetricTitleLabel,
            Label componentCountValueLabel,
            FlowPane componentDetailsPane,
            SplitPane workspaceSplitPane,
            VBox componentCountPanel,
            ScrollPane componentDetailsScrollPane
    ) {
        this.resultPanel = resultPanel;
        this.resultTabPane = resultTabPane;
        this.overviewTab = overviewTab;
        this.executionTraceTab = executionTraceTab;
        this.resultMetricTitleLabel = resultMetricTitleLabel;
        this.componentCountValueLabel = componentCountValueLabel;
        this.componentDetailsPane = componentDetailsPane;
        this.workspaceSplitPane = workspaceSplitPane;
        this.componentCountPanel = componentCountPanel;
        this.componentDetailsScrollPane = componentDetailsScrollPane;
    }

    public void initializeView() {
        showOverviewTab();
        workspaceSplitPane.getItems().remove(resultPanel);
        overviewTab.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue) {
                        updateWorkspaceDivider();
                    }
                }
        );
        executionTraceTab.selectedProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue) {
                        updateWorkspaceDivider();
                    }
                }
        );
    }

    public void showOverviewTab() {
        resultTabPane.getSelectionModel().select(overviewTab);
    }

    public void showExecutionTraceTab() {
        resultTabPane.getSelectionModel().select(executionTraceTab);
    }

    public void setResultPanelVisible(boolean visible) {
        if (visible) {
            if (!workspaceSplitPane.getItems().contains(resultPanel)) {
                workspaceSplitPane.getItems().add(resultPanel);
            }
            updateWorkspaceDivider();
        } else {
            workspaceSplitPane.getItems().remove(resultPanel);
        }
    }

    private void updateWorkspaceDivider() {
        if (overviewTab.isSelected()) {
            workspaceSplitPane.setDividerPositions(0.75);
        } else if (executionTraceTab.isSelected()) {
            workspaceSplitPane.setDividerPositions(0.63);
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
            card.getStyleClass().add("component-card");
            Label title = new Label("Component " + (i + 1));
            title.getStyleClass().add("component-card-title");
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
                vertexLabel.setStyle("-fx-background-color: " + colorHex + ";" + "-fx-background-radius: 50%;" + "-fx-text-fill: #27272A;" + "-fx-font-size: 12px;" + "-fx-font-weight: bold;");
                verticesPane.getChildren().add(vertexLabel);
            }

            verticesPane.setPrefWrapLength(190);

            // Card tự tính chiều cao theo số hàng đỉnh
            card.setMinWidth(220);
            card.setPrefWidth(220);
            card.setMaxWidth(220);
            card.setMinHeight(Region.USE_PREF_SIZE);
            card.setMaxHeight(Region.USE_PREF_SIZE);
            card.getChildren().setAll(title, verticesPane);
            componentDetailsPane.getChildren().add(card);
        }
    }

    public void displayDFSResult(List<Vertex> traversal) {
        componentDetailsPane.getChildren().clear();
        VBox traversalContainer = new VBox(10);
        traversalContainer.setAlignment(Pos.TOP_LEFT);
        traversalContainer.setPadding(new Insets(8));
        Label title = new Label("Traversal Order");
        title.getStyleClass().add("DFS-traversal-order");
        HBox sequenceBox = new HBox(8);
        sequenceBox.setAlignment(Pos.CENTER);
        for (int i = 0; i < traversal.size(); i++) {
            Vertex vertex = traversal.get(i);
            Label vertexLabel = new Label(vertex.getLabel());
            vertexLabel.setMinSize(32, 32);
            vertexLabel.setPrefSize(32, 32);
            vertexLabel.setAlignment(Pos.CENTER);
            vertexLabel.getStyleClass().add("DFS-vertex-traversal");
            sequenceBox.getChildren().add(vertexLabel);
            if (i < traversal.size() - 1) {
                Label arrow = new Label("→");
                arrow.setStyle(
                        "-fx-font-size: 14px;" + "-fx-text-fill: #71717A;");
                sequenceBox.getChildren().add(arrow);
            }
        }
        traversalContainer.getChildren().setAll(title, sequenceBox);
        componentDetailsPane.getChildren().add(traversalContainer);
    }

    public void setTextComponentCountValueLabel(String s) {
        componentCountValueLabel.setText(s);
    }

    public void setResultMetricTitle(String title) {
        resultMetricTitleLabel.setText(title);
    }
}
