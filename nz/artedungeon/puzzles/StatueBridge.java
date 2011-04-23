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
 * Time: 7:43 PM
 * Package: nz.artedungeon.puzzles;
 */
public class StatueBridge extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Statue Bridge";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.UNREPAIRED_STATUE_BRIDGE) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.UNREPAIRED_STATUE_BRIDGE).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Statue Bridge";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
