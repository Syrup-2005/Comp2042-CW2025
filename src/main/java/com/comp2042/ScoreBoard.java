package com.comp2042;

import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
        valueLabel.setMaxWidth(Double.MAX_VALUE);
        valueLabel.setMinWidth(Region.USE_PREF_SIZE);
        valueLabel.setAlignment(Pos.CENTER_RIGHT);
        valueLabel.setTextOverrun(OverrunStyle.CLIP);
        VBox.setVgrow(valueLabel, Priority.ALWAYS);
        this.setFillWidth(true);
    }

    /**
     * Binds the scoreboard to an IntegerProperty.
     * Automatically updates the label when the score changes.
     * Also adjusts font size slightly for large numbers.
     */

    public void bind(IntegerProperty scoreProperty) {
        valueLabel.textProperty().bind(scoreProperty.asString());

        scoreProperty.addListener((obs, oldVal, newVal) -> {
            int length = String.valueOf(newVal.intValue()).length();
            double newFontSize = 48 - (length - 1) * 4; // reduce font 4px per extra digit
            if (newFontSize < 20) newFontSize = 20;      // minimum font size
            valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, newFontSize));
        });
    }

}
