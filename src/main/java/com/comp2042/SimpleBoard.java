package com.comp2042;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.awt.*;

public class SimpleBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;
    private Point currentOffset;
    private final Score score;

    public SimpleBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[width][height];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(0, 1);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    /** snapBrickDown() method
     *
     * when the pieces are falling in the well the offset is calculated repeatedly,
     * It will check the positioning in the well to see if any collisions occur either with the ground or previously locked bricks in the well,
     * If there is no collisions it will move down one row, this continues until a collision occurs
     * When collision occurs, the real game offset will become its final positioning, allowing it to be locked in the game matrix
     * It will return the value true for snapping the brick into the well and return it to the method used in GameController.java
     *
     * @return
     */

    // Snap piece down to bottom of well
    @Override
    public boolean snapBrickDown() {
        Point p = new Point(currentOffset);
        while (!MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), p.x, p.y + 1)) {
            p.y += 1;
        }

        // Lock piece at bottom of well
        currentOffset = p;

        return true;
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(-1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        Point p = new Point(currentOffset);
        p.translate(1, 0);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), (int) p.getX(), (int) p.getY());
        if (conflict) {
            return false;
        } else {
            currentOffset = p;
            return true;
        }
    }

    // Rotation of bricks
    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /** rotateBrickCC() method for Counter-Clockwise rotation
     *
     * Essentially, the method functions the same as rotateLeftBrick() but it uses the counter-clockwise rotation of the brick instead of rotating clockwise
     * It checks for collision, if collision happens it will return the value false for this method
     * if there is no collision it will get the next shape and position and return the correct counter-clockwise rotation
     * it then returns the value true for boolean
     * This method is then called in GameController.java
     *
     * @return the updated instance of the position of the counter-clockwise rotated logic brick back to onzEvent() method
     */

    // Counter-clockwise rotation of bricks
    @Override
    public boolean rotateBrickCC() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextCounterClockwiseShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    /** rotateBrickCC() method for Clockwise rotation
     *
     * Essentially, the method functions the same as rotateLeftBrick() but it is called when X key is pressed
     * It checks for collision, if collision happens it will return the value false for this method
     * if there is no collision it will get the next shape and position and return the correct clockwise rotation
     * it then returns the value true for boolean
     * This method is then called in GameController.java
     *
     * @return the updated instance of the position of the clockwise rotated logic brick back to onXEvent() method
     */

    // Clockwise rotation of bricks
    @Override
    public boolean rotateBrickC() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextClockwiseShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    // Spawns the pieces
    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentOffset = new Point(4, 0); // Changed y value to 0 to make the pieces spawn at the top of the screen
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    @Override
    public int[][] getBoardMatrix() {
        return currentGameMatrix;
    }

    @Override
    public ViewData getViewData() {
        return new ViewData(brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY(), brickGenerator.getNextBrick().getShapeMatrix().get(0));
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), (int) currentOffset.getX(), (int) currentOffset.getY());
    }

    /** updated clearRows();
     *
     * Clears any fully completed horizontal rows from the current game matrix
     * it will calculate the amount of lines removed
     * it will also calculate the updated game matrix after line removal
     *
     * After taking in these 2 factors it will calculate a score bonus based on the number of cleared lines
     *
     * @return the updated instance of cleared rows and the score bonus earned
     */

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        int lines = clearRow.getLinesRemoved();
        int[][] newMatrix = clearRow.getNewMatrix();
        currentGameMatrix = newMatrix;

        // Points awarded for multiple line clears
        int bonus = switch (lines) {
            case 1 -> 100;
            case 2 -> 300;
            case 3 -> 500;
            case 4 -> 800;
            default -> 0;
        };

        return new ClearRow(lines,newMatrix, bonus);

    }

    @Override
    public Score getScore() {
        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[width][height];
        score.reset();
        createNewBrick();
    }
}
