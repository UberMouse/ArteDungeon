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
 * Time: 7:09 PM
 * Package: nz.artedungeon.puzzles;
 */
public class DamagedConstruct extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Damaged Construct";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest("Damaged Construct") != null &&
               Util.tileInRoom(Objects.getNearest("Damaged Construct").getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Damaged Construct";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
