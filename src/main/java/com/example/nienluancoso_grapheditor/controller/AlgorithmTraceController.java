package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.algorithm.AlgorithmStep;
import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;

import java.util.List;
import java.util.Map;

public class AlgorithmTraceController {
    private final Label stepDescriptionLabel;
    private final Label stackValueLabel;
    private final Label visitedValueLabel;
    private final Label traversalValueLabel;
    private final Map<Integer, Label> indexPseudoMap;
    private final ScrollPane pseudocodeScrollPane;


    public AlgorithmTraceController(
            Label stepDescriptionLabel,
            Label stackValueLabel,
            Label visitedValueLabel,
            Label traversalValueLabel,
            Map<Integer, Label> indexPseudoMap,
            ScrollPane pseudocodeScrollPane
    ) {
        this.stepDescriptionLabel = stepDescriptionLabel;
        this.stackValueLabel = stackValueLabel;
        this.visitedValueLabel = visitedValueLabel;
        this.traversalValueLabel = traversalValueLabel;
        this.indexPseudoMap = indexPseudoMap;
        this.pseudocodeScrollPane = pseudocodeScrollPane;
    }

    public void displayStep(AlgorithmStep step) {
        updateDescription(step);
        updateStack(step);
        updateVisited(step);
        updateTraversalOrder(step);
        highlightPseudocode(step.getLineNumber());
    }

    private void updateDescription(AlgorithmStep step) {
        stepDescriptionLabel.setText(step.getDescription());
    }

    private void updateStack(AlgorithmStep step) {
        List<Vertex> stack = step.getStack();
        stackValueLabel.setText(convertToString(stack));
    }

    private void updateVisited(AlgorithmStep step) {
        List<Vertex> visitedVertex = step.getVisitedVertices();
        visitedValueLabel.setText(convertToString(visitedVertex));
    }

    private void updateTraversalOrder(AlgorithmStep step) {
        StringBuilder traversalOrderString = new StringBuilder();
        List<Vertex> traversalOrder = step.getResult();
        Label arrow = new Label("→ ");
        arrow.setStyle("-fx-font-size: 14px;" + "-fx-text-fill: #71717A;");
        for (int i = 0; i < traversalOrder.size(); i++) {
            if (i != 0) traversalOrderString.append(" ").append(arrow.getText());
            traversalOrderString.append(traversalOrder.get(i).toString());
        }
        traversalValueLabel.setText(String.valueOf(traversalOrderString));
    }

    private void highlightPseudocode(int lineNumber) {
        for (int i = 1; i < 13; i++) {
            if (indexPseudoMap.containsKey(i)) {
                if (i == lineNumber) {
                    indexPseudoMap.get(i).setStyle("-fx-background-color: #DCFCE7; " + "-fx-text-fill: #15803D; " + "-fx-font-weight: bold; " + "-fx-background-radius: 4px; " + "-fx-padding: 3px");
                } else {
                    indexPseudoMap.get(i).setStyle("-fx-background-color: transparent; " + "-fx-text-fill: #000; " + "-fx-font-weight: normal; ");
                }
            }
        }
        scrollToLine(lineNumber);
    }

    private void scrollToLine(int lineNumber) {
        Label targetLine = indexPseudoMap.get(lineNumber);
        if (targetLine == null) return;
        pseudocodeScrollPane.applyCss();
        pseudocodeScrollPane.layout();
        double contentHeight = pseudocodeScrollPane.getContent().getBoundsInLocal().getHeight();
        double viewportHeight = pseudocodeScrollPane.getViewportBounds().getHeight();
        double maxScroll = contentHeight - viewportHeight;
        if (maxScroll <= 0) {
            pseudocodeScrollPane.setVvalue(0);
            return;
        }
        double lineCenterY = targetLine.getBoundsInParent().getMinY()
                + targetLine.getBoundsInParent().getHeight() / 2;
        double targetY = lineCenterY - viewportHeight / 2;
        double scrollValue = Math.clamp(targetY / maxScroll, 0, 1);
        pseudocodeScrollPane.setVvalue(scrollValue);
    }

    private String convertToString(List<Vertex> list) {
        StringBuilder listItem = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            listItem.append(list.get(i));
            if (i != list.size() - 1) listItem.append(", ");
        }
        return String.valueOf(listItem);
    }
}
