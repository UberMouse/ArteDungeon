package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Objects;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 7:45 PM
 * Package: nz.artedungeon.puzzles;
 */
public class UnfinishedBridge extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Unfinished Bridge";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest("Unfinished bridge") != null &&
               Util.tileInRoom(Objects.getNearest("Unfinished bridge").getLocation());
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int loop() {
        return 0;
    }
}
