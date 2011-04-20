package nz.artedungeon.strategies;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.Dungeon;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.util;
import nz.uberutils.methods.MyMovement;


public class EndDungeon extends Strategy
{

    public EndDungeon(DungeonMain parent) {
        super(parent);
    }


    public int execute() {
        GameObject finishedLadder = Objects.getNearest(GameConstants.FINISHEDLADDERS);
        if (finishedLadder != null) {
            Tile finishTile = util.getNearestNonWallTile(finishedLadder.getLocation());
            MyMovement.turnTo(finishTile);
            if (!MyPlayer.location().equals(finishTile)) {
                if (!Calculations.isTileOnScreen(finishTile))
                    finishTile.clickOnMap();
                else
                    finishTile.interact("Walk");
            }
            int timeout = 0;
            while (!MyPlayer.isMoving() && ++timeout <= 15)
                sleep(100);
            timeout = 0;
            while (!finishedLadder.isOnScreen() && ++timeout <= 15)
                sleep(100);
            if (finishedLadder.click()) {
                timeout = 0;
                while (!Widgets.getComponent(236, 1).isValid() && ++timeout <= 15)
                    sleep(100);
                if (Widgets.getComponent(236, 1).isValid())
                    Widgets.getComponent(236, 1).click();
                else if (Widgets.getComponent(933, 13).isValid())
                    Widgets.getComponent(933, 13).click();
                if (Widgets.get(933).isValid()) {
                    while (Widgets.get(933).isValid()) {
                        MyPlayer.addTokensGained(Integer.parseInt(Widgets.getComponent(933, 41)
                                                                         .getText()
                                                                         .replace("%", "")));
                        if (Widgets.getComponent(519, 1).isValid())
                            parent.prestige = true;
                        if (Widgets.getComponent(933, 322).isValid())
                            if (Widgets.getComponent(933, 322).click())
                                break;
                        sleep(100);
                    }
                    while (Widgets.get(933).isValid()) {
                        sleep(100);
                    }
                    parent.clearAll();
                    parent.dungeonsDone++;
                    Dungeon.iFloor();
                }
            }
        }
        return random(400, 600);
    }


    public boolean isValid() {
        return Objects.getNearest(GameConstants.FINISHEDLADDERS) != null
               && (Explore.getBossRoom() != null) && Explore.getBossRoom().contains(MyPlayer.location());
    }


    public void reset() {

    }

    public String getStatus() {
        return "Finishing dungeon";
    }

}
