package com.comp2042;

import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/** This is a small UI overlay for when the game is paused
 *
 * This class creates a white "PAUSED" label in the center of the game board when the game is paused
 *
 */

public class PausePanel extends BorderPane {

    public PausePanel() {
        final Label pauseLabel = new Label("PAUSED");
        pauseLabel.setTextFill(Color.WHITE);
        pauseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        setCenter(pauseLabel);
    }
}
