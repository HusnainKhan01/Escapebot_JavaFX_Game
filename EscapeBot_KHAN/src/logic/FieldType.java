package logic;

/**
 * Store all Field Types used on the board
 */
public enum FieldType {
    PIT, COIN, DOOR,
    NORMAL, BOT_FIELD_TYPE, GRAY_WALL;

    /**
     * Check if this is a door
     * @return true if this is a door
     */
    boolean isADoor(){
        return this == DOOR;
    }
}
