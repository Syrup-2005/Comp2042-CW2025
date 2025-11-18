package com.comp2042;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PausePanel extends BorderPane {

    public PausePanel() {
        final Label pauseLabel = new Label("PAUSED");
        pauseLabel.setTextFill(Color.WHITE);
        pauseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        setCenter(pauseLabel);
    }
}
