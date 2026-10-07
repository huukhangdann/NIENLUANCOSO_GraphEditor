package com.example.nienluancoso_grapheditor.view;

import javafx.scene.layout.Pane;

public class InteractionBlocker extends Pane {
    boolean isBlocking = false;

    public InteractionBlocker(){
        this.getStyleClass().add("interaction-blocker");
        this.setOnMouseClicked(event -> {
            if(isBlocking){
                event.consume();
            }
        });
    }

    public void setOnVisible(boolean value){
        setVisible(value);
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public void setBlocking(boolean isBlocking) {
        this.isBlocking = isBlocking;
    }
}
