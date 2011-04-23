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
 * Time: 7:11 PM
 * Package: nz.artedungeon.puzzles;
 */
public class FremennikCamp extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Fremennik Camp";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.FREMMY_CRATES) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.FREMMY_CRATES).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Fremennik Camp";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
