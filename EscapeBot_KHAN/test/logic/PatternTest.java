package logic;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static logic.Instructions.*;
import static org.junit.jupiter.api.Assertions.*;

class PatternTest {

    @Test
    void generateProgramProcedures() {
        Instructions[] allInstructions = {
                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, FORWARD, JUMP,
                JUMP, ANTI_CLOCK, ANTI_CLOCK, FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD,
                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, FORWARD, JUMP,
                JUMP, ANTI_CLOCK, ANTI_CLOCK, FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD,
                JUMP, JUMP, CLOCKWISE, JUMP, JUMP, CLOCKWISE, JUMP, JUMP, CLOCKWISE, JUMP, JUMP, CLOCKWISE, JUMP, JUMP, CLOCKWISE
        };

        LinkedList<LinkedList<Instructions>> proceduresProgram = Pattern.generateProgramProcedures2(allInstructions);

        assertEquals(3, proceduresProgram.size());
        assertTrue(proceduresProgram.getFirst().contains(PROCEDURE1));
        assertTrue(proceduresProgram.getFirst().contains(PROCEDURE2));

        assertTrue(proceduresProgram.getLast().contains(PROCEDURE1));

        assertTrue(proceduresProgram.getLast().size() <= 8);
        assertTrue(proceduresProgram.get(1).size() <= 8);

    }

    @Test
    public void generateProgramProcedures2(){
        Instructions[] allInstructions = {
                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, FORWARD, JUMP, EXIT
        };

        LinkedList<LinkedList<Instructions>> proceduresProgram = Pattern.generateProgramProcedures2(allInstructions);
        assertEquals(2, proceduresProgram.size());
        assertTrue(proceduresProgram.getFirst().size() <= 12);
        assertTrue(proceduresProgram.getLast().size() <= 8);


        Instructions[] allInstructions2 = {
                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, FORWARD, JUMP,
                FORWARD, FORWARD, CLOCKWISE, CLOCKWISE, CLOCKWISE, EXIT
        };

        proceduresProgram = Pattern.generateProgramProcedures2(allInstructions2);
        assertEquals(2, proceduresProgram.size());
        assertTrue(proceduresProgram.getFirst().size() <= 12);
        assertTrue(proceduresProgram.getLast().size() <= 8);
    }

    /**
     * size of moves passed to generateProgramProcedure should always greater then 12
     */
    @Test
    public void generateProgramProcedures3(){
        Instructions[] allInstructions = {
                FORWARD, CLOCKWISE, ANTI_CLOCK, JUMP, JUMP, FORWARD, JUMP, CLOCKWISE,
                FORWARD, ANTI_CLOCK, ANTI_CLOCK, FORWARD, EXIT
        };

        LinkedList<LinkedList<Instructions>> proceduresProgram = Pattern.generateProgramProcedures2(allInstructions);
        assertEquals(2, proceduresProgram.size());
        assertTrue(proceduresProgram.getFirst().size() <= 12);
        assertTrue(proceduresProgram.getLast().size() <= 8);


        Instructions[] allInstructions2 = {
                FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, CLOCKWISE, CLOCKWISE, FORWARD, JUMP
        };
        proceduresProgram = Pattern.generateProgramProcedures2(allInstructions2);
        assertEquals(2, proceduresProgram.size());
        assertTrue(proceduresProgram.getFirst().size() <= 12);
        assertTrue(proceduresProgram.getLast().size() <= 8);
    }


    @Test
    void searchGivenPattern() {
        Instructions[] allInstructions = {
                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, FORWARD, JUMP,
                JUMP, ANTI_CLOCK, ANTI_CLOCK, FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP
        };

        assertEquals(4, Pattern.searchGivenPattern(new Instructions[]{FORWARD, FORWARD, FORWARD}, allInstructions).size());
        assertEquals(4, Pattern.searchGivenPattern(new Instructions[]{FORWARD, FORWARD, FORWARD, JUMP}, allInstructions).size());
        assertEquals(2, Pattern.searchGivenPattern(new Instructions[]{FORWARD, FORWARD, FORWARD, JUMP, FORWARD}, allInstructions).size());


    }

    @Test
    void searchGivenPattern1() {
        Instructions[] allInstructions = {
                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, FORWARD, JUMP,
                JUMP, ANTI_CLOCK, ANTI_CLOCK, FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP
        };

        Instructions[] pattern = {
                FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, FORWARD
        };

        Instructions[] pattern1 = {
                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD
        };

        assertEquals(0, Pattern.searchGivenPattern(pattern, allInstructions).size());
        assertEquals(2, Pattern.searchGivenPattern(pattern1, allInstructions).size());
    }

    @Test
    public void getSeparatedProcedure(){
        Instructions[] allInstructions = {
                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, FORWARD, JUMP,
                JUMP, ANTI_CLOCK, ANTI_CLOCK, FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP
        };

        int maxLength = 3;
        LinkedList<Instructions> procedureMoves = Pattern.getSeparatedProcedure(5, maxLength, allInstructions);
        assertEquals(maxLength, procedureMoves.size());
        assertEquals(FORWARD, procedureMoves.get(0));

        maxLength = 12;
        procedureMoves = Pattern.getSeparatedProcedure(3, maxLength, allInstructions);
        assertEquals(maxLength, procedureMoves.size());
        assertEquals(JUMP, procedureMoves.get(0));
    }

    @Test
    public void getSeparatedProgram(){
        Instructions[] allInstructions = {
                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, FORWARD, JUMP,
                JUMP, ANTI_CLOCK, ANTI_CLOCK, FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP
        };

        LinkedList<Integer> maxOccurrenceList = new LinkedList<>();
        maxOccurrenceList.add(0);
        maxOccurrenceList.add(10);
        int length = 4;

        LinkedList<Instructions> sepratedProgram = Pattern.getSeparatedProgram(maxOccurrenceList, allInstructions, length, true);

        assertTrue(sepratedProgram.contains(PROCEDURE1));
        assertEquals(PROCEDURE1, sepratedProgram.getFirst());
        assertEquals(PROCEDURE1, sepratedProgram.get(7));
    }

    @Test
    public void toRemoveProcedureFromProgram(){
        LinkedList<Instructions> allInstructions = new LinkedList<>();
        allInstructions.add(FORWARD);
        allInstructions.add(FORWARD);
        allInstructions.add(FORWARD);
        allInstructions.add(JUMP);
        allInstructions.add(FORWARD);
        allInstructions.add(FORWARD);
        allInstructions.add(JUMP);
        allInstructions.add(CLOCKWISE);
        allInstructions.add(FORWARD);
        allInstructions.add(JUMP);
        allInstructions.add(ANTI_CLOCK);
        allInstructions.add(FORWARD);
        allInstructions.add(FORWARD);
        allInstructions.add(JUMP);
        allInstructions.add(EXIT);

        LinkedList<Integer> startPos = new LinkedList<>();
        startPos.add(3);
        int lengthOfProcedure = 4;
        LinkedList<Instructions> removedProcedureFromProgram;

        removedProcedureFromProgram = Pattern.toRemoveProcedureFromProgram(allInstructions, startPos, lengthOfProcedure, true);

        assertEquals(allInstructions.size() - 3, removedProcedureFromProgram.size());
        assertEquals(PROCEDURE1, removedProcedureFromProgram.get(3));
    }
}