package nz.artedungeon.strategies;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Mouse;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.wrappers.Component;
import com.rsbuddy.script.wrappers.GameObject;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.Dungeon;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.uberutils.methods.MyMovement;

import java.awt.*;


public class EnterDungeon extends Strategy
{

    public EnterDungeon(DungeonMain parent) {
        super(parent);
    }

    public int execute() {
        GameObject entrance = Objects.getNearest(GameConstants.ENTRANCE);
        MyMovement.turnTo(entrance);
        if (Widgets.getComponent(939, 83).isValid() &&
            (Integer.parseInt(Widgets.getComponent(939, 83).getText()) > GameConstants.MAX_FLOOR)) {
            parent.prestige = true;
            return random(400, 600);
        }
        else
            parent.prestige = false;
        if (Widgets.get(210).isValid())
            Widgets.get(210).getComponent(2).click();
        else if (Widgets.get(236).isValid())
            Widgets.get(236).getComponent(1).click();
        else if (Widgets.get(947).getComponent(608).isValid()) {
            int curProgess = Integer.parseInt(Widgets.getComponent(939, 83).getText());
            Component floor = Widgets.get(947).getComponent(608 + ((curProgess <
                                                                    GameConstants.MAX_FLOOR) ? curProgess : 0));
            if (curProgess < 17)
                Dungeon.setFloor(curProgess);
            else
                Dungeon.setFloor(0);
            while (floor.getCenter().getY() > 210) {
                Component floorSelectBox = Widgets.getComponent(947, 38);
                if (!floorSelectBox.getBoundingRect().contains(Mouse.getLocation())) {
                    Rectangle floorSelectBounds = floorSelectBox.getBoundingRect();
                    Mouse.move((int) floorSelectBounds.getCenterX(), (int) floorSelectBounds.getCenterY());
                }
                Mouse.scroll(false);
                sleep(random(400, 500));
            }
            Mouse.click(floor.getCenter(),
                        true);
            sleep(400, 600);
            Widgets.get(947).getComponent(761).interact("Confirm");
        }
        else if (Widgets.get(938).isValid()) {
            int timeout = 0;
            while (!Widgets.get(938).getComponent(51 + (MyPlayer.getComplexity() * 5)).interact("Select") &&
                   ++timeout <= 3)
                sleep(random(600, 750));
            sleep(400, 600);
            Widgets.get(938).getComponent(37).interact("Confirm");
        }
        else {
            entrance.interact("Climb");
            sleep(700, 800);
        }
        return random(400, 600);
    }


    public boolean isValid() {
        return Objects.getNearest(GameConstants.ENTRANCE) != null;
    }


    public void reset() {
    }

    public String getStatus() {
        return "Entering Dungeon";
    }

}
