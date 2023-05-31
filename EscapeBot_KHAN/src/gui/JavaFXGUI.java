package gui;

import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import logic.*;

import java.util.LinkedList;

import static javafx.scene.paint.Color.*;
import static logic.Actions.*;
import static logic.FieldType.BOT_FIELD_TYPE;

/**
 * JavaFXGUI
 */
public class JavaFXGUI implements GUIConnector {

    /**
     * TOTAL cols in the game
     */
    final static int COLS = 8;

    /**
     * Used to store stop button state
     * Will be set true if presed
     * and animation will be stoped and was cleared by clearButton
     */
    private boolean stopButtonIsPressed = false;

    /**
     * Keep track if animation is running or not
     */
    boolean animationIsRunning = false;

    /**
     * Procedure rows and cols
     */
    final static int PROCEDURE_ROWS = 2;
    final static int PROCEDURE_COLS = 4;

    /**
     * Start Animation speed
     */
    private int animationTime = 1500;

    /**
     * bot imageView to be moved on the board
     */
    ImageView bot = new ImageView(BOT_IMG);

    /**
     * Load all the images used in the game
     */
    public static final Image FORWARD_IMG = new Image("gui/img/Forward.png");
    public static final Image JUMP_IMG = new Image("gui/img/Jump.png");
    public static final Image NORMAL_IMG = new Image("gui/img/Normal.png");
    public static final Image ANTI_CLOCK_IMG = new Image("gui/img/Anticlock.png");
    public static final Image CLOCKWISE_IMG = new Image("gui/img/Clockwise.png");
    public static final Image BOT_IMG = new Image("gui/img/Bot.png");
    public static final Image BOT_WITH_BACKGROUND = new Image("gui/img/botWithBackground.png");

    public static final Image EXIT_IMG = new Image("gui/img/Exit.png");
    public static final Image OBJECT_IMG = new Image("gui/img/Object.png");
    public static final Image PROCEDURE1_IMG = new Image("gui/img/Procedure1.png");
    public static final Image PROCEDURE2_IMG = new Image("gui/img/Procedure2.png");
    public static final Image WALL_IMG = new Image("gui/img/Wall.png");
    public static final Image DOOR_IMG = new Image("gui/img/Door.png");
    public static final Image GREY_WALL_IMG = new Image("gui/img/Graywall.png");


    /**
     * Store Prev Program position, prevPro1Pos and prevPro2Pos
     */
    Position prevProgramPos = new Position(0, 0);
    Position prevPro1Pos = new Position(0, 0);
    Position prevPro2Pos = new Position(0, 0);

    /**
     * Used to store prev Program Positions
     * Will be cleared when animation is done
     */
    LinkedList<Integer> prevProgramProcedure = new LinkedList<>();

    /**
     * imageViews for the Field, Create and Solve
     */
    ImageView[][] imageViewsMainField;
    ImageView[][] imageViewsLeftCreate;
    ImageView[][] imageViewsLeftSolve;

    /**
     * ImageViews for the Program and procedures
     */
    ImageView[][] imageViewsProgram;
    ImageView[][] imageViewsProcedure1;
    ImageView[][] imageViewsProcedure2;

    /**
     * Main Field GridPane
     */
    GridPane gridPaneMainField;

    /**
     * FOr the Create and Solve
     */
    GridPane gridPaneLeftCreate;
    GridPane gridPaneLeftSolve;

    /**
     * Message Label
     * Used to show message to the user
     * can be an error message or success
     */
    Label messageLabel;

    /**
     * BorderPane main Field,
     * Used to disable and enable procedures, Main Field
     * and leftGridPane for solve and create containing buttons
     * can't be used for disabling other buttons like toEdit and solve etc
     * remaining buttons are disabled individually
     */
    BorderPane borderPaneMain;

    /**
     * Menu bar containing animation options and all
     * used to disable and enable
     */
    MenuBar menuBar;

    /**
     * Start button, used to execute the instructions on the field
     * start animation on the field, trigger method inside the logic
     * "handleStartButton"
     */
    Button startButton;

    /**
     * Solve button,
     * Used to get the solution for the current map
     * trigger the method in the logic game
     * "handleSolveLevel()"
     */
    Button solveButton;

    Button clearButton;

    /**
     * Constructor for the javaFxGui
     * @param gridPaneMainField gridPane main Field
     * @param gridPaneLeftCreate gridPane Left for the buttons of create
     * @param gridPaneLeftSolve girdPane solve for the buttons of solve
     * @param imageViewsMainField imageViews main field containing all the imageViews on the filed, except bot
     * @param imageViewsLeftSolve imageViews For the left Solve
     * @param imageViewsLeftCreate imageViews for the left Create
     * @param imageViewProgram imageViews for the program
     * @param imageViewProcedure1 imageViews for the procedure1
     * @param imageViewProcedure2 imageViews for the procedure2
     * @param messageLabel message label to show message
     * @param borderPaneMain borderPane main, used to disable and enable left right center during animation
     *                       and after the clearbutton is clicked to reset the field
     * @param menuBar menu bar for disabling and enabling the menu, disabled when animation is started and enabled
     *                when clearButton is clicked to reset the field (Clear button event is called in Controller)
     * @param startButton startButton to start game,
     * @param solveButton solve to solve the level of the game
     */
    public JavaFXGUI(GridPane gridPaneMainField, GridPane gridPaneLeftCreate, GridPane gridPaneLeftSolve,
                     ImageView[][] imageViewsMainField, ImageView[][] imageViewsLeftSolve, ImageView[][] imageViewsLeftCreate,
                     ImageView[][] imageViewProgram, ImageView[][] imageViewProcedure1, ImageView[][] imageViewProcedure2,
                     Label messageLabel, BorderPane borderPaneMain, MenuBar menuBar, Button startButton, Button solveButton,
                     Button clearButton){

        this.startButton = startButton;
        this.solveButton = solveButton;
        this.clearButton = clearButton;

        // GridPanes
        this.gridPaneMainField = gridPaneMainField;
        this.gridPaneLeftCreate = gridPaneLeftCreate;
        this.gridPaneLeftSolve = gridPaneLeftSolve;

        // ImageViews
        this.imageViewsMainField = imageViewsMainField;
        this.imageViewsLeftCreate = imageViewsLeftCreate;
        this.imageViewsLeftSolve = imageViewsLeftSolve;
        this.imageViewsProgram = imageViewProgram;
        this.imageViewsProcedure1 = imageViewProcedure1;
        this.imageViewsProcedure2 = imageViewProcedure2;

        //BorderPane main
        this.borderPaneMain = borderPaneMain;
        this.menuBar = menuBar;

        this.messageLabel = messageLabel;

        // create starting game field
        initializeField();
        setToSolveMode();
    }

    /**
     * Setter for stopButton
     */
    void setStopButtonIsPressed() {
        this.stopButtonIsPressed = true;

        clearButton.setDisable(false);
    }

    /**
     * Private method for initializing the field
     */
    private void initializeField(){
        setToNewGame();
        setClearProgram();
        setClearProcedure1();
        setClearProcedure2();
    }

    /**
     * Clear The Program
     */
    @Override
    public void setClearProgram() {
        for (ImageView[] imageViews : imageViewsProgram) {
            for (ImageView imageView : imageViews) {
                imageView.setImage(NORMAL_IMG);
                imageView.setEffect(null);
            }
        }
    }

    /**
     * User to clear Effect from program
     */
    public void clearEffectsFromProgram(){
        for (ImageView[] imageViews : imageViewsProgram) {
            for (ImageView imageView : imageViews) {
                imageView.setEffect(null);
            }
        }
    }

    /**
     * clear Procedure1
     */
    @Override
    public void setClearProcedure1(){
        for (ImageView[] imageViews : imageViewsProcedure1) {
            for (ImageView imageView : imageViews) {
                imageView.setImage(NORMAL_IMG);
                imageView.setEffect(null);
            }
        }
    }

    /**
     * User to clear Effect from procedure1
     */
    public void clearEffectsProcedure1(){
        for (ImageView[] imageViews : imageViewsProcedure1) {
            for (ImageView imageView : imageViews) {
                imageView.setEffect(null);
            }
        }
    }

    /**
     * clear Procedure2
     */
    @Override
    public void setClearProcedure2(){
        for(int i = 0; i < imageViewsProcedure1.length; i++){
            for(int j = 0; j < imageViewsProcedure1[i].length; j++){
                imageViewsProcedure2[i][j].setImage(NORMAL_IMG);
                imageViewsProcedure2[i][j].setEffect(null);
            }
        }
    }

    /**
     * User to clear Effect from procedure2
     */
    public void clearEffectProcedure2(){
        for(int i = 0; i < imageViewsProcedure1.length; i++){
            for(int j = 0; j < imageViewsProcedure1[i].length; j++){
                imageViewsProcedure2[i][j].setEffect(null);
            }
        }
    }

    /**
     * Set to New game
     */
    @Override
    public void setToNewGame() {
        for (ImageView[] imageViews : imageViewsMainField) {
            for (ImageView imageView : imageViews) {
                imageView.setImage(NORMAL_IMG);
            }
        }
    }

    /**
     * Set the given field on the gui
     * @param map map to to be set on the gui
     */
    @Override
    public void setMapOnGui(FieldType[][] map) {
        removeBotFrom();
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[0].length; j++){
                setImage(map[i][j], i, j);
            }
        }
    }

    /**
     * Used to set the image to program or procedure1 or procedure2
     * @param col column
     * @param row row
     * @param imageNum imagenumber
     * @param programProcedure program procedure to be set
     */
    @Override
    public void setProgramImage(int col, int row, int imageNum, int programProcedure){
        if(programProcedure == 1){
            imageViewsProgram[row][col].setImage(getImageByNum(imageNum, false));
        } else if(programProcedure == 2){
            imageViewsProcedure1[row][col].setImage(getImageByNum(imageNum, false));
        } else {
            imageViewsProcedure2[row][col].setImage(getImageByNum(imageNum, false));
        }
    }


    /**
     * Used to set the image on the field
     * @param col Col
     * @param row Row
     * @param imageNum image Number
     */
    @Override
    public void setFieldImage(int col, int row, int imageNum, int rot){
        if(imageNum == 5){
            setBotOnGui(new Position(col, row));
        } else {
            imageViewsMainField[row][col].setImage(getImageByNum(imageNum, true));
        }
    }

    /**
     * Is called when Actions are performed on the screen
     *       Only pass the starting position of the bot and the starting rotation
     *       and All actions and from those actions next position of the bot is
     *       derived
     *       Position class can be used to to find the neighbours of the position
     *       and can help to find the position of the ot according to the actions
     * @param allActions actions
     */
    @Override
    public void handleActions(Position botPos, int rot, LinkedList<Actions> allActions) {
        prevProgramProcedure.add(1);
        // clear effects if there is any remaining
        clearEffectsFromProgram();
        clearEffectsProcedure1();
        clearEffectProcedure2();
        // before calling handleActionWithAnimation1 set all the positions to 0, 0
        prevProgramPos = new Position(0, 0);
        prevPro1Pos = new Position(0, 0);
        prevPro2Pos = new Position(0, 0);
        // clear Effect from all the program and procedures
        animationIsRunning = true;
        setForAnimationIsRunning();
        handleActionWithAnimation(botPos, rot, allActions);
    }

    /**
     * set for Animation is running
     */
    private void setForAnimationIsRunning(){
        if(animationIsRunning){
            startButton.setDisable(true);
            solveButton.setDisable(true);
            borderPaneMain.getRight().setDisable(true);
        }
    }

    /**
     * set for animation in finished
     */
    private void setForAnimationIsFinish(){
        if(!animationIsRunning){
            startButton.setDisable(false);
            solveButton.setDisable(false);
        }
    }

    /**
     * Used to handle Actions for animation
     * @param botPosition bot Position to start with
     * @param rot rotation
     * @param allActions all actions
     */
    void handleActionWithAnimation(Position botPosition, int rot, LinkedList<Actions> allActions) {
        Position nextPos;
        Actions a = allActions.getFirst();
        if(a == COLLECT_COIN){
            imageViewsMainField[botPosition.getCol()][botPosition.getRow()].setImage(NORMAL_IMG);
            allActions.removeFirst();
            a = allActions.getFirst();
        }

        // for setting mark of the running procedure or program
        if(prevProgramProcedure.getLast() == 1){
            // my current position to glow, clear all other
            setBorderForProcedureProgram(imageViewsProgram, prevProgramPos);
            // get next position and set this to prevProgram position
            prevProgramPos = prevProgramPos.getNextPos(imageViewsProgram[0].length, imageViewsProgram.length);
            if(a == START_P1){
                prevProgramProcedure.add(2);
            } else if(a == START_P2){
                prevProgramProcedure.add(3);
            }
        } else if(prevProgramProcedure.getLast() == 2){
            if(a != END_P1){
                // mark current position
                setBorderForProcedureProgram(imageViewsProcedure1, prevPro1Pos);
                // get next position and set to this prevProcedure1Position
                prevPro1Pos = prevPro1Pos.getNextPos(imageViewsProcedure1[0].length, imageViewsProcedure1.length);
            }
            if(a == END_P1){
                prevProgramProcedure.removeLast();
                // clear the last position border
                Position pos = prevPro1Pos.getPreviousPos(PROCEDURE_COLS);
                if(pos != null){
                    clearBorderFrom(imageViewsProcedure1, pos);
                }
                // set the prev position for program1 to 0, 0
                prevPro1Pos = new Position(0, 0);
            } else if(a == START_P2){
                prevProgramProcedure.add(3);
            }
        } else if(prevProgramProcedure.getLast() == 3){
            if(a != END_P2){
                setBorderForProcedureProgram(imageViewsProcedure2, prevPro2Pos);
                prevPro2Pos = prevPro2Pos.getNextPos(PROCEDURE_ROWS, PROCEDURE_COLS);
            }
            if(a == END_P2){
                prevProgramProcedure.removeLast();
                // clear border
                Position pos = prevPro2Pos.getPreviousPos(PROCEDURE_COLS);
                if(pos != null){
                    clearBorderFrom(imageViewsProcedure2, prevPro2Pos.getPreviousPos(PROCEDURE_COLS));
                }
                // set position for prevProcedure2Position to 0, 0
                prevPro2Pos = new Position(0, 0);
            } else if(a == START_P1){
                prevProgramProcedure.add(2);
            }
        }

        if(a == START_P1 || a == START_P2 || a == END_P1 || a == END_P2){
            allActions.removeFirst();
            handleActionWithAnimation(botPosition, rot, allActions);
        }

        ////////////////////////// AFTER ////////////////////////
        /////////////////////////////////////////////////////////
        if(a == MOVE_IN_DIRECTION || a == EXIT_THROUGH){
            nextPos = botPosition.getNextPosition(rot);
            setAnimation1(nextPos, allActions, rot, false, false);
            if(a == EXIT_THROUGH){
                imageViewsMainField[nextPos.getCol()][nextPos.getRow()].setImage(NORMAL_IMG);
                notValidGeneral("LEVEL SOLVED ...");
//                messageLabel.setText("LEVEL SOLVED ...");
//                setDisableAllTillClear(true);
            }
        } else if(a == CLOCK_WISE){
            if(rot == 3){
                rot = 0;
            } else rot++;
            setAnimation1(botPosition, allActions, rot, true, false);
        } else if(a == ANTI_CLOCKWISE){
            if(rot == 0){
                rot = 3;
            } else rot--;
            setAnimation1(botPosition, allActions, rot, false, true);
        } else if(a == JUMP_THROUGH){
            nextPos = botPosition.getNextPosition(rot);
            nextPos = nextPos.getNextPosition(rot);
            setAnimation1(nextPos, allActions, rot, false, false);

        } else if(isAnError(a)){
            notValidGeneral(a.toString());
            setForAnimationIsFinish();
        }
    }



    /**
     * Used to check if the given action is an error or not
     * @param action given action
     * @return true if it is type of an error
     */
    boolean isAnError(Actions action){
        return (action == BOT_RUN_OUT_OF_FIELD || action == BOT_RUN_TO_PIT || action == BOT_RUN_TO_WALL || action == BOT_EXIT_WITHOUT_COINS
                || action == BOT_NOT_FACING_DOOR || action == BOT_RUN_TO_DOOR || action == BOT_JUMP_OFF_FIELD ||
                action == BOT_JUMP_ON_WALL || action == BOT_JUMP_ON_NORMAL || action == BOT_JUMP_ON_COIN || action == BOT_JUMP_ON_TWO_PIT
                || action == BOT_JUMP_ONTO_DOOR || action == BOT_JUMP_ONTO_WALL || action == THROUGH_ERR);
    }

    /**
     * Used to set the Border of the running procedure or program
     * @param position position where the border to be set for the given imageView
     */
    void setBorderForProcedureProgram(ImageView[][] imageViews, Position position){
        setBorderOnPos(imageViews, position);
        Position prev = position.getPreviousPos(imageViews.length);
        clearBorderFrom(imageViews, prev);
    }

    /**
     * Used to set border color
     * @param imageViews for ImageView
     * @param position to position
     */
    private void setBorderOnPos(ImageView[][] imageViews, Position position){
        imageViews[position.getCol()][position.getRow()].
                setEffect(new InnerShadow(null, BLUE, 2, 0.8, 0, 0));
    }

    /**
     * Used to clear border from given position from the given imageView
     * @param imageViews imageViews
     * @param position position
     */
    private void clearBorderFrom(ImageView[][] imageViews, Position position){
        if(position != null){
            imageViews[position.getCol()][position.getRow()].
                    setEffect(null);
        }
    }

    /**
     * Used to set Image
     * @param fieldType mapChar
     * @param row row
     * @param col col
     */
    public void setImage(FieldType fieldType, int row, int col){
        if(fieldType == FieldType.PIT){
            imageViewsMainField[col][row].setImage(WALL_IMG);
        } else if(fieldType == FieldType.COIN){
            imageViewsMainField[col][row].setImage(OBJECT_IMG);
        } else if(fieldType == FieldType.DOOR){
            imageViewsMainField[col][row].setImage(DOOR_IMG);
        } else if(fieldType == FieldType.NORMAL){
            imageViewsMainField[col][row].setImage(NORMAL_IMG);
        } else if(fieldType == BOT_FIELD_TYPE){
            setBotOnGui(new Position(row, col));
        } else {
            imageViewsMainField[col][row].setImage(GREY_WALL_IMG);
        }
    }

    /**
     * Used to set the animation time for the bot
     * @param speed speed to be set for the animation
     */
    void setAnimationTime(FXMLDocumentController.Speed speed){
        if(speed == FXMLDocumentController.Speed.SLOW_SPEED){
            animationTime = 1500;
        } else if(speed == FXMLDocumentController.Speed.MEDIUM_SPEED){
            animationTime = 1000;
        } else if(speed == FXMLDocumentController.Speed.FAST_SPEED){
            animationTime = 500;
        } else if(speed == FXMLDocumentController.Speed.VERY_FAST_SPEED){
            animationTime = 200;
        } else {
            animationTime = 1;
        }
    }

    /**
     * Used to change the bot rotation at the given position
     * @param rot rotation to be set
     */
    public void changeBotRot(int rot){
        bot.setRotate(90 * rot);
    }

    /**
     * Used to get Image by number
     * @param imageNum Number of imag
     * @param createMode true then fieldType image is
     *                   returned else Instructions
     * @return image by number
     */
    private Image getImageByNum(int imageNum, boolean createMode){
        if(imageNum == 1){
            return createMode ? WALL_IMG : FORWARD_IMG;
        } else if(imageNum == 2){
            return createMode ? OBJECT_IMG : CLOCKWISE_IMG;
        } else if(imageNum == 3){
            return createMode ? DOOR_IMG : ANTI_CLOCK_IMG;
        } else if(imageNum == 4){
            return createMode ? NORMAL_IMG : JUMP_IMG;
        } else if(imageNum == 5){
            return createMode ? BOT_IMG : PROCEDURE1_IMG;
        } else if(imageNum == 6){
            return createMode ? GREY_WALL_IMG : PROCEDURE2_IMG;
        } else if(imageNum == 7 && !createMode){
            return EXIT_IMG;
        }
        return null;
    }

    /**
     * Used to set the left GridPane for create mode
     */
    void setToCreateMode(){
        gridPaneLeftCreate.setVisible(true);
        imageViewsLeftCreate[0][0].setImage(WALL_IMG);
        imageViewsLeftCreate[0][1].setImage(OBJECT_IMG);
        imageViewsLeftCreate[0][2].setImage(DOOR_IMG);
        imageViewsLeftCreate[0][3].setImage(NORMAL_IMG);
        imageViewsLeftCreate[0][4].setImage(BOT_WITH_BACKGROUND);
        imageViewsLeftCreate[0][5].setImage(GREY_WALL_IMG);

        gridPaneLeftSolve.setVisible(false);
    }

    /**
     * Used to set to solve Mode
     */
    public void setToSolveMode(){
        gridPaneLeftSolve.setVisible(true);

        imageViewsLeftSolve[0][0].setImage(FORWARD_IMG);
        imageViewsLeftSolve[0][1].setImage(CLOCKWISE_IMG);
        imageViewsLeftSolve[0][2].setImage(ANTI_CLOCK_IMG);
        imageViewsLeftSolve[0][3].setImage(JUMP_IMG);
        imageViewsLeftSolve[0][4].setImage(PROCEDURE1_IMG);
        imageViewsLeftSolve[0][5].setImage(PROCEDURE2_IMG);
        imageViewsLeftSolve[0][6].setImage(EXIT_IMG);

        gridPaneLeftCreate.setVisible(false);

    }

    /**
     * setAnimation, Execute animation, wait for toFinish the animation and call
     * handleActionWithAnimation1 again if animation is not finished or no error occured
     * @param endPos Bot ending position after animation is finished
     * @param actions action to be executed
     * @param rot rotation of the bot, used to find the direction element which side
     *            the bot should be moving
     */
    void setAnimation1(Position endPos, LinkedList<Actions> actions, int rot, boolean isClockWise, boolean isAnticlockwise){
        int valueForJum = 1;
        Duration ANIMATION_DURATION = Duration.millis(animationTime);
        if(actions.getFirst() == JUMP_THROUGH){
            valueForJum = 2;
            ANIMATION_DURATION = Duration.millis(animationTime * 2);
        } else if(actions.getFirst() == CLOCK_WISE || actions.getFirst() == ANTI_CLOCKWISE){
            valueForJum = 0;
        }

        final int colWidth = (int) (gridPaneMainField.getWidth() / COLS);
        final int colHeight = (int) (gridPaneMainField.getHeight() / COLS);

        TranslateTransition t = new TranslateTransition(ANIMATION_DURATION, bot);
        RotateTransition rt = new RotateTransition(ANIMATION_DURATION, bot);

        if(rot == 0){
            t.byXProperty().set(valueForJum * colWidth);
        } else if(rot == 1){
            t.byYProperty().set(valueForJum * colHeight);
        } else if(rot == 2){
            t.byXProperty().set(valueForJum * (-1) * colWidth);
        } else if(rot == 3){
            t.byYProperty().set(valueForJum * (-1) * colHeight);
        }

        if(isClockWise){
            rt.setByAngle(90);
            rt.play();
        } else if(isAnticlockwise){
            rt.setByAngle(-90);
            rt.play();
        }

        t.play();

        t.onFinishedProperty().set((ActionEvent actionEvent) -> {

            setImageViewTo(bot, endPos.getCol(), endPos.getRow());

            Actions a = actions.removeFirst();
            if(!stopButtonIsPressed && actions.size() > 0){
                handleActionWithAnimation(endPos, rot, actions);
            } else if(stopButtonIsPressed){
                stopButtonIsPressed = false;
                notValidGeneral("ANIMATION IS BEING STOPPED .....");
                setForAnimationIsFinish();
                // set the bot to the starting position, reset the level
            } if(actions.size() == 0 && a != EXIT_THROUGH){
                notValidGeneral("LEVEL IS NOT SOLVED ... NOT ENOUGH INSTRUCTIONS");
            }
            if(actions.size() == 0){
                animationIsRunning = false;
                setForAnimationIsFinish();
            }
        });
    }

    /**
     * not valid message and disable till clear
     * @param message message to be shown
     */
    @Override
    public void notValidGeneral(String message){
        setDisableAllTillClear(true);
        animationIsRunning = false;
        messageLabel.setText(message);

        setForAnimationIsFinish();

        clearButton.setDisable(false);
    }

    /**
     * Set bot on the gui
     * @param position position of the bot to be set
     */
    @Override
    public void setBotOnGui(Position position){
        bot.setImage(BOT_IMG);

        final int colWidth = (int) (gridPaneMainField.getWidth() / COLS);
        final int colHeight = (int) (gridPaneMainField.getHeight() / COLS);

        int y = position.getRow();
        int x = position.getCol();

        imageViewsMainField[x][y].setImage(NORMAL_IMG);

        if(!gridPaneMainField.getChildren().contains(bot)){
            gridPaneMainField.getChildren().add(bot);

            bot.setFitWidth(colWidth);
            bot.setFitHeight(colHeight);
            bot.setPreserveRatio(false);
            bot.setSmooth(true);
        }
        GridPane.setRowIndex(bot, y);
        GridPane.setColumnIndex(bot, x);


        bot.fitWidthProperty().bind(gridPaneMainField.widthProperty().divide(8));
        bot.fitHeightProperty().bind(gridPaneMainField.heightProperty().divide(8));
        GridPane.setConstraints(bot, x, y);
        bot.setManaged(true);
    }

    /**
     * Remove bot from gui
     */
    @Override
    public void removeBotFrom(){
        bot.setImage(null);
    }

    /**
     * Set Disable All till clear is pressed
     * @param state true for disable
     */
    void setDisableAllTillClear(boolean state){
        borderPaneMain.getLeft().setDisable(state);
        borderPaneMain.getRight().setDisable(state);
        borderPaneMain.getCenter().setDisable(state);
        menuBar.setDisable(state);
    }

    /**
     * Used to set the given imageView to the given position
     * @param iv image view to be set
     * @param col to col
     * @param row to row
     */
    private void setImageViewTo(ImageView iv, int col, int row) {
        GridPane.setColumnIndex(iv, col);
        GridPane.setRowIndex(iv, row);
        iv.setTranslateX(0);
        iv.setTranslateY(0);
        iv.setManaged(true);
    }
}
