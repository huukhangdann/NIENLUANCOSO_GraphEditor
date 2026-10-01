package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Edge;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

public class EdgeView extends Pane{
    private final Line line;
    private final Edge edge;
    private final Label weightLabel = new Label();
    private final TextField weightTextField = new TextField();

    public EdgeView(Edge edge, VertexView vertexView1, VertexView vertexView2) {
        this.edge = edge;
        line = new Line();
        line.getStyleClass().add("graph-line");
        line.startXProperty().bind(vertexView1.getCenterXProperty());
        line.startYProperty().bind(vertexView1.getCenterYProperty());
        line.endXProperty().bind(vertexView2.getCenterXProperty());
        line.endYProperty().bind(vertexView2.getCenterYProperty());


        // weight textField
        weightTextField.setLayoutX(line.getLayoutX());
        weightTextField.setLayoutY(line.getLayoutY());

        this.getChildren().add(line);
        this.getChildren().add(weightLabel);

    }

    public Line getLine(){
        return line;
    }



}
