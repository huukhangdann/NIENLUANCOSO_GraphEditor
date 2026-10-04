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

public class EdgeView extends Pane {
    private final Line line;
    private final Edge edge;
    private final Label weightLabel = new Label();
    private final Label weightLabelErr = new Label();
    private final TextField weightTextField = new TextField();
    private final static int PADDING = 40;
    private final static int GAP = 20;

    public EdgeView(Edge edge, VertexView vertexView1, VertexView vertexView2) {
        this.setMouseTransparent(true);
        this.edge = edge;
        line = new Line();
        line.getStyleClass().add("graph-line");
        this.getStyleClass().add("edge-view");

        setUpEdgeViewBinding(vertexView1, vertexView2);

        setUpWeightInput();

        handleWeightEnter();

        handleWeightEdit();

        this.getChildren().addAll(line, weightTextField, weightLabel, weightLabelErr);
        weightLabel.toFront();
        Platform.runLater(weightTextField::requestFocus);
    }

    private void handleWeightEdit() {
        weightLabel.setOnMouseClicked(event -> {
            event.consume();
            if(event.getClickCount() == 2){
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

    private void setUpWeightInput() {
        weightLabel.setVisible(false);
        // TextField creation
        weightTextField.setPrefWidth(60);
        weightTextField.setAlignment(Pos.CENTER);
        weightTextField.getStyleClass().add("edge-weight-field");

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
                () -> midPointX.get() + GAP * unitX.get() - weightTextField.getWidth() / 2,
                midPointX,
                unitX,
                weightTextField.widthProperty()
        ));

        weightTextField.layoutYProperty().bind(Bindings.createDoubleBinding(
                () -> midPointY.get() + GAP * unitY.get() - weightTextField.getHeight() / 2,
                midPointY,
                unitY,
                weightTextField.heightProperty()
        ));
    }

    private void handleWeightEnter() {
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

    private void setUpEdgeViewBinding(VertexView vertexView1, VertexView vertexView2) {
        // Binding layoutX, layoutY of the edgeView Pane to the vertexView
        this.layoutXProperty().bind(Bindings.createDoubleBinding(
                () -> Math.min(vertexView1.getCenterX(), vertexView2.getCenterX()) - PADDING,
                vertexView1.layoutXProperty(),
                vertexView2.layoutXProperty()));
        this.layoutYProperty().bind(Bindings.createDoubleBinding(
                () -> Math.min(vertexView1.getCenterY(), vertexView2.getCenterY()) - PADDING,
                vertexView1.layoutYProperty(),
                vertexView2.layoutYProperty()));

        // Binding area (width and height) of the edgeView Pane to the vertexView
        this.prefWidthProperty().bind(Bindings.createDoubleBinding(
                () -> Math.abs(vertexView1.getCenterX() - vertexView2.getCenterX()) + 2 * PADDING,
                vertexView1.layoutXProperty(),
                vertexView2.layoutXProperty()));
        this.prefHeightProperty().bind(Bindings.createDoubleBinding(
                () -> Math.abs(vertexView1.getCenterY() - vertexView2.getCenterY()) + 2 * PADDING,
                vertexView1.layoutYProperty(),
                vertexView2.layoutYProperty()));


        // Binding line to the vertexView (change vertexView to the local coordinate)
        line.startXProperty().bind(vertexView1.getCenterXProperty().subtract(this.layoutXProperty()));
        line.startYProperty().bind(vertexView1.getCenterYProperty().subtract(this.layoutYProperty()));
        line.endXProperty().bind(vertexView2.getCenterXProperty().subtract(this.layoutXProperty()));
        line.endYProperty().bind(vertexView2.getCenterYProperty().subtract(this.layoutYProperty()));
    }

    public Line getLine() {
        return line;
    }


}
