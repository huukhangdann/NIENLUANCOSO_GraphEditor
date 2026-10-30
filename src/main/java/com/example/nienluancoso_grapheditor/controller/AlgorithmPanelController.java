package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;

import java.util.function.Consumer;

public class AlgorithmPanelController {
    private final ComboBox<Vertex> sourceComboBox;
    private final ComboBox<Vertex> targetComboBox;
    private final ComboBox<String> algorithmComboBox;
    private final Consumer<Boolean> setResultPanelVisibleCallBack;
    private final Consumer<Boolean> setExecutionTraceVisibleCallBack;

    public AlgorithmPanelController(ComboBox<Vertex> sourceComboBox, ComboBox<Vertex> targetComboBox, ComboBox<String> algorithmComboBox, Consumer<Boolean> setResultPanelVisibleCallBack, Consumer<Boolean> setExecutionTraceVisibleCallBack) {
        this.sourceComboBox = sourceComboBox;
        this.targetComboBox = targetComboBox;
        this.algorithmComboBox = algorithmComboBox;
        this.setResultPanelVisibleCallBack = setResultPanelVisibleCallBack;
        this.setExecutionTraceVisibleCallBack = setExecutionTraceVisibleCallBack;
    }

    public void setupAlgorithmSelection(Graph graph, Button runButton, Button resetButton, Button stepButton, Label algorithmStatusLabel) {
        sourceComboBox.setPromptText("Choose source Vertex");
        targetComboBox.setPromptText("Choose target Vertex");
        sourceComboBox.setItems(FXCollections.observableArrayList());
        targetComboBox.setItems(FXCollections.observableArrayList());
        StringConverter<Vertex> vertexConverter = new StringConverter<>() {
            @Override
            public String toString(Vertex vertex) {
                return vertex == null ? null : vertex.getLabel();
            }

            @Override
            public Vertex fromString(String label) {
                return null;
            }
        };
        sourceComboBox.setConverter(vertexConverter);
        targetComboBox.setConverter(vertexConverter);
        algorithmComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    sourceComboBox.setValue(null);
                    targetComboBox.setValue(null);

                    setResultPanelVisibleCallBack.accept(newValue != null);

                    boolean showTrace = !"Count Component".equals(newValue);
                    setExecutionTraceVisibleCallBack.accept(showTrace);

                    updateAlgorithmInputs(graph);
                    updateAlgorithmControls(graph, runButton, resetButton, stepButton, algorithmStatusLabel);
                }
        );
        sourceComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateAlgorithmControls(graph, runButton, resetButton, stepButton, algorithmStatusLabel)
        );
        targetComboBox.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        updateAlgorithmControls(graph, runButton, resetButton, stepButton, algorithmStatusLabel)
        );
        updateAlgorithmInputs(graph);
        refreshVertexChoices(graph);
    }

    public void updateAlgorithmControls(Graph graph, Button runButton, Button resetButton, Button stepButton, Label algorithmStatusLabel) {
        String algorithm = algorithmComboBox.getValue();
        boolean hasVertices = !graph.getVertexList().isEmpty();
        boolean hasAlgorithm = algorithm != null;
        boolean needsSource = hasAlgorithm
                && !algorithm.equals("Count Component");
        boolean needsTarget = "Moore Dijkstra".equals(algorithm);
        boolean validSource = !needsSource
                || sourceComboBox.getValue() != null;
        boolean validTarget = !needsTarget
                || targetComboBox.getValue() != null;
        boolean canRun = hasVertices
                && hasAlgorithm
                && validSource
                && validTarget;
        runButton.setDisable(!canRun);
        stepButton.setDisable(!canRun);

        // Chưa có kết quả thuật toán để reset ở giai đoạn này.
        resetButton.setDisable(true);
        if (!hasAlgorithm) {
            algorithmStatusLabel.setText("Algorithm Status: Choose an algorithm");
        } else if (!hasVertices) {
            algorithmStatusLabel.setText("Algorithm Status: No vertices available");
        } else if (!validSource || !validTarget) {
            algorithmStatusLabel.setText("Algorithm Status: Select required vertices");
        } else {
            algorithmStatusLabel.setText("Algorithm Status: Ready");
        }
    }

    public void updateAlgorithmInputs(Graph graph) {
        refreshVertexChoices(graph);
        String algorithm = algorithmComboBox.getValue();
        boolean hasAlgorithm = algorithm != null;
        boolean needsSource = hasAlgorithm
                && !algorithm.equals("Count Component");
        boolean needsTarget = "Moore Dijkstra".equals(algorithm);
        sourceComboBox.setDisable(!needsSource);
        targetComboBox.setDisable(!needsTarget);
    }

    public void refreshVertexChoices(Graph graph) {
        Vertex selectedSource = sourceComboBox.getValue();
        Vertex selectedTarget = targetComboBox.getValue();
        sourceComboBox.getItems().setAll(graph.getVertexList());
        targetComboBox.getItems().setAll(graph.getVertexList());
        if (graph.getVertexList().contains(selectedSource)) {
            sourceComboBox.setValue(selectedSource);
        } else {
            sourceComboBox.setValue(null);
        }
        if (graph.getVertexList().contains(selectedTarget)) {
            targetComboBox.setValue(selectedTarget);
        } else {
            targetComboBox.setValue(null);
        }
    }

}
