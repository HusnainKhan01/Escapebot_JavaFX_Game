package logic;

import java.util.LinkedList;

/**
 * GUI Controller
 */
public interface GUIConnector {

    /**
     * set given map on gui
     * @param map map
     */
    void setMapOnGui(FieldType[][] map);

    /**
     * set to new game
     */
    void setToNewGame();

    /**
     * set program procedure image on the gui
     * @param row row
     * @param col col
     * @param imageNum image
     * @param programProcedure program procedure number
     */
    void setProgramImage(int row, int col, int imageNum, int programProcedure);

    /**
     * clear program
     */
    void setClearProgram();

    /**
     * clear procedure1
     */
    void setClearProcedure1();

    /**
     * clear procedure2
     */
    void setClearProcedure2();

    /**
     * change bot rotation
     * @param rot rotation
     */
    void changeBotRot(int rot);

    /**
     * set the field type image
     * @param fieldType field type
     * @param row row
     * @param col col
     */
    void setImage(FieldType fieldType, int row, int col);

    /**
     * set field image
     * @param col
     * @param row
     * @param imageNum
     * @param rot
     */
    void setFieldImage(int col, int row, int imageNum, int rot);

    /**
     * handle actions, all actions are passed to be executed on gui
     * @param botPos bot start position
     * @param rot start rotaion
     * @param allActions all actions
     * @throws InterruptedException exception
     */
    void handleActions(Position botPos, int rot, LinkedList<Actions> allActions) throws InterruptedException;

    /**
     * set bot on the gui
     * @param position
     */
    void setBotOnGui(Position position);

    /**
     * remove the bot
     */
    void removeBotFrom();

    /**
     * used set any message and then wait for clear
     * @param message message to be set
     */
    void notValidGeneral(String message);

}
