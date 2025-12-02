package com.comp2042;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private GridPane brickPanel;

    @FXML
    private GameOverPanel gameOverPanel;

    @FXML
    private PausePanel pausePanel;

    @FXML
    private ScoreBoard ScoreBoardView;

    @FXML
    private NextPieceBoard nextPieceBoard;

    private Rectangle[][] displayMatrix;

    private InputEventListener eventListener;

    private Rectangle[][] rectangles;

    private Timeline timeLine;

    // Ghost Piece
    private Rectangle[][] ghostRectangles;

    @FXML
    private Pane ghostPane;

    private Board board;

    /**  private final BooleanProperty isPause = new SimpleBooleanProperty(false);
     *
     * Changed the default boolean to false for isPause
     */

    // Set false to default
    private final BooleanProperty isPause = new SimpleBooleanProperty(false);

    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    /** updateNextPiece()
     * gets the next brick data from the unused method getNextBrickData() in ViewData class
     *
     * @param currentBrick
     */

    public void updateNextPiece(ViewData currentBrick) {
        nextPieceBoard.updateNextPiece(currentBrick.getNextBrickData());
    }

    // Initialization
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        ghostPane.setLayoutX(gamePanel.getLayoutX());
        ghostPane.setLayoutY(gamePanel.getLayoutY());
        brickPanel.setLayoutX(gamePanel.getLayoutX());
        brickPanel.setLayoutY(gamePanel.getLayoutY());

        /** To initialize the pausePanel after adding it to GameLayout.fxml
         *
         * pausePanel is set to (false) by default when initialized
         * pausePanel.setPrefSize(gamePanel.getWidth(), gamePanel.getHeight());, this ensures the overlay covers the entire game board.
         */

        pausePanel.setVisible(false);
        pausePanel.setPrefSize(gamePanel.getWidth(), gamePanel.getHeight());

        ScoreBoardView.setVisible(true);

        /** Keyboard Mapping
         *
         *  1. If ESC key or P key is pressed, pauseGame() method will be called
         *
         *  2. If X key is pressed, input is sent to GameController.java and the method onXEvent() is called for the logic of clockwise rotation.
         *
         *  3. If z key is pressed, input is sent to GameController.java and the method onzEvent() is called for the logic of counter-clockwise rotation.
         *
         *  4. If SPACE key is pressed, input is sent to GameController.java and the method onSpaceEvent() is called for the logic of snapping the current piece to bottom of the well.
         *
         */

        // Sets up keyboard mapping
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                // Set ESC for pausing the game
                if (keyEvent.getCode() == KeyCode.ESCAPE || keyEvent.getCode() == KeyCode.P) {
                    // Toggle pause and unpause
                    pauseGame();
                    keyEvent.consume();
                }
                // Checks if game is paused or GameOver before processing inputs
                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    // Inputs mapped to arrow keys and WASD
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        // Move piece to left
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        // Move piece to right
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        // Rotate piece
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        // Move piece down
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    // Adding more options to rotate pieces
                    // Set Z Key for counter-clockwise rotation
                    if (keyEvent.getCode() == KeyCode.Z) {
                        refreshBrick(eventListener.onZEvent(new MoveEvent(EventType.ROTATECC, EventSource.USER)));
                        keyEvent.consume();
                    }
                    // Set X Key for clockwise rotation
                    if (keyEvent.getCode() == KeyCode.X) {
                        refreshBrick(eventListener.onXEvent(new MoveEvent(EventType.ROTATEC, EventSource.USER)));
                        keyEvent.consume();
                    }
                    // Set Space key for downwards snap to bottom of well
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        refreshBrick(eventListener.onSpaceEvent(new MoveEvent(EventType.SNAPDOWN, EventSource.USER)));
                        keyEvent.consume();
                    }
                }
                // N key is mapped to start new game
                if (keyEvent.getCode() == KeyCode.N) {
                    newGame(null);
                }

            }
        });
        gameOverPanel.setVisible(false);
        pausePanel.setVisible(false);
        ScoreBoardView.setVisible(true);

        // Not assigned to any node
        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    // Creates bricks/pieces in the board
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        /** this block of code is to indicate the preview of the falling piece in the well
         * initialization of ghostRectangles
         * creates a 2d array of rectangles that matches the size of the current brick
         * each rectangle will represent one square of the ghost piece
         *
         * for each cell, creates a new rectangle of the same size as a regular tetris piece
         * the color is set to be transparent and its opacity is set to 0.3
         *
         * it then stores the rectangle in the ghostRectangles array for easy access
         * the ghost piece is also added to the brick panel so it is visible in the UI
         */

        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rect.setFill(Color.GRAY);
                rect.setOpacity(0.3);
                ghostRectangles[i][j] = rect;
                ghostPane.getChildren().add(rect);
            }
        }

        updateNextPiece(brick);

        /**
         * If statement for isPause
         * this if statement checks is the game is paused,
         * if the game is not paused the timeline proceeds as normal, making the game run as usual
         *
         * if the game is paused, the timeline will be frozen and the pieces will not fall and be suspended in whatever current position they were in
         *
         */

        // Timeline which defines game speed
        // Sets how fast the pieces fall
        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400), // set to 400ms as default
                // When S key or down arrow key is pressed
                // Move piece down
                ae -> {
                    if (!isPause.get()) {
                        // Move piece if not paused
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD));
                    }
                }
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    // Maps an integer value to a color
    // Essential for rendering piece/brick color correctly
    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0:
                returnPaint = Color.TRANSPARENT;
                break;
            case 1:
                returnPaint = Color.AQUA;
                break;
            case 2:
                returnPaint = Color.BLUEVIOLET;
                break;
            case 3:
                returnPaint = Color.DARKGREEN;
                break;
            case 4:
                returnPaint = Color.YELLOW;
                break;
            case 5:
                returnPaint = Color.RED;
                break;
            case 6:
                returnPaint = Color.BEIGE;
                break;
            case 7:
                returnPaint = Color.BURLYWOOD;
                break;
            default:
                returnPaint = Color.WHITE;
                break;
        }
        return returnPaint;
    }

    /** refreshBrick()
     * a line refreshGhost(brick) is added to ensure the ghost piece is updated upon creating a new piece in the board
     * Optimized for one call per rectangle instead of calling it multiple times per rectangle
     *
     * a line refreshGhost(brick) is added to ensure the next piece preview is updated every time the new piece is refreshed
     * @param brick
     */

    // Handles visual updates
    private void refreshBrick(ViewData brick) {
        if (!isPause.get()) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(gamePanel.getLayoutY() + brick.getyPosition() * BRICK_SIZE);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);
                }
            }
            // update ghost piece once per frame
            refreshGhost(brick);

            updateNextPiece(brick);
        }
    }

    // Updates the colors of the fixed bricks when lines are cleared
    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    // Determines brick/piece color
    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    // Node to move piece down
    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            // If any lines were cleared display score notification
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }

            // Make the dropping piece part of the well, so it is available for collision detection

            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * bindScore method has been refactored to correctly bind score to scoreboard UI
     * @param scoreProperty
     */

    public void bindScore(IntegerProperty scoreProperty) {
        ScoreBoardView.bind(scoreProperty);
    }

    // Game Over Screen
    public void gameOver() {
        timeLine.stop();
        gameOverPanel.setVisible(true);
        isGameOver.setValue(Boolean.TRUE);
    }

    // Start new game
    public void newGame(ActionEvent actionEvent) {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        timeLine.play();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
    }

    /** pauseGame() method was refactored
     *
     * isPaused.get() returns the current pause status
     * !isPaused flips the boolean value, so if the game is running it will be paused, if the game is paused it will be resumed
     * depending on the pause status of the game, the pausePanel overlay will be revealed or hidden accordingly
     * gamePanel.requestFocus(); allows keyboard inputs to still be taken while paused or unpaused
     *
     */

    // Pausing Game
    public void pauseGame() {
        boolean paused = !isPause.get();
        isPause.set(paused);

        pausePanel.setVisible(paused);

        gamePanel.requestFocus();
    }

    /**
     * This constructor method is used to enable methods from board.java
     * @param board
     */

    public void setBoard(Board board) {
        this.board = board;
    }

    /** This method is used to refresh the ghost piece everytime a new brick is created
     * it starts by copying the current piece's position
     * the ghost piece then moves down until it collides with something in the board
     *
     * the process loops through each block of the piece's shape
     * if the value is 1 there's a block in that position
     * if the value is 0 it is empty
     * the color is set to be gray and the color is transparent
     * finally the rectangle is positioned in the correct location on the screen based on the coordinates
     *
     * @param brick
     */

    private void refreshGhost(ViewData brick) {
        int ghostX = brick.getxPosition();
        int ghostY = brick.getyPosition();

        // Drop ghost down until collision
        while (!MatrixOperations.intersect(board.getBoardMatrix(), brick.getBrickData(), ghostX, ghostY + 1)) {
            ghostY++;
        }
        ghostY -= 2;
        ghostX--;

        // Place ghost rectangles
        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                if (brick.getBrickData()[i][j] != 0) {
                    ghostRectangles[i][j].setFill(Color.GRAY);
                    ghostRectangles[i][j].setArcHeight(9);
                    ghostRectangles[i][j].setArcWidth(9);

                    // Snap to grid using only BRICK_SIZE
                    ghostRectangles[i][j].setLayoutX((ghostX + j) * BRICK_SIZE);
                    ghostRectangles[i][j].setLayoutY((ghostY + i) * BRICK_SIZE);
                } else {
                    ghostRectangles[i][j].setFill(Color.TRANSPARENT);
                }
            }
        }
    }
}
