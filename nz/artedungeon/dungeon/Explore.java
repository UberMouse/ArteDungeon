package nz.artedungeon.dungeon;

import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.GroundItem;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.common.RSBuddyCommon;
import nz.artedungeon.dungeon.doors.Door;
import nz.artedungeon.dungeon.doors.Key;
import nz.artedungeon.dungeon.doors.Skill;
import nz.artedungeon.dungeon.rooms.Boss;
import nz.artedungeon.dungeon.rooms.Normal;
import nz.artedungeon.dungeon.rooms.Puzzle;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.FloodFill;
import nz.artedungeon.utils.RSArea;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.Utils;

import java.lang.reflect.Constructor;
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
        MyPlayer.setCurArea(roomArea);
        GroundItem[] groundItems = GroundItems.getLoaded(new Filter<GroundItem>()
        {
            public boolean accept(GroundItem groundItem) {
                return Util.tileInRoom(groundItem.getLocation());
            }
        });
        if (bossRoom == null && Objects.getNearest(GameConstants.BOSS_DOORS) != null) {
            Boss room = new Boss(roomArea, newDoors(roomArea), groundItems, parent);
            rooms.add(room);
            return room;
        }
        for (PuzzlePlugin p : parent.getPuzzles()) {
            if (p.isValid()) {
                PuzzlePlugin puzzle = null;
                try {
                    Constructor ctor = p.getClass().getDeclaredConstructor();
                    ctor.setAccessible(true);
                    puzzle = (PuzzlePlugin) ctor.newInstance();
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                Puzzle room = new Puzzle(roomArea, newDoors(roomArea), puzzle, parent);
                rooms.add(room);
                return room;
            }
        }
        Normal room = new Normal(roomArea, newDoors(roomArea), groundItems, parent);
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
            if (!roomArea.contains(obj.getLocation()))
                continue;
            if (Util.arrayContains(GameConstants.KEY_DOORS, objID)) {
                doors.add(new Key(obj, parent));
            }
            else if (Util.arrayContains(GameConstants.SKILL_DOORS, objID)) {
                doors.add(new Skill(obj, parent));
            }
            else if (Util.arrayContains(GameConstants.BASIC_DOORS, objID)) {
                doors.add(new nz.artedungeon.dungeon.doors.Normal(obj, parent));
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
