package logic;

import java.util.LinkedList;
import java.util.Objects;

/**
 * Position
 */
public class Position {

    /**
     * Position Row and Col
     */
    private int row;
    private int col;

    /**
     * Used when neighbours are generated
     */
    final int[][] neighboursPos = {
            {0, 1}, {0, -1}, {1, 0}, {-1, 0}
    };

    /**
     * Constructor
     * @param row row
     * @param col col
     */
    public Position(int row, int col){
        this.row = row;
        this.col = col;
    }

    /**
     * Getter for Col
     * @return col
     */
    public int getCol() {
        return col;
    }

    /**
     * Getter for Row
     * @return row
     */
    public int getRow() {
        return row;
    }

    /**
     * Used to get the next Position
     * @param rot rotation of the bot
     * @return next Position
     */
    public Position getNextPosition(int rot){
        if(rot == 0){
            return new Position(this.getRow(),this.getCol() + 1);
        } else if(rot == 1){
            return new Position(this.getRow() + 1, this.getCol());
        } else if(rot == 2){
            return new Position(this.getRow(), this.getCol() - 1);
        } else {
            return new Position(this.getRow() - 1, this.getCol());
        }
    }

    /**
     * Used to get the neightbours of the given position
     * @return neighbours
     */
    LinkedList<Position> getNeighbours(Board board, boolean isForDoor){
        LinkedList<Position> allNeighbours = new LinkedList<>();
        for(int i = 0; i < neighboursPos.length; i++){
            Position position = new Position(this.getRow() + neighboursPos[i][0],this.getCol() + neighboursPos[i][1]);
            if(board.isValidPos(position)){
                FieldType fieldType = board.getMapcharAt(position);
                FieldType fieldTypeForDoor = board.getMapcharAt(position);
                if(board.getMapcharAt(this) == FieldType.DOOR){
                    if(fieldTypeForDoor != FieldType.GRAY_WALL && fieldTypeForDoor != FieldType.PIT){
                        allNeighbours.add(position);
                    }
                } else if(fieldType == FieldType.PIT){
                    position = new Position(this.getRow() + 2 * (neighboursPos[i][0]),
                            this.getCol() + 2 * (neighboursPos[i][1]));
                    if(board.isValidPos(position)){
                        fieldType = board.getMapcharAt(position);
                        if(fieldType != FieldType.GRAY_WALL && fieldType != FieldType.PIT){
                            allNeighbours.add(position);
                        }
                    }
                } else if(!isForDoor && fieldType != FieldType.DOOR && fieldType != FieldType.GRAY_WALL){
                    allNeighbours.add(position);
                } else if(fieldType != FieldType.GRAY_WALL && isForDoor){
                    allNeighbours.add(position);
                }
            }
        }
        return allNeighbours;
    }

    /**
     * used to get the previous position
     * USED IN GUI TO GET THE PREVIOUS POSITION TO CHECK IF THERE IS AN ELEMENT ON THE PREV POS
     * @param totalCols TOTAL COLS OF IMAGEVIEW
     * @return PREVIOUS POSITION
     */
    public Position getPreviousPos(int totalCols){
        if(this.getCol() - 1 >= 0){
            return new Position(this.getRow(), this.getCol() - 1);
        } else if(this.getRow() - 1 >= 0){
            return new Position(this.getRow() - 1, totalCols - 1);
        } else {
            return null;
        }
    }

    /**
     * Used to get the next position for the given maxRows and maxCols
     * @param maxRows maxRows
     * @param maxCols maxCols
     * @return next Position
     */
    public Position getNextPos(int maxRows, int maxCols){
        Position nextPos;
        if(this.getCol() + 1 < maxCols){
            nextPos = new Position(this.getRow(), this.getCol() + 1);
        } else {
            nextPos = new Position(this.getRow() + 1, 0);
        }
        return nextPos;
    }

    /**
     * Equals for the position
     * @param o object to be compared
     * @return true if equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row &&
                col == position.col;
    }

    /**
     * Hash code for row and col
     * @return hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
