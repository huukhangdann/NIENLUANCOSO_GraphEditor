package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Edge;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class EdgeView {
    private final Line line;
    private final Edge edge;

    public EdgeView(Edge edge, VertexView vertexView1, VertexView vertexView2) {
        this.edge = edge;
        line = new Line();
        line.getStyleClass().add("graph-line");
        line.startXProperty().bind(vertexView1.getCenterXProperty());
        line.startYProperty().bind(vertexView1.getCenterYProperty());
        line.endXProperty().bind(vertexView2.getCenterXProperty());
        line.endYProperty().bind(vertexView2.getCenterYProperty());
    }

    public Line getLine(){
        return line;
    }



}
