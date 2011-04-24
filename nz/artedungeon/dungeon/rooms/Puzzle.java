package nz.artedungeon.dungeon.rooms;

import com.rsbuddy.script.wrappers.GroundItem;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.PuzzlePlugin;
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
    private final PuzzlePlugin puzzle;

    /**
     * Instantiates a new room.
     *
     * @param area   the area
     * @param doors  the doors
     * @param puzzle the puzzle instance to inject
     * @param parent instance of main script
     */
    public Puzzle(RSArea area,
                  LinkedList<Door> doors,
                  PuzzlePlugin puzzle,
                  DungeonMain parent) {
        super(area, doors, PUZZLE, parent);
        this.puzzle = puzzle;
    }

    public boolean isSolved() {
        return puzzle.isSolved();
    }

    public int solve() {
        return puzzle.loop();
    }

    public String status() {
        return puzzle.getStatus();
    }

    public PuzzlePlugin getPuzzle() {
        return puzzle;
    }
}
