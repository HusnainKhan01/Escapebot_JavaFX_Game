package logic;

/**
 * Used to store Program
 */
public class Program {

    /**
     * All the moves in the program
     */
    private Instructions[] movements;

    /**
     * Constructor for setting the total number of rows and col for the program
     */
    Program(){
        clearMoves();
    }

    /**
     * clear moves
     */
    void clearMoves(){
        movements = null;
    }

    /**
     * Used to copy given moves to movements
     * @param moves moves to be copied
     */
    void copyGivenMovements(Instructions[] moves){
        int count = 0;
        for(Instructions m: moves){
            if(m != null){
                count++;
            }
        }
        movements = new Instructions[count];
        for(int i = 0; i < count; i++){
            movements[i] = moves[i];
        }
    }

    /**
     * Used to get the movement array
     * @return movements for this program
     */
    Instructions[] getMovementArray(){
        return movements;
    }

}
