package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Objects;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/3/11
 * Time: 2:58 PM
 * Package: nz.artedungeon.puzzles;
 */
public class SuspiciousGrooves extends PuzzlePlugin
{
    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.SUSPICOUS_GROOVES_ROW_1) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.SUSPICOUS_GROOVES_ROW_1).getLocation());
    }

    @Override
    public String getStatus() {
        return "Solving: Suspicious Grooves";
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Suspicious Grooves";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
