package com.comp2042;

public class GameController implements InputEventListener {

    // Board Size
    private Board board = new SimpleBoard(25, 10);

    private final GuiController viewGuiController;

    public GameController(GuiController c) {
        viewGuiController = c;
        board.createNewBrick();
        viewGuiController.setEventListener(this);
        viewGuiController.initGameView(board.getBoardMatrix(), board.getViewData());
        viewGuiController.bindScore(board.getScore().scoreProperty());
    }

    // When down key is pressed
    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    // Move Piece to left
    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    // Move piece to right
    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    // Rotate piece
    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }

    /** onZEvent(MoveEvent event)
     *  On Z key input, this method gets the logic from SimpleBoard.java rotateBrickCC() method
     *  it then returns the data, and updates the board visually
     *
     * @param event The move event that triggered the counter-clockwise rotation is by Z key
     * @return the updated instance of the brick state in the board
     */

    // On Z key rotation
    @Override
    public ViewData onZEvent(MoveEvent event) {
        board.rotateBrickCC();
        return board.getViewData();
    }

    /** onXEvent(MoveEvent event)
     *  On X key input, this method gets the logic from SimpleBoard.java rotateBrickC() method
     *  it then returns the data, and updates the board visually
     *
     * @param event The move event that triggered the counter-clockwise rotation is by X key
     * @return the updated instance of the brick state in the board
     */

    // On X key rotation
    @Override
    public ViewData onXEvent(MoveEvent event) {
        board.rotateBrickC();
        return board.getViewData();
    }

    /** onSpaceEvent(MoveEvent event)
     * boolean canMove is created to move the brick down when there is no collisions using moveBrickDown()
     *
     *
     * @param event The move event that triggered the hard drop is by pressing SPACE key
     * @return the updated instance where the brick is locked into the game matrix
     */

    // Hard Drop
    @Override
    public ViewData onSpaceEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        board.snapBrickDown();
        ClearRow clearRow = null;

        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                viewGuiController.gameOver();
            }

            viewGuiController.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }

        return board.getViewData();
    }

    // When N key is pressed, clear board and start new game
    @Override
    public void createNewGame() {
        board.newGame();
        viewGuiController.refreshGameBackground(board.getBoardMatrix());
    }

    public void updateScoreBoard() {
        Score score = board.getScore();

    }
}
