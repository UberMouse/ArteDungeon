package nz.artedungeon.strategies;

import com.rsbuddy.script.methods.Combat;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.EnemyDef;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.utils.MyCombat;
import nz.uberutils.helpers.Options;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/13/11
 * Time: 9:08 PM
 * Package: nz.artedungeon.strategies;
 */
public class ChangeAttackStyle extends Strategy
{
    /**
     * Instantiates a new strategy.
     *
     * @param parent instance of main script
     */
    public ChangeAttackStyle(DungeonMain parent) {
        super(parent);
    }

    @Override
    public int execute() {
        if ((MyCombat.getActionIndex("Block") - 11) == Combat.getFightMode() && Options.getBoolean("pureMode"))
            Combat.setFightMode(1);
        else {
            EnemyDef enemyDef = new EnemyDef(MyPlayer.interacting());
            for (String weakness : enemyDef.weaknesses()) {
                if (MyCombat.haveStyle(weakness) && !MyCombat.currentAttackStyle().equals(weakness)) {
                    MyCombat.setAttackStyle(weakness);
                    return random(400, 600);
                }
                else if (MyCombat.currentAttackStyle().equals(weakness))
                    return random(400, 600);
            }
        }
        return random(400, 600);
    }

    @Override
    public boolean isValid() {
        if ((MyCombat.getActionIndex("Block") - 11) == Combat.getFightMode() && Options.getBoolean("pureMode"))
            return true;
        if (!MyPlayer.isInteracting() || !Options.getBoolean("switchStyles"))
            return false;
        EnemyDef enemyDef = new EnemyDef(MyPlayer.interacting());
        for (String weakness : enemyDef.weaknesses()) {
            if (MyCombat.haveStyle(weakness) && MyCombat.getStyleIndex(weakness) != MyCombat.getActionIndex("Block")) {
                if (MyCombat.getStyleIndex(weakness) != MyCombat.getStyleIndex(MyCombat.currentAttackStyle()))
                    return true;
            }
            else
                return false;
        }
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public String getStatus() {
        return "Changing attack style to enemy weakness";
    }
}
