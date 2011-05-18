package nz.artedungeon.dungeon.rooms;

import com.rsbuddy.script.wrappers.GroundItem;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.common.PuzzlePlugin;
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
public class Boss extends Room
{
    private final Plugin boss;

    /**
     * Instantiates a new room.
     *
     * @param area   the area
     * @param doors  the doors
     * @param boss   the boss this room contains
     * @param parent instance of main script
     */
    public Boss(RSArea area, LinkedList<Door> doors, Plugin boss, DungeonMain parent) {
        super(area, doors, BOSS, parent);
        this.boss = boss;
    }

    public int kill() {
        return boss.loop();
    }

    public String status() {
        return boss.getStatus();
    }

    public Plugin getBoss() {
        return boss;
    }

    public boolean isFinished() {
        return boss.isFinished();
    }
}
