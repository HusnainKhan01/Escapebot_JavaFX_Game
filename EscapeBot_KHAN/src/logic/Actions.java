package logic;

/**
 * There must exist
 * type/class(es) for actions
 * (move to position, rotate, collect coin,..).
 * An action tells the GUI what it must represent.
 *
 * USAGE --> ENUM OF ACTIONS to be passed to gui
 */
public enum  Actions {
    MOVE_IN_DIRECTION,
    CLOCK_WISE,
    ANTI_CLOCKWISE,
    COLLECT_COIN,
    JUMP_THROUGH,
    EXIT_THROUGH,

    START_P1,
    START_P2,
    END_P1,
    END_P2,

    THROUGH_ERR,
    BOT_RUN_OUT_OF_FIELD,
    BOT_RUN_TO_PIT,
    BOT_RUN_TO_WALL,
    BOT_EXIT_WITHOUT_COINS,
    BOT_NOT_FACING_DOOR,
    BOT_RUN_TO_DOOR,
    BOT_JUMP_OFF_FIELD,
    BOT_JUMP_ON_WALL,
    BOT_JUMP_ON_NORMAL,
    BOT_JUMP_ON_COIN,
    BOT_JUMP_ON_TWO_PIT,
    BOT_JUMP_ONTO_DOOR,
    BOT_JUMP_ONTO_WALL
}
