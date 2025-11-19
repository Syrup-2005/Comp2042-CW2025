package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/** This is for the scoreboard UI in the game
 *
 * This class creates a white "SCORE" label and places it on the right side of the layout
 *
 */

public class ScoreBoard extends VBox {

    private final Label valueLabel;

    public ScoreBoard() {
        Label title = new Label("SCORE"); // Not Working as intended
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));

        // Score value
        valueLabel = new Label("0");
        valueLabel.setTextFill(Color.WHITE);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 48));

        // Add to this VBox
        this.getChildren().addAll(title, valueLabel);

        // Align content to center-right
        this.setAlignment(Pos.CENTER_RIGHT);

        // Spacing
        this.setSpacing(100);
        valueLabel.setMinWidth(100);
        valueLabel.setMinHeight(100);
    }

    public void bind(IntegerProperty scoreProperty) {
        valueLabel.textProperty().bind(scoreProperty.asString());
    }

}
