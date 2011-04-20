package nz.artedungeon.bosses;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.Enemy;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.utils.util;
import nz.uberutils.methods.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/15/11
 * Time: 5:13 PM
 * Package: nz.artedungeon.bosses;
 */
public class Rammernaut extends Plugin
{
    private boolean dodge;

    private static enum State
    {
        ATTACK("Attacking"), DODGING("Dodging charge");

        private State(String name) {
            get = name;
        }

        private final String get;

        public String toString() {
            return get;
        }
    }

    private State getState() {
        if (dodge)
            return State.DODGING;
        else
            return State.ATTACK;
    }

    @Override
    public boolean isValid() {
        return MyPlayer.currentRoom().getNearestNpc("rammernaut") != null;
    }

    @Override
    public String getStatus() {
        return "Killing Rammernaut: " + getState().toString();
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Rammernaut";
    }

    @Override
    public int loop() {
        if(MyPlayer.needToEat())
            MyPlayer.eat();
        Npc rammernaut = MyPlayer.currentRoom().getNearestNpc("rammernaut");
        if (rammernaut.getMessage() != null) {
            if (rammernaut.getMessage().toLowerCase().contains("arge"))
                dodge = true;
            else if (rammernaut.getMessage().toLowerCase().contains("oof"))
                dodge = false;
        }
        if (rammernaut.getAnimation() == -1)
            dodge = false;
        switch (getState()) {
            case ATTACK:
                if (!MyPlayer.isInteracting()) {
                    Enemy.setEnemy(rammernaut);
                    MyMovement.turnTo(rammernaut);
                    Enemy.interact("attack");
                }
                sleep(550);
                break;
            case DODGING:
                Tile doorTile = util.getNearestNonWallTile(MyPlayer.currentRoom().getDoors().get(0).getLocation());
                doorTile.clickOnMap();
                if (Calculations.distanceTo(rammernaut) < 5) {
                    util.clickRandomTileOnMap();
                }
                break;
        }
        return 100;
    }
}
