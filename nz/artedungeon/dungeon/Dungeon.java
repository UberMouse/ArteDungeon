package nz.artedungeon.dungeon;

import nz.artedungeon.utils.util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/6/11
 * Time: 10:12 PM
 * Package: nz.artedungeon.dungeon;
 */
public class Dungeon
{
    private static long startTime;
    private static int timesDied;
    private static long fastestTime = 999999999;
    private static long longestTime = 0;
    private static String floorType;
    private static int doorDifferential;
    private static int curFloor;

    public static void start() {
        long timeTaken = System.currentTimeMillis() - startTime;
        if (timeTaken > longestTime)
            longestTime = timeTaken;
        if (timeTaken < fastestTime)
            fastestTime = timeTaken;
        startTime = System.currentTimeMillis();
        timesDied = 0;
    }

    public static String curTime() {
        if (Explore.inDungeon())
            return util.parseTime(System.currentTimeMillis() - startTime);
        return "Not in Dungeon";
    }

    public static String fastestTime() {
        if (fastestTime != 999999999)
            return util.parseTime(fastestTime);
        else return "00:00:00";
    }

    public static String slowestTime() {
        return util.parseTime(longestTime);
    }

    public static String floorType() {
        return floorType;
    }

    public static void setFloorType(String floorType) {
        Dungeon.floorType = floorType;
    }

    public static void iTimesDied() {
        timesDied++;
    }

    public static int timesDied() {
        return timesDied;
    }

    public static int doorDifferential() {
        return doorDifferential;
    }

    public static void setDoorDifferential(int differential) {
        Dungeon.doorDifferential = differential;
    }

    public static int curFloor() {
        return curFloor;
    }

    public static void setFloor(int floor) {
        Dungeon.curFloor = floor;
    }

    public static void iFloor() {
        curFloor++;
    }
}
