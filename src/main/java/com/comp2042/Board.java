package com.comp2042;

public interface Board {

    boolean moveBrickDown();

    // Snap down current falling brick to bottom of well
    boolean snapBrickDown();

    boolean moveBrickLeft();

    boolean moveBrickRight();

    boolean rotateLeftBrick();

    // Counter-clockwise rotation of bricks
    boolean rotateBrickCC();

    // Clockwise rotation of bricks
    boolean rotateBrickC();

    boolean createNewBrick();

    int[][] getBoardMatrix();

    ViewData getViewData();

    void mergeBrickToBackground();

    ClearRow clearRows();

    Score getScore();

    void newGame();

}
