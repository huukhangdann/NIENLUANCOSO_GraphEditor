package com.example.nienluancoso_grapheditor.view;

import com.example.nienluancoso_grapheditor.model.Edge;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class WeightView extends Pane {
    private final Label weightLabel = new Label();
    private final Label weightLabelErr = new Label();
    private final TextField weightTextField = new TextField();

    private final static int GAP = 20;

    public WeightView(Edge edge, EdgeView edgeView){
        setUpWeightInput(edgeView);

        handleWeightEnter(edge);

        handleWeightEdit(edge);

        Platform.runLater(weightTextField::requestFocus);

        this.getChildren().addAll(weightLabelErr, weightTextField, weightLabel);
        this.toFront();
    }

    private void setUpWeightInput(EdgeView edgeView) {
        weightLabel.setVisible(false);
        // TextField creation
        weightTextField.setPrefWidth(60);
        weightTextField.setAlignment(Pos.CENTER);
        weightTextField.getStyleClass().add("edge-weight-field");

        Line line =  edgeView.getLine();
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

        weightTextField.layoutXProperty().bind(Bindings.createDoubleBinding(
                () -> edgeView.getLayoutX() + midPointX.get() + GAP * unitX.get() - weightTextField.getWidth() / 2,
                edgeView.layoutXProperty(),
                midPointX,
                unitX,
                weightTextField.widthProperty()
        ));

        weightTextField.layoutYProperty().bind(Bindings.createDoubleBinding(
                () -> edgeView.getLayoutY() +midPointY.get() + GAP * unitY.get() - weightTextField.getHeight() / 2,
                edgeView.layoutYProperty(),
                midPointY,
                unitY,
                weightTextField.heightProperty()
        ));
    }

    private void handleWeightEnter(Edge edge) {
        weightTextField.setOnAction(event -> {
            String weightText = weightTextField.getText().trim();
            try {
                int weight = Integer.parseInt(weightText);
                setUpWeightLabel(weight);
                edge.setWeight(weight);
                weightTextField.setVisible(false);

            } catch (NumberFormatException e) {
                handleInvalidWeight();
            }
        });
    }

    private void setUpWeightLabel(int weight) {
        weightLabel.setVisible(true);
        weightLabel.setText(String.valueOf(weight));
        weightLabel.setPrefWidth(60);
        weightLabel.setAlignment(Pos.CENTER);
        weightLabel.getStyleClass().add("weight-text-label");

        weightLabel.layoutXProperty().bind(weightTextField.layoutXProperty());
        weightLabel.layoutYProperty().bind(weightTextField.layoutYProperty());

    }

    private void handleInvalidWeight() {
        setUpWeightLabelErr();
    }

    private void setUpWeightLabelErr() {
        weightLabelErr.setText("Enter integer weight!");
        weightLabelErr.setPrefWidth(120);
        weightLabelErr.setAlignment(Pos.CENTER);
        weightLabelErr.getStyleClass().add("weight-text-err-label");
        showWeightError();

        weightLabelErr.layoutXProperty().bind(weightTextField.layoutXProperty().subtract(30));
        weightLabelErr.layoutYProperty().bind(weightTextField.layoutYProperty().add(23));
    }

    private void showWeightError() {
        weightLabelErr.setVisible(true);
        weightLabelErr.setOpacity(1.0);

        FadeTransition fade = new FadeTransition(
                Duration.millis(1200),
                weightLabelErr
        );

        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        fade.setDelay(Duration.millis(800));

        fade.setOnFinished(event -> {
            weightLabelErr.setVisible(false);
            weightLabelErr.setOpacity(1.0);
        });

        fade.play();
    }

    private void handleWeightEdit(Edge edge) {
        weightLabel.setOnMouseClicked(event -> {
            event.consume();
            if(event.getClickCount() == 2){
                this.toFront();
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
}
