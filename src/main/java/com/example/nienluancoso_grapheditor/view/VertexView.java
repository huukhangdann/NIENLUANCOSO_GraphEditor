package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;


public class VertexView extends Pane {
    private Circle circle;
    private Label label;
    private static VertexView selectedVertex = null;

    public VertexView(Vertex vertex) {
        this.circle = new Circle(0,0,20);
        this.label = new Label((char) ('A' + vertex.getId() - 1) + "");

        // Middle align the label
        label.setPrefWidth(40);
        label.setPrefHeight(40);
        label.setAlignment(Pos.CENTER);
        label.setLayoutX(-20);
        label.setLayoutY(-20);
        label.setMouseTransparent(true);   // Avoid mouse clicked event
        circle.setMouseTransparent(true);

        // CSS
        circle.getStyleClass().add("vertex");
        label.getStyleClass().add("vertex-label");

        // add circle and label to vertex view's children list
        this.getChildren().addAll(circle, label);
        this.setLayoutX(vertex.getX());
        this.setLayoutY(vertex.getY());
    }

    public void onSelect(boolean value){
        if(value) {
            circle.getStyleClass().add("vertex-selected");
            label.getStyleClass().add("vertex-label-selected");
            System.out.println("vertex selected!");
            selectedVertex = this;
        }
        else {
            circle.getStyleClass().remove("vertex-selected");
            label.getStyleClass().remove("vertex-label-selected");
            selectedVertex = null;
        }
    }

    public static boolean hasSelectedVertex(){
        return selectedVertex!=null;
    }

    public static VertexView getSelectedVertex(){
        return selectedVertex;
    }

}
