package com.example.nienluancoso_grapheditor.view;

import javafx.scene.layout.Pane;

public class InteractionBlocker extends Pane {


    public InteractionBlocker(){
        this.getStyleClass().add("interaction-blocker");
    }

    public void setOnVisible(boolean value){
        setVisible(value);
    }
}
