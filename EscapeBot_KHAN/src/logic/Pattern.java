package logic;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import static logic.Instructions.PROCEDURE1;
import static logic.Instructions.PROCEDURE2;

/**
 * Used to search for repeated patterns in All Instructions
 * Generate Program Procedure1 and Procedure2
 */
public final class Pattern {

    /**
     * Main Method to be called from game class
     * if all moves are greater than 12 then generate procedure 1 and new program then check
     * if program is still greater than 12 then generate procedure 2 and program
     *
     * Size of allMoves should always greater then 12 and there should be exit at the end
     *
     * @param allMoves from procedure and programs are generated
     * @return LinkedList of linkedList containing procedures and program
     *         size can be 2 or 3
     */
    static LinkedList<LinkedList<Instructions>> generateProgramProcedures2(Instructions[] allMoves){
        LinkedList<LinkedList<Instructions>> listProgramProcedure = new LinkedList<>();
        //if(allMoves.length > 12){
            // generate new program and procedure1
            LinkedList<Instructions>[] resp = generateProcureProgramMoves(allMoves, true);
            if(resp[0].size() > 12){
                // generate new program and procedure2
                LinkedList<Instructions>[] resp2 = generateProcureProgramMoves(resp[0].toArray(new Instructions[0]), false);
                if(resp2[0].size() > 12 && resp2[1].size() < 8 ){
                    // replace first 8 elements of the program with procedure2
                    resp2 = generateProcedureOfStartingMoves(resp[0].toArray(new Instructions[0]), false);
                }

                listProgramProcedure.add(resp2[0]);
                listProgramProcedure.add(resp[1]);
                listProgramProcedure.add(resp2[1]);
            } else {
                listProgramProcedure.add(resp[0]);
                listProgramProcedure.add(resp[1]);
            }

        return listProgramProcedure;
    }

    /**
     * Used to generate procedure1 from all moves and return allMoves for program
     *
     *
     * The pattern for procedure is selected based upon
     * the reduction of the length of all the moves
     *  For that --> A variable to store total Length (totalLength)
     *               A Variable to store the reduced length (reducedLength)
     *               A Variable to store the result of the equation
     *               A Variable to store number Of times pattern is repeated(repeatLen)
     *               A Variable to store length of the pattern (patLen)
     *                      --> totalLength - ((repeatLen) * (patLen)) + repeatLen
     * if no pattern is found then generate a procedure1 having length 8 from start till 8
     *
     * @param  isForProcedure1 true if procedure1 is generated, else false for procedure2
     * @param allMoves all Moves of the solution of solvability
     * @return program procedure
     */
    static LinkedList<Instructions>[] generateProcureProgramMoves(Instructions[] allMoves, boolean isForProcedure1){
        int minPatternLen = 2;
        LinkedList<Integer> maxOccurrenceList = new LinkedList<>();
        int maxPoints = Integer.MAX_VALUE;
        int points;

        int i = 0;
        int j = i + minPatternLen;
        int maxLength = 0;
        LinkedList<Integer> allOccurrences;

        int len = allMoves.length;
        while (j < len){
            Instructions[] pattern = Arrays.copyOfRange(allMoves, i, j);
            allOccurrences = searchGivenPattern(pattern, allMoves);
            if(allOccurrences.size() == 1){
                i++;
                j = i+2;
            } else {
                int patternLen = pattern.length;
                if(patternLen > 8){
                    i++;
                    j = i + minPatternLen;
                } else {
                    points = allMoves.length - (allOccurrences.size() * (patternLen)) + allOccurrences.size();
                    if(points < maxPoints){
                        maxPoints = points;
                        maxLength = patternLen;
                        maxOccurrenceList = allOccurrences;
                    }
                    j++;
                }
            }
        }

        // Generate Procedure Moves separated
        return generateProcedureMovesSeparated(maxOccurrenceList, maxLength, allMoves, isForProcedure1);
    }

    /**
     * Generate Procedure Moves separated
     * Helper for generateProcureProgramMoves
     * @param maxOccurrenceList list of position for the max occurring pattern
     * @param maxLength max length
     * @param allMoves allMoves
     * @param isForProcedure1 true for procedure1 else false
     * @return linkedList of Moves containing program and procedures
     */
    private static LinkedList<Instructions>[] generateProcedureMovesSeparated(LinkedList<Integer> maxOccurrenceList, int maxLength, Instructions[] allMoves,
                                                                              boolean isForProcedure1){
        LinkedList<Instructions> procedureMoves;
        // if there is no pattern found then the value is 0
        if (maxOccurrenceList.size() == 0) {
            // if value is 0 then ==> generate procedure of 8 moves starting from first index
            maxOccurrenceList.add(1);
            maxLength = 8;
        }
        procedureMoves = getSeparatedProcedure(maxOccurrenceList.getFirst(), maxLength, allMoves);

        // Get Program moves
        LinkedList<Instructions> programMoves = getSeparatedProgram(maxOccurrenceList, allMoves, maxLength, isForProcedure1);

        LinkedList<Instructions>[] toReturn2 = new LinkedList[2];
        toReturn2[0] = programMoves;
        toReturn2[1] = procedureMoves;

        return toReturn2;
    }


    /**
     * Replace first 8 elements of the program with procedure2
     * @param allMoves all Moves
     * @param isForProcedure1 true for procedure1 else false for procedure2
     * @return LinkedList of moves
     */
    private static LinkedList<Instructions>[] generateProcedureOfStartingMoves(Instructions[] allMoves, boolean isForProcedure1){
        LinkedList<Instructions> procedureMoves;
        procedureMoves = getSeparatedProcedure(0, 8, allMoves);
        LinkedList<Integer> toPass = new LinkedList<>();
        toPass.add(0);
        LinkedList<Instructions> programMoves = getSeparatedProgram(toPass, allMoves, 8, isForProcedure1);
        LinkedList<Instructions>[] toReturn2 = new LinkedList[2];
        toReturn2[0] = programMoves;
        toReturn2[1] = procedureMoves;

        return toReturn2;
    }

    /**
     * Used to get separated procedure
     * @param startIdx starting position
     * @param length length of procedure program
     * @param allMoves allMoves
     * @return Separated Procedure from the given moves
     */
    static LinkedList<Instructions> getSeparatedProcedure(int startIdx, int length, Instructions[] allMoves){
        LinkedList<Instructions> procedureMoves = new LinkedList<>();
        // Generate Procedure 1 Moves
        int size = allMoves.length;
        for(int x = 0; x < length; x++){
            if(startIdx + x < size){
                procedureMoves.add(allMoves[startIdx + x]);
            }
        }
        return procedureMoves;
    }

    /**
     * Used to generate Program
     * @param maxOccurrenceList occurrences position
     * @param allMoves all moves from which the procedure is
     * @param length length of the procedure
     * @param isProcedure1 used to put procedure1 if true else procedure 2
     * @return seprated program and procedures
     */
    static LinkedList<Instructions> getSeparatedProgram(LinkedList<Integer> maxOccurrenceList, Instructions[] allMoves, int length, boolean isProcedure1){
        LinkedList<Instructions> converted = new LinkedList<>();
        Collections.addAll(converted, allMoves);
        return toRemoveProcedureFromProgram(converted, maxOccurrenceList, length, isProcedure1);
    }
    /**
     * Method to remove Procedure from the Program
     * @param allMoves all moves from those procedure is to to removed
     * @param startPos start positions of the pattern
     * @param lengthOfProcedure length of procedure to be removed
     * @return all Moves after removing procedure
     */
    static LinkedList<Instructions> toRemoveProcedureFromProgram(LinkedList<Instructions> allMoves, LinkedList<Integer> startPos, Integer lengthOfProcedure, boolean isProcedure1){
        LinkedList<Instructions> toReturn = new LinkedList<>();
        int count = 0;
        int len = allMoves.size();
        for(int i = 0; i < len; i++){
            if(startPos.get(count) == i){
                if(isProcedure1){
                    toReturn.add(PROCEDURE1);
                } else {
                    toReturn.add(PROCEDURE2);
                }
                i = i + lengthOfProcedure - 1;
                if(count + 1 < startPos.size()){
                    count++;
                }
            } else {
                toReturn.add(allMoves.get(i));
            }
        }
        return toReturn;
    }

    /**
     * searchGivenPattern used to find the given pattern in the moves
     * Reference : https://www.geeksforgeeks.org/kmp-algorithm-for-pattern-searching/
     *
     * @param pattern to be searched from
     * @param instructionsFromPatternIsExtracted all moves from those pattern is extracted
     * @return position at which the given pattern occurs
     */
    static LinkedList<Integer> searchGivenPattern(Instructions[] pattern, Instructions[] instructionsFromPatternIsExtracted){
        LinkedList<Integer> patternOccurrencePositions = new LinkedList<>();

        int patternLen = pattern.length;
        int instructionsFromPatternLen = instructionsFromPatternIsExtracted.length;

        // prefix suffix values for pattern
        int[] longesPrefixSufFixVal = new int[patternLen];
        int indexForPat = 0;
        longesPrefix(pattern, patternLen, longesPrefixSufFixVal);
        // index for instructionsFromPattern
        int i = 0;
        while (i < instructionsFromPatternLen) {
            if (pattern[indexForPat] == instructionsFromPatternIsExtracted[i]) {
                indexForPat++;
                i++;
            }
            if (indexForPat == patternLen) {
                patternOccurrencePositions.add(i - indexForPat);
                indexForPat = 0;
            }
            else if (i < instructionsFromPatternLen && pattern[indexForPat] != instructionsFromPatternIsExtracted[i]) {
                if (indexForPat != 0){
                    indexForPat = longesPrefixSufFixVal[indexForPat - 1];
                } else{
                    i++;
                }
            }
        }
        return patternOccurrencePositions;
    }

    /**
     * Helper for KMP, longest prefix
     * @param pattern pattern
     * @param M pattern length
     * @param prefexSuffixValue lps prefix suffix values for pattern
     */
    private static void longesPrefix(Instructions[] pattern, int M, int[] prefexSuffixValue) {
        int len = 0;
        int i = 1;
        prefexSuffixValue[0] = 0;
        while (i < M) {
            if (pattern[i] == pattern[len]) {
                len++;
                prefexSuffixValue[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = prefexSuffixValue[len - 1];
                } else {
                    prefexSuffixValue[i] = len;
                    i++;
                }
            }
        }
    }
}
