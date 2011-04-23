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
 * Time: 7:03 PM
 * Package: nz.artedungeon.puzzles;
 */
public class DamagedBridge extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Damaged Bridge";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest("Damaged Bridge") != null &&
               Util.tileInRoom(Objects.getNearest("Damaged Bridge").getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Damaged Bridge";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
