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
 * Time: 3:39 PM
 * Package: nz.artedungeon.dungeon.rooms;
 */
public class Puzzle extends Room
{
    private boolean solved;

    /**
     * Instantiates a new room.
     *
     * @param area   the area
     * @param doors  the doors
     * @param parent instance of main script
     */
    public Puzzle(RSArea area, LinkedList<Door> doors, GroundItem[] groundItems, DungeonMain parent) {
        super(area, doors, PUZZLE, parent);
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
