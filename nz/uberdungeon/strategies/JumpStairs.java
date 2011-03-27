package nz.uberdungeon.strategies;

import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.Strategy;
import nz.uberdungeon.dungeon.Dungeon;
import nz.uberdungeon.dungeon.Explore;
import nz.uberdungeon.misc.GameConstants;
import nz.uberdungeon.utils.MyMovement;


public class JumpStairs extends Strategy
{

    public JumpStairs(DungeonMain parent) {
        super(parent);
    }


    public int execute() {
        parent.clearAll();
        parent.inDungeon = false;
        Dungeon.setFloor(0);
        Explore.setExit(false);
        if (parent.teleportFailSafe == 3) {
            parent.timesAborted++;
        }
        parent.teleportFailSafe = 0;
        GameObject stairs = Objects.getNearest(GameConstants.END_STAIRS);
        MyMovement.turnTo(stairs);
        if (stairs.interact("Jump")) {
            debug("Jumped down stairs");
            sleep(1200, 1500);
        }
        return random(400, 600);
    }


    public boolean isValid() {
        return Objects.getNearest(GameConstants.END_STAIRS) != null;
    }


    public void reset() {

    }

    public String getStatus() {
        return "Jumping down stairs";
    }

}
