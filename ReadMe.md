-------------------------------------------------------------------------------------------------

Tetris Overhaul
Shawn Chan - 20590530

-------------------------------------------------------------------------------------------------

Github Link:
https://github.com/Syrup-2005/Comp2042-CW2025

-------------------------------------------------------------------------------------------------

Compilation:
1. Download all files from Overhaul-Version branch.
2. Extract into desired location
3. Compile and Run with Maven using javaFX

-------------------------------------------------------------------------------------------------

Keybinds:
Z - Rotate falling piece counter-clockwise
X - Rotate falling piece clockwise
A/Left Arrow - Move brick Left
S/Down Arrow - Move Brick Down
D/Right Arrow - Move Brick Right
Space Bar - Snap Piece to well instantly/Hard Drop
N - New Game
P/ESC - Pause Game

-------------------------------------------------------------------------------------------------

Gameplay Loop:
Indefinite loop until player triggers a Game Over by not being able to place block in the board

-------------------------------------------------------------------------------------------------
Features and Changes Made:

11th November 2025
1. Changed the height in which the pieces/bricks spawn
  - Reason for changing the spawn height of the pieces is to fully utilise the entire grid given,

  * Changes:
    - the offset within the createNewBrick() method is changed from y: 10 to y:0

  * Summary:
    - this change gives more opportunities to prolong the game as there is more space in the well to place bricks before a gameover.

2. Added Rotation Options for the pieces
  - Reason for adding more rotation options is to give players more control of the piece to be placed in the well,

  * Changes:
    - In EventType.java, public enum EventType{ROTATECC and ROTATEC} were added,
    - In GuiController.java, public void handle(KeyEvent keyEvent), the Z and X keys on the keyboard were mapped to rotate clockwise and counter-clockwise,
    - In GameController.java added public ViewData onZEvent(MoveEvent event) and public ViewData onXEvent(MoveEvent event)
    - In InputEventListener.java added ViewData onZEvent(MoveEvent event); & ViewData onXEvent(MoveEvent event);,
    - In SimpleBoard.java added public boolean rotateBrickCC() & public boolean rotateBrickC()
    - In Board.java added boolean rotateBrickCC & boolean rotateBrickC
    - In BrickRotator.java added new classes getNextCounterClockwiseShape() and getNextClockwiseShape() for clockwise and counter-clockwise rotation logic

  * Problems Encountered:
    - Figuring out the logic for rotateBrickC(), rotateBrickCC(), new classes for getNextShape(), getNextCounterClockwiseShape() and getNextClockwiseShape()

  - Summary:
    - this change gives more control to players to place the pieces in the well more efficiently, by rotating the piece either clockwise or counter-clockwise

12th November 2025
3. Downwards Snap to Well
  - Reason for adding this is to allow players to make a decisive move by snapping the current piece to the bottom of the well.

  * Changes:
    - In GuiController.java added SPACE Key mapping, Space Bar is now used to snap current piece to bottom of the well
    - In GameController.java added onSpaceEvent(MoveEvent event) to call snapBrickDown() and update visual input
    - In InputEventListener.java added ViewData onSpaceEvent(MoveEvent event) to take input when space key is pressed
    - In SimpleBoard.java added public void snapBrickDown() which implements hard drop logic and process for merging into game matrix
    - In EventType.java added SNAPDOWN to public enum Eventype

  * Problems Encountered:
    - the logic to actually make the hard drop work took a few days to figure out, there was a lot of unexepected errors such as collision not working correctly

  * Summary:
    - Hard Dropping a piece to the bottom of a well gives a more decisive playstyle and allows for more points to be scored(+150)

17th November 2025
4. Pause Screen Implementation
  - To enable players to pause the game upon a button press

  * Changes:
    - In GuiController.java added keyEvent when P or ESC is pressed, toggle game pause or unpause.
    - In GuiController.java added if statement to initGameView, move pieces down only when game is not paused
    - In GuiController.java intialized pausePanel to game
    - Created new java file PauseLabel.java for overlay when game is paused
    - In gameLayout.fxml added a node for PausePanel to show on game screen correctly

  * Problems Encountered:
    - Adding and ensuring the Pause Screen Overlay works correctly in game

  * Summary:
    - When player presses P or Esc keys, game will be paused and a pause label will appear on screen, upon pressing the same keys the game will be resumed

18th November 2025
5. Scoreboard Implementation
  - To enable players to keep track of their current score counter while playing

  * Changes:
    - In gameLayout.fxml added a node for showing scoreBoard
    - In GuiController.java intitalized scoreBoard to game
    - In SimpleBoard.java edited clearRows() to have different switch case point distribution for bonus points when clearing multiple rows
    - Created ScoreBoard.java to store all necessary methods and UI customisations related to scoreboard 
    - In ScoreBoard.java added fixed '...' appearing on the scoreboard due to width of VBox being too small
    - In GuiController.java bindScore(IntegerProperty scoreProperty) has been properly set to bind score to scoreboard using method bind() from ScoreBoard.java
    - Added CSS for scoreboard(unused)

  * Problems Encountered:
    - The logic for making the score bind properly to the scoreboard took a while to figure out
    - ScoreBoard title is not showing in the UI

  * Summary:
    - This enables players to keep track of how many points they have obtained while playing
   
  * Bugs:
    - The Score Label is not working correctly due to the spacing being restricted
  
20th November 2025
6. Preview of the falling piece, where it will snap at the bottom of the well (Ghost Piece) (Imperfect)
  - This gives players a preview of where the piece is fallin onto

  * Changes:
    - In GuiController.java added contructor method setBoard(Board board) to enable methods from Board.java 
    - In GuiController.java added refreshGhost() method to refresh ghost piece whenever a new piece is created in the board
    - In GuiController.java, in refreshBrick() method called refreshGhost() method after every new piece is created to update ghost piece visual
    - In GuiController.java, in initGameView() added logic for intialising ghost piece in game
    - In gameLayout.fxml added a node to ghostPane to show the ghost piece 

  * Problems Encountered:
    - Couldnt get the ghost piece to perfectly allign with the pieces in the well
    - Upon merging the piece will have visual bugs before correctly snapping into the correct position in the board

  * Summary:
    - Ghost piece is implemented to let players know where the pieces will land
   
  * Bugs:
    - Allignment is not correctly linked to offset making visual cues a little misaligned with pieces in the well
    - Collision for ghost piece isnt always correct due to logic in the method and intialization

27th Novemeber 2025
7. Increasing Timeline Speed as Game goes on
  - This makes it more difficult and challenging for players, to make more decisions as the pieces fall faster and starts to fill the board faster
  
  * Changes:
    - In GuiController.java Created new class TimelineUpdater.java to hold responsibilities of updating timeline every 1000 points gained
    - In GuiController.java updated bindScore() method with a listener

  * Summary:
    - the game speeds up exponentially to force players to adapt to a faster playstle or risk ending the game faster
    - This ensures the infinite loop holds up while making sure gameplay does not go stale

8.  Next Piece Board implementation
    - This allows players to get a heads up for the next coming piece

    * Changes:
      - Created new class NextPieceBoard
      - In guiController.java added new method updateNextPiece()
      - In guicontroller.java added updateNextPiece(brick); inside refreshBrick() method
      - In gameLayout.fxml added a node to show 4x4 grid below scoreboard for next piece preview

    * Summary:
      - This gives players more information and time to react and plan where the next piece goes in the well

9. Holding a piece for later use / Hold feature implementation
  - This allows for players to hold certain pieces they would want to use later on for a specific scenario

  * Changes:


----------------------------------------------------------------------------------------------------------------------------------------------------------------------------

///////////Ideas that were implemented but not working || Ideas that were too complex to implement///////////

1. Tweaks in MatrixOperations.java
  - The format for intersect() and merge() uses i and j to indicate y and x, making the index, format and structure of code expectation a little complex
  - The idea to change and update it was scrapped as it messed up a lot of the links that were already in place
  - Changing it made visual inputs dissapear entirely, pieces floating after merging to game matrix, collision of the bricks also got messed up

2. Implementation of the Super Rotation System (SRS) and T-Spins
  - According to the Super Rotation System, making a lock delay timer at the bottom of the well is very complex
  - Personally I dont think i can implement this feature as it is far too complex with wallkicks and other features which make up the Super Rotation System
  - Adding to this reason, T-spinning is also a part of the SRS hence the idea was scrapped

3. Additional rotations in the matrix for z and x bricks
  - these were implemented but it wasnt working, the rotations visually does not look smooth and the collision does not match up with the well when it is merged
  - I wanted to give a more smooth transition for s and z brick when rotating clockwise or counter-clockwise in the well

4. Score Label implemented but not working correctly

5. Ghost Piece has been implemented but the visual updates for collision and allignment are off due to offset of board

21th Novemeber - 27th Novemeber
6. Refactoring SimpleBoard.java into smaller files (failed)
    - This was done to fulfill SOLID Principles as SimpleBoard handles too many different responsibilities
    - I shouldve been able to refactor this file but I took too much time and it kept getting more and more complex as I linked the files

    * Changes:
      - Extracted all matrix operation methods in SimpleBoard.java into BoardState.java
      - Extracted all movement methods in SimpleBoard.java into MovementSystem.java 
      - In MovementSystem.java created new move() method to reduce code duplication of moveBrickDown(), moveBrickLeft() and moveBrickRight();
      - Extracted all Rotation methods in SimpleBoard.java into RotationSystem.java
      - Extracted all brick spawning methods in SimpleBoard.java into BrickSpawner.java
      - Extracted clearRows method in SimpleBoard.java into ScoreCalculator.java
      - In BoardState.java added a new method getViewData() 
      - In RotationSystem.java removed rotateleftBrick() as I've already implemented clockwise and counterclockwise rotation (for memory efficiency)
      - Refactored SimpleBoard.java to a coordinator or a hub that connects all the systems
      - In Board.java removed rotateLeftBrick()
      - In GuiController.java removed input for up arrow key and w key
      - In GameController.java removed all logic related to rotateLeftBrick()
      - In InputEventListener.java removed onRotateEvent()
      - In Brick.java added option to compute width and height of brick
      - In GuiController.java corrected refreshghost() logic to match new refactored SimpleBoard.java
      - In GuiController.java corrected initGameView() logic to match corrected offset position for bricks
      - In Main.java added line c.setBoardState(boardState) to intialize it after FXML loads
      - In BrickRotator.java added a constructor BrickRotator() and spawnNewBrick() method 
      - In GuiController.java updated refreshBrick() method to update UI (?)
      - Updated BrickRotator.java to fix Offset mismatch
      - In MatrixOperations.java updated intersect() logic as [i][j] were placed incorrectly
      - In MatrixOperations.java updated merge() logic as [i][j] were placed incorrectly

    * Problems Encountered:
      - Collision broke immediately after refactoring
      - Some links had to be properly verified before they started working correctly
      - While moving and refactoring the blocks of code the offset position got really messy to determine what is out-of-bound or not
      - After refactoring the UI wasnt updating visually
      - Ghost piece wasnt alligned correctly
      - MatrixOperations was not handling the collision check correctly after refactoring
  



