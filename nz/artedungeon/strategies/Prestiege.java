package nz.artedungeon.strategies;

import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.wrappers.Item;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.misc.GameConstants;


public class Prestiege extends Strategy
{

    public Prestiege(DungeonMain parent) {
        super(parent);
    }


    public int execute() {
        if (Widgets.get(213).isValid()) {
            Widgets.get(213).getComponent(5).interact("Continue");
            sleep(random(500, 600));
        }
        else if (Widgets.get(228).isValid()) {
            if (Widgets.get(228).getComponent(2).interact("Continue")) {
                parent.prestige = false;
                parent.prestiegeCount++;
            }
        }
        else if (Widgets.get(939).isValid() &&
                 (Game.getCurrentTab() != Game.TAB_EQUIPMENT && Game.getCurrentTab() != Game.TAB_INVENTORY)) {
            Widgets.get(939).getComponent(87).interact("Reset");
            sleep(random(500, 600));

        }
        else {
            Item item = Inventory.getItem(GameConstants.KINSHIP_RING);
            if (item != null) {
                if (item.click(true)) {
                    int timeout = 0;
                    while (Game.getCurrentTab() == Game.TAB_INVENTORY
                           && ++timeout <= 10)
                        sleep(100);
                }
            }
        }
        return random(400, 600);
    }


    public boolean isValid() {
        return Widgets.get(939).getComponent(83).isValid() && !Explore.inDungeon()
               && parent.prestige && !Widgets.get(939).getComponent(83).getText().equals("0");
    }


    public void reset() {

    }

    public String getStatus() {
        return "Prestiging";
    }

}
