package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/22/11
 * Time: 7:11 AM
 * Package: nz.artedungeon.puzzles;
 */
public class PondSkater extends PuzzlePlugin
{
    static final int SKATER = 12089;
    static boolean canSolve = true;

    public boolean isValid() {
        Util.debug(MyPlayer.curArea().getCentralTile());
        Util.debug(Util.tileInRoom(MyPlayer.curArea().getCentralTile()));
        return Npcs.getNearest(SKATER) != null && Util.tileInRoom(Npcs.getNearest(SKATER).getLocation());
    }

    public String getStatus() {
        return "Solving: Pondskaters";
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Pondskaters";
    }

    public int loop() {
        if (Util.getWidgetWithText("You require a fishing") != null)
            canSolve = false;
        Npc skater = Npcs.getNearest(SKATER);
        GameObject wallPiece = Objects.getNearest(53984);
        if (!MyPlayer.location().equals(wallPiece.getLocation()))
            MyMovement.walkTo(wallPiece);
        if (!MyPlayer.isMoving() && Calculations.distanceTo(skater) <= 2)
            skater.interact("Catch");
        return 500;
    }

    public boolean isPossible() {
        return canSolve;
    }
}
