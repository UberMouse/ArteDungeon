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
 * Time: 6:53 PM
 * Package: nz.artedungeon.puzzles;
 */
public class BloodFountain extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Blood fountain";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.DRY_BLOOD_FOUNTAIN) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.DRY_BLOOD_FOUNTAIN).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Blood fountain";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
