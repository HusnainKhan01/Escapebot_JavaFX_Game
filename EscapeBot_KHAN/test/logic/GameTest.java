package logic;

import org.junit.Test;

import java.util.LinkedList;

import static logic.FieldType.*;
import static logic.Instructions.*;
import static org.junit.Assert.*;

public class GameTest {

    @Test
    public void setCreateMode() {
        Game g = new Game(new FakeGUI());
        assertFalse(g.getCreateMode());

        g.setCreateMode(true);
        assertTrue(g.getCreateMode());
    }

    @Test
    public void changeBotRotation() {
        Game g = new Game(new FakeGUI());

        assertEquals(0, g.board.getBotRotCreatedLevel());
        g.changeBotRotation();
        assertEquals(1, g.board.getBotRotCreatedLevel());
        g.changeBotRotation();
        assertEquals(2, g.board.getBotRotCreatedLevel());
        g.changeBotRotation();
        assertEquals(3, g.board.getBotRotCreatedLevel());

        g.changeBotRotation();
        assertEquals(0, g.board.getBotRotCreatedLevel());
    }

    @Test
    public void changePositionOfToPos() throws Exception {
        Game g = new Game(new FakeGUI());
        assertNull(g.board.getBotPositionCreatedLevel());

        g.setMap(0);
        assertEquals(4, g.board.getBotPositionCreatedLevel().getRow());
        assertEquals(0, g.board.getBotPositionCreatedLevel().getCol());

        g.changePositionOfToPos(BOT_FIELD_TYPE, 1, 1);
        assertEquals(1, g.board.getBotPositionCreatedLevel().getRow());
        assertEquals(1, g.board.getBotPositionCreatedLevel().getCol());

        assertEquals(4, g.board.getDoorPositionCreatedLevel().getRow());
        assertEquals(7, g.board.getDoorPositionCreatedLevel().getCol());

        g.changePositionOfToPos(DOOR, 5, 5);
        assertEquals(5, g.board.getDoorPositionCreatedLevel().getRow());
        assertEquals(5, g.board.getDoorPositionCreatedLevel().getCol());

    }

    @Test
    public void setMap() throws Exception {
        Game g = new Game(new FakeGUI());
        g.setMap(null);

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++)
                assertEquals(NORMAL, g.board.getMapcharAt(new Position(5, j)));
        }
    }


    /**
     * // TODO: Information of the button is passed from the gui,
     *     //       Test Cases to be modified
     * TODO: implement this
     * Used to test all the combined moves
     */
    @Test
    public void test0getCombinedMoves(){
        Game g = new Game(new FakeGUI());
        Instructions[] program = {
                FORWARD, PROCEDURE1, FORWARD, CLOCKWISE, CLOCKWISE
        };

        Instructions[] procedure1 = {
                FORWARD, FORWARD, PROCEDURE2, FORWARD, CLOCKWISE, CLOCKWISE, ANTI_CLOCK, JUMP
        };

        Instructions[] procedure2 = {
                FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, ANTI_CLOCK, JUMP
        };

        g.getProgram().copyGivenMovements(program);
        g.getProcedure1().copyGivenMovements(procedure1);
        g.getProcedure2().copyGivenMovements(procedure2);

        LinkedList<Instructions> combinedInst = g.getCombinedInstructions();

        assertEquals(22, combinedInst.size());
    }

    @Test
    public void generateActionsFromMoves() throws Exception {
        Game g = new Game(new FakeGUI());
        Instructions[] program = {
                FORWARD, PROCEDURE1, FORWARD, CLOCKWISE, CLOCKWISE
        };

        Instructions[] procedure1 = {
                FORWARD, FORWARD, PROCEDURE2, FORWARD, CLOCKWISE, CLOCKWISE, ANTI_CLOCK, JUMP, EXIT
        };

        g.board.setBotPosition(new Position(0, 0));
        Instructions[] procedure2 = {
                FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, ANTI_CLOCK, JUMP
        };

        g.getProgram().copyGivenMovements(program);
        g.getProcedure1().copyGivenMovements(procedure1);
        g.getProcedure2().copyGivenMovements(procedure2);

        LinkedList<Instructions> combinedInst = g.getCombinedInstructions();

        assertEquals(8, g.generateActionsFromInstructions(combinedInst).size());
        assertEquals(Actions.BOT_RUN_OUT_OF_FIELD, g.generateActionsFromInstructions(combinedInst).getLast());
    }

    @Test
    public void hasMultipleExit() throws Exception {
        Game g = new Game(new FakeGUI());
        Instructions[] program = {
                FORWARD, PROCEDURE1, FORWARD, CLOCKWISE, CLOCKWISE
        };

        Instructions[] procedure1 = {
                FORWARD, FORWARD, PROCEDURE2, FORWARD, CLOCKWISE, CLOCKWISE, ANTI_CLOCK, JUMP, EXIT
        };

        g.board.setBotPosition(new Position(0, 0));
        Instructions[] procedure2 = {
                FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, ANTI_CLOCK, JUMP
        };

        g.getProgram().copyGivenMovements(program);
        g.getProcedure1().copyGivenMovements(procedure1);
        g.getProcedure2().copyGivenMovements(procedure2);

        LinkedList<Instructions> combinedInst = g.getCombinedInstructions();

        assertTrue(g.hasMultipleExit(combinedInst));
    }

}