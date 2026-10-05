package com.example.nienluancoso_grapheditor.view;

import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class EdgeView extends Pane {
    private final Line line;
    private final static int PADDING = 40;

    public EdgeView(VertexView vertexView1, VertexView vertexView2) {
        this.setMouseTransparent(true);
        line = new Line();
        line.getStyleClass().add("graph-line");
        this.getStyleClass().add("edge-view");

        setUpEdgeViewBinding(vertexView1, vertexView2);

        this.getChildren().add(line);
        line.toBack();
    }



    private void setUpEdgeViewBinding(VertexView vertexView1, VertexView vertexView2) {
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
    }

    public Line getLine() {
        return line;
    }
}
