package nz.uberdungeon.strategies;

import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.Strategy;
import nz.uberdungeon.dungeon.Explore;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.dungeon.doors.Door;
import nz.uberdungeon.dungeon.rooms.Room;
import nz.uberdungeon.utils.Pathfinding;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 1/30/11
 * Time: 1:56 AM
 * Package: nz.ubermouse.uberdungeon.strategies;
 */
public class WalkToRoom extends Strategy
{

    public WalkToRoom(DungeonMain parent) {
        super(parent);
    }

    public int execute() {
        Pathfinding bfs = new Pathfinding(parent);
        Room end = null;
        Door searchDoor = getDoor();
        if (searchDoor != null) {
            end = searchDoor.getParent();
        }
        else if (!MyPlayer.currentRoom().hasEnemies()) {
            for (Room room : Explore.getRooms()) {
                if (room.containsKeys())
                    end = room;
            }
        }
        else {
            for (Room room : Explore.getRooms())
                if (!room.hasEnemies())
                    end = room;
        }
        Room[] path = bfs.findPath(MyPlayer.currentRoom(), end);
        bfs.walkPath(path);
        return random(400, 600);
    }


    public boolean isValid() {
        Pathfinding bfs = new Pathfinding(parent);
        Room end = null;
        Door searchDoor = getDoor();
        if (searchDoor != null) {
            end = searchDoor.getParent();
            Room[] path = bfs.findPath(MyPlayer.currentRoom(), end);
            Room[] pathFromStart = bfs.findPath(Explore.getStartRoom(), end);
            if (path != null && pathFromStart != null)
                return true;
        }
        if (!MyPlayer.currentRoom().hasEnemies()) {
            for (Room room : Explore.getRooms()) {
                if (room.containsKeys())
                    return true;
            }
            return false;
        }
        for (Room room : Explore.getRooms())
            if (!room.hasEnemies())
                end = room;
        return MyPlayer.currentRoom().getNearestDoor() == null && MyPlayer.currentRoom().getItem() == null && end != null;
    }

    private Door getDoor() {
        for (Door door : Explore.getDoors()) {
            if (door.canOpen() && !door.isOpen())
                return door;
        }
        return null;
    }

    public void reset() {

    }


    public String getStatus() {
        if (DungeonMain.debug) {
            Door searchDoor = getDoor();
            if (searchDoor != null) {
                return "Pathfinding to door in Room: " +
                       Explore.getRooms().indexOf(searchDoor.getParent()) +
                       " Door: " +
                       Explore.getDoors().indexOf(searchDoor);
            }
            else if (!MyPlayer.currentRoom().hasEnemies()) {
                return "Pathfinding to room to pickup key";
            }
            else
                return "Pathfinding to room to avoid enemies";
        }
        return "Pathfinding to room";
    }

}

