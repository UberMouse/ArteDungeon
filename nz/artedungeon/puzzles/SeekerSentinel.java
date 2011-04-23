package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 7:41 PM
 * Package: nz.artedungeon.puzzles;
 */
public class SeekerSentinel extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Seeker Sentinel";
    }

    @Override
    public boolean isValid() {
        return Npcs.getNearest("Seeker sentinel") != null &&
               Util.tileInRoom(Npcs.getNearest("Seeker sentinel").getLocation());
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int loop() {
        return 0;
    }
}
