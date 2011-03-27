package nz.uberdungeon.bosses;

import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.util.Timer;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.uberdungeon.common.Plugin;
import nz.uberdungeon.dungeon.Enemy;
import nz.uberdungeon.dungeon.Explore;
import nz.uberdungeon.dungeon.ItemHandler;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.utils.MyMovement;
import nz.uberdungeon.utils.util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/18/11
 * Time: 11:46 PM
 * Package: nz.uberdungeon.bosses;
 */
public class Sagittare extends Plugin
{
    private boolean dodgeArrows;
    private final Timer arrowRest = new Timer(5000);
    private Tile arrowLoc;

    private static enum State
    {
        ATTACK("Attacking"), EAT("Eating"), DODGE_ARROWS("Dodging arrow rain");

        private State(String name) {
            get = name;
        }

        private final String get;

        public String toString() {
            return get;
        }
    }

    private State getState() {
        if (dodgeArrows && MyPlayer.location().equals(arrowLoc))
            return State.DODGE_ARROWS;
        if (MyPlayer.needToEat() && MyPlayer.hasFood())
            return State.EAT;
        return State.ATTACK;
    }

    @Override
    public String getStatus() {
        return "Killing " + getName() + ": " + getState().toString();
    }

    @Override
    public boolean isValid() {
        return MyPlayer.currentRoom().getNearestNpc("sagittare") != null;
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Sagittare";
    }

    @Override
    public int loop() {
        if(MyPlayer.combatStyle() == ItemHandler.Style.RANGED)
            Explore.setExit(true);
        Npc sagittare = MyPlayer.currentRoom().getNearestNpc("sagittare");
        if (sagittare.getMessage() != null && sagittare.getMessage().contains("k off")) {
            dodgeArrows = true;
            arrowRest.reset();
            arrowLoc = MyPlayer.location();
        }
        switch (getState()) {
            case ATTACK:
                if (!MyPlayer.inCombat()) {
                    Enemy.setEnemy(sagittare);
                    MyMovement.turnTo(Enemy.getNPC());
                    Enemy.interact("attack");
                }
                break;
            case DODGE_ARROWS:
                if (!MyPlayer.isMoving())
                    util.clickRandomTileOnMap();
                break;
            case EAT:
                while (MyPlayer.hp() < 60 && MyPlayer.hasFood())
                    MyPlayer.eat();
                break;
        }
        return Random.nextInt(300, 500);
    }
}