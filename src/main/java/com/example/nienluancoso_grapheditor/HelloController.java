package com.example.nienluancoso_grapheditor;

import com.example.nienluancoso_grapheditor.model.Edge;
import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;
import com.example.nienluancoso_grapheditor.view.*;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

import java.util.HashMap;
import java.util.Map;


public class HelloController {
    @FXML
    public NotificationView notificationView;
    @FXML
    public BorderPane root;
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
    private EdgeView selectedEdgeView = null;
    private Line previewLine = null;
    private final Map<Vertex, VertexView> vertexVertexViewMap = new HashMap<>();
    private final Map<Edge, EdgeView> edgeEdgeViewMap = new HashMap<>();
    private final Map<Edge, WeightView> edgeWeightViewMap = new HashMap<>();
    private final InteractionBlocker interactionBlocker = new InteractionBlocker();

    @FXML
    public void leftPanelToggleHandle() {
        boolean visible = leftPanel.isVisible();
        leftPanel.setVisible(!visible);
        leftPanel.setManaged(!visible);
        leftToggleButton.setText(visible ? "▶" : "◀");
    }

    @FXML
    private void initialize() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
        setUpGraphCanvas();
        setUpInteractionBlocker();
        setUpNotificationView();
    }

    private void setUpNotificationView() {
        notificationView.layoutXProperty().bind(
                graphPane.widthProperty()
                        .subtract(notificationView.widthProperty())
                        .subtract(20)
        );

        notificationView.setLayoutY(20);
    }

    private void setUpInteractionBlocker() {
        System.out.println("interactionblocker!");
        interactionBlocker.prefHeightProperty().bind(graphPane.heightProperty());
        interactionBlocker.prefWidthProperty().bind(graphPane.widthProperty());
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
            if(interactionBlocker.isBlocking()){
                event.consume();
                return;
            }
            if(selectedEdgeView!=null)
                selectedEdgeView.onSelect(false);
            if(!VertexView.hasSelectedVertex()){
                if(event.getClickCount()==2) {
                    double x = event.getX();
                    double y = event.getY();
                    Vertex vertex = new Vertex(x, y);
                    graph.addVertex(vertex);
                    VertexView vertexView = new VertexView(vertex);
                    vertexVertexViewMap.put(vertex, vertexView);

                    // set callback when vertexView clicked
                    vertexView.setOnClicked(vertexClicked -> {
                        // first Vertex clicked
                        if (firstVertexClicked == null) {
                            firstVertexClicked = vertexClicked;
                            if(selectedEdgeView!=null){
                                selectedEdgeView.onSelect(false);
                                selectedEdgeView=null;
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

                    // VertexView clicked
                    vertexView.handleVertexClicked();

                    // Vertex dragged
                    vertexView.handleVertexDragged(graphPane);


                    graphPane.getChildren().add(vertexView);
                }
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
        VertexView.setOnBlockDragging(true);
    }

    public void createEdge(Vertex u, Vertex v){
        Edge edge = new Edge(u, v);
        if(graph.addEdge(edge)) {
            // Init and connect model-view
            EdgeView edgeView = new EdgeView(vertexVertexViewMap.get(u), vertexVertexViewMap.get(v), edge);
            setEdgeCallBack(edgeView);
            edgeEdgeViewMap.put(edge, edgeView);
            WeightView weightView = new WeightView(edge, edgeView);
            edgeWeightViewMap.put(edge, weightView);

            setWeightViewCallback(weightView);
            graphPane.getChildren().addAll(edgeView, weightView);
            edgeView.toBack();
            interactionBlocker.toFront();
            interactionBlocker.block(true);
            weightView.toFront();
        }
    }

    private void setWeightViewCallback(WeightView weightView) {
        weightView.setOnWeightEntered(()->{ //callback entered weight
            interactionBlocker.toBack();
            interactionBlocker.block(false);
            interactionBlocker.setVisible(false);
        });
        weightView.setOnEditingWeight(()->{
            interactionBlocker.setVisible(true);
            interactionBlocker.toFront();
            interactionBlocker.block(true);
            weightView.toFront();
        });
        weightView.setOnNotification(this::showNotification);
        interactionBlocker.setOnClickedBlocker(this::showNotification);
    }

    public void setEdgeCallBack(EdgeView edgeView){
        edgeView.setSelectEdgeViewCallback((selectedEV)->{
            // select edge when selecting vertex
            this.selectedEdgeView = selectedEV;
            if(firstVertexClicked!=null){
                firstVertexClicked.onSelect(false);
                removePreviewLine();
            }
        });
    }

    public void removePreviewLine(){
        if(previewLine!=null){
            graphPane.getChildren().remove(previewLine);
            previewLine = null;
        }
        firstVertexClicked = null;
        VertexView.setOnBlockDragging(false);

    }

    public void handleKeyPressed(KeyEvent event){
        if(event.getCode() == KeyCode.DELETE){
            deleteSelected();
        }
    }

    public void showNotification(String message){
        notificationView.show(message);
    }

    public void deleteSelected(){
        if(firstVertexClicked!=null) {
            graph.removeVertex(firstVertexClicked.getVertex()); // Delete selected vertex
        }
        else if(selectedEdgeView!=null){
            graph.removeEdge(selectedEdgeView.getEdge());
        }

    }
}

