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
 * Time: 6:57 PM
 * Package: nz.artedungeon.puzzles;
 */
public class ColoredRecesses extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Colored Recesses";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.RECESS_FOUNTAINS) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.RECESS_FOUNTAINS).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Colored Recesses";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
