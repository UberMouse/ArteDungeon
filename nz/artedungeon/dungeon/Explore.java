package nz.artedungeon.dungeon;

import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.bosses.Default;
import nz.artedungeon.common.Plugin;
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
import nz.artedungeon.utils.PluginFactory;
import nz.artedungeon.utils.RSArea;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.Utils;

import java.util.LinkedList;



public class Explore extends RSBuddyCommon
{
    private static final LinkedList<Room> rooms = new LinkedList<Room>();
    private static final LinkedList<Door> doors = new LinkedList<Door>();
    private static Room    startRoom;
    private static Room    bossRoom;
    private static String  floorType;
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
        RSArea roomArea = new RSArea(floodFill.fill(MyPlayer.location()));
        MyPlayer.setCurArea(roomArea);
        if (bossRoom == null && Objects.getNearest(GameConstants.BOSS_DOORS) != null) {
            for (Plugin p : parent.getBosses()) {
                if (p.isValid()) {
                    Boss room = new Boss(roomArea,
                                         newDoors(roomArea),
                                         PluginFactory.instance().createPlugin(p.getClass().getSimpleName()),
                                         parent);
                    rooms.add(room);
                    return room;
                }
            }
            Utils.debug("No boss class found, adding default one");
            Boss room = new Boss(roomArea, newDoors(roomArea), new Default(), parent);
            rooms.add(room);
            return room;
        }
        for (PuzzlePlugin p : parent.getPuzzles()) {
            if (p.isValid()) {
                Puzzle room = new Puzzle(roomArea,
                                         newDoors(roomArea),
                                         (PuzzlePlugin) PluginFactory.instance()
                                                                     .createPlugin(p.getClass().getSimpleName()),
                                         parent);
                rooms.add(room);
                return room;
            }
        }
        Normal room = new Normal(roomArea, newDoors(roomArea), parent);
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
