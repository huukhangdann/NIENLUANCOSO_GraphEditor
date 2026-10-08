package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Edge;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;

import java.util.function.Consumer;

public class WeightView extends Pane {
    private final Label weightLabel = new Label();
    private final TextField weightTextField = new TextField();
    private Runnable onWeightEntered;
    private Runnable onEditingWeight;
    private Consumer<String> OnNotification;
    private boolean isTyping = false;

    // Binding Variable for dragging weight label
    private final DoubleProperty offsetX = new SimpleDoubleProperty(0);
    private final DoubleProperty offsetY = new SimpleDoubleProperty(0);
    private double lastMouseX = 0;
    private double lastMouseY = 0;

    private final static int GAP = 20;

    public WeightView(Edge edge, EdgeView edgeView){
        setPickOnBounds(false);
        // connect edgeView-weightView
        edgeView.setWeightView(this);

        this.getStyleClass().add("weight-view");

        setUpWeightInput(edgeView);

        weightViewBinding(edgeView);

        handleWeightEnter(edge);

        handleWeightEdit(edge);

        handleWeightViewPressed();
        handleWeightViewDragged();

        Platform.runLater(weightTextField::requestFocus);

        this.getChildren().addAll(weightTextField, weightLabel);
    }

    public void weightViewBinding(EdgeView edgeView) {
        this.setPrefWidth(weightLabel.getPrefWidth());
        this.setPrefHeight(weightLabel.getPrefHeight());
        Line line = edgeView.getLine();


        // Binding to line (line (binding)-> vertexView)
        DoubleBinding dx = Bindings.createDoubleBinding(
                () -> line.getEndX() - line.getStartX(),
                line.startXProperty(),
                line.endXProperty()
        );
        DoubleBinding dy = Bindings.createDoubleBinding(
                () -> line.getEndY() - line.getStartY(),
                line.startYProperty(),
                line.endYProperty()
        );

        DoubleBinding ndx = dy.negate();
        DoubleBinding ndy = dx;

        DoubleBinding length = Bindings.createDoubleBinding(
                () -> Math.sqrt(ndx.get() * ndx.get() + ndy.get() * ndy.get()),
                ndx,
                ndy
        );

        // unitVector
        DoubleBinding unitX = Bindings.createDoubleBinding(
                () -> ndx.get() / length.get(),
                ndx,
                length
        );

        DoubleBinding unitY = Bindings.createDoubleBinding(
                () -> ndy.get() / length.get(),
                ndy,
                length
        );

        // midPoint
        DoubleBinding midPointX = Bindings.createDoubleBinding(
                () -> (line.getStartX() + line.getEndX()) / 2,
                line.startXProperty(),
                line.endXProperty());

        DoubleBinding midPointY = Bindings.createDoubleBinding(
                () -> (line.getStartY() + line.getEndY()) / 2,
                line.startYProperty(),
                line.endYProperty());

        this.layoutXProperty().bind(Bindings.createDoubleBinding(
                () -> edgeView.getLayoutX() + offsetX.get() +  midPointX.get() + GAP * unitX.get() - this.getWidth() / 2 ,
                offsetX,
                edgeView.layoutXProperty(),
                midPointX,
                unitX,
                this.widthProperty()
        ));

        this.layoutYProperty().bind(Bindings.createDoubleBinding(
                () -> edgeView.getLayoutY() + offsetY.get() + midPointY.get() + GAP * unitY.get() - this.getHeight() / 2,
                offsetY,
                edgeView.layoutYProperty(),
                midPointY,
                unitY,
                this.heightProperty()
        ));
    }

    private void setUpWeightInput(EdgeView edgeView) {
        weightLabel.setVisible(false);
        isTyping = true;
        // TextField creation
        weightTextField.setPrefWidth(60);
        weightTextField.setAlignment(Pos.CENTER);
        weightTextField.getStyleClass().add("edge-weight-field");
    }

    private void handleWeightEnter(Edge edge) {
        weightTextField.setOnAction(event -> {
            String weightText = weightTextField.getText().trim();
            try {
                int weight = Integer.parseInt(weightText);
                if(weight<0 || weight>1000){
                    this.OnNotification.accept("Weight must have value between 0 and 1000!");
                    return;
                }
                setUpWeightLabel(weight);
                edge.setWeight(weight);
                isTyping = false;
                weightTextField.setVisible(false);
                this.onWeightEntered.run();
            } catch (NumberFormatException e) {
                this.OnNotification.accept("Weight must be integer!");
            }
        });
    }

    private void setUpWeightLabel(int weight) {
        weightLabel.setVisible(true);
        weightLabel.setText(String.valueOf(weight));
        weightLabel.setPrefWidth(weightTextField.getPrefWidth());
        weightLabel.setPrefHeight(weightTextField.getPrefHeight());
        weightLabel.setAlignment(Pos.CENTER);
        weightLabel.getStyleClass().add("weight-text-label");

        weightLabel.layoutXProperty().bind(weightTextField.layoutXProperty());
        weightLabel.layoutYProperty().bind(weightTextField.layoutYProperty());

    }

    private void handleWeightEdit(Edge edge) {
        weightLabel.setOnMouseClicked(event -> {
            event.consume();
            if(event.getClickCount() == 2){
                this.onEditingWeight.run(); // run callback -> controller
                weightLabel.setVisible(false);
                weightTextField.setVisible(true);
                weightTextField.setText(String.valueOf(edge.getWeight()));
                Platform.runLater(() -> {
                    weightTextField.requestFocus();
                    weightTextField.selectAll();
                });
            }
        } );
    }

    private void handleWeightViewPressed(){
        this.setOnMousePressed(event -> {
            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
            event.consume();
        });
    }

    private void handleWeightViewDragged(){
        this.setOnMouseDragged(event -> {
            double dx = event.getSceneX() - lastMouseX;
            double dy = event.getSceneY() - lastMouseY;

            offsetX.set(offsetX.get() + dx);
            offsetY.set(offsetY.get() + dy);

            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();

            event.consume();
        });
    }

    public void setOnWeightEntered(Runnable callback){
        this.onWeightEntered = callback;
    }

    public void setOnEditingWeight(Runnable callback){
        this.onEditingWeight = callback;
    }

    public void setOnNotification(Consumer<String> callback) {
        this.OnNotification = callback;
    }

    public ObservableList<String> getStyleClassWeighLabel(){
        return weightLabel.getStyleClass();
    }
}
