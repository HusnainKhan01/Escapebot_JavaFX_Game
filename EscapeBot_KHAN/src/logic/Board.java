package logic;

import java.util.LinkedList;

import static logic.FieldType.*;
import static logic.Game.COLS;
import static logic.Game.ROWS;

/**
 * Board Class
 */
public class Board {

    /**
     * Fixed Maps used in the game
     */
    static final FieldType[][][] ALL_MAPS = {
            {
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {NORMAL,     NORMAL,    NORMAL,    NORMAL,    NORMAL,    NORMAL,    NORMAL,   DOOR     },
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
            },
            {
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {NORMAL,     NORMAL,    NORMAL,    NORMAL, PIT,      NORMAL,    NORMAL,    NORMAL,  },
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, NORMAL   },
                    {DOOR,     NORMAL,    NORMAL,    NORMAL, PIT,      NORMAL,    NORMAL,    NORMAL,  },
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL, GRAY_WALL},
            },
            {
                    {NORMAL,     NORMAL,    NORMAL,    NORMAL,    NORMAL,      NORMAL,    NORMAL, COIN},
                    {GRAY_WALL, NORMAL, PIT, PIT, PIT, PIT, PIT,      NORMAL },
                    {DOOR,      NORMAL, PIT, PIT, PIT, PIT, PIT,      NORMAL },
                    {NORMAL, PIT, PIT, PIT, PIT, PIT, PIT,      NORMAL },
                    {NORMAL, PIT, PIT, PIT, PIT, PIT, PIT,      NORMAL },
                    {NORMAL, PIT, PIT, PIT, PIT, PIT, PIT,      NORMAL },
                    {NORMAL, PIT, PIT, PIT, PIT, PIT, PIT,      NORMAL },
                    {COIN,    NORMAL,    NORMAL,    NORMAL,    NORMAL,      NORMAL,    NORMAL, COIN},
            },
            {
                    {NORMAL,    GRAY_WALL, NORMAL,    NORMAL,    NORMAL,    GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, NORMAL,    GRAY_WALL, GRAY_WALL, GRAY_WALL},
                    {NORMAL,    NORMAL,    NORMAL,    GRAY_WALL, NORMAL,    NORMAL,    NORMAL,    DOOR     }
            }
    };

    /**
     * Fix rotation of bot for every Level
     */
    static final int[] ROT_OF_BOT_BY_MAP = {0, 0, 0, 1};

    /**
     * Fix positions of bot for every Level
     */
    static final Position[] BOT_POS_BY_MAP = {
            new Position(4, 0), new Position(3, 0),
            new Position(0, 0), new Position(0, 0)
    };

    /**
     * Created Level and rotation of bot for the currentLevel,
     * createdLevel is actually current Level which is on the field
     */
    private FieldType[][] createdLevel;

    /**
     * Bot rotation and bot position
     */
    private int botRotCreatedLevel;
    private Position botPositionCreatedLevel;

    /**
     * Used to store starting position and rotation of the bot
     * will be used to reset the bot to the starting position
     */
    private Position botStartPos;
    private int botStartRot;

    /**
     * Used to store positions of all the collected coins
     */
    private LinkedList<Position> collectedCoinsPos = new LinkedList<>();

    /**
     * Constructor,
     * Default map (Which has all normal FieldTypes in it) doesn't contain bot
     * so (botPositionCreatedLevel = null)
     */
    public Board(){
        defaultMap();
    }

    /**
     * Used in Test cases only
     * There must be a constructor that accepts a
     * (smaller) game board including the field types.
     * @param map game map
     */
    public Board(FieldType[][] map, Position botPosition){
        this.createdLevel = map;
        this.botPositionCreatedLevel = botPosition;
        botRotCreatedLevel = 0;
    }

    /**
     * Used for Testing
     * There must be a constructor that takes
     * a game board and the bot position/orientation.
     * @param map map or board
     * @param botRot bot orientation
     */
    public Board(FieldType[][] map, Position botPosition, int botRot){
        this.createdLevel = map;
        botRotCreatedLevel = botRot;
        botPositionCreatedLevel = botPosition;
    }

    /**
     * Used to set the bot start position from botPositionCreatedLevel
     */
    void setBotStartPosStartRot(){
        if(botPositionCreatedLevel == null){
            botStartPos = null;
            botStartRot = 0;
        } else {
            botStartPos = new Position(botPositionCreatedLevel.getRow(), botPositionCreatedLevel.getCol());
            botStartRot = botRotCreatedLevel;
        }
    }

    /**
     * Used to set the botPositionCreatedLevel from botStartPos
     */
    void setBotPositionRotCreatedLevelToStarting(){
        if(botStartPos == null){
            botPositionCreatedLevel = null;
            botRotCreatedLevel = 0;
        } else {
            botPositionCreatedLevel = new Position(botStartPos.getRow(), botStartPos.getCol());
            botRotCreatedLevel = botStartRot;
        }
    }

    public void setCreatedLevel(FieldType[][] field){
        for(int i = 0; i < field.length; i++){
            System.arraycopy(field[i], 0, createdLevel[i], 0, field[0].length);
        }
    }

    /**
     * Used to get map at given index
     *
     * @param map map number
     * @return Map copy of the map so if some changes are made at currentLevel
     *         ALL_MAPS are not changed
     */
    public FieldType[][] getMap(int map) {
        return copyMap(ALL_MAPS[map]);
    }

    /**
     * Used to get the created Level
     * @return Used to get the createdLevel
     */
    public FieldType[][] getCreatedLevel() {
        return createdLevel;
    }

    /**
     * Used to get the rotation of bot at the created level
     * @return rotation of bot in the created level
     */
    int getBotRotCreatedLevel(){
        return botRotCreatedLevel;
    }

    /**
     * Used to set the rotation of the bot
     * @param rot rotation of the bot
     */
    void setRotOfBotByMap(int rot) throws Exception {
        if(rot > 4){
            throw new Exception("Rot value should not be greater then 4");
        } else if(rot == 4){
            botRotCreatedLevel = 0;
        } else if(rot == -1){
            botRotCreatedLevel = 3;
        } else {
            botRotCreatedLevel = rot;
        }
    }

    /**
     * Clear all the collected coins
     */
    void clearCollectedCoins(){
        collectedCoinsPos = new LinkedList<>();
    }

    /**
     * Used to get the character at the given position
     * @param position Position to be checked
     * @return character at given position
     */
    FieldType getMapcharAt(Position position){
        if(mapHasABot() && position.equals(getBotPositionCreatedLevel())){
            return BOT_FIELD_TYPE;
        }
        return createdLevel[position.getRow()][position.getCol()];
    }

    /**
     * Getter for botPositionCreatedLevel
     * @return botPositionCreatedLevel
     */
    Position getBotPositionCreatedLevel(){
        return botPositionCreatedLevel;
    }

    /**
     * Used to get the position of the door in the createdLevel
     * @return position of the door
     */
    Position getDoorPositionCreatedLevel(){
        boolean doorIsFound = false;
        Position doorPos = null;
        for(int i = 0; i < createdLevel.length && !doorIsFound; i++){
            for(int j = 0; j < createdLevel[i].length && !doorIsFound; j++){
                if(createdLevel[i][j] == DOOR){
                    doorPos = new Position(i, j);
                    doorIsFound = true;
                }
            }
        }
        return doorPos;
    }

    /**
     * Used to set the given character to given position
     * @param position position
     * @param fieldType character to be set at given position
     */
    public void setCharToCreatedMap(Position position, FieldType fieldType) throws Exception {
        if(fieldType == BOT_FIELD_TYPE){
            setBotPosition(position);
            setCharToCreatedMap(position, NORMAL);
        } else {
            createdLevel[position.getRow()][position.getCol()] = fieldType;
        }
    }

    /**
     * used to check if the map has a bot of not
     * @return true if the createdLevel has a bot else false
     */
    boolean mapHasABot(){
       return botPositionCreatedLevel != null;
    }

    /**
     * Used to check if the map has the given character,
     * @param fieldType character should be bot or Door one of those
     * @return true if map has the given character
     */
    boolean mapHasAChar(FieldType fieldType){
        if(fieldType == BOT_FIELD_TYPE){
            return mapHasABot();
        }
        boolean toReturn = false;
        for(int i = 0; i < createdLevel.length && !toReturn; i++){
            for(int j = 0; j < createdLevel[i].length && !toReturn; j++){
                if(fieldType == createdLevel[i][j]){
                    toReturn = true;
                }
            }
        }
        return toReturn;
    }

    /**
     * Used to check if the given position has a bot
     * @param position given position to be compared with the bot position
     * @return true if bot is on given position
     */
    boolean hasABotOn(Position position){
        if(botPositionCreatedLevel == null){
            return false;
        }
        return botPositionCreatedLevel.equals(position);
    }

    /**
     * Used to check if there is a door on the given position
     * @param position position which has to be checked
     * @return true if it has a door on given position
     */
    boolean hasADoorOn(Position position){
        return isValidPos(position) && createdLevel[position.getRow()][position.getCol()] == DOOR;
    }

    /**
     * Used to check if the given position has a coin on it
     * @param position given position to be checked
     * @return true if it has a coin
     */
    boolean hasACoinOn(Position position){
        return createdLevel[position.getRow()][position.getCol()] == COIN;
    }

    /**
     * Used to check if the given position has a given character
     * @param fieldType character to be checked for the given position
     * @param position position which is to be checked
     * @return true if the given position has a character
     */
    boolean hasACharOn(FieldType fieldType, Position position){
        return createdLevel[position.getRow()][position.getCol()] == fieldType;
    }

    /**
     * Used to set currentMap, rotation of bot
     * and position of the bot
     * Whenever map is set from the final maps
     * the position of the bot and rotation is also set
     * @param map map to be set
     */
    public void setCurrentMap(int map) throws Exception {
        for(int i = 0; i < ALL_MAPS[map].length; i++){
            System.arraycopy(ALL_MAPS[map][i], 0, createdLevel[i], 0, ALL_MAPS[map][i].length);
        }
        setRotOfBotByMap(ROT_OF_BOT_BY_MAP[map]);
        setBotPosition(getBotPosByMap(map));

        // set the starting position and startRot from the current position
        setBotStartPosStartRot();
    }

    /**
     * Used to set the bot position
     * @param botPos position of bot to be set, null if the bot is not on board
     */
    void setBotPosition(Position botPos) throws Exception {
        if(botPos == null){
            botPositionCreatedLevel = null;
            setRotOfBotByMap(0);
        } else {
            botPositionCreatedLevel = new Position(botPos.getRow(), botPos.getCol());
        }
    }

    /**
     * Can change the position of any MapChar on the board but only used for bot and door
     * Used to change the bot position to given position
     * Also used to change the position of any mapChar to given position
     *
     * Condition: Only called when there is a bot on the map else not called
     *
     * @param fieldType MapChar whose position tgo be changed
     * @param row row
     * @param col col
     * @return the previous position of the bot, which is
     *         used to remove bot from the gui
     */
    public Position changeMapCharPositionTo(FieldType fieldType, int row, int col) throws Exception {
        Position prevPos = null;
        if(fieldType == BOT_FIELD_TYPE){
            prevPos = new Position(botPositionCreatedLevel.getRow(), botPositionCreatedLevel.getCol());
            botPositionCreatedLevel = new Position(row, col);
            setCharToCreatedMap(prevPos, NORMAL);
            setCharToCreatedMap(new Position(row, col), NORMAL);
            //setRotOfBotByMap(0);
            return prevPos;
        }

        boolean stop = false;
        for(int i = 0; i < createdLevel.length && !stop; i++){
            for(int j = 0; j < createdLevel[0].length && !stop; j++){
                if(createdLevel[i][j] == fieldType){
                    prevPos = new Position(i, j);
                    createdLevel[i][j] = NORMAL;
                    createdLevel[row][col] = DOOR;
                    stop = true;
                }
            }
        }
        return prevPos;
    }

    /**
     * set created level to default which is Normal
     * for every position
     * Also set the botPosition to null which means that
     * there is no Bot on the board
     * and Rot to Zero as default
     */
    void defaultMap(){
        createdLevel = new FieldType[ROWS][COLS];
        for(int i = 0; i < ROWS; i++){
            for(int j = 0; j < COLS; j++){
                createdLevel[i][j] = NORMAL;
            }
        }
        botRotCreatedLevel = 0;
        botPositionCreatedLevel = null;
    }

    /**
     * Used to copy given map to another
     * @param map given map to be copied
     * @return copy of the given map
     */
    FieldType[][] copyMap(FieldType[][] map){
        FieldType[][] toReturn = new FieldType[map.length][map.length];
        for(int i = 0; i < map.length; i++){
            System.arraycopy(map[i], 0, toReturn[i], 0, map[i].length);
        }
        return toReturn;
    }

    /**
     * Used to get new position by map from the final position of the bot
     * for every map
     * return null if the map is greater then the provided map
     * @param map map number
     * @return new Position from the BOT_POS_BY_MAP
     */
    Position getBotPosByMap(int map) throws Exception {
        if(map >= BOT_POS_BY_MAP.length) {
            throw new Exception("value of map should not not be greater then total maps available ");
        }
        return new Position(BOT_POS_BY_MAP[map].getRow(), BOT_POS_BY_MAP[map].getCol());
    }

    /**
     * Used to check if the current map is Valid
     * Map is valid if it has Only one DOOR and a Bot
     * @return true if it currentMap is valid
     */
    boolean isValidMap(){
        return mapHasABot() && mapHasAChar(DOOR);
    }

    /**
     * Used to check if the given position is Valid,
     * For Valid Position it has to be on Board
     * @param pos position to be checked
     * @return true if the position is valid
     */
    boolean isValidPos(Position pos){
        return pos.getRow() < createdLevel.length && pos.getRow() >= 0 && pos.getCol() < createdLevel[0].length && pos.getCol() >= 0;
    }

    /**
     * Used to move the bot in the direction in which it is pointing to
     * @return false if move is not possible
     */
    boolean moveBot() throws Exception {
        return moveBotHelp(getBotPositionCreatedLevel().getNextPosition(getBotRotCreatedLevel()));
    }

    /**
     * if the next pos has WALL Or GreyWall then return false
     * else make the bot to next position
     * @param posToCheck position to check
     * @return false if the given position has PIT or GRAY_WALL or DOOR else set the bot position
     *         to the given position and return true which means move is possible
     */
    private boolean moveBotHelp(Position posToCheck) throws Exception {
        if(isValidPos(posToCheck)){
            if(hasACharOn(PIT, posToCheck) || hasACharOn(GRAY_WALL, posToCheck) || hasADoorOn(posToCheck)){
                return false;
            } else {
                setBotPosition(posToCheck);
                return true;
            }
        }
        return false;
    }

    /**
     * Used to rotate bot clockwise
     */
    void rotateBotClockwise() throws Exception {
        setRotOfBotByMap(getBotRotCreatedLevel() + 1);
    }

    /**
     * Used to rotate bot Anticlockwise
     */
    void rotateBotAntiClockwise() throws Exception {
        setRotOfBotByMap(getBotRotCreatedLevel() - 1);
    }

    /**
     * Used to jump the bot over pit
     * @return true if jump is possible
     */
    boolean jumpBot() throws Exception {
        boolean toReturn = false;
        Position pos = getBotPositionCreatedLevel().getNextPosition(getBotRotCreatedLevel());
        if(isValidPos(pos) && hasACharOn(PIT, pos)){
            Position nextPos = pos.getNextPosition(getBotRotCreatedLevel());
            if(isValidPos(nextPos) && !hasACharOn(PIT, nextPos) && !hasACharOn(GRAY_WALL, nextPos) && !hasADoorOn(nextPos)){
                setBotPosition(nextPos);
                toReturn = true;
            }
        }
        return toReturn;
    }

    /**
     * Used to collect coin from the current Bot position if it has
     * @return true if current bot position has a coin
     *         collect the coin to the linkedList
     */
    boolean collectCoin() {
        boolean collectionIsPossible = false;
        if(hasACharOn(COIN, getBotPositionCreatedLevel())){
            collectedCoinsPos.add(getBotPositionCreatedLevel());
            collectionIsPossible = true;
        }
        return collectionIsPossible;
    }

    /**
     * Used for generating actions
     * Used to check if exit is possible
     * exit is only possible when bot current position is on door
     * and all coins are collected
     * @return true if exit is possible
     */
    boolean exitIsPossible(){
        return coinsAreCollected() && hasADoorOn(getBotPositionCreatedLevel().getNextPosition(getBotRotCreatedLevel()));
    }

    /**
     * Used to check if all coins are collected
     * @return true if all coins are collected
     */
    boolean coinsAreCollected(){
        boolean collected = true;
        LinkedList<Position> allCoinsPos = getAllCoinsPositions();
        for(Position coinPos: allCoinsPos){
            if(!collectedCoinsPos.contains(coinPos)){
                collected = false;
            }
        }
        return collected;
    }

    /**
     * Used to get all the coins positions on the board
     * @return positions of all the coins
     */
    LinkedList<Position> getAllCoinsPositions(){
        LinkedList<Position> allPosition = new LinkedList<>();
        for(int i = 0; i < createdLevel.length; i++){
            for(int j = 0; j < createdLevel[i].length; j++){
                if(hasACoinOn(new Position(i, j))){
                    allPosition.add(new Position(i, j));
                }
            }
        }
        return allPosition;
    }

}
