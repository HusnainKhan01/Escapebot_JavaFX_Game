package gui;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import logic.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import static gui.JavaFXGUI.*;
import static javafx.scene.paint.Color.RED;
import static logic.FieldType.*;
import static logic.FieldType.GRAY_WALL;
import static logic.Instructions.*;

/**
 * The FXMLDocumentController implements Initializable. Add the appropriate code to initialize(..) so that can
 * immediatly start playing once the program is running.
 * In the FXMLDocumentController the following have to exist:
 * *
 * @author husnain
 */
public class FXMLDocumentController implements Initializable {

    /**
     * All the speed levels for the animation
     */
    enum Speed{
        SLOW_SPEED, MEDIUM_SPEED, FAST_SPEED, VERY_FAST_SPEED, NO_ANIMATION_SPEED
    }

    /**
     *
     */
    enum ProcedureProgram{
        PROGRAM_TO_SELECT, PROCEDRE1_TO_SELECT, PROCEDRE2_TO_SELECT
    }

    /** Buttons for the editor mode, Solve Mode */
    public Button toEditorButton;
    public Button toSolveModeButton;

    public Button buttonStart;
    public Button stopButton;
    public Button clearButton;

    public Button clearProgramButton;
    public Button clearProcedure2Button;
    public Button clearProcedure1Button;

    public BorderPane borderPaneMain;

    public MenuBar menuBar;
    public Button solveButton;

    /**
     * Label for message, to be passed to JavaFXGUI
     * which will set the message on the gui
     */
    public Label messageLabel;

    /**
     * Main AnchorPane
     */
    public AnchorPane mainAchorPane;

    /**
     * keep track which mouse button is clicked
     */
    boolean isLeftClick;

    /**
     * Used to store the information of the instruction button clicked, will be passed to logic
     */
    public Instructions prevButton;

    /**
     * Store the information about the field type
     */
    public FieldType prevButtonChar;

    /**
     * Instance of the JavaFXGUI
     */
    public JavaFXGUI gui;

    /**
     * Game instance from logic
     */
    private Game game;

    /**
     * GridPane for the Main field of the game
     */
    public GridPane gridPaneField;

    /**
     * GridPane for the CreateMode of the Left side and Right side
     */
    public GridPane gridPaneLeftCreate;

    /**
     * Store buttons for leftside solve (Instructions)
     */
    public GridPane gridPaneLeftSolve;

    /**
     * GridPane for the Program and procedures
     */
    public GridPane gridPaneProgram;
    public GridPane gridPaneProcedure1;
    public GridPane gridPaneProcedure2;

    /**
     * Image Views of the Main field
     */
    ImageView[][] imageViewsField;

    /**
     * Image view for the CreateMode of the left side and Right side
     * For createMode Field Types are shown else Instructions
     */
    ImageView[][] imageViewsLeftSolve;
    ImageView[][] imageViewsLeftCreate;

    /**
     * Image Views for the program and procedures
     */
    ImageView[][] imageViewsProgram;
    ImageView[][] imageViewsProcedure1;
    ImageView[][] imageViewsProcedure2;

    /**
     * Initialize the ImageViews, Create Constructor for JavaFXGUI and Game
     * Set to SolveMode in the start
     * set the Map1 at the start
     *
     * @param url dot dot
     * @param resourceBundle dot dot
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // init main field
        imageViewsField = initImages(gridPaneField);

        // init left side create and solve mode
        imageViewsLeftCreate = initImages(gridPaneLeftCreate);
        imageViewsLeftSolve = initImages(gridPaneLeftSolve);

        // imageViews for program procedures
        imageViewsProgram = initImages(gridPaneProgram);
        imageViewsProcedure1 = initImages(gridPaneProcedure1);
        imageViewsProcedure2 = initImages(gridPaneProcedure2);

        gui = new JavaFXGUI(gridPaneField, gridPaneLeftCreate, gridPaneLeftSolve,
                imageViewsField, imageViewsLeftSolve, imageViewsLeftCreate,
                imageViewsProgram, imageViewsProcedure1, imageViewsProcedure2, messageLabel, borderPaneMain,
                menuBar, buttonStart, solveButton, clearButton);

        game = new Game(gui);

        clearButton.setDisable(true);
        // start with solve Mode
        setToSolveMode();
        try {
            onMap1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Called when stop button is pressed, which is used to stop animation
     */
    public void stopAnimation() {
        if(gui.animationIsRunning){
            gui.setStopButtonIsPressed();
        }
    }

    /**
     * Clear Button which is to be clicked to clear Field
     * Only work when animation is finished
     * 10 is for the previous map
     * @throws Exception exception
     */
    public void clearButton() throws Exception {
        // set to starting map
        if(!gui.animationIsRunning){
            game.setMap(10);
            messageLabel.setText("");
            gui.setDisableAllTillClear(false);
            gui.clearEffectProcedure2();
            gui.clearEffectsProcedure1();
            gui.clearEffectsFromProgram();

            clearButton.setDisable(true);
        }
    }


    /**
     * used to initialize the imageViews for the field
     * @param grdPn gridPane for the images
     * @return all the imageViews for the passed gridPane
     */
    private ImageView[][] initImages(GridPane grdPn) {
        int colcount = grdPn.getColumnConstraints().size();
        int rowcount = grdPn.getRowConstraints().size();
        ImageView[][] imageViews = new ImageView[colcount][rowcount];
        // bind each Imageview to a cell of the gridpane
        int cellWidth = (int) grdPn.getWidth() / colcount;
        int cellHeight = (int) grdPn.getHeight() / rowcount;
        for (int x = 0; x < colcount; x++) {
            for (int y = 0; y < rowcount; y++) {
                //creates an empty imageview
                imageViews[x][y] = new ImageView();
                //image has to fit a cell and mustn't preserve ratio
                imageViews[x][y].setFitWidth(cellWidth);
                imageViews[x][y].setFitHeight(cellHeight);
                imageViews[x][y].setPreserveRatio(false);
                imageViews[x][y].setSmooth(true);
                //assign the correct indicees for this imageview
                GridPane.setConstraints(imageViews[x][y], x, y);
                //add the imageview to the cell
                grdPn.add(imageViews[x][y], x, y);
                //the image shall resize when the cell resizes
                imageViews[x][y].fitWidthProperty().bind(grdPn.widthProperty().divide(colcount));
                imageViews[x][y].fitHeightProperty().bind(grdPn.heightProperty().divide(rowcount));
            }
        }
        return imageViews;
    }

    /**
     * used to get the clicked coordinates for the given gridPane
     * @param mouseEvent event
     * @param gridPane gridPane passed on which event occur
     * @return the clicked position
     */
    private int[] onMouseClickForAll(MouseEvent mouseEvent, GridPane gridPane){
        int col = -1;
        int row = -1;
        boolean leftClicked = mouseEvent.getButton() == MouseButton.PRIMARY;
        boolean rightClicked = mouseEvent.getButton() == MouseButton.SECONDARY;
        //determine the imageview of the grid that contains the coordinates of the mouseclick
        //to determine the board-coordinates
        for (Node node : gridPane.getChildren()) {
            if (node instanceof ImageView) {
                if (node.getBoundsInParent().contains(mouseEvent.getX(), mouseEvent.getY())) {
                    //to use following methods the columnIndex and rowIndex
                    //must have been set when adding the imageview to the grid
                    col = GridPane.getColumnIndex(node);
                    row = GridPane.getRowIndex(node);
                }
            }
        }
        assert (col >= 0 && row >= 0) : "no co ordinates ";
        if (leftClicked) {
            isLeftClick = true;
        } else if (rightClicked) {
            isLeftClick = false;
        }
        return new int[] {row, col};
    }

    /**
     * On Mouse Click program field
     * get the clicked coordinates calculate the position in 1d
     * set the Instruction if the leftClick is true, else if right clicked and has
     * a Instruction on that point then remove the instruction from Clicked position,
     * shift all by one so there is no remaining slot
     * return if the clicked position is between which gives -1 and -1 as return
     *
     *    DOCUMENTATION:
     *    QUESTION: How to validate the correct position of adding Moves inside Program and Procedure?
     *    1 -> One way is to create a grid and store all the previous positions and increment the
     *         Position every time an element is added to the program (Already implemented In Program)
     *    2 -> DO It in the Controller. First check the previous position of the clicked position
     *         on the program if it has an element and then check the next position of the clicked position
     *         if it doesn't have an element then this is the valid position for the move
     *    3 -> Check the gridPane children
     *    4 -> Another possibility is to store all the previous positions
     *    5 -> Assign Id's to every ImageView having NORMAL image, check the previous Position
     *    6 -> All the moves are stored in a 1D array for that first position is calculated
     *         pos[0] gives us the row and pos[1] col so we have total 4 cols in every row
     *         that means if we multiply pos[0] with 4 and add the col (pos[1]) we will get the
     *         position in 1D and that is stored in 1D array
     *         To check if the clicked position is valid is very simple with that approch
     *         only the previous position is checked if it is not equal to null then this is valid position
     *         and then the move can be set into this position
     *         all the moves are stored into 1d array and when start button is pressed these moves are passed
     *         to the game which will do further processing
     *         Same procedure is done with onMouseClickProcedure1 and 2
     *
     * @param mouseEvent event
     */
    public void onMouseClickProgram(MouseEvent mouseEvent) {
        int colInRow = 4;
        int[] pos = onMouseClickForAll(mouseEvent, gridPaneProgram);
        if(pos[0] == -1 || pos[1] == -1){
            return;
        }
        int calPos = pos[0] * colInRow + pos[1];
        if(calPos == 0 || hasElementOnPrevPos(new Position(pos[0], pos[1]), imageViewsProgram)){
            if(isLeftClick){
                gui.setProgramImage(pos[0], pos[1], prevButton.ordinal() + 1, 1);
            } else {
                removeMoveFromProgram(getProgramProcedureImageView(ProcedureProgram.PROGRAM_TO_SELECT), new Position(pos[1], pos[0]));
            }
        }
    }

    /**
     * On Mouse Click Procedure1
     * get the clicked coordinates calculate the position in 1d
     * set the Instruction if the leftClick is true, else if right clicked and has
     * a Instruction on that point then remove the instruction from Clicked position,
     * shift all by one so there is no remaining slot
     * return if the clicked position is between which gives -1 and -1 as return
     *
     * FOR DOCUMENTATION
     *  is valid position on procedure( is valid position
     *             when the previous position
     *             in the procedure is filled)
     *   then ---> forward previousSolve button to the logic of
     *             the procedure which will add it on the gui and logic
     *
     * @param mouseEvent event
     */
    public void onMouseClickProcedure1(MouseEvent mouseEvent) {
        int[] pos = onMouseClickForAll(mouseEvent, gridPaneProcedure1);
        if(pos[0] == -1 || pos[1] == -1){
            return;
        }
        int calPos = pos[0] * 4 + pos[1];
        // if 0, 0 then no need to check insert
        if(calPos == 0 || hasElementOnPrevPos(new Position(pos[0], pos[1]), imageViewsProcedure1)){
            if(isLeftClick){
                gui.setProgramImage(pos[0], pos[1], prevButton.ordinal() + 1, 2);
            } else {
                removeMoveFromProgram(getProgramProcedureImageView(ProcedureProgram.PROCEDRE1_TO_SELECT), new Position(pos[1], pos[0]));
            }
        }
    }

    /**
     * On Mouse Click Procedure2
     * get the clicked coordinates calculate the position in 1d
     * set the Instruction if the leftClick is true, else if right clicked and has
     * a Instruction on that point then remove the instruction from Clicked position,
     * shift all by one so there is no remaining slot
     * return if the clicked position is between which gives -1 and -1 as return
     * @param mouseEvent event
     */
    public void onMouseClickProcedure2(MouseEvent mouseEvent) {
        int[] pos = onMouseClickForAll(mouseEvent, gridPaneProcedure2);
        if(pos[0] == -1 || pos[1] == -1){
            return;
        }
        int calPos = pos[0] * 4 + pos[1];
        if(calPos == 0 || hasElementOnPrevPos(new Position(pos[0], pos[1]), imageViewsProcedure2)){
            if(isLeftClick){
                gui.setProgramImage(pos[0], pos[1], prevButton.ordinal() + 1, 3);
            } else {
                removeMoveFromProgram(getProgramProcedureImageView(ProcedureProgram.PROCEDRE2_TO_SELECT), new Position(pos[1], pos[0]));
            }
        }
    }

    /**
     * Used to check if there is element on the prev position for the given imageView
     * given element can be any other then normal
     * @param position position to be check
     * @param imageViewsProgramProcedure imageView
     * @return true if has element on prev position else false
     */
    private boolean hasElementOnPrevPos(Position position, ImageView[][] imageViewsProgramProcedure) {
        Position p = position.getPreviousPos(imageViewsProgramProcedure.length);
        return !(imageViewsProgramProcedure[p.getCol()][p.getRow()].getImage().getUrl().equals(NORMAL_IMG.getUrl()));
    }

    /**
     * Called when clicked on the field
     * return if the clicked position is -1 -1 which will be in the case
     * it is clicked between lines
     * @param mouseEvent event
     */
    public void onMouseClickedField(MouseEvent mouseEvent) throws Exception {
        int[] pos = onMouseClickForAll(mouseEvent, gridPaneField);
        if(pos[0] == -1 || pos[1] == -1){
            return;
        }
        if(!gui.animationIsRunning){
            game.handleOnField(pos[0], pos[1], prevButtonChar);
        }
    }

    /**
     * Called ON BUTTON CLICK
     * Used to set the game to editor mode
     * in editor mode the map is created by the user,
     * Procedures and programs will be disabled because in editorMode
     * the Instructions can't be added to program and procedures
     *
     * Top button is selected every time when it is set back to editor mode
     */
    public void setToEditorMode() {
        gui.setToCreateMode();
        toEditorButton.setDisable(true);
        if(!gui.animationIsRunning){
            borderPaneMain.getRight().setDisable(true);
            borderPaneMain.getCenter().setDisable(false);
        }

        toSolveModeButton.setDisable(false);
        game.setCreateMode(true);

        setPrevButton(1);
        setEffectForCreateButton(0);
    }

    /**
     * Called ON BUTTON CLICK
     * Used to set the gui and game to solve mode
     * In Solve Mode the gui is set to solve mode
     */
    public void setToSolveMode() {
        gui.setToSolveMode();
        toSolveModeButton.setDisable(true);

        if(!gui.animationIsRunning){
            borderPaneMain.getRight().setDisable(false);
            borderPaneMain.getCenter().setDisable(true);
        }
        toEditorButton.setDisable(false);
        game.setCreateMode(false);

        setPrevButton(1);
        setEffectForLeftSolveButton(0);
    }

    /**
     * Called ON BUTTON CLICK
     * Used to solve the current map
     */
    public void solveCurrentLevel() throws Exception {
        game.handleSolveLevel();
    }

    /**
     * Called ON BUTTON CLICK
     * Called When start game is pressed
     */
    public void startGame() throws Exception {
        stopButton.setVisible(true);
        Instructions[] programMoves = getProgramProcedures(getProgramProcedureImageView(ProcedureProgram.PROGRAM_TO_SELECT));
        game.handleStartGame(programMoves, getProgramProcedures(getProgramProcedureImageView(ProcedureProgram.PROCEDRE1_TO_SELECT)),
                getProgramProcedures(getProgramProcedureImageView(ProcedureProgram.PROCEDRE2_TO_SELECT)));
    }

    /**
     * Used to get the imageViews for the corresponding
     * @param procedureProgram procedure or program
     * @return imageView for the given procedure or program
     */
    ImageView[][] getProgramProcedureImageView(ProcedureProgram procedureProgram){
        ImageView[][] toReturn = null;
        if(procedureProgram == ProcedureProgram.PROGRAM_TO_SELECT){
            toReturn = imageViewsProgram;
        } else if(procedureProgram == ProcedureProgram.PROCEDRE1_TO_SELECT){
            toReturn = imageViewsProcedure1;
        } else if(procedureProgram == ProcedureProgram.PROCEDRE2_TO_SELECT){
            toReturn = imageViewsProcedure2;
        }
        return toReturn;
    }

    /**
     * Used to get program, Procedure1 or Procedure2 moves from given imageViews
     * Used to generate program and procedure from the gui
     * @return moves for the given program or procedure
     */
    Instructions[] getProgramProcedures(ImageView[][] toCheck){
        Instructions[] toReturn;

        int count = 0;
        int rows = toCheck[0].length;
        int cols = toCheck.length;
        toReturn = new Instructions[rows * cols];

        for(int i = 0; i < rows; i++){
            for (ImageView[] imageViews : toCheck) {
                String url = imageViews[i].getImage().getUrl();
                if (url.equals(FORWARD_IMG.getUrl())) {
                    toReturn[count] = FORWARD;
                } else if (url.equals(JUMP_IMG.getUrl())) {
                    toReturn[count] = JUMP;
                } else if (url.equals(CLOCKWISE_IMG.getUrl())) {
                    toReturn[count] = CLOCKWISE;
                } else if (url.equals(ANTI_CLOCK_IMG.getUrl())) {
                    toReturn[count] = ANTI_CLOCK;
                } else if (url.equals(EXIT_IMG.getUrl())) {
                    toReturn[count] = EXIT;
                } else if (url.equals(PROCEDURE1_IMG.getUrl())) {
                    toReturn[count] = PROCEDURE1;
                } else if (url.equals(PROCEDURE2_IMG.getUrl())) {
                    toReturn[count] = PROCEDURE2;
                }
                count++;
            }
        }
        return toReturn;
    }

    /**
     * Used to remove Instruction from the given program and shift all by one
     * @param toCheck imageViews from them the move is removed
     * @param position position position to remove from
     */
    public void removeMoveFromProgram(ImageView[][] toCheck, Position position){
        boolean toStop = false;
        int rowToChk = 0;
        int colToChk = 0;
        int rows = toCheck[0].length;
        int cols = toCheck.length;
        int startCol = position.getRow();
        String normalImageUrl = NORMAL_IMG.getUrl();
        for(int i = position.getCol(); i < rows && !toStop; i++) {
            for (int j = startCol; j < cols && !toStop; j++) {
                Position nextPos = new Position(i, j);
                nextPos = nextPos.getNextPos(rows, cols);
                if(toCheck[j][i].getImage().getUrl().equals(normalImageUrl)){
                    toStop = true;
                }

                if(nextPos == null){
                    toStop = true;
                } else {
                    colToChk = nextPos.getCol();
                    rowToChk = nextPos.getRow();
                }

                startCol = 0;
                if(i == rows - 1 && j == cols - 1){
                    toCheck[j][i].setImage(NORMAL_IMG);
                } else if(!toStop){
                    toCheck[j][i].setImage(toCheck[colToChk][rowToChk].getImage());
                } else {
                    toCheck[j][i].setImage(NORMAL_IMG);
                }
            }
        }
    }

    /**
     * Called ON BUTTON CLICK
     * Called when left side solve buttons are clicked like FORWARD, ROTATE, JUMP etc
     * @param mouseEvent event
     */
    public void onMouseClickLeftSolve(MouseEvent mouseEvent) {
        int[] pos = onMouseClickForAll(mouseEvent, gridPaneLeftSolve);
        if(pos[0] == -1 || pos[1] == -1){
            return;
        }
        setPrevButton(pos[0] + 1);
        setEffectForLeftSolveButton(pos[0]);
    }

    /**
     * Used to call when left side grid is clicked having create buttons,
     * Like BOT, OBJECT, WALL etc
     * @param mouseEvent event
     */
    public void onMouseClickLeftCreate(MouseEvent mouseEvent) {
        int[] pos = onMouseClickForAll(mouseEvent, gridPaneLeftCreate);
        if(pos[0] == -1 || pos[1] == -1){
            return;
        }
        setPrevButton(pos[0] + 1);
        setEffectForCreateButton(pos[0]);
    }

    /**
     * Effect for create Button
     * Set effect for given button and clear all other
     * @param button button to set effect
     */
    public void setEffectForCreateButton(int button){
        for(int i = 0; i < 6; i++){
            imageViewsLeftCreate[0][i].setEffect(null);
        }
        imageViewsLeftCreate[0][button].setEffect(new InnerShadow(null, RED, 2, 0.8, 0, 0));
    }

    /**
     * Effect for solveButton
     * Set effect for given button and clear all other
     * @param buttonNum button to set effect
     */
    public void setEffectForLeftSolveButton(int buttonNum){
        for(int i = 0; i < 7; i++){
            imageViewsLeftSolve[0][i].setEffect(null);
        }
        imageViewsLeftSolve[0][buttonNum].setEffect(new InnerShadow(null, RED, 2, 0.8, 0, 0));
    }

    /**
     * TO SET THE BUTTONS
     * Used to set the previous button
     * Will be set when user pressed any left side button
     * @param buttonNum to be set
     */
    private void setPrevButton(int buttonNum){
        if(buttonNum == 1){
            prevButton = FORWARD;
            prevButtonChar = PIT;
        } else if(buttonNum == 2){
            prevButton = CLOCKWISE;
            prevButtonChar = COIN;
        } else if(buttonNum == 3){
            prevButton = ANTI_CLOCK;
            prevButtonChar = DOOR;
        } else if(buttonNum == 4){
            prevButton = JUMP;
            prevButtonChar = NORMAL;
        } else if(buttonNum == 5){
            prevButton = PROCEDURE1;
            prevButtonChar = BOT_FIELD_TYPE;
        } else if(buttonNum == 6){
            prevButton = PROCEDURE2;
            prevButtonChar = GRAY_WALL;
        } else if(buttonNum == 7){
            prevButton = EXIT;
        }
    }

    /**
     * Called ON BUTTON CLICK
     */
    public void setToSlowSpeed() {
        gui.setAnimationTime(Speed.SLOW_SPEED);
    }

    /**
     * Called ON BUTTON CLICK
     */
    public void setToMediumSpeed() {
        gui.setAnimationTime(Speed.MEDIUM_SPEED);
    }

    /**
     * Called ON BUTTON CLICK
     */
    public void setToFastSpeed() {
        gui.setAnimationTime(Speed.FAST_SPEED);
    }

    /**
     * Called ON BUTTON CLICK
     */
    public void setToVeryFastSpeed() {
        gui.setAnimationTime(Speed.VERY_FAST_SPEED);
    }

    /**
     * Set to No Animation
     */
    public void setToNoAnimationSpeed() {
        gui.setAnimationTime(Speed.NO_ANIMATION_SPEED);
    }

    /**
     * Called ON BUTTON CLICK
     * @throws Exception exception
     */
    public void onNewMap() throws Exception {
        if(!gui.animationIsRunning){
            game.setMap(null);
        }
    }

    /**
     * Called ON BUTTON CLICK
     * @throws Exception exception
     */
    public void onMap1() throws Exception {
        if(!gui.animationIsRunning){
            game.setMap(0);
        }
    }

    /**
     * Called ON BUTTON CLICK
     * @throws Exception exception
     */
    public void onMap2() throws Exception {
        if(!gui.animationIsRunning){
            game.setMap(1);
        }
    }

    /**
     * Called ON BUTTON CLICK
     * @throws Exception exception
     */
    public void onMap3() throws Exception {
        if(!gui.animationIsRunning){
            game.setMap(2);
        }
    }

    /**
     * Called ON BUTTON CLICK
     * @throws Exception exception
     */
    public void onMap4() throws Exception {
        if(!gui.animationIsRunning){
            game.setMap(3);
        }
    }

    /**
     * Called ON BUTTON CLICK
     */
    public void clearProgram() {
        gui.setClearProgram();
    }

    /**
     * Called ON BUTTON CLICK
     */
    public void clearProcedure1() {
        gui.setClearProcedure1();
    }

    /**
     * Called ON BUTTON CLICK
     */
    public void clearProcedure2() {
        gui.setClearProcedure2();
    }

    /**
     * //https://stackoverflow.com/questions/320542/how-to-get-the-path-of-a-running-jar-file
     * Save the file
     */
    public void onSaveFile() {
        if(!gui.animationIsRunning){
            String content = game.saveFile();
            File currDir = null;
            try {
                currDir = new File(FXMLDocumentController.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI());
            } catch (URISyntaxException ex) {
                //oops... ¯\_(ツ)_/¯
                //guess we won't be opening the dialog in the right directory
            }
            //Step 2: Put it together
            FileChooser fileChooser = new FileChooser();
            if (currDir != null) {
                //ensure the dialog opens in the correct directory
                fileChooser.setInitialDirectory(currDir.getParentFile());
            }
            fileChooser.setTitle("Open JSON File");
            //Step 3: Open the Dialog (set window owner, so nothing in the original window
            //FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
//            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt"));
            File selectedFile = fileChooser.showSaveDialog(mainAchorPane.getScene().getWindow());
            if(selectedFile != null){
                try {
                    PrintWriter printWriter = new PrintWriter(selectedFile);
                    printWriter.write(content);
                    printWriter.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Used to load the file
     * Load the file
     */
    public void onLoadFile() {
        if(!gui.animationIsRunning){
            File currDir = null;
            try {
                currDir = new File(FXMLDocumentController.class.getProtectionDomain()
                        .getCodeSource().getLocation().toURI());
            } catch (URISyntaxException ex) {
                //oops... ¯\_(ツ)_/¯
                //guess we won't be opening the dialog in the right directory
            }
            //Step 2: Put it together
            FileChooser fileChooser = new FileChooser();
            if (currDir != null) {
                //ensure the dialog opens in the correct directory
                fileChooser.setInitialDirectory(currDir.getParentFile());
            }
            fileChooser.setTitle("Open JSON Graph-File");
            fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt"));
            File selectedFile = fileChooser.showOpenDialog(mainAchorPane.getScene().getWindow());
            if(selectedFile != null){
                Gson gson = new Gson();
                try (Reader reader = new FileReader(selectedFile)) {
                    // Convert JSON File to Java Object
                    SavingFile staff = gson.fromJson(reader, SavingFile.class);
                    /////////////////////////////////////////////
//                    JsonParser jsonpar = new JsonParser();
//                    Object obj = jsonpar.parse(reader);
//                    SavingFile save = (SavingFile) obj;
//
//                    System.out.println("DATA IS READY ................................");
//                    System.out.println(save.botRotation);
                    /////////////////////////////////////////////
                    game.loadingFile(staff);
                }  catch (IOException e) {
                    game.setBotStartPosStartRot();
                    gui.notValidGeneral("IO EXCEPTION ");
                } catch (JsonIOException ex){
                    game.setBotStartPosStartRot();
                    gui.notValidGeneral("PARSING Error, Not in a proper format ");
                } catch (JsonSyntaxException e){
                    game.setBotStartPosStartRot();
                    gui.notValidGeneral("Syntax Exception ....");
                } catch (Exception e) {
                    game.setBotStartPosStartRot();
                    gui.notValidGeneral("NOT VALID LOADED MAP ....");
                }
            }
        }
    }
}