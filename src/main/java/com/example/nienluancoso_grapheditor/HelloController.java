package com.example.nienluancoso_grapheditor;

import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;
import com.example.nienluancoso_grapheditor.view.VertexView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
//        // Text Test Canvas
//        Label label = new Label("Create your first vertex here!");
//        label.setId("testLayoutText");
//        label.layoutXProperty().bind(graphPane.widthProperty().subtract(label.widthProperty()).divide(2));
//        label.layoutYProperty().bind(graphPane.heightProperty().subtract(label.heightProperty()).divide(2));
//        graphPane.getChildren().add(label);

        // Pane Click
        handleGraphPaneClicked();

    }

    public void handleGraphPaneClicked(){
        graphPane.setOnMouseClicked(event -> {
            if(VertexView.hasSelectedVertex()){
                VertexView.getSelectedVertex();
            }
            double x = event.getX();
            double y = event.getY();
            Vertex vertex = new Vertex(x, y);
            graph.addVertex(vertex);
            VertexView vertexView = new VertexView(vertex);
            // Vertex clicked
            handleVertexClicked(vertexView);
            graphPane.getChildren().add(vertexView);
        });
    }

    public void handleVertexClicked(VertexView vertexView){
        vertexView.setOnMouseClicked(event -> {
            VertexView selectedVertexView = VertexView.getSelectedVertex();
            // self-click
            if(selectedVertexView == vertexView){
                selectedVertexView.onSelect(false);
                selectedVertexView = null;
            }
            else {
                if (selectedVertexView != null) selectedVertexView.onSelect(false);
                selectedVertexView = vertexView;
                vertexView.onSelect(true);
            }
            event.consume(); // stop bubbling to the pane
        });
    }
}
