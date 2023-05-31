package logic;

import org.junit.Test;

import java.util.HashMap;

/**
 * TODO: This class test are commented bcz of changes in Bot
 *       Fix test
 */
public class SolvabilityTest {

    @Test
    public void targetIsReachable() {
    }

    @Test
    public void getDoorPos() {
    }

    @Test
    public void hasObject() {
    }

    @Test
    public void getNearestObjectPos() {
    }

    @Test
    public void getDistance() {
    }

    @Test
    public void getAllObjectsPos() {
    }

    @Test
    public void getAllNeigh() {
    }

    @Test
    public void getDistances() {
    }

    @Test
    public void constructProgram() {
    }


    @Test
    public void testFloodFillExample() {

    }

    @Test
    public void testRotation(){
        Solvability s = new Solvability();

        Position currentPos = new Position(1, 1);
        Position posToMove = new Position(0, 1);
        int rot = 0;

        System.out.println(Solvability.calculateRotation(currentPos, posToMove, rot));
    }

    @Test
    public void sortByValue() {
        Solvability s = new Solvability();
        Position p1 = new Position(1, 0);
        Position p2 = new Position(4, 2);
        Position p3 = new Position(2, 4);
        Position p4 = new Position(2, 1);
        Position p5 = new Position(4, 1);

        HashMap<Position, Integer> hm = new HashMap<Position, Integer>();
        hm.put(p1, 10);
        hm.put(p2, 11);
        hm.put(p3, 5);
        hm.put(p4, 2);
        hm.put(p5, 3);

    }

    @Test
    public void testKMP(){
//        Solvability s = new Solvability();
//        Moves[] allMoves = {
//                FORWARD, FORWARD, FORWARD, FORWARD, FORWARD, CLOCKWISE, CLOCKWISE, FORWARD, JUMP,
//                JUMP, ANTI_CLOCK, ANTI_CLOCK, FORWARD, FORWARD, FORWARD, JUMP
//        };
//
//        Moves[] pat = {
//                FORWARD, FORWARD, FORWARD,
//        };
//
//        s.KMPSearch(pat, allMoves);
//        //s.testSubString("H E L L 0");
    }

    @Test
    public void testSearchPatterns(){
//        Solvability s = new Solvability();
//        Moves[] allMoves = {
//                FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP, CLOCKWISE, CLOCKWISE, FORWARD, JUMP,
//                JUMP, ANTI_CLOCK, ANTI_CLOCK, FORWARD, FORWARD, FORWARD, JUMP, FORWARD, FORWARD, FORWARD, JUMP
//        };
//
//        HashMap<Position, LinkedList<Integer>> occurrencesPos = s.searchPatterns(allMoves);
////        for (Map.Entry<Position, Integer> aa : list) {
////            temp.put(aa.getKey(), aa.getValue());
////        }
//
//        for(Position p: occurrencesPos.keySet()){
//            System.out.println(p.getRow() + " :  : " + p.getCol());
//            System.out.println("Rep Len : " +occurrencesPos.get(p).size());
//            for(Integer a: occurrencesPos.get(p)){
//                System.out.print("   " + a);
//            }
//            System.out.println();
//            //toReturn.add(p);
//        }

    }
}