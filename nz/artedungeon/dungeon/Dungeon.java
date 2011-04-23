package nz.artedungeon.dungeon;

import nz.artedungeon.utils.Util;

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
    private static int doorIdOffset;
    private static int curFloor = -1;

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
            return Util.parseTime(System.currentTimeMillis() - startTime);
        return "Not in Dungeon";
    }

    public static String fastestTime() {
        if (fastestTime != 999999999)
            return Util.parseTime(fastestTime);
        else return "00:00:00";
    }

    public static String slowestTime() {
        return Util.parseTime(longestTime);
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
        return doorIdOffset;
    }

    public static void setDoorIdOffset(int offset) {
        Dungeon.doorIdOffset = offset;
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
