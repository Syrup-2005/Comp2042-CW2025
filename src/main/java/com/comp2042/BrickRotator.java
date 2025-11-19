package com.comp2042;

import com.comp2042.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    // To get next Shape/Brick info
    public NextShapeInfo getNextShape() {
        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /** getNextCounterClockwiseShape()
     *
     * It will first get the current shape position,
     * then using holder as a placeholder to get the size of shapeMatrix
     * It will then get the previous position of the shape of the logic brick
     * lastly it will return the counter-clockwise rotation of the brick
     *
     * @return the updated instance of the counter-clockwise rotated brick piece
     */

    // Add logic for Counter-clockwise and Clockwise Rotation
    // Counter-Clockwise
    public NextShapeInfo getNextCounterClockwiseShape() {
        int currentPosition = currentShape;
        int holder = brick.getShapeMatrix().size();
        int NextPosition = (currentPosition - 1 + holder) % holder;
        return new NextShapeInfo(brick.getShapeMatrix().get(NextPosition), NextPosition);
    }

    /** getNextClockwiseShape()
     *
     * It will first get the current shape position,
     * then using holder as a placeholder to get the size of shapeMatrix
     * It will then get the next position of the shape of the logic brick
     * lastly it will return the clockwise rotation of the brick
     *
     * @return the updated instance of the clockwise rotated brick piece
     */

    // Clockwise
    public NextShapeInfo getNextClockwiseShape() {
        int currentPosition = currentShape;
        int holder = brick.getShapeMatrix().size();
        int NextPosition = (currentPosition + 1) % holder;
        return new NextShapeInfo(brick.getShapeMatrix().get(NextPosition), NextPosition);
    }


    public int[][] getCurrentShape() {
        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    public void setBrick(Brick brick) {
        this.brick = brick;
        currentShape = 0;
    }


}
