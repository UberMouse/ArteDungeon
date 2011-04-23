package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Objects;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;

import javax.tools.Tool;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 7:46 PM
 * Package: nz.artedungeon.puzzles;
 */
public class WinchBridge extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Winch Bridge";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.ALL_UNWINCHED_BRIDGES) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.ALL_UNWINCHED_BRIDGES).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Winch Bridge";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
