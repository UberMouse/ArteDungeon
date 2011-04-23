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
 * Time: 6:51 PM
 * Package: nz.artedungeon.puzzles;
 */
public class Barrels extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Barrels";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.BARREL_PIPES) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.BARREL_PIPES).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Barrels";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
