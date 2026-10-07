package com.example.nienluancoso_grapheditor;

import com.example.nienluancoso_grapheditor.model.Edge;
import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;
import com.example.nienluancoso_grapheditor.view.EdgeView;
import com.example.nienluancoso_grapheditor.view.InteractionBlocker;
import com.example.nienluancoso_grapheditor.view.VertexView;
import com.example.nienluancoso_grapheditor.view.WeightView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;


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

    private final Graph graph = new Graph();
    private Label label = new Label();
    private VertexView firstVertexClicked = null;
    private Line previewLine = null;
    private final Map<Vertex, VertexView> vertexVertexViewMap = new HashMap<>();
    private final Pane interactionBlocker = new InteractionBlocker();

    @FXML
    public void leftPanelToggleHandle() {
        boolean visible = leftPanel.isVisible();
        leftPanel.setVisible(!visible);
        leftPanel.setManaged(!visible);
        leftToggleButton.setText(visible ? "▶" : "◀");
    }

    @FXML
    private void initialize() {
        setUpGraphCanvas();
        setUpInteractionBlocker();
    }

    private void setUpInteractionBlocker() {
        System.out.println("interactionblocker!");
        interactionBlocker.prefHeight(graphPane.getPrefHeight());
        interactionBlocker.prefWidth(graphPane.getPrefWidth());
        graphPane.getChildren().add(interactionBlocker);
    }

    public void setUpGraphCanvas() {
        // Text Test Canvas
        onTestTextCanvas(true);

        // Pane Click
        handleGraphPaneClicked();

        // Mouse moved for preview line
        handleMouseMoved();

    }

    private void onTestTextCanvas(boolean value) {
        if(value){
            label.setText("Create your first vertex here!");
            label.setId("testLayoutText");
            label.layoutXProperty().bind(graphPane.widthProperty().subtract(label.widthProperty()).divide(2));
            label.layoutYProperty().bind(graphPane.heightProperty().subtract(label.heightProperty()).divide(2));
            graphPane.getChildren().add(label);
        }
        else{
            label.setVisible(false);
            label.setManaged(false);
        }
    }

    public void handleGraphPaneClicked() {
        graphPane.setOnMouseClicked(event -> {
            if(graph.getSize()==0)
                onTestTextCanvas(false);
            if(previewLine!=null){
                removePreviewLine();
            }
            if(!VertexView.hasSelectedVertex()){
                double x = event.getX();
                double y = event.getY();
                Vertex vertex = new Vertex(x, y);
                graph.addVertex(vertex);
                VertexView vertexView = new VertexView(vertex);
                vertexVertexViewMap.put(vertex, vertexView);

                // set callback when vertexView clicked
                vertexView.setOnClicked(vertexClicked -> {
                    // first Vertex clicked
                    if(firstVertexClicked == null){
                        firstVertexClicked = vertexClicked;
                        // Create preview line
                        createPreviewLine();
                    }
                    // second Vertex clicked
                    else{
                        createEdge(firstVertexClicked.getVertex(), vertexClicked.getVertex());
                        removePreviewLine();
                        vertexClicked.onSelect(false);
                        firstVertexClicked = null;
                    }
                });

                // VertexView clicked
                vertexView.handleVertexClicked();

                // Vertex dragged
                vertexView.handleVertexDragged(graphPane);


                graphPane.getChildren().add(vertexView);
            }
            else{
                VertexView.getSelectedVertex().onSelect(false);
            }
        });
    }

    private void handleMouseMoved() {
        graphPane.setOnMouseMoved(event -> {
            if(previewLine!=null){
                previewLine.setEndX(event.getX());
                previewLine.setEndY(event.getY());
            }
        });
    }

    private void createPreviewLine() {
        previewLine = new Line();
        previewLine.getStyleClass().add("preview-line");
        previewLine.setStartX(firstVertexClicked.getCenterX());
        previewLine.setStartY(firstVertexClicked.getCenterY());
        previewLine.setEndX(firstVertexClicked.getCenterX());
        previewLine.setEndY(firstVertexClicked.getCenterY());
        graphPane.getChildren().add(previewLine);
        previewLine.toBack();
    }

    public void createEdge(Vertex u, Vertex v){
        Edge edge = new Edge(u, v);
        if(graph.addEdge(edge)) {
            EdgeView edgeView = new EdgeView(vertexVertexViewMap.get(u), vertexVertexViewMap.get(v));
            WeightView weightView = new WeightView(edge, edgeView);
            graphPane.getChildren().addAll(edgeView, weightView);
            edgeView.toBack();
        }
    }

    public void removePreviewLine(){
        if(previewLine!=null){
            graphPane.getChildren().remove(previewLine);
            previewLine = null;
        }
        firstVertexClicked = null;
    }
}
