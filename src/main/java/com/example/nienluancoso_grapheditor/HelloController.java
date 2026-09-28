package com.example.nienluancoso_grapheditor;

import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;
import com.example.nienluancoso_grapheditor.view.VertexView;
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

    @FXML
    public void leftPanelToggleHandle() {
        boolean visible = leftPanel.isVisible();
        leftPanel.setVisible(!visible);
        leftPanel.setManaged(!visible);
        leftToggleButton.setText(visible ? "▶" : "◀");
    }

    @FXML
    private void initialize(){
        setUpGraphCanvas();
    }

    public void setUpGraphCanvas(){
        // Text Test Canvas
        Label label = new Label("Create your first vertex here!");
        label.setId("testLayoutText");
        label.layoutXProperty().bind(graphPane.widthProperty().subtract(label.widthProperty()).divide(2));
        label.layoutYProperty().bind(graphPane.heightProperty().subtract(label.heightProperty()).divide(2));
        graphPane.getChildren().add(label);

        // Graph Init
        Graph graph = new Graph();

        // Pane Click
        handleGraphPaneClicked(graph);

    }

    public void handleGraphPaneClicked(Graph graph){
        graphPane.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();
            Vertex vertex = new Vertex(x, y);
            VertexView vertexView = new VertexView(vertex);
            graph.addVertex(vertex);
            graphPane.getChildren().add(vertexView);
        });
    }
}
