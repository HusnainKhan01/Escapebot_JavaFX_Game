package logic;

import org.junit.Test;

import static logic.FieldType.*;
import static logic.FieldType.GRAY_WALL;
import static logic.Board.ALL_MAPS;
import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void getMaps() {
        Board board = new Board();

        assertArrayEquals(ALL_MAPS[0], board.getMap(0));
        assertArrayEquals(ALL_MAPS[1], board.getMap(1));
        assertArrayEquals(ALL_MAPS[2], board.getMap(2));
        assertArrayEquals(ALL_MAPS[3], board.getMap(3));

        FieldType[][] map = board.getMap(0);
        map[0][0] = NORMAL;
        map[0][1] = NORMAL;
        map[0][2] = NORMAL;

        assertArrayEquals(ALL_MAPS[0], board.getMap(0));

    }

    @Test
    public void getCreatedLevel() throws Exception {
        Board board = new Board();

        board.setCurrentMap(0);
        FieldType[][] map = board.getCreatedLevel();

        assertArrayEquals(board.getMap(0), map);

        map[0][0] = NORMAL;

        assertNotEquals(board.getMap(0), map);

        assertEquals(4, board.getBotPositionCreatedLevel().getRow());
        assertEquals(0, board.getBotPositionCreatedLevel().getCol());

    }

    @Test
    public void setCharToCreatedMap() throws Exception {
        Board board = new Board();
        board.setCharToCreatedMap(new Position(1, 1), FieldType.PIT);
        board.setCharToCreatedMap(new Position(2, 2), FieldType.PIT);
        board.setCharToCreatedMap(new Position(3, 3), FieldType.PIT);

        FieldType[][] createdMap = board.getCreatedLevel();
        for(int i = 0; i < board.getCreatedLevel().length; i++){
            for(int j = 0; j < board.getCreatedLevel()[i].length; j++){
                if(i == 1 && j == 1 || i == 2 && j == 2 || i == 3 && j == 3 ){
                    assertEquals(PIT, createdMap[i][j]);
                } else {
                    assertEquals(NORMAL, createdMap[i][j]);
                }
            }
        }

    }

    @Test
    public void changeBotPositionTo() throws Exception {
        Board board = new Board();

        // Null if the board doesn't have bot
        assertNull(board.getBotPositionCreatedLevel());
        assertNull(board.getDoorPositionCreatedLevel());

        board.setCurrentMap(0);

        assertEquals(4, board.getBotPositionCreatedLevel().getRow());
        assertEquals(0, board.getBotPositionCreatedLevel().getCol());

        board.changeMapCharPositionTo(BOT_FIELD_TYPE, 5, 5);
        assertEquals(5, board.getBotPositionCreatedLevel().getRow());
        assertEquals(5, board.getBotPositionCreatedLevel().getCol());

        board.changeMapCharPositionTo(DOOR, 5, 5);
        assertEquals(5, board.getBotPositionCreatedLevel().getRow());
        assertEquals(5, board.getBotPositionCreatedLevel().getCol());

    }

    @Test
    public void getBotRotCreatedLevel() throws Exception {
        Board board = new Board();
        assertEquals(0, board.getBotRotCreatedLevel());

        board.setCurrentMap(3);
        assertEquals(1, board.getBotRotCreatedLevel());
    }

    @Test
    public void setRotOfBotByMap() throws Exception {
        Board board = new Board();
        board.setCurrentMap(0);
        assertEquals(0, board.getBotRotCreatedLevel());

        board.setCurrentMap(1);
        assertEquals(0, board.getBotRotCreatedLevel());

        board.setCurrentMap(2);
        assertEquals(0, board.getBotRotCreatedLevel());

        board.setCurrentMap(3);
        assertEquals(1, board.getBotRotCreatedLevel());
    }

    @Test
    public void getMapcharAt() throws Exception {
        Board board = new Board();
        board.setCurrentMap(0);

        assertEquals(GRAY_WALL, board.getMapcharAt(new Position(0, 0)));
        assertEquals(BOT_FIELD_TYPE, board.getMapcharAt(new Position(4, 0)));
        assertEquals(NORMAL, board.getMapcharAt(new Position(4, 1)));
        assertEquals(NORMAL, board.getMapcharAt(new Position(4, 2)));
        assertEquals(NORMAL, board.getMapcharAt(new Position(4, 3)));

    }

    @Test
    public void getBotPositionCreatedLevel() throws Exception {
        Board board = new Board();

        board.setCurrentMap(0);
        FieldType[][] map = board.getCreatedLevel();

        assertArrayEquals(board.getMap(0), map);

        map[0][0] = NORMAL;

        assertNotEquals(board.getMap(0), map);

        assertEquals(4, board.getBotPositionCreatedLevel().getRow());
        assertEquals(0, board.getBotPositionCreatedLevel().getCol());


        board.changeMapCharPositionTo(BOT_FIELD_TYPE, 5, 5);
        assertEquals(5, board.getBotPositionCreatedLevel().getRow());
        assertEquals(5, board.getBotPositionCreatedLevel().getCol());

    }

    @Test
    public void testSetCharToCreatedMap() throws Exception {
        Board board = new Board();
        board.setCharToCreatedMap(new Position(0, 0), BOT_FIELD_TYPE);

        assertEquals(BOT_FIELD_TYPE, board.getMapcharAt(new Position(0, 0)));
    }

    @Test
    public void mapHasABot() throws Exception {
        Board board = new Board();

        assertFalse(board.mapHasABot());

        board.setCharToCreatedMap(new Position(0, 0), BOT_FIELD_TYPE);

        assertTrue(board.mapHasABot());
    }

    @Test
    public void masHasAChar() throws Exception {
        Board board = new Board();

        assertFalse(board.mapHasAChar(BOT_FIELD_TYPE));
        assertFalse(board.mapHasAChar(DOOR));

        board.setCurrentMap(0);
        assertTrue(board.mapHasAChar(BOT_FIELD_TYPE));
        assertTrue(board.mapHasAChar(DOOR));

    }

    @Test
    public void hasABotOn() throws Exception {
        Board board = new Board();

        board.setCurrentMap(0);
        assertTrue(board.hasABotOn(new Position(4, 0)));

        assertFalse(board.hasABotOn(new Position(4, 4)));

        board.changeMapCharPositionTo(BOT_FIELD_TYPE, 0, 0);
        assertFalse(board.hasABotOn(new Position(4, 4)));
        assertTrue(board.hasABotOn(new Position(0, 0)));

    }

    @Test
    public void hasADoorOn() throws Exception {
        Board board = new Board();

        board.setCurrentMap(0);
        assertTrue(board.hasADoorOn(new Position(4, 7)));

    }

    @Test
    public void copyMap() {
    }

    /**
     * Used to check if the given map is Valid
     * Map is valid if it has Only one DOOR and also has
     * Only ONE BOT
     */
    @Test
    public void isValidMap() {

        FieldType[][] map = {
                {DOOR, DOOR, NORMAL, NORMAL, NORMAL},
                {DOOR, DOOR, NORMAL, NORMAL, NORMAL},
                {DOOR, DOOR, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] map1 = {
                {DOOR,   NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, NORMAL, NORMAL, NORMAL, DOOR  },
                {NORMAL, NORMAL, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] map2 = {
                {DOOR,  NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] map3 = {
                {NORMAL, NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] map4 = {
                {NORMAL,   DOOR, NORMAL, NORMAL, NORMAL},
                {NORMAL, DOOR, NORMAL, NORMAL, NORMAL},
                {DOOR,    DOOR, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] map5 = {
                {NORMAL,   NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,  NORMAL, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] map6 = {
                {NORMAL,  NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL, NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,  NORMAL, NORMAL, NORMAL,  NORMAL}
        };

        FieldType[][] map7 = {
                {NORMAL,  NORMAL, NORMAL, NORMAL, NORMAL },
                {NORMAL, NORMAL, NORMAL, NORMAL, NORMAL },
                {NORMAL,  NORMAL, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] map8 = {
                {NORMAL,  NORMAL, NORMAL, NORMAL, NORMAL },
                {NORMAL, NORMAL, NORMAL, DOOR,   NORMAL },
                {NORMAL,  NORMAL, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] map9 = {
                {NORMAL,  DOOR},
        };

        FieldType[][] map10 = {
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, GRAY_WALL },

        };

        FieldType[][] map11 = {
                {DOOR,    NORMAL,    NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, DOOR      },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, NORMAL, NORMAL,    GRAY_WALL },

        };

        FieldType[][] map12 = {
                {DOOR,    NORMAL,    NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, PIT,   GRAY_WALL, GRAY_WALL },
                {NORMAL,  GRAY_WALL, PIT,   GRAY_WALL, NORMAL    },
                {NORMAL,  GRAY_WALL, COIN, GRAY_WALL, DOOR      },
                {NORMAL,  GRAY_WALL, NORMAL, GRAY_WALL, COIN},
                {NORMAL,  GRAY_WALL, NORMAL, NORMAL,    GRAY_WALL },
        };

        FieldType[][] map13 = {
                {GRAY_WALL, NORMAL,    NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,    GRAY_WALL, PIT,   GRAY_WALL, GRAY_WALL },
                {NORMAL,    GRAY_WALL, PIT,   GRAY_WALL, NORMAL    },
                {NORMAL,    GRAY_WALL, COIN, GRAY_WALL, DOOR      },
                {NORMAL,    GRAY_WALL, NORMAL, GRAY_WALL, COIN},
                {NORMAL,    GRAY_WALL, NORMAL, NORMAL,    NORMAL   },
        };


        Board m1 = new Board(map, new Position(0, 0));
        Board m2 = new Board(map1, new Position(0, 0));
        Board m3 = new Board(map2, new Position(0, 0));
        Board m4 = new Board(map3, null);
        Board m5 = new Board(map4, null);
        Board m6 = new Board(map5, new Position(0, 0));
        Board m7 = new Board(map6, new Position(0, 0));
        Board m8 = new Board(map7, new Position(0, 0));
        Board m9 = new Board(map8, new Position(0, 0));
        Board m10 = new Board(map9, new Position(0, 0));
        Board m11 = new Board(map10, new Position(0, 0));
        Board m12 = new Board(map11, new Position(0, 0));
        Board m13 = new Board(map12, new Position(0, 0));
        Board m14 = new Board(map13, new Position(0, 0));


        assertFalse(m1.isValidMap());
        assertFalse(m2.isValidMap());
        assertFalse(m3.isValidMap());
        assertFalse(m4.isValidMap());
        assertFalse(m5.isValidMap());
        assertFalse(m6.isValidMap());
        assertFalse(m7.isValidMap());
        assertFalse(m8.isValidMap());
        assertFalse(m9.isValidMap());
        assertFalse(m10.isValidMap());
        assertFalse(m11.isValidMap());
        assertFalse(m12.isValidMap());
        assertFalse(m13.isValidMap());
        assertFalse(m14.isValidMap());


        FieldType[][] mapTrue = {
                {DOOR,      NORMAL,    NORMAL, GRAY_WALL, GRAY_WALL },
                {NORMAL,    GRAY_WALL, PIT,   GRAY_WALL, GRAY_WALL },
                {NORMAL,    GRAY_WALL, PIT,   GRAY_WALL, NORMAL    },
                {NORMAL,    GRAY_WALL, COIN, GRAY_WALL, NORMAL    },
                {NORMAL,    GRAY_WALL, NORMAL, GRAY_WALL, COIN},
                {NORMAL,    GRAY_WALL, NORMAL, NORMAL, PIT},
        };

        FieldType[][] mapTrue1 = {
                {DOOR, PIT, PIT, NORMAL, NORMAL},
                {GRAY_WALL, PIT, PIT, NORMAL, NORMAL },
                {GRAY_WALL, PIT, PIT, NORMAL, NORMAL}
        };

        FieldType[][] mapTrue2 = {
                {DOOR, NORMAL}
        };

        FieldType[][] mapTrue3 = {
                {DOOR, PIT, PIT, PIT, NORMAL, NORMAL}
        };

        FieldType[][] mapTrue4 = {
                {PIT, DOOR,      NORMAL, NORMAL, NORMAL},
                {PIT, GRAY_WALL, NORMAL, NORMAL, NORMAL},
                {PIT, GRAY_WALL, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] mapTrue5 = {
                {PIT, NORMAL, NORMAL, NORMAL, NORMAL},
                {PIT, NORMAL, NORMAL, NORMAL, NORMAL},
                {PIT, NORMAL, NORMAL, NORMAL, NORMAL},
                {PIT, NORMAL, NORMAL, NORMAL, NORMAL},
                {PIT, PIT,   NORMAL, NORMAL, NORMAL},
                {PIT, PIT,   NORMAL, NORMAL,NORMAL},
                {PIT, PIT,   NORMAL, NORMAL, NORMAL},
                {PIT, NORMAL, NORMAL, NORMAL, NORMAL},
                {PIT, DOOR,   NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] mapTrue6 = {
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL,  NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL,  NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL,  NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL,  NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL,  NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL,  NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL,  NORMAL, DOOR  }
        };

        FieldType[][] mapTrue7 = {
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {DOOR,  NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL,  NORMAL}
        };

        FieldType[][] mapTrue8 = {
                {NORMAL,NORMAL, NORMAL, NORMAL, DOOR  },
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL,NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, NORMAL, NORMAL, NORMAL, NORMAL}
        };

        FieldType[][] mapTrue9 = {
                {NORMAL,NORMAL, NORMAL, NORMAL, PIT},
                {NORMAL,NORMAL, NORMAL, PIT, PIT},
                {NORMAL,NORMAL, NORMAL, PIT, PIT},
                {NORMAL,NORMAL, NORMAL, PIT,   DOOR},
                {NORMAL,NORMAL, NORMAL, PIT, PIT},
                {NORMAL,NORMAL, NORMAL, PIT, PIT}
        };

        FieldType[][] mapTrue10 = {
                {NORMAL,NORMAL, NORMAL, NORMAL, PIT},
                {NORMAL,NORMAL, NORMAL, PIT, PIT},
                {NORMAL,NORMAL, NORMAL, PIT, PIT},
                {NORMAL,NORMAL, PIT, PIT,   DOOR},
                {NORMAL,NORMAL, NORMAL, PIT, PIT},
                {NORMAL,NORMAL, NORMAL, PIT, PIT}
        };

        FieldType[][] mapTrue11 = {
                {NORMAL,NORMAL, NORMAL,   NORMAL, PIT},
                {NORMAL,NORMAL, NORMAL, PIT, PIT},
                {NORMAL,NORMAL, NORMAL, PIT, PIT},
                {NORMAL, NORMAL, GRAY_WALL, PIT,   DOOR},
                {NORMAL,NORMAL, NORMAL, PIT, PIT},
                {NORMAL,NORMAL, NORMAL, PIT, PIT}
        };

        Board m_1 = new Board(mapTrue, new Position(0, 0));
        Board m_2 = new Board(mapTrue1, new Position(0, 0));
        Board m_3 = new Board(mapTrue2, new Position(0, 0));
        Board m_4 = new Board(mapTrue3, new Position(0, 0));
        Board m_5 = new Board(mapTrue4, new Position(0, 0));
        Board m_6 = new Board(mapTrue5, new Position(0, 0));
        Board m_7 = new Board(mapTrue6, new Position(0, 0));
        Board m_8 = new Board(mapTrue7, new Position(0, 0));
        Board m_9 = new Board(mapTrue8, new Position(0, 0));
        Board m_10 = new Board(mapTrue9, new Position(0, 0));
        Board m_11 = new Board(mapTrue10, new Position(0, 0));
        Board m_12 = new Board(mapTrue, new Position(0, 0));


        assertTrue(m_1.isValidMap());
        assertTrue(m_2.isValidMap());
        assertTrue(m_3.isValidMap());
        assertTrue(m_4.isValidMap());
        assertTrue(m_5.isValidMap());
        assertTrue(m_6.isValidMap());
        assertTrue(m_7.isValidMap());
        assertTrue(m_8.isValidMap());
        assertTrue(m_9.isValidMap());
        assertTrue(m_10.isValidMap());
        assertTrue(m_11.isValidMap());
        assertTrue(m_12.isValidMap());
    }


    /**
     * Bot walks several steps
     */
    @Test
    public void BotWalksSeveralSteps() throws Exception {
        Board board = new Board();

        board.setCurrentMap(0);
        assertTrue(board.moveBot());
        assertTrue(board.moveBot());
        assertTrue(board.moveBot());
        assertTrue(board.moveBot());
        assertTrue(board.moveBot());

        assertEquals(4, board.getBotPositionCreatedLevel().getRow());
        assertEquals(5, board.getBotPositionCreatedLevel().getCol());
        assertEquals(0, board.getBotRotCreatedLevel());
    }

    /**
     * Bot turns and then walks several steps
     */
    @Test
    public void BotTurnThenSeveralSteps() throws Exception {
        Board board = new Board();

        board.setCurrentMap(2);

        assertTrue(board.moveBot());
        board.rotateBotClockwise();
        assertTrue(board.moveBot());
        assertTrue(board.moveBot());

        assertEquals(2, board.getBotPositionCreatedLevel().getRow());
        assertEquals(1, board.getBotPositionCreatedLevel().getCol());
    }

    /**
     * Bot walks into a wall
     */
    @Test
    public void BotWalksIntoWall() throws Exception {
        Board board = new Board();

        board.setCurrentMap(3);
        assertTrue(board.moveBot());
    }

    /**
     * Bot walks into a pit
     */
    @Test
    public void BotWalksIntoPit() throws Exception {
        Board board = new Board();
        board.setCurrentMap(2);

        assertTrue(board.moveBot());
        assertTrue(board.moveBot());

        board.rotateBotClockwise();
        assertFalse(board.moveBot());
    }

    /**
     * Bot jumps over a pit
     */
    @Test
    public void BotJumpsOverPit() throws Exception {
        Board board = new Board();

        board.setCurrentMap(2);

        board.setBotPosition(new Position(0, 2));
        board.rotateBotClockwise();

        board.setCharToCreatedMap(new Position(2,2), NORMAL);

        assertTrue(board.jumpBot());

        assertEquals(2, board.getBotPositionCreatedLevel().getRow());
        assertEquals(2, board.getBotPositionCreatedLevel().getCol());

    }

    /**
     * Bot jumps on a normal field
     */
    @Test
    public void BotJumpsOnNormalField() throws Exception {
        Board board = new Board();

        board.setCurrentMap(2);

        assertFalse(board.jumpBot());
    }

    /**
     * Bot collects coin
     */
    @Test
    public void BotCollectsCoin() throws Exception {
        Board board = new Board();

        board.setCurrentMap(2);

        board.setCharToCreatedMap(new Position(0,1), NORMAL);
        assertTrue(board.moveBot());

        board.collectCoin();

        assertFalse(board.hasACoinOn(new Position(0, 1)));

    }

    /**
     * Bot receives instruction "Exit" next to a door
     */
    @Test
    public void BotReceiveInstrExit() throws Exception {
        FieldType[][] board = {
                {NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, NORMAL, NORMAL, DOOR  },
                {NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, NORMAL, NORMAL, NORMAL},
        };

        Position botStartPos = new Position(0, 0);
        Board maps = new Board(board, botStartPos, 0);

        assertTrue(maps.moveBot());
        assertTrue(maps.moveBot());
        assertTrue(maps.moveBot());
        maps.rotateBotClockwise();
        assertTrue(maps.exitIsPossible());
    }

    /**
     * Bot receives instruction "Exit" away from a door
     */
    @Test
    public void BotReceiveInstrExitAwayFromDoor() throws Exception {
        FieldType[][] board = {
                {NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, NORMAL, NORMAL, DOOR  },
                {NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, NORMAL, NORMAL, NORMAL},

        };

        Position botStartPos = new Position(0, 0);
        Board maps = new Board(board, botStartPos, 0);
        assertTrue(maps.moveBot());
        assertTrue(maps.moveBot());
        assertTrue(maps.moveBot());

        assertFalse(maps.exitIsPossible());
    }

    /**
     * Bot collects coins and goes on Exit
     */
    @Test
    public void BotCollectsCoinsAndExit() throws Exception {
        FieldType[][] board = {
                {NORMAL, COIN, COIN, NORMAL},
                {NORMAL, NORMAL, NORMAL, DOOR  },
                {NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, NORMAL, NORMAL, NORMAL},

        };

        Position botStartPos = new Position(0, 0);
        Board maps = new Board(board, botStartPos, 0);

        assertTrue(maps.moveBot());
        maps.collectCoin();
        assertTrue(maps.moveBot());
        maps.collectCoin();
        assertTrue(maps.moveBot());
        assertFalse(maps.exitIsPossible());
        maps.rotateBotClockwise();
        assertTrue(maps.exitIsPossible());
    }

    /**
     * Bot goes to Exit without having collected all the coins
     */
    @Test
    public void BotGoesExitWithoutAllCoins() throws Exception {
        FieldType[][] board = {
                {NORMAL, COIN, COIN, NORMAL},
                {NORMAL, NORMAL, NORMAL, DOOR  },
                {NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, COIN, NORMAL, NORMAL},

        };

        Position botStartPos = new Position(0, 0);

        Board maps = new Board(board, botStartPos, 0);

        assertTrue(maps.moveBot());
        maps.collectCoin();
        assertTrue(maps.moveBot());
        maps.collectCoin();
        assertTrue(maps.moveBot());
        maps.rotateBotClockwise();

        assertFalse(maps.exitIsPossible());
    }

    /**
     * bot turn and then walk, JUMP then Walk, rotateClockWise then exit is not possible
     * all coins are not collected
     */
    @Test
    public void botTurnThenWalk() throws Exception {
        FieldType[][] board = {
                {NORMAL, COIN, COIN, NORMAL},
                {NORMAL, NORMAL, NORMAL, PIT  },
                {NORMAL, NORMAL, NORMAL, NORMAL},
                {NORMAL, COIN, DOOR, NORMAL},

        };

        Position botStartPos = new Position(0, 0);

        Board maps = new Board(board, botStartPos, 0);

        assertTrue(maps.moveBot());
        maps.collectCoin();
        assertTrue(maps.moveBot());
        maps.collectCoin();
        assertTrue(maps.moveBot());
        maps.rotateBotClockwise();
        assertTrue(maps.jumpBot());
        assertTrue(maps.moveBot());
        maps.rotateBotClockwise();
        assertFalse(maps.exitIsPossible());
    }


}