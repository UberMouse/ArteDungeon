package nz.artedungeon.strategies;

import com.rsbuddy.script.methods.Equipment;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.wrappers.Component;
import com.rsbuddy.script.wrappers.Item;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.misc.GameConstants;


public class LeaveDungeon extends Strategy
{

    public LeaveDungeon(DungeonMain parent) {
        super(parent);
    }

    //TODO fix issue with it not clicking leave party - I think I did it?
    public int execute() {
        Component yes = Widgets.getComponent(236, 1);
        Component leave = Widgets.getComponent(939, 36);
        if (yes.isValid())
            yes.click();
        else if (Widgets.canContinue())
            Widgets.clickContinue();
        else if (leave.isValid() &&
                 (Game.getCurrentTab() != Game.TAB_EQUIPMENT &&
                  Game.getCurrentTab() != Game.TAB_INVENTORY &&
                  Game.getCurrentTab() != Game.TAB_MAGIC))
            leave.click();
        else if (Game.getCurrentTab() == Game.TAB_EQUIPMENT ||
                 Game.getCurrentTab() == Game.TAB_INVENTORY ||
                 Game.getCurrentTab() == Game.TAB_MAGIC) {
            Item rok;
            if (Inventory.getItem(GameConstants.KINSHIP_RING) == null) {
                Game.openTab(Game.TAB_EQUIPMENT);
                rok = Equipment.getItem(Equipment.RING);
            }
            else
                rok = Inventory.getItem(GameConstants.KINSHIP_RING);
            if (rok.interact("Open Party")) {
                for (int i = 0; Game.getCurrentTab() != Game.TAB_QUESTS && i < 15; i++)
                    sleep(100);
            }
        }
        return random(400, 600);
    }


    public boolean isValid() {
        return (Explore.exit() || parent.teleportFailSafe >= 3 || parent.prestige);
    }


    public void reset() {

    }

    public String getStatus() {
        return "Leaving dungeon";
    }

}
