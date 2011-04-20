package nz.artedungeon.dungeon;

import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.GroundItem;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.RSBuddyCommon;
import nz.artedungeon.dungeon.doors.Door;
import nz.artedungeon.dungeon.doors.KeyDoor;
import nz.artedungeon.dungeon.doors.NormalDoor;
import nz.artedungeon.dungeon.doors.SkillDoor;
import nz.artedungeon.dungeon.rooms.BossRoom;
import nz.artedungeon.dungeon.rooms.NormalRoom;
import nz.artedungeon.dungeon.rooms.PuzzleRoom;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.FloodFill;
import nz.artedungeon.utils.RSArea;
import nz.artedungeon.utils.util;

import java.util.LinkedList;


// TODO: Auto-generated Javadoc
public class Explore extends RSBuddyCommon
{
    private static final LinkedList<Room> rooms = new LinkedList<Room>();
    private static final LinkedList<Door> doors = new LinkedList<Door>();
    private static Room startRoom;
    private static Room bossRoom;
    private static String floorType;
    private static boolean exit;

    public static Room getStartRoom() {
        return startRoom;
    }

    public static void setStartRoom(Room startRoom) {
        Explore.startRoom = startRoom;
    }

    public static Room getBossRoom() {
        return bossRoom;
    }

    public static void setBossRoom(Room bossRoom) {
        Explore.bossRoom = bossRoom;
    }

    public static String getFloorType() {
        return floorType;
    }

    public static void setFloorType(String floorType) {
        Explore.floorType = floorType;
    }

    public static boolean exit() {
        return exit;
    }

    public static void setExit(boolean exit) {
        Explore.exit = exit;
    }

    public static LinkedList<Room> getRooms() {
        return rooms;
    }

    public static LinkedList<Door> getDoors() {
        return doors;
    }

    /**
     * Instantiates a new explore.
     *
     * @param parent instance of main script
     */
    public Explore(DungeonMain parent) {
        super(parent);
    }

    /**
     * Generates a new room.
     *
     * @return the room
     */
    public static Room newRoom() {
        FloodFill floodFill = new FloodFill(parent);
        RSArea roomArea = new RSArea(floodFill.fill(MyPlayer.location()), parent);
        GroundItem[] groundItems = GroundItems.getLoaded(new Filter<GroundItem>()
        {
            public boolean accept(GroundItem groundItem) {
                return util.tileInRoom(groundItem.getLocation());
            }
        });
        if (bossRoom == null && Objects.getNearest(GameConstants.BOSS_DOORS) != null) {
            BossRoom room = new BossRoom(roomArea, newDoors(roomArea), groundItems, parent);
            rooms.add(room);
            return room;
        }
        for (Object object : parent.getPuzzles()) {
            if ((Boolean) util.callMethod(object, "isValid")) {
                PuzzleRoom room = new PuzzleRoom(roomArea, newDoors(roomArea), groundItems, parent);
                rooms.add(room);
                return room;
            }
        }
        NormalRoom room = new NormalRoom(roomArea, newDoors(roomArea), groundItems, parent);
        rooms.add(room);
        return room;
    }

    /**
     * Finds all doors in supplied room.
     *
     * @param roomArea the room area
     * @return the array list
     */
    private static LinkedList<Door> newDoors(RSArea roomArea) {
        LinkedList<Door> doors = new LinkedList<Door>();
        GameObject[] allObject = Objects.getLoaded();
        for (GameObject obj : allObject) {
            int objID = obj.getId();
            Tile objTile = obj.getLocation();
            top:
            for (int[] doorArray : GameConstants.DOORS) {
                for (int door : doorArray) {
                    if (!roomArea.contains(obj.getLocation()))
                        continue;
                    if (util.arrayContains(GameConstants.KEY_DOORS, objID)) {
                        doors.add(new KeyDoor(obj, parent));
                        break top;
                    }
                    else if (util.arrayContains(GameConstants.SKILL_DOORS, objID)) {
                        doors.add(new SkillDoor(obj, parent));
                        break top;
                    }
                    else if (util.arrayContains(GameConstants.BASIC_DOORS, objID)) {
                        doors.add(new NormalDoor(obj, parent));
                        break top;
                    }
                }
            }
        }
        Explore.doors.addAll(doors);
        return doors;
    }

    /**
     * In dungeon.
     *
     * @return true, if successful
     */

    public static boolean inDungeon() {
        return Widgets.getComponent(945, 0).isValid();
    }

}
