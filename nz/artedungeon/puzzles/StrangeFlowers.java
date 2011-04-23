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
 * Time: 7:44 PM
 * Package: nz.artedungeon.puzzles;
 */
public class StrangeFlowers extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Strange Flowers";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.CENTER_FLOWERS) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.CENTER_FLOWERS).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Strange Flowers";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
