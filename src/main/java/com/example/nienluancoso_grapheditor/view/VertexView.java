package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Graph;
import com.example.nienluancoso_grapheditor.model.Vertex;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import java.util.function.Consumer;


public class VertexView extends StackPane {
    private final Circle circle;
    private final Label label;
    private final TextField labelTextField;
    private final Vertex vertex;
    private static VertexView selectedVertex = null;
    private static final double RADIUS = 20;
    private static final double SIZE = RADIUS * 2;
    private static boolean onBlockDragging = false;


    private Runnable onLabelEntered;
    private Runnable onEditingLabel;
    private Consumer<String> OnNotification;

    private Point2D clickedPointOffset;

    // callback clicked vertex
    private Consumer<VertexView> onVertexClicked;


    public VertexView(Vertex vertex) {
        this.circle = new Circle(RADIUS);
        this.label = new Label(vertex.getLabel());
        this.labelTextField  = new TextField();
        labelTextField.setVisible(false);

        // Middle align the label
        label.setPrefSize(SIZE, SIZE);
        labelTextField.setPrefSize(SIZE, SIZE);
        label.setAlignment(Pos.CENTER);
        labelTextField.setAlignment(Pos.CENTER);
        label.setMouseTransparent(true);   // Avoid mouse clicked event
        circle.setMouseTransparent(true);

        // CSS
        circle.getStyleClass().add("vertex");
        label.getStyleClass().add("vertex-label");
        this.getChildren().addAll(circle, label, labelTextField);
        this.setLayoutX(vertex.getX() - RADIUS);
        this.setLayoutY(vertex.getY() - RADIUS);
        this.vertex = vertex;

        handleLabelEditEnter();

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

    public Vertex getVertex() {
        return vertex;
    }


    public double getCenterX(){
        return this.getLayoutX() + RADIUS;
    }

    public double getCenterY(){
        return this.getLayoutY() + RADIUS;
    }

    public DoubleBinding getCenterXProperty(){
        return this.layoutXProperty().add(RADIUS);
    }

    public DoubleBinding getCenterYProperty(){
        return this.layoutYProperty().add(RADIUS);
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

    public void handleVertexDragged(Pane graphPane) {

        this.setOnMousePressed(event -> {
            clickedPointOffset = new Point2D(
                    this.getRadius() - event.getX(),
                    this.getRadius() - event.getY());
        });
        this.setOnMouseDragged(event -> {
            if(this==selectedVertex || onBlockDragging){
                return;
            }
            Point2D point = graphPane.sceneToLocal(event.getSceneX(), event.getSceneY());
            this.setCenterX(point.getX() + clickedPointOffset.getX());
            this.setCenterY(point.getY() + clickedPointOffset.getY());

            // update view -> modal
            ((Vertex) this.getVertex()).update(this);
        });
    }

    public void handleVertexClicked() {
        this.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) handleLabelEdit();
            if (event.isStillSincePress()) {
                VertexView selectedVertexView = VertexView.getSelectedVertex();
                // self-click
                if (selectedVertexView == this) {
                    selectedVertexView.onSelect(false);
                } else {
                    if (selectedVertexView != null) selectedVertexView.onSelect(false);
                    this.onSelect(true);
                }
                if (onVertexClicked != null) {
                    onVertexClicked.accept(this);
                }
            }
            event.consume(); // stop bubbling to the pane
        });
    }

    private void handleLabelEdit() {
        onEditingLabel.run();
        labelTextField.setVisible(true);
        label.setVisible(false);
        labelTextField.setText(label.getText());
        Platform.runLater(() -> {
            labelTextField.requestFocus();
            labelTextField.selectAll();
        });
    }

    private void handleLabelEditEnter() {
        labelTextField.setOnAction(event -> {
            String labelText = labelTextField.getText().trim().toUpperCase();
            if (!labelText.matches("[A-Z]{1,3}")) {
                OnNotification.accept("Label must contain 1 to 3 letters A-Z!");
                return;
            }
            else if (Graph.getUsedLabels().contains(labelText)) {
                OnNotification.accept("You already have vertex " + labelText);
                return;
            }

            Graph.getUsedLabels().remove(label.getText());
            label.setText(labelText);
            Graph.getUsedLabels().add(labelText);
            labelTextField.setVisible(false);
            label.setVisible(true);
            this.onLabelEntered.run();

        });
    }

    public void setOnClicked(Consumer<VertexView> callback){
        this.onVertexClicked = callback;
    }

    @Override
    public String toString() {
        return "VertexView{" +
                "vertex=" + vertex +
                '}';
    }

    public static void setOnBlockDragging(boolean value){
        onBlockDragging = value;
    }

    public void setOnLabelEntered(Runnable callback) {
        this.onLabelEntered = callback;
    }

    public void setOnEditingLabel(Runnable callback) {
        this.onEditingLabel = callback;
    }

    public void setOnNotification(Consumer<String> callback) {
        this.OnNotification = callback;
    }
}

