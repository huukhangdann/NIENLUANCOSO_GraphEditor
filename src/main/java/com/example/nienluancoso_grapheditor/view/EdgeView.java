package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Edge;
import javafx.scene.shape.Line;

import java.util.function.Consumer;

public class EdgeView extends Line {
    private final Line line;
    private final static int PADDING = 0;
    private static EdgeView selectedEdgeView = null;
    private final Edge edge;
    private WeightView weightView;
    private Consumer<EdgeView> selectEdgeViewCallback;


    public EdgeView(VertexView vertexView1, VertexView vertexView2, Edge edge) {
        line = new Line();
        this.edge = edge;
        this.setMouseTransparent(true);
        line.setMouseTransparent(false);
        line.getStyleClass().add("graph-line");
        this.getStyleClass().add("edge-view");

        setUpEdgeViewBinding(vertexView1, vertexView2);

        setUpEdgeClicked();

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
                    selectEdgeViewCallback.accept(this);
                }
            }
            event.consume();
        });
    }

    public void onSelect(boolean value) {
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

    public void setSelectEdgeViewCallback(Consumer<EdgeView> selectEdgeViewCallback) {
        this.selectEdgeViewCallback = selectEdgeViewCallback;
    }
}

