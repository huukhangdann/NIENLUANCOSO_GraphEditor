package com.example.nienluancoso_grapheditor.controller;

import com.example.nienluancoso_grapheditor.model.Edge;
import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;
import com.example.nienluancoso_grapheditor.view.*;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class GraphEditorController {
    private final Pane graphPane;
    private final Group graphContent;
    private final InteractionBlocker interactionBlocker;

    // Callback for AppController when having changed
    private final Runnable onGraphChanged;
    private final Consumer<String> onNotification;

    private final Graph graph = new Graph();
    private final Map<Vertex, VertexView> vertexVertexViewMap = new HashMap<>();
    private final Map<Edge, EdgeView> edgeEdgeViewMap = new HashMap<>();
    private final Map<Edge, WeightView> edgeWeightViewMap = new HashMap<>();
    private VertexView firstVertexClicked = null;
    private EdgeView selectedEdgeView = null;
    private Line previewLine = null;
    private final Label emptyCanvasLabel = new Label();

    public GraphEditorController(Pane graphPane, Group graphContent,
                                 InteractionBlocker interactionBlocker,
                                 Runnable onGraphChanged, Consumer<String> onNotification) {
        this.graphPane = graphPane;
        this.graphContent = graphContent;
        this.interactionBlocker = interactionBlocker;
        this.onGraphChanged = onGraphChanged;
        this.onNotification = onNotification;
    }

    public Graph getGraph() {
        return graph;
    }

    public VertexView getVertexView(Vertex vertex) {
        return vertexVertexViewMap.get(vertex);
    }

    public Iterable<VertexView> getVertexViews() {
        return vertexVertexViewMap.values();
    }

    public void setUpGraphCanvas(NotificationView notificationView, Label zoomSliderLabel) {
        notificationView.setMouseTransparent(true);
        zoomSliderLabel.getStyleClass().add("zoom-slider-label");
        // Text Test Canvas
        onTestTextCanvas(true);
        // Pane Click
        graphPane.setOnMouseClicked(this::handleGraphPaneClicked);
        graphPane.setOnMouseMoved(this::handleMouseMoved);
    }

    private void handleMouseMoved(MouseEvent mouseEvent) {
        if (previewLine != null) {
            Point2D mouse = graphContent.sceneToLocal(new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
            previewLine.setEndX(mouse.getX());
            previewLine.setEndY(mouse.getY());
        }
    }

    private void handleGraphPaneClicked(MouseEvent mouseEvent) {
        if (graph.getSize() == 0)
            onTestTextCanvas(false);
        if (previewLine != null) {
            removePreviewLine();
        }
        if (interactionBlocker.isBlocking()) {
            mouseEvent.consume();
            return;
        }
        if (selectedEdgeView != null) {
            selectedEdgeView.onSelect(false);
            selectedEdgeView = null;
        }
        if (!VertexView.hasSelectedVertex()) {
            if ((mouseEvent.getClickCount() == 2) && (mouseEvent.isStillSincePress())) {
                // Change coordinate to graphContent
                Point2D eventPoint = graphContent.sceneToLocal(
                        new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                double x = eventPoint.getX();
                double y = eventPoint.getY();
                Vertex vertex = new Vertex(x, y);
                // Xóa giao diện thuật toán cũ
                clearAlgorithmColors();

                graph.addVertex(vertex);
                VertexView vertexView = new VertexView(vertex);
                vertexVertexViewMap.put(vertex, vertexView);
                setVertexViewCallback(vertexView);

                vertexView.handleVertexClicked();
                vertexView.handleVertexDragged(graphContent);
                graphContent.getChildren().add(vertexView);
                onGraphChanged.run();
            }
        } else {
            VertexView.getSelectedVertex().onSelect(false);
        }
    }

    private void onTestTextCanvas(boolean value) {
        if (value) {
            emptyCanvasLabel.setText("CREATE YOUR FIRST VERTEX HERE!");
            emptyCanvasLabel.setId("testLayoutText");
            emptyCanvasLabel.layoutXProperty().bind(graphPane.widthProperty().subtract(emptyCanvasLabel.widthProperty()).divide(2));
            emptyCanvasLabel.layoutYProperty().bind(graphPane.heightProperty().subtract(emptyCanvasLabel.heightProperty()).divide(2));
            if (!graphPane.getChildren().contains(emptyCanvasLabel)) {
                graphPane.getChildren().add(emptyCanvasLabel);
            }
            emptyCanvasLabel.setVisible(true);
            emptyCanvasLabel.setManaged(true);
        } else {
            emptyCanvasLabel.setVisible(false);
            emptyCanvasLabel.setManaged(false);
        }
    }

    private void createPreviewLine() {
        previewLine = new Line();
        previewLine.getStyleClass().add("preview-line");
        previewLine.setStartX(firstVertexClicked.getCenterX());
        previewLine.setStartY(firstVertexClicked.getCenterY());
        previewLine.setEndX(firstVertexClicked.getCenterX());
        previewLine.setEndY(firstVertexClicked.getCenterY());
        graphContent.getChildren().add(previewLine);
        previewLine.toBack();
        VertexView.setOnBlockDragging(true);
    }

    public void createEdge(Vertex u, Vertex v) {
        Edge edge = new Edge(u, v);

        // Xóa giao diện thuật toán cũ
        clearAlgorithmColors();

        if (graph.addEdge(edge)) {
            // Init and connect model-view
            EdgeView edgeView = new EdgeView(vertexVertexViewMap.get(u), vertexVertexViewMap.get(v), edge);
            setEdgeCallBack(edgeView);
            edgeEdgeViewMap.put(edge, edgeView);
            WeightView weightView = new WeightView(edge, edgeView, graphContent);
            edgeWeightViewMap.put(edge, weightView);

            setWeightViewCallback(weightView);
            graphContent.getChildren().addAll(edgeView, weightView);
            edgeView.toBack();
            interactionBlocker.toFront();
            interactionBlocker.block(true);
            weightView.toFront();
        }
    }

    public void removePreviewLine() {
        if (previewLine != null) {
            graphContent.getChildren().remove(previewLine);
            previewLine = null;
        }
        firstVertexClicked = null;
        VertexView.setOnBlockDragging(false);

    }

    private void setVertexViewCallback(VertexView vertexView) {
        // set callback when vertexView clicked
        vertexView.setOnClicked(vertexClicked -> {
            // first Vertex clicked
            if (firstVertexClicked == null) {
                firstVertexClicked = vertexClicked;
                if (selectedEdgeView != null) {
                    selectedEdgeView.onSelect(false);
                    selectedEdgeView = null;
                }
                // Create preview line
                createPreviewLine();
            }
            // second Vertex clicked
            else {
                createEdge(firstVertexClicked.getVertex(), vertexClicked.getVertex());
                removePreviewLine();
                vertexClicked.onSelect(false);
                firstVertexClicked = null;
            }
        });
        vertexView.setOnEditingLabel(() -> {
            interactionBlocker.setVisible(true);
            interactionBlocker.toFront();
            interactionBlocker.block(true);
            vertexView.toFront();

        });
        vertexView.setOnLabelEntered(() -> {
            interactionBlocker.toBack();
            interactionBlocker.block(false);
            interactionBlocker.setVisible(false);

            onGraphChanged.run();
        });
        vertexView.setOnNotification(onNotification);
        interactionBlocker.setOnClickedBlocker(onNotification);
    }

    private void setEdgeCallBack(EdgeView edgeView) {
        edgeView.setSelectEdgeViewCallback((selectedEV) -> {
            // select edge when selecting vertex
            this.selectedEdgeView = selectedEV;
            if (firstVertexClicked != null) {
                firstVertexClicked.onSelect(false);
                removePreviewLine();
            }
        });
    }

    private void setWeightViewCallback(WeightView weightView) {
        weightView.setOnWeightEntered(() -> { //callback entered weight
            interactionBlocker.toBack();
            interactionBlocker.block(false);
            interactionBlocker.setVisible(false);
        });
        weightView.setOnEditingWeight(() -> {
            interactionBlocker.setVisible(true);
            interactionBlocker.toFront();
            interactionBlocker.block(true);
            weightView.toFront();
        });
        weightView.setOnNotification(onNotification);
        interactionBlocker.setOnClickedBlocker(onNotification);
    }

    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            deleteSelected();
        }
    }

    public void deleteSelected() {
        if (interactionBlocker.isBlocking()) return;
        if (firstVertexClicked != null) {
            deleteSelectedVertex();
        } else if (selectedEdgeView != null) {
            deleteSelectedEdge();
        }
    }

    private void deleteSelectedEdge() {
        graph.removeEdge(selectedEdgeView.getEdge());
        graphContent.getChildren().remove(selectedEdgeView);
        graphContent.getChildren().remove(selectedEdgeView.getWeightView());
        edgeWeightViewMap.remove(selectedEdgeView.getEdge());
        edgeEdgeViewMap.remove(selectedEdgeView.getEdge());
        selectedEdgeView = null;
        // Xóa giao diện thuật toán cũ
        clearAlgorithmColors();
        onGraphChanged.run();
    }

    private void deleteSelectedVertex() {

        for (Edge e : graph.getEdgeList()) {
            if (e.containsVertex(firstVertexClicked.getVertex())) {
                graphContent.getChildren().remove(edgeEdgeViewMap.get(e));
                edgeEdgeViewMap.remove(e);
                graphContent.getChildren().remove(edgeWeightViewMap.get(e));
                edgeWeightViewMap.remove(e);
            }
        }
        graph.removeVertex(firstVertexClicked.getVertex());

        // Xóa giao diện thuật toán cũ
        clearAlgorithmColors();
        onGraphChanged.run();

        vertexVertexViewMap.remove(firstVertexClicked.getVertex());
        graphContent.getChildren().remove(firstVertexClicked);
        firstVertexClicked.onSelect(false);
        firstVertexClicked = null;
        if (previewLine != null)
            removePreviewLine();

    }

    public void deleteAll() {
        // Xóa giao diện thuật toán cũ
        clearAlgorithmColors();
        graphContent.getChildren().clear();
        vertexVertexViewMap.clear();
        edgeWeightViewMap.clear();
        edgeEdgeViewMap.clear();
        Graph.getUsedLabels().clear();
        graph.makeNull();
        firstVertexClicked = null;
        selectedEdgeView = null;
        previewLine = null;
        VertexView.setOnBlockDragging(false);
        onTestTextCanvas(true);
        onGraphChanged.run();
    }

    private void clearAlgorithmColors() {
        for (VertexView view : getVertexViews()) {
            view.changeColorCircle(Color.WHITE);
        }
    }
}
