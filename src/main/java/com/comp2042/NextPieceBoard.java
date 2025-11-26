package com.comp2042;

import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Displays the next piece preview in a small 4x4 grid.
 */
public class NextPieceBoard extends GridPane {

    private static final int BRICK_SIZE = 20;
    private final Rectangle[][] rectangles = new Rectangle[4][4];

    public NextPieceBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setFill(Color.TRANSPARENT);
                rect.setStroke(Color.DARKGRAY);
                rect.setStrokeWidth(1);
                rectangles[i][j] = rect;
                this.add(rect, j, i);
            }
        }
        this.setHgap(2);
        this.setVgap(2);
    }

    /**
     * Updates the preview with a new brick.
     * @param brickData 2D int array representing the piece
     *                  0 = empty, >0 = block color
     */
    public void updateNextPiece(int[][] brickData) {
        // Clear previous piece
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                rectangles[i][j].setFill(Color.TRANSPARENT);
            }
        }

        // Draw new piece
        for (int i = 0; i < brickData.length; i++) {
            for (int j = 0; j < brickData[i].length; j++) {
                if (brickData[i][j] != 0) {
                    rectangles[i][j].setFill(getFillColor(brickData[i][j]));
                }
            }
        }
    }

    private Color getFillColor(int value) {
        return switch (value) {
            case 1 -> Color.AQUA;
            case 2 -> Color.BLUEVIOLET;
            case 3 -> Color.DARKGREEN;
            case 4 -> Color.YELLOW;
            case 5 -> Color.RED;
            case 6 -> Color.BEIGE;
            case 7 -> Color.BURLYWOOD;
            default -> Color.TRANSPARENT;
        };
    }
}
