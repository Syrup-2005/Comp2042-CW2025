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

    // Add logic for Counter-clockwise and Clockwise Rotation
    // Counter-Clockwise
    public NextShapeInfo getNextCounterClockwiseShape() {
        int currentPosition = currentShape;
        int holder = brick.getShapeMatrix().size();
        int NextPosition = (currentPosition - 1 + holder) % holder;
        return new NextShapeInfo(brick.getShapeMatrix().get(NextPosition), NextPosition);
    }

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
