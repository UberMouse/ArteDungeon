package nz.artedungeon.dungeon.rooms;

import com.rsbuddy.script.wrappers.GroundItem;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.dungeon.doors.Door;
import nz.artedungeon.utils.RSArea;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/1/11
 * Time: 4:28 PM
 * Package: nz.artedungeon.dungeon.rooms;
 */
public class Normal extends Room
{
    /**
     * Instantiates a new room.
     *
     * @param area   the area
     * @param doors  the doors
     * @param parent instance of main script
     */
    public Normal(RSArea area, LinkedList<Door> doors, DungeonMain parent) {
        super(area, doors, NORMAL, parent);
    }
}
