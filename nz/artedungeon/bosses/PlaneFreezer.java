package nz.artedungeon.bosses;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.Enemy;
import nz.artedungeon.dungeon.EnemyDef;
import nz.artedungeon.dungeon.ItemHandler;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.MyCombat;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/8/11
 * Time: 9:21 PM
 * Package: nz.artedungeon.bosses;
 */
public class PlaneFreezer extends Plugin
{
    public boolean isValid() {
        try {
            return Enemy.pickEnemy("lakhr") != null ||
                   (Objects.getNearest(GameConstants.LAKHRANZ_PILLERS) != null &&
                    Calculations.distanceTo(Util.getNearestNonWallTile(Objects.getNearest(GameConstants.FINISHEDLADDERS)
                                                                              .getLocation())) > 5);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public String getStatus() {
        return "Killing " + getName();
    }

    public String getAuthor() {
        return "UberMouse";
    }

    public String getName() {
        return "Plane-freezer Lakhrahnaz";
    }

    @Override
    public int loop() {
        try {
            if (MyPlayer.isMoving())
                return 500;
            if (Enemy.pickEnemy("lakhr") != null) {
                if (MyPlayer.hp() < 60)
                    MyPlayer.eat();
                Enemy.setEnemy("lakhr");
                MyCombat.doPrayerFor(new EnemyDef(Enemy.getNPC()));
                MyMovement.turnTo(Enemy.getNPC());
                if (Calculations.distanceTo(Enemy.location()) > 2 &&
                    Enemy.getNPC().isOnScreen() &&
                    MyPlayer.combatStyle() == ItemHandler.Style.MELEE)
                    Enemy.location().interact("Walk");
                else if (!MyPlayer.isInteracting())
                    Enemy.interact("Attack");
            }
            else {
                if (MyPlayer.isMoving())
                    return Random.nextInt(500, 600);
                GameObject walkSnow = Objects.getNearest(GameConstants.SNOW);
                Tile laderLoc = Util.getNearestNonWallTile(Objects.getNearest(GameConstants.FINISHEDLADDERS)
                                                                  .getLocation());
                if (Calculations.distanceTo(laderLoc) > 4 &&
                    walkSnow != null &&
                    !MyPlayer.location().equals(walkSnow.getLocation()))
                    walkSnow.getLocation().clickOnMap();
                else {
                    if (Calculations.isTileOnScreen(laderLoc))
                        laderLoc.interact("walk");
                    else
                        laderLoc.clickOnMap();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Random.nextInt(500, 600);
    }
}
