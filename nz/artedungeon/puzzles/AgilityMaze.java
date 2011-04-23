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
 * Time: 5:27 PM
 * Package: nz.artedungeon.puzzles;
 */
public class AgilityMaze extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Agility maze";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.AGILITY_DOORS) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.AGILITY_DOORS).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Agility Maze";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
