package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.wrappers.Npc;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.Utils;
import nz.uberutils.methods.MyCamera;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/23/11
 * Time: 8:41 PM
 * Package: nz.artedungeon.puzzles;
 */
public class Ghosts extends PuzzlePlugin
{

    public boolean isValid() {
        return Npcs.getNearest(GameConstants.GHOSTS) != null &&
               Util.tileInRoom(Npcs.getNearest(GameConstants.GHOSTS).getLocation());
    }

    public String getStatus() {
        return "Solving: Ghosts";
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Ghosts";
    }

    public int loop() {
        try {
            if (Players.getLocal().getInteracting() == null ||
                !Utils.arrayContains(GameConstants.GHOSTS, ((Npc) Players.getLocal().getInteracting()).getId())) {
                Npc n = Npcs.getNearest(GameConstants.GHOSTS);
                MyCamera.turnTo(n);
                n.interact("Attack");
            }
        } catch (Exception ignored) {
        }
        return 300;
    }
}
