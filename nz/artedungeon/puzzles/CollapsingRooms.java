package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Objects;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 7:00 PM
 * Package: nz.artedungeon.puzzles;
 */
public class CollapsingRooms extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Collapsing Room";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.SMALL_DEBRIS) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.SMALL_DEBRIS).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Collapsing Room";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
