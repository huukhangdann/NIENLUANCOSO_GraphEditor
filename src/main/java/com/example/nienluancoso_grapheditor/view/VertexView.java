package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class VertexView extends Pane {
    private Circle circle;
    private Label label;

    public VertexView(Vertex vertex) {
        this.circle = new Circle(20);
        circle.setFill(Color.RED);
        System.out.println(vertex.getId() - 1);
        this.label = new Label((char) ('A' + vertex.getId() - 1) + "");
        label.setLayoutX(circle.getCenterX());
        label.setLayoutY(circle.getCenterY());

        // add circle and label to vertex view's children list
        this.getChildren().addAll(circle, label);
        this.setLayoutX(vertex.getX());
        this.setLayoutY(vertex.getY());
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }
}
