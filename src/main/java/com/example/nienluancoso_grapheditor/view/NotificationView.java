package com.example.nienluancoso_grapheditor.view;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class NotificationView extends Pane {
    private final Label messageLabel = new Label();

    private final FadeTransition fadeIn;
    private final PauseTransition pause;
    private final FadeTransition fadeOut;
    private final SequentialTransition animation;

    public NotificationView() {
        setVisible(false);
        messageLabel.getStyleClass().add("notification-message");

        getChildren().add(messageLabel);

        fadeIn = new FadeTransition(Duration.millis(200), this);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        pause = new PauseTransition(Duration.seconds(2));

        fadeOut = new FadeTransition(Duration.millis(500), this);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        animation = new SequentialTransition(
                fadeIn,
                pause,
                fadeOut
        );

        animation.setOnFinished(event -> {
            setVisible(false);
            setManaged(false);
        });
    }

    public void show(String message) {
        // Nếu notification cũ đang chạy thì dừng lại
        animation.stop();

        messageLabel.setText(message);

        setVisible(true);
        setManaged(true);

        setOpacity(0);

        animation.playFromStart();
    }
}
