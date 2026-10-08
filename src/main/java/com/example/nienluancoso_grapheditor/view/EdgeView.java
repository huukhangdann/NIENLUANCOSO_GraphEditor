package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Edge;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class EdgeView extends Pane {
    private final Line line;
    private final static int PADDING = 40;
    private static EdgeView selectedEdgeView = null;
    private final Edge edge;
    private WeightView weightView;
    private Runnable selectEdgeViewCallback;


    public EdgeView(VertexView vertexView1, VertexView vertexView2, Edge edge) {
        line = new Line();
        this.edge = edge;
        line.getStyleClass().add("graph-line");
        this.getStyleClass().add("edge-view");

        setUpEdgeViewBinding(vertexView1, vertexView2);

        setUpEdgeClicked();

        this.getChildren().add(line);
        line.toBack();
    }

    private void setUpEdgeClicked() {
        line.setOnMouseClicked(event -> {
            EdgeView selectedEdgeView = EdgeView.selectedEdgeView;
            // self-click
            if(selectedEdgeView == this)
                selectedEdgeView.onSelect(false);
            else {
                // deselect other edges
                if(selectedEdgeView!=null) selectedEdgeView.onSelect(false);
                this.onSelect(true);

                if(selectEdgeViewCallback!=null){
                    selectEdgeViewCallback.run();
                }
            }
            event.consume();
        });
    }

    private void onSelect(boolean value) {
        if(value) {
            line.getStyleClass().add("line-selected");
            System.out.println("edge selected!");
            selectedEdgeView = this;
        } else {
            line.getStyleClass().remove("line-selected");
            selectedEdgeView = null;
        }
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

    public Edge getEdge() {
        return edge;
    }

    public WeightView getWeightView() {
        return weightView;
    }

    public void setWeightView(WeightView weightView) {
        this.weightView = weightView;
    }

    public void setSelectEdgeViewCallback(Runnable selectEdgeViewCallback) {
        this.selectEdgeViewCallback = selectEdgeViewCallback;
    }
}

