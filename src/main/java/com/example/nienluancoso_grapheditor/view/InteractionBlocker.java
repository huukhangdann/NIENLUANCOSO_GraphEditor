package com.example.nienluancoso_grapheditor.view;

import javafx.scene.layout.Pane;

import java.util.function.Consumer;

public class InteractionBlocker extends Pane {
    boolean isBlocking = false;
    private Consumer<String> onClickedBlocker;

    public InteractionBlocker(){
        this.getStyleClass().add("interaction-blocker");
        this.setOnMouseClicked(event -> {
            if(isBlocking){
                onClickedBlocker.accept("Please enter weight first!");
                event.consume();
            }
        });
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public void block(boolean value){
        this.setVisible(value);
        isBlocking = value;
    }

    public void setOnClickedBlocker(Consumer<String> callback){
        this.onClickedBlocker = callback;
    }
}
