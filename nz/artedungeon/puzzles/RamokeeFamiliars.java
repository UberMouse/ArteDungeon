package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 7:39 PM
 * Package: nz.artedungeon.puzzles;
 */
public class RamokeeFamiliars extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Ramokee Familiars";
    }

    @Override
    public boolean isValid() {
        return Npcs.getNearest("bloodrager") != null &&
               Util.tileInRoom(Npcs.getNearest("bloodrager").getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Ramokee Familiars";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        return 1000;
    }
}
