package nz.uberdungeon.utils;

import com.rsbuddy.script.util.Timer;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.RSBuddyCommon;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.dungeon.doors.Door;
import nz.uberdungeon.dungeon.rooms.Room;

import java.util.ArrayList;
import java.util.Arrays;


// TODO: Auto-generated Javadoc
public class Pathfinding extends RSBuddyCommon
{

    /**
     * Instantiates a new breadth first search.
     *
     * @param parent instance of main script
     */
    public Pathfinding(DungeonMain parent) {
        super(parent);
    }

    /**
     * Find path from room to room.
     *
     * @param start the start room
     * @param end   the end room
     * @return the room[]'s found
     */
    public Room[] findPath(Room start, Room end) {
        ArrayList<Room> open = new ArrayList<Room>();
        ArrayList<Room> closed = new ArrayList<Room>();
        open.add(start);
        Timer timer = new Timer(5000);
        while (open.size() > 0 && timer.isRunning()) {
            Room curr = open.get(0);
            open.remove(curr);
            closed.add(curr);
            for (Room room : curr.getConnectors()) {
                if (!closed.contains(room)) {
                    if (!open.contains(room)) {
                        room.setParent(curr);
                        open.add(room);
                    }
                }
            }
            if (curr.equals(end)) {
                Room[] path = new Room[0];
                do {
                    path = Arrays.copyOf(path, path.length + 1);
                    path[path.length - 1] = curr;
                    curr = curr.getParent();
                    if (curr == null || start == null)
                        return null;
                } while (!curr.equals(start));
                path = Arrays.copyOf(path, path.length + 1);
                path[path.length - 1] = start;
                Room[] pathReverse = new Room[path.length];
                for (int i = 0; i < path.length; i++)
                    pathReverse[i] = path[path.length - i - 1];
                return pathReverse;
            }
        }
        return null;
    }

    /**
     * Walk path of Room[]'s.
     *
     * @param path the path
     */
    public void walkPath(Room[] path) {
        if (path.length < 2)
            return;
        Door toOpen = path[0].getDoorForConnector(path[1]);
        MyMovement.turnTo(toOpen.getLocation());
        toOpen.open();
    }

    public int distanceToRoom(Room room) {
        Room[] path = findPath(MyPlayer.currentRoom(), room);
        if (path != null)
            return path.length;
        return -1;
    }
}
