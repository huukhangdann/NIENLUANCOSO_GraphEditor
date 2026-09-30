package com.example.nienluancoso_grapheditor;

import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;
import com.example.nienluancoso_grapheditor.view.VertexView;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
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

    private Point2D clickedPointOffset;

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
    }

    public void setUpGraphCanvas() {
//        // Text Test Canvas
//        Label label = new Label("Create your first vertex here!");
//        label.setId("testLayoutText");
//        label.layoutXProperty().bind(graphPane.widthProperty().subtract(label.widthProperty()).divide(2));
//        label.layoutYProperty().bind(graphPane.heightProperty().subtract(label.heightProperty()).divide(2));
//        graphPane.getChildren().add(label);

        // Pane Click
        handleGraphPaneClicked();

    }

    public void handleGraphPaneClicked() {
        graphPane.setOnMouseClicked(event -> {
            if (VertexView.hasSelectedVertex()) {
                VertexView.getSelectedVertex();
            }
            double x = event.getX();
            double y = event.getY();
            Vertex vertex = new Vertex(x, y);
            graph.addVertex(vertex);
            System.out.println(vertex);
            VertexView vertexView = new VertexView(vertex);

            // vertexView connect -> vertex
            vertexView.setUserData(vertex);

            // Vertex clicked
            handleVertexClicked(vertexView);

            // Vertex dragged
            handleVertexDragged(vertexView);

            graphPane.getChildren().add(vertexView);
        });
    }

    private void handleVertexDragged(VertexView vertexView) {
        vertexView.setOnMousePressed(event -> {
            clickedPointOffset = new Point2D(
                    vertexView.getRadius() - event.getX(),
                    vertexView.getRadius() - event.getY());
        });
        vertexView.setOnMouseDragged(event -> {
            Point2D point = graphPane.sceneToLocal(event.getSceneX(), event.getSceneY());
            vertexView.setCenterX(point.getX() + clickedPointOffset.getX());
            vertexView.setCenterY(point.getY() + clickedPointOffset.getY());

            // update view -> modal
            ((Vertex) vertexView.getUserData()).update(vertexView);
            System.out.println(vertexView.getUserData());
        });
    }

    public void handleVertexClicked(VertexView vertexView) {
        vertexView.setOnMouseClicked(event -> {
            if (event.isStillSincePress()) {
                VertexView selectedVertexView = VertexView.getSelectedVertex();
                // self-click
                if (selectedVertexView == vertexView) {
                    selectedVertexView.onSelect(false);
                } else {
                    if (selectedVertexView != null) selectedVertexView.onSelect(false);
                    vertexView.onSelect(true);
                }
            }
            event.consume(); // stop bubbling to the pane
        });
    }
}
