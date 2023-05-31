package logic;

import java.util.*;
import static java.lang.Math.abs;

/**
 * TODO: Solvability class should not change anything
 *       from the map like bot rotation, position, coins etc
 *  TODO: cleanup methods sortByValue, createMovesForAllCoins etc
 *  TODO: The Gui stuck after calculation
 *  TODO: Debug to find why extra coins positions are added
 */
public final class Solvability {

    /**
     * Constructor
     */
    public Solvability(){
    }

    /**
     * Main method to be called from gui
     * Used to Generate Moves for all the coins
     * @param board maps instance
     * @throws Exception exception
     */
    static LinkedList<Instructions> createMovesForAllCoins(Board board) throws Exception {
        Position doorPos = board.getDoorPositionCreatedLevel();
        Position startPos = board.getBotPositionCreatedLevel();
        Integer startRot = board.getBotRotCreatedLevel();

        LinkedList<Instructions> allMoves = new LinkedList<>();

        LinkedList<Position> allCoinsPositions = board.getAllCoinsPositions();
        boolean toStop = false;
        Position toMove;
        int var = allCoinsPositions.size();
        for(int i = 0; i < var && !toStop; i++){
            toMove = getCoinOfMinDistance(startPos, allCoinsPositions);
            // TODO: Add starting position, Or remove that position from the allCoinsPositions
            allCoinsPositions.remove(toMove);
            startRot = calculateMoves(board, startPos, startRot, toMove, allMoves, false);
            if(startRot == null){
               toStop = true;
            } else {
                startPos = toMove;
            }
        }
        if(toStop) return  null;
        else if(calculateMoves(board, startPos, startRot, doorPos, allMoves, true) == null) return null;

        return allMoves;
    }

    /**
     * used to calculate the moves from the given starting position to the positionToMove
     * @param board map instance
     * @param startPos starting position
     * @param positionToMove position to move
     * @return ending rotation of the bot
     */
    static Integer calculateMoves(Board board, Position startPos, int rotation,
                                  Position positionToMove, LinkedList<Instructions> moves, boolean isForDoor){
        LinkedList<Position> allPositions = getPathFromGoalToStart(startPos, positionToMove, board, isForDoor);
        if(allPositions == null){
            return null;
        }
        LinkedList<Instructions> allMoves = new LinkedList<>();
        for(Position p: allPositions){
            Instructions m = Instructions.FORWARD;
            int diffRow = p.getRow() - startPos.getRow();
            int diffCol = p.getCol() - startPos.getCol();
            if(diffRow > 1 || diffCol > 1 || diffRow < -1 || diffCol < -1){
                m = Instructions.JUMP;
            }

            // for the last if the algo is looking for door then the end element is EXIT
            if(isForDoor && p.equals(allPositions.getLast())){
                m = Instructions.EXIT;
            }

            int toBeSetRot = calculateRotation(startPos, p, rotation);
            if(toBeSetRot == 1){
                allMoves.add(Instructions.CLOCKWISE);
                rotation++;
                rotation = rotation % 4;
            } else if(toBeSetRot == 2){
                allMoves.add(Instructions.CLOCKWISE);
                allMoves.add(Instructions.CLOCKWISE);
                rotation += 2;
                rotation = rotation % 4;
            } else if(toBeSetRot == -1){
                allMoves.add(Instructions.ANTI_CLOCK);
                rotation--;
                rotation = rotation % 4;
            } else if(toBeSetRot == -2){
                allMoves.add(Instructions.ANTI_CLOCK);
                allMoves.add(Instructions.ANTI_CLOCK);
                rotation -= 2;
                rotation = rotation % 4;
            }
            allMoves.add(m);
            startPos = new Position(p.getRow(), p.getCol());
        }

        moves.addAll(allMoves);

        //return allMoves;
        return rotation;
        // get the current position of the bot and rotation
        // find the difference -> posToMove.row - currentPos.Row, posToMove.col - currentPos.col
        // if
    }

    /**
     * Implementation of Flood Fill Algorithm to find the path
     *
     * Calculate all the positions traveled (Path)
     * from door to the bot
     * return null when the goal is not reachable
     * when isForDoor is true then the goal is Door, that will be the start node
     *
     * @param start start position (will be the end node)
     * @param goal goal (exploration starts from this node)
     * @return Queue
     */
    private static LinkedList<Position> getPathFromGoalToStart(Position start, Position goal, Board board, boolean isForDoor){
        Map<Position, Position> nextTileToGoal = new HashMap<>();
        Queue<Position> frontier = new LinkedList<>();
        List<Position> visited = new LinkedList<>();
        frontier.add(goal);
        while(frontier.size() > 0) {
            Position curTile = frontier.poll();
            LinkedList<Position> neighbours = curTile.getNeighbours(board, isForDoor);
            for(Position neighbor: neighbours){
                if (!visited.contains(neighbor) && !frontier.contains(neighbor)){
                    FieldType fieldType = board.getMapcharAt(neighbor);
                    if(fieldType != FieldType.GRAY_WALL){
                        frontier.add(neighbor);
                        nextTileToGoal.put(neighbor, curTile);
                    }
                }
            }
            visited.add(curTile);
        }

        // Return Null if not able to reach to the goal
        if (visited.contains(start) == false)
            return null;

        LinkedList<Position> path = new LinkedList<>();
        Position curPathTile = start;
        while(curPathTile != goal){
            curPathTile = nextTileToGoal.get(curPathTile);
            path.add(curPathTile);
        }
        return path;
    }

    /**
     * Used to get the distance between the currentPos and targetPos
     * @param currentPos distance from
     * @param targetPos to distance
     * @return distance btw two positions
     */
    static int getDistance(Position currentPos, Position targetPos){
        int row = abs(currentPos.getRow() - targetPos.getRow());
        int col = abs(currentPos.getCol() - targetPos.getCol());
        return row + col;
    }

    /**
     * - means AntiClockWise number means times Anti clockWise
     * Used to calculate the rotation of the bot
     * @param currentPos currentPos
     * @param toMovePos toMove Position
     * @return the rotation
     */
    static int calculateRotation(Position currentPos, Position toMovePos, int rot){
        int diffRow = toMovePos.getRow() - currentPos.getRow();
        int diffCol = toMovePos.getCol() - currentPos.getCol();

        int toReturn = 0;
        if(diffRow > 0 ){
            toReturn = 1 - rot;
        } else if(diffRow < 0){
            toReturn = 3 - rot;
        } else if(diffCol > 0){
            toReturn = 0 - rot;
        } else if(diffCol < 0){
            toReturn = 2 - rot;
        } else toReturn = 0;

        if(toReturn == 3){
            return -1;
        } else if(toReturn == -3){
            return 1;
        } else {
            return toReturn;
        }
    }

    /**
     * Used to get the coin position whose distance is least from the starting position
     * @param startPosition start position
     * @param coinPositions coins Positions
     * @return position of the coin having least distance from the starting position
     */
    static Position getCoinOfMinDistance(Position startPosition, LinkedList<Position> coinPositions){
        int minDistance = Integer.MAX_VALUE;
        int distance;
        Position toReturn = null;
        for(Position p: coinPositions){
            distance = getDistance(startPosition, p);
            if(distance < minDistance){
                toReturn = p;
                minDistance = distance;
            }
        }
        return toReturn;
    }


    /* ----------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------- */


//    /**
//     * Used to get all the positions of the coins
//     * @param maps
//     * @param startPos
//     * @return
//     */
//    static LinkedList<Position> getAllObjectsByDistance(Maps maps, Position startPos){
//        // get all the coins position on the board
//        LinkedList<Position> allCoinPos = maps.getAllCoinsPositions();
//
//        HashMap<Position, Integer> allCoinsDistance = new HashMap();
//
//        for(Position p: allCoinPos){
//            allCoinsDistance.put(p, getDistance(startPos, p));
//        }
//
//        HashMap<Position, Integer> keys = sortByValue(allCoinsDistance);
//
//        LinkedList<Position> toReturn = new LinkedList<>();
//        for(Position p: keys.keySet()){
//            toReturn.add(p);
//        }
//        return toReturn;
//    }
//
//    /**
//     * Used to sort
//     * @param hm
//     * @return
//     */
//    public static HashMap<Position, Integer> sortByValue(HashMap<Position, Integer> hm){
//        // Create a list from elements of HashMap
//        List<Map.Entry<Position, Integer> > list =
//                new LinkedList<Map.Entry<Position, Integer> >(hm.entrySet());
//
//        // Sort the list
//        Collections.sort(list, new Comparator<Map.Entry<Position, Integer> >() {
//            public int compare(Map.Entry<Position, Integer> o1,
//                               Map.Entry<Position, Integer> o2)
//            {
//                return (o1.getValue()).compareTo(o2.getValue());
//            }
//        });
//
//        // put data from sorted list to hashmap
//        HashMap<Position, Integer> temp = new LinkedHashMap<>();
//        for (Map.Entry<Position, Integer> aa : list) {
//            temp.put(aa.getKey(), aa.getValue());
//        }
//        return temp;
//    }

    /* ----------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------- */
    /* ----------------------------------------------------------------------- */

    void testSubString(String s){
        List<String> list= Arrays.asList(s.split(" "));
        Set<String> set =new HashSet<String>(list);
        for (String string : set) {
            System.out.println(string+"  "+Collections.frequency(list, string));
        }
    }

}
