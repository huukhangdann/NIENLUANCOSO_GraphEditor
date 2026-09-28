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
        this.circle = new Circle(vertex.getX(), vertex.getY(), 20);
        System.out.println(circle);
        circle.setFill(Color.RED);
        this.label = new Label((vertex.getId() - 1) + "A");
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
