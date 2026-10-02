package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Edge;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class EdgeView extends Pane {
    private final Line line;
    private final Edge edge;
    private final Label weightLabel = new Label();
    private final TextField weightTextField = new TextField();
    private final int PADDING = 40;

    public EdgeView(Edge edge, VertexView vertexView1, VertexView vertexView2) {
        this.getStyleClass().add("edge-view");
        this.edge = edge;
        line = new Line();
        line.getStyleClass().add("graph-line");

        // Binding layoutX, layoutY of the edgeView Pane to the vertexView
        this.layoutXProperty().bind(Bindings.createDoubleBinding(
                () -> Math.min(vertexView1.getCenterX(), vertexView2.getCenterX()) - PADDING,
                vertexView1.layoutXProperty(),
                vertexView2.layoutXProperty()));
        this.layoutYProperty().bind(Bindings.createDoubleBinding(
                () -> Math.min(vertexView1.getCenterY(), vertexView2.getCenterY()) - PADDING,
                vertexView1.layoutYProperty(),
                vertexView2.layoutYProperty()));

        // Binding area (width and height) of the edgeView Pane to the vertexView
        this.prefWidthProperty().bind(Bindings.createDoubleBinding(
                () -> Math.abs(vertexView1.getCenterX() - vertexView2.getCenterX()) + 2 * PADDING,
                vertexView1.layoutXProperty(),
                vertexView2.layoutXProperty()));
        this.prefHeightProperty().bind(Bindings.createDoubleBinding(
                () -> Math.abs(vertexView1.getCenterY() - vertexView2.getCenterY()) + 2 * PADDING,
                vertexView1.layoutYProperty(),
                vertexView2.layoutYProperty()));


        // Binding line to the vertexView (change vertexView to the local coordinate)
        line.startXProperty().bind(vertexView1.getCenterXProperty().subtract(this.layoutXProperty()));
        line.startYProperty().bind(vertexView1.getCenterYProperty().subtract(this.layoutYProperty()));
        line.endXProperty().bind(vertexView2.getCenterXProperty().subtract(this.layoutXProperty()));
        line.endYProperty().bind(vertexView2.getCenterYProperty().subtract(this.layoutYProperty()));

        this.getChildren().add(line);
    }

    public Line getLine() {
        return line;
    }


}
