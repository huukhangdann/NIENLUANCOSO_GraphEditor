package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class VertexView extends Pane {
    private Circle circle;
    private Label label;

    public VertexView(Vertex vertex) {
        this.circle = new Circle(0,0,20);
        circle.setFill(Color.RED);
        System.out.println(vertex.getId() - 1);
        this.label = new Label((char) ('A' + vertex.getId() - 1) + "");

        // Middle align the label
        label.setPrefWidth(40);
        label.setPrefHeight(40);
        label.setAlignment(Pos.CENTER);
        label.setLayoutX(-20);
        label.setLayoutY(-20);
        label.setMouseTransparent(true);   // Avoid mouse clicked event


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
