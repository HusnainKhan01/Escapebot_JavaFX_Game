package logic;

import java.util.LinkedList;

import static logic.Actions.*;
import static logic.FieldType.*;
import static logic.Instructions.*;

/**
 * Game Class to be called from gui
 * Has all the handle method of the logic
 */
public class Game {

    /**
     * TOTAL rows and cols in the game
     */
    final static int ROWS = 8;
    final static int COLS = 8;

    /**
     * Used to keep track which mode is operating
     */
    private boolean createMode;

    /**
     * Programs and procedures
     */
    private Program program;
    private Program procedure1;
    private Program procedure2;

    /**
     * Instance of the gui
     */
    GUIConnector gui;

    /**
     * Maps instance
     */
    Board board;

    /**
     * Initialize the game, Create map, program, procedure1 and procedure2
     * Construct
     * @param gui gui
     */
    public Game(GUIConnector gui){
        this.gui = gui;
        board = new Board();

        program = new Program();
        procedure1 = new Program();
        procedure2 = new Program();
    }

    /**
     * get program
     * Used in test cases
     * @return program
     */
    Program getProgram(){
        return program;
    }

    /**
     * get procedure 1
     * Used in test cases
     * @return procedure1
     */
    Program getProcedure1(){
        return procedure1;
    }

    /**
     * get procedure 2
     * Used in test cases
     * @return procedure2
     */
    Program getProcedure2(){
        return procedure2;
    }

    /**
     * Used to handle call from gui
     * @return string
     */
    public String saveFile(){
        SavingFile savingFile = new SavingFile();
        return savingFile.saveFile(board);
    }

    /**
     * Loading of file
     * @param savingFile Object to be loaded
     */
    public void loadingFile(SavingFile savingFile) throws Exception {
        FieldType[][] toReturn = savingFile.loadingFile();
        if(toReturn == null){
            // write message on the gui
            gui.notValidGeneral("NOT VALID LOADED MAP ....");
        } else {
            board.setRotOfBotByMap(savingFile.getBotRotationFromForLoading());
            board.setBotPosition(savingFile.getBotPositionForLoad());
            board.setCreatedLevel(toReturn);
            board.setBotStartPosStartRot();
            setMap(10);
        }
    }

    /**
     * Used to set the bot start position from
     * bot position created level
     */
    public void setBotStartPosStartRot(){
        board.setBotStartPosStartRot();
    }

    /**
     * used to set the create mode
     * @param state state of the game can be created or solve
     */
    public void setCreateMode(boolean state){
        createMode = state;
    }

    /**
     * Used in test cases
     * @return createMode
     */
    boolean getCreateMode(){
        return createMode;
    }

    /**
     * Called which on Main Field is clicked
     * @param rows clicked row
     * @param cols clicked col
     */
    public void handleOnField(int rows, int cols, FieldType prevButtonChar) throws Exception {
       if(createMode){
            // used to remove the bot from the clicked position
            if(prevButtonChar != BOT_FIELD_TYPE && board.hasABotOn(new Position(rows, cols))){
                board.setBotPosition(null);
                board.setBotStartPosStartRot();
                board.setRotOfBotByMap(0);
                gui.removeBotFrom();
            }
            // if map has a bot or door and prev button is either bot or door
            if(board.mapHasAChar(prevButtonChar) && (prevButtonChar.isADoor() || prevButtonChar == BOT_FIELD_TYPE)){
                if(prevButtonChar == BOT_FIELD_TYPE && board.hasABotOn(new Position(rows, cols))){
                    // change rotation of the bot
                    changeBotRotation();
                } else if(prevButtonChar.isADoor()){
                    changePositionOfToPos(DOOR, rows, cols);
                } else if(prevButtonChar == BOT_FIELD_TYPE){
                    changePositionOfToPos(BOT_FIELD_TYPE, rows, cols);
                }
            } else {
                gui.setFieldImage(rows, cols, prevButtonChar.ordinal() + 1, 0);
                board.setCharToCreatedMap(new Position(rows, cols), prevButtonChar);
            }
       }
    }

    /**
     * Used to change the bot rotation
     */
    void changeBotRotation(){
        int botRot = board.getBotRotCreatedLevel();
        try {
            board.setRotOfBotByMap(botRot + 1);
            gui.changeBotRot(board.getBotRotCreatedLevel());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to change the MapChar position to given position
     * @param fieldType map Char whose position to be changed
     * @param rows row to be changed to
     * @param cols col to be changed to
     */
    void changePositionOfToPos(FieldType fieldType, int rows, int cols) throws Exception {
        if(fieldType == DOOR){
            Position pos = board.changeMapCharPositionTo(fieldType, rows, cols);
            gui.setImage(NORMAL, pos.getRow(), pos.getCol());
            gui.setImage(fieldType, rows, cols);
        } else if(fieldType == BOT_FIELD_TYPE){
            Position pos = board.changeMapCharPositionTo(fieldType, rows, cols);
            gui.setImage(NORMAL, pos.getRow(), pos.getCol());
            gui.setBotOnGui(new Position(rows, cols));
        }
    }

    /**
     * Used to set the map on the gui and in the logic
     * Called from the gui when any map button is pressed
     * @param mapNum number of map to be set, if null then default map
     */
    public void setMap(Integer mapNum) throws Exception {
        if(mapNum == null){
            gui.setToNewGame();
            board.defaultMap();
            gui.removeBotFrom();
            gui.changeBotRot(0);
        } else if(mapNum < 4){
            gui.setMapOnGui(board.getMap(mapNum));
            board.setCurrentMap(mapNum);
            gui.setBotOnGui(board.getBotPositionCreatedLevel());
            gui.changeBotRot(board.getBotRotCreatedLevel());
        } else {
            // set bot to starting Position rotation
            board.setBotPositionRotCreatedLevelToStarting();
            // set map to created
            if(board.getBotPositionCreatedLevel() != null){
                gui.setMapOnGui(board.getCreatedLevel());
                gui.changeBotRot(board.getBotRotCreatedLevel());
                gui.setImage(BOT_FIELD_TYPE, board.getBotPositionCreatedLevel().getRow(), board.getBotPositionCreatedLevel().getCol());
            }
        }
    }

    /**
     * Called when start game is pressed
     * Call isValid and check if all moves are valid,
     * Implementation:
     *         check if the isValidProgram
     *         (check if program and procedures don't have recursion)
     *         returns true, then proceed
     *         Generate Actions from AllCombined Moves
     *
     */
    public void handleStartGame(Instructions[] programMoves, Instructions[] procedure1Moves, Instructions[] procedure2Moves) throws Exception {
        // Copy the moves to program and procedures, this  will take care of the null
        // bcz this will copy till the point we don't have null
        board.setBotStartPosStartRot();
        if(programMoves[0] == null){
            gui.notValidGeneral(" NO PROGRAM MOVES ...");
            return;
        } else if(isNotValidPrograms(procedure1Moves, procedure2Moves)){
            gui.notValidGeneral("PROCEDURE1 Or PROCEDURE2 has Recursion ...");
            return;
        } else if(!board.isValidMap()){
            gui.notValidGeneral("NOT A VALID MAP ON BOARD ...");
            return;
        }

        program.copyGivenMovements(programMoves);
        procedure1.copyGivenMovements(procedure1Moves);
        procedure2.copyGivenMovements(procedure2Moves);

        // clear all Collected coins
        board.clearCollectedCoins();

        // set bot start Position, start Rotation
        board.setBotStartPosStartRot();

        // Combine all moves
        LinkedList<Instructions> combinedInstructions = getCombinedInstructions();
        // pass the starting position of the bot because the bot position for created level changes while generating actions
        // also starting rotation
        if(!hasMultipleExit(combinedInstructions)){
            gui.handleActions(board.getBotPositionCreatedLevel(), board.getBotRotCreatedLevel(), generateActionsFromInstructions(combinedInstructions));
        }
    }

    /**
     * Used to check if the given program is valid
     * given program is valid if it doesn't have three different types of recursions
     * 1 -> Program has P1 and P1 has P1
     * 2 -> Program has p1 and p1 has p2 and p2 has p1
     * 3 -> Program has p2 and p2 has p1 and p1 has p2
     * is to be optimized in such a way it should also check if there is more then one exit
     *         // loop through procedure1 if it has procedure 1 return false
     *         // loop through procedure2 if it has procedure 2 return false
     *         // loop through procedure1 and procedure2 if p1 has p2 and p2 has p1
     *         // loop through procedure1 and procedure2 if p2 has p1 and p1 has p2
     * @return true if program is not valid else false
     */
    boolean isNotValidPrograms(Instructions[] procedure1Moves, Instructions[] procedure2Moves){
        boolean hasSelfRecursion = false;
        boolean procedure1HasP2 = false;
        boolean procedure2HasP1 = false;

        for(int i = 0; i < procedure1Moves.length && !hasSelfRecursion; i++){
            if(procedure1Moves[i] == PROCEDURE1){
                hasSelfRecursion = true;
            } else if(procedure1Moves[i] == PROCEDURE2){
                procedure1HasP2 = true;
            }
        }

        if(!hasSelfRecursion){
            for(int i = 0; i < procedure2Moves.length && !hasSelfRecursion; i++){
                if(procedure2Moves[i] == PROCEDURE2){
                    hasSelfRecursion = true;
                } else if(procedure2Moves[i] == PROCEDURE1){
                    procedure2HasP1 = true;
                }
            }
        }

        if(!hasSelfRecursion && procedure2HasP1 && procedure1HasP2){
            hasSelfRecursion = true;
        }
        return hasSelfRecursion;
    }

    /**
     * Used to check if there Multiple exits or Instruction After exit
     * Will set message on gui and return true if has Multiple exits or instructions
     * after exit
     * @param instructions all instructions to be checked
     */
    boolean hasMultipleExit(LinkedList<Instructions> instructions){
        boolean hasMultipleExit = false;
        int len = instructions.size();
        boolean exitIdxIsFound = false;
        Integer exitIdx = null;
        for(int i = 0; i < len && !exitIdxIsFound; i++){
            if(instructions.get(i) == EXIT){
                exitIdxIsFound = true;
                exitIdx = i;
            }
        }
        if(exitIdxIsFound){
            if(len > exitIdx + 1){
                // has more Exit or Instructions after Exit
                hasMultipleExit = true;
                gui.notValidGeneral("HAS MORE THEN 1 EXIT OR HAS INSTRUCTIONS AFTER EXIT ...");
            }
        }
        return hasMultipleExit;
    }

    /**
     * Solvability
     * Called when solve Level is pressed
     */
    public void handleSolveLevel() throws Exception {
        int maxProgramMoves = 12;
        board.setBotStartPosStartRot();
        if(board.isValidMap()){
            gui.setClearProcedure1();
            gui.setClearProcedure2();
            gui.setClearProgram();

            // Generate all the moves for all the coins
            LinkedList<Instructions> allMoves = Solvability.createMovesForAllCoins(board);
            if(allMoves == null){
                gui.notValidGeneral("BOT Can't reach to all Positions");
            }
            else if(allMoves.size() <= maxProgramMoves){
                // set only program and don't calculate procedures
                setProgramOnGui(allMoves.toArray(new Instructions[0]));
            } else {
                // Generate Program and ProcedureMoves
                LinkedList<LinkedList<Instructions>> programsProcedures;
                programsProcedures = Pattern.generateProgramProcedures2(allMoves.toArray(new Instructions[0]));
                if(!setAllProgramProceduresOnGui(programsProcedures)){
                    gui.notValidGeneral("Map is too Complex to Solve");
                }
            }
        } else {
            gui.notValidGeneral("NOT A VALID MAP ... ");
        }
    }

    /**
     * Used to set All the program and Procedures on the gui
     * @param programsProcedures all Program and Procedures
     */
    private boolean setAllProgramProceduresOnGui(LinkedList<LinkedList<Instructions>> programsProcedures){
        int i = 0;
        boolean isAValidSol = false;
        if(programsProcedures.size() == 3 && programsProcedures.get(0).size() <= 12 && programsProcedures.get(1).size() <= 8 &&
                programsProcedures.get(2).size() <= 8){
            isAValidSol = true;
        } else if(programsProcedures.get(0).size() <= 12 && programsProcedures.get(1).size() <= 8) {
            isAValidSol = true;
        }
        if(isAValidSol){
            for(LinkedList<Instructions> moves: programsProcedures){
                if(i == 0){
                    setProgramOnGui(moves.toArray(new Instructions[0]));
                } else if(i == 1){
                    setProcedure1OnGui(moves.toArray(new Instructions[0]));
                } else if(i == 2){
                    setProcedure2OnGui(moves.toArray(new Instructions[0]));
                }
                i++;
            }
        }
        return isAValidSol;
    }

    /**
     * Used to set the program on the gui
     * @param moves moves to be set on the program
     */
    private void setProgramOnGui(Instructions[] moves){
        int maxCol = 3;
        program.copyGivenMovements(moves);
        int r = 0;
        int c = 0;
        for(Instructions m: moves){
            gui.setProgramImage(r, c, m.ordinal() + 1, 1);
            if(c < maxCol){
                c++;
            } else {
                c = 0;
                r++;
            }
        }
    }

    /**
     * Used to set all the moves on the Procedure1
     * @param moves moves
     */
    private void setProcedure1OnGui(Instructions[] moves){
        int maxCol = 3;
        program.copyGivenMovements(moves);
        int r = 0;
        int c = 0;
        for(Instructions m: moves){
            gui.setProgramImage(r, c, m.ordinal() + 1, 2);
            if(c < maxCol){
                c++;
            } else {
                c = 0;
                r++;
            }
        }
    }

    /**
     * Used to set all the moves to the procedure2 on the gui
     * @param moves moves to be set on the gui
     */
    private void setProcedure2OnGui(Instructions[] moves){
        int maxCol = 3;
        program.copyGivenMovements(moves);
        int r = 0;
        int c = 0;
        for(Instructions m: moves){
            gui.setProgramImage(r, c, m.ordinal() + 1, 3);
            if(c < maxCol){
                c++;
            } else {
                c = 0;
                r++;
            }
        }
    }

    /**
     * All movements are copied to single linkedList
     * Used to get all the moves combined in the form of list
     * When procedure 1 starts at any point procedure 1 is add along with moves and when procedure 1 ends
     * then also procedure 1 is added
     * same goes for the procedure 2
     * @return All moves stored
     */
    LinkedList<Instructions> getCombinedInstructions(){
        LinkedList<Instructions> combMoves = new LinkedList<>();
        Instructions[] programMoves = program.getMovementArray();

        for (Instructions programMove : programMoves) {
            if (programMove == PROCEDURE1) {
                combMoves.add(PROCEDURE1);
                copyProcedure1(combMoves);
                combMoves.add(PROCEDURE1);
            } else if (programMove == PROCEDURE2) {
                combMoves.add(PROCEDURE2);
                copyProcedure2(combMoves);
                combMoves.add(PROCEDURE2);
            } else {
                combMoves.add(programMove);
            }
        }

        return combMoves;
    }

    /**
     * Copy procedure1 to given linkedList
     * @param combMoves given list
     */
    private void copyProcedure1(LinkedList<Instructions> combMoves){
        Instructions[] procedure1Moves = procedure1.getMovementArray();
        for (Instructions procedure1Move : procedure1Moves) {
            if (procedure1Move == PROCEDURE2) {
                combMoves.add(PROCEDURE2);
                copyProcedure2(combMoves);
                combMoves.add(PROCEDURE2);
            } else {
                combMoves.add(procedure1Move);
            }
        }
    }

    /**
     * Copy procedure2 to given list
     * @param combMoves given list
     */
    private void copyProcedure2(LinkedList<Instructions> combMoves){
        Instructions[] procedure2Moves = procedure2.getMovementArray();
        for (Instructions procedure2Move : procedure2Moves) {
            if (procedure2Move == PROCEDURE1) {
                combMoves.add(PROCEDURE1);
                copyProcedure1(combMoves);
                combMoves.add(PROCEDURE1);
            } else {
                combMoves.add(procedure2Move);
            }
        }
    }

    /**
     * Used to generate actions from all the moves
     * @param instructions from those actions are derived
     * @return Actions all actions
     */
    LinkedList<Actions> generateActionsFromInstructions(LinkedList<Instructions> instructions) throws Exception {

        boolean isInProcedure1 = false;
        boolean isInProcedure2 = false;

        // ALL Actions in a list
        LinkedList<Actions> allActions = new LinkedList<>();

        int size = instructions.size();
        boolean errorFound = false;
        Instructions move;
        for(int i = 0; i < size && !errorFound; i++){
            move = instructions.get(i);
            if (move == FORWARD) {
                if (board.moveBot()) {
                    // save Action
                    allActions.add(MOVE_IN_DIRECTION);
                    // Used to add action COLLECT COIN IF there is a coin
                    if (board.collectCoin()) {
                        // collect coins
                        // save action
                        allActions.add(COLLECT_COIN);
                    }
                } else {
                    // Show error
                    allActions.add(getTypeOfErrorForForwardMove());
                    errorFound = true;
                }
            } else if (move == CLOCKWISE) {
                allActions.add(CLOCK_WISE);
                board.rotateBotClockwise();
                // save action
            } else if (move == ANTI_CLOCK) {
                allActions.add(ANTI_CLOCKWISE);
                board.rotateBotAntiClockwise();
                // save action
            } else if (move == JUMP) {
                if (board.jumpBot()) {
                    // save action
                    allActions.add(JUMP_THROUGH);
                } else {
                    // Show error
                    allActions.add(getTypeOfErrorForJumpNotPossible());
                    errorFound = true;
                }
            } else if (move == EXIT) {
                if (board.exitIsPossible()) {
                    // Generate Action
                    allActions.add(EXIT_THROUGH);
                } else {
                    // Show error
                    allActions.add(getTypeOfErrorForExitNotPossible());
                    errorFound = true;
                }
            } else if(move == PROCEDURE1){
                if(!isInProcedure1){
                    isInProcedure1 = true;
                    allActions.add(START_P1);
                } else{
                    isInProcedure1 = false;
                    allActions.add(END_P1);
                }
            } else if(move == PROCEDURE2){
                if(!isInProcedure2){
                    isInProcedure2 = true;
                    allActions.add(START_P2);
                } else {
                    isInProcedure2 = false;
                    allActions.add(END_P2);
                }
            }
        }

        return allActions;
    }

    /**
     * Used to return type of error for ForwardMove
     * @return type of error for Forward move
     */
    private Actions getTypeOfErrorForForwardMove(){
        Actions typeOfError = THROUGH_ERR;
        Position position = board.getBotPositionCreatedLevel().getNextPosition(board.getBotRotCreatedLevel());
        if(!board.isValidPos(position)){
            typeOfError = BOT_RUN_OUT_OF_FIELD;
        } else if(board.getMapcharAt(position) == PIT){
            typeOfError = BOT_RUN_TO_PIT;
        } else if(board.getMapcharAt(position) == GRAY_WALL){
            typeOfError = BOT_RUN_TO_WALL;
        } else if(board.getMapcharAt(position) == DOOR){
            // can't run to DOOR
            typeOfError = BOT_RUN_TO_DOOR;
        }
        return typeOfError;
    }

    /**
     * Used to return type of error for Exit not Possible
     * @return type of error for exit not possible
     */
    private Actions getTypeOfErrorForExitNotPossible(){
        Actions typeOfError;
        if(!board.coinsAreCollected()){
            typeOfError = BOT_EXIT_WITHOUT_COINS;
        } else {
            typeOfError = BOT_NOT_FACING_DOOR;
        }
        return typeOfError;
    }

    /**
     * User to get type of Error When jump is not possible
     * @return type of error when jump is not possible
     */
    private Actions getTypeOfErrorForJumpNotPossible(){
        Actions typeOfError = THROUGH_ERR;
        Position position = board.getBotPositionCreatedLevel().getNextPosition(board.getBotRotCreatedLevel());
        Position nexPos = position.getNextPosition(board.getBotRotCreatedLevel());
        if(!board.isValidPos(nexPos)){
            // Not a valid position for jum
            typeOfError = BOT_JUMP_OFF_FIELD;
        } else if(board.getMapcharAt(position) == GRAY_WALL){
            // can't jump on grey wall
            typeOfError = BOT_JUMP_ON_WALL;
        } else if(board.getMapcharAt(position) == NORMAL){
            // can't jump on normal field
            typeOfError = BOT_JUMP_ON_NORMAL;
        } else if(board.getMapcharAt(position) == COIN){
            // can't jump on coin
            typeOfError = BOT_JUMP_ON_COIN;
        } else if(board.getMapcharAt(position) == PIT){
            if(board.getMapcharAt(nexPos) == PIT){
                // can't jump on two walls
                typeOfError = BOT_JUMP_ON_TWO_PIT;
            } else if(board.getMapcharAt(nexPos) == DOOR){
                // can't jump to door
                typeOfError = BOT_JUMP_ONTO_DOOR;
            } else if(board.getMapcharAt(nexPos) == GRAY_WALL){
                // can't stand of GREY WAL after jump
                typeOfError = BOT_JUMP_ONTO_WALL;
            }
        }
        return typeOfError;
    }
}


