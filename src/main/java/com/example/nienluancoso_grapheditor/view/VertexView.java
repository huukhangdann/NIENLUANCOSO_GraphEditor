package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;


public class VertexView extends StackPane {
    private Circle circle;
    private Label label;
    private static VertexView selectedVertex = null;
    private static final double RADIUS = 20;
    private static final double SIZE = RADIUS * 2;


    public VertexView(Vertex vertex) {
        this.circle = new Circle(RADIUS);
        this.label = new Label((char) ('A' + vertex.getId() - 1) + "");

        // Middle align the label
        label.setPrefSize(SIZE, SIZE);
        label.setAlignment(Pos.CENTER);
        label.setMouseTransparent(true);   // Avoid mouse clicked event
        circle.setMouseTransparent(true);

        // CSS
        circle.getStyleClass().add("vertex");
        label.getStyleClass().add("vertex-label");

        // add circle and label to vertex view's children list
        this.getChildren().addAll(circle, label);
        this.setLayoutX(vertex.getX() - RADIUS);
        this.setLayoutY(vertex.getY() - RADIUS);
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

    public double getCenterX(){
        return this.getLayoutX() + RADIUS;
    }

    public double getCenterY(){
        return this.getLayoutY() + RADIUS;
    }

    public void setCenterX(double x){
        this.setLayoutX(x - RADIUS);
    }

    public void setCenterY(double y){
        this.setLayoutY(y - RADIUS);
    }

    public double getRadius(){
        return RADIUS;
    }
}

