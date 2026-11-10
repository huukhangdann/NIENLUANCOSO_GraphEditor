package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;


public class AlgorithmResultController {
    private final VBox resultPanel;
    private final Label componentCountValueLabel;
    private final Label overallResultsLabel;
    private final FlowPane componentDetailsPane;
    private final HBox detailHeader;
    private final SplitPane detailSplitPane;
    private final Button toggleDetailButton;
    private final SplitPane workspaceSplitPane;
    private boolean detailsVisible;
    private final Label resultMetricTitleLabel;
    private final VBox componentCountPanel;
    private final ScrollPane componentDetailsScrollPane;

    public AlgorithmResultController(
            VBox resultPanel,
            Label resultMetricTitleLabel,
            Label componentCountValueLabel,
            Label overallResultsLabel,
            FlowPane componentDetailsPane,
            HBox detailHeader,
            SplitPane detailSplitPane,
            Button toggleDetailButton,
            SplitPane workspaceSplitPane,
            VBox componentCountPanel,
            ScrollPane componentDetailsScrollPane
    ) {
        this.resultPanel = resultPanel;
        this.resultMetricTitleLabel = resultMetricTitleLabel;
        this.componentCountValueLabel = componentCountValueLabel;
        this.overallResultsLabel = overallResultsLabel;
        this.componentDetailsPane = componentDetailsPane;
        this.detailHeader = detailHeader;
        this.detailSplitPane = detailSplitPane;
        this.toggleDetailButton = toggleDetailButton;
        this.workspaceSplitPane = workspaceSplitPane;
        this.componentCountPanel = componentCountPanel;
        this.componentDetailsScrollPane = componentDetailsScrollPane;
        detailsVisible = true;
    }

    public void initializeView() {
        detailsVisible = true;
        setExecutionTraceVisible(false);
        workspaceSplitPane.getItems().remove(resultPanel);
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

    public void setTextComponentCountValueLabel(String s) {
        componentCountValueLabel.setText(s);
    }

    public void setResultMetricTitle(String title) {
        resultMetricTitleLabel.setText(title);
    }

    public void setResultMetricValue(String value) {
        componentCountValueLabel.setText(value);
    }

    public void setOverviewCompact(boolean compact) {
        componentCountPanel.setPrefHeight(compact ? 70 : 99);
        componentCountPanel.setSpacing(compact ? 3 : 8);
        resultMetricTitleLabel.setPadding(compact ? new Insets(10, 0, 0, 0) : new Insets(4, 0, 0, 0) );
        componentCountValueLabel.setFont(Font.font(componentCountValueLabel.getFont().getFamily(), compact ? 30 : 50));
        componentDetailsScrollPane.setMinHeight(compact ? 58 : 80);
        componentDetailsScrollPane.setPrefViewportHeight(compact ? 58 : 120);
    }

}
