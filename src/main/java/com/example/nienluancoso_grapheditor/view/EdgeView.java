package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Edge;
import javafx.scene.shape.Line;

import java.util.function.Consumer;

public class EdgeView extends Line {
    private final static int PADDING = 0;
    private static EdgeView selectedEdgeView = null;
    private final Edge edge;
    private WeightView weightView;
    private Consumer<EdgeView> selectEdgeViewCallback;


    public EdgeView(VertexView vertexView1, VertexView vertexView2, Edge edge) {
        this.edge = edge;
        this.getStyleClass().add("graph-line");

        setUpEdgeViewBinding(vertexView1, vertexView2);

        setUpEdgeClicked();
    }

    private void setUpEdgeClicked() {
        this.setOnMouseClicked(event -> {
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
            this.weightView.getStyleClassWeighLabel().add("weight-selected");
            this.getStyleClass().add("line-selected");
            System.out.println("edge selected!");
            selectedEdgeView = this;
        } else {
            this.getStyleClass().remove("line-selected");
            this.weightView.getStyleClassWeighLabel().remove("weight-selected");
            selectedEdgeView = null;
        }
    }


    private void setUpEdgeViewBinding(VertexView vertexView1, VertexView vertexView2) {
       // Binding line to the vertexView (change vertexView to the local coordinate)
        this.startXProperty().bind(vertexView1.getCenterXProperty().subtract(this.layoutXProperty()));
        this.startYProperty().bind(vertexView1.getCenterYProperty().subtract(this.layoutYProperty()));
        this.endXProperty().bind(vertexView2.getCenterXProperty().subtract(this.layoutXProperty()));
        this.endYProperty().bind(vertexView2.getCenterYProperty().subtract(this.layoutYProperty()));
    }

    public Line getLine() {
        return this;
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

