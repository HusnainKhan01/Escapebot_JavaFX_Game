package logic;

import com.google.gson.Gson;

import static logic.FieldType.*;

/**
 * Used to save file
 */
public class SavingFile {

    /**
     * Field or integers and bot rotation
     */
    private int[][] field;
    private int botRotation;

    /**
     * bot position
     */
    private Position botPosition;

    /**
     * Constructor
     * @param field field of integers
     * @param botRotation rotation
     */
    SavingFile(int[][] field, int botRotation){
        this.field = field;
        this.botRotation = botRotation;
    }

    SavingFile(){
    }

    int getBotRotationFromForLoading() {
        return convertForLoad(botRotation);
    }

    Position getBotPositionForLoad() {
        return botPosition;
    }

    /**
     * To be called for saving file
     * @param board instance of Map
     * @return string
     */
    String saveFile(Board board){
        int[][] field = convertedFieldToIntValues(board);
        SavingFile file = new SavingFile(field, convertRotForSave(board.getBotRotCreatedLevel()));
        Gson gson = new Gson();
        return gson.toJson(file);
    }

    /**
     * conversion
     * @param board bord
     * @return field of int
     */
    private int[][] convertedFieldToIntValues(Board board){
        FieldType[][] currentMap = board.getCreatedLevel();
        int[][] field = new int[currentMap.length][currentMap[0].length];
        for(int i = 0; i < currentMap.length; i++){
            for(int j = 0; j < currentMap[i].length; j++){
                field[i][j] = getIntValueOfMapChar(new Position(i, j), board);
            }
        }
        return field;
    }

    /**
     * Convert the bot rotation for the rotation to be saved
     * Bot rotation 0 ---> 1, 1 ---> 2, 2 ---> 3, 3 ---> 0
     * @param givenRotation rotation
     * @return converted rotation
     */
    private int convertRotForSave(int givenRotation){
        int toReturn = 0;
        if(givenRotation != 3){
            toReturn = givenRotation + 1;
        }
        return toReturn;
    }

    /**
     * saved rotation to bot Rotation
     * 1 --> 0, 2 --> 1, 3 --> 2, 0 --> 3
     * @param givenRotation given rotation
     * @return rotation converted to be loaded for the bot
     */
    private int convertForLoad(int givenRotation){
        int toReturn = 3;
        if(givenRotation != 0){
            toReturn = givenRotation - 1;
        }
        return toReturn;
    }

    /**
     * Used to check if the given rotation is valid
     * @param botRotation bot rotation to be checked
     * @return true if rotation is valid
     */
    private boolean isValidRotationForBot(int botRotation){
        return botRotation <= 3 && botRotation >= 0;
    }

    /**
     * Used to be called for loading file
     */
    FieldType[][] loadingFile(){
        FieldType[][] toReturn = new FieldType[8][8];
        if(isValidField(this.field) && isValidRotationForBot(this.botRotation)){
            // convert the given values to MapChar and set them to current map
            for(int i = 0; i < 8; i++){
                for(int j = 0; j < 8; j++){
                    toReturn[i][j] =  getMapCharFromFieldValue(this.field[i][j]);
                    if(toReturn[i][j] == BOT_FIELD_TYPE){
                        botPosition = new Position(i, j);
                        toReturn[i][j] = NORMAL;
                    }
                }
            }
        } else {
            toReturn = null;
        }

        return toReturn;
        // get the bot position and set to bot position,
        // get bot rotation and set the bot rotation
    }

    /**
     * Used to check if the loaded field is valid or not
     * @param field field to be loaded
     * @return true if is valid
     */
    boolean isValidField(int[][] field){
        int rows = field.length;
        int cols = field[0].length;
        boolean isValidField = true;
        if(rows < 8 || cols < 8){
            return false;
        }
        for(int i = 0; i < rows && isValidField; i++){
            for(int j = 0; j < cols && isValidField; j++){
                if(field[i][j] < 0 || field[i][j] > 5){
                    isValidField = false;
                }
            }
        }
        return isValidField;
    }

    /**
     * Used to get the Int Value of Map character at given position
     * @param position position to b checked
     * @param board map instance
     * @return Int value
     */
    Integer getIntValueOfMapChar(Position position, Board board){
        Integer toReturn = null;
        if(board.hasABotOn(position)){
            toReturn = 4;
        } else if(board.hasADoorOn(position)){
            toReturn = 2;
        } else if(board.getMapcharAt(position) == PIT){
            toReturn = 0;
        } else if(board.hasACoinOn(position)){
            toReturn = 1;
        } else if(board.hasACharOn(GRAY_WALL, position)){
            toReturn = 5;
        } else if(board.hasACharOn(NORMAL, position)){
            toReturn = 3;
        }
        return toReturn;
    }

    /**
     * Used to calculate MapChar from field Value
     * @param fieldValue field Value
     * @return MapChar
     */
    FieldType getMapCharFromFieldValue(int fieldValue){
        FieldType toReturn = null;
        if(fieldValue == 4){
            toReturn = BOT_FIELD_TYPE;
        } else if(fieldValue == 2){
            toReturn = DOOR;
        } else if(fieldValue == 0){
            toReturn = PIT;
        } else if(fieldValue == 1){
            toReturn = COIN;
        } else if(fieldValue == 5){
            toReturn = GRAY_WALL;
        } else if(fieldValue == 3){
            toReturn = NORMAL;
        }
        return toReturn;
    }
}
