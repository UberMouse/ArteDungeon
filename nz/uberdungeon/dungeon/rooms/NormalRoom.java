package nz.uberdungeon.dungeon.rooms;

import com.rsbuddy.script.wrappers.GroundItem;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.dungeon.doors.Door;
import nz.uberdungeon.utils.RSArea;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/1/11
 * Time: 4:28 PM
 * Package: nz.uberdungeon.dungeon.rooms;
 */
public class NormalRoom extends Room
{
    /**
     * Instantiates a new room.
     *
     * @param area   the area
     * @param doors  the doors
     * @param parent instance of main script
     */
    public NormalRoom(RSArea area,
                      LinkedList<Door> doors,
                      GroundItem[] groundItems,
                      DungeonMain parent) {
        super(area, doors, NORMAL, parent);
    }
}
