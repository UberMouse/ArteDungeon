package nz.artedungeon.misc;

import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.task.LoopTask;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.util.Timer;
import com.rsbuddy.script.wrappers.Component;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.dungeon.Dungeon;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.MyPlayer;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/28/11
 * Time: 11:56 AM
 * Package: nz.artedungeon.threads;
 */
public class FailSafeThread extends LoopTask
{
    private Tile lastPos;
    private final Timer posTimer = new Timer(180000);
    private int lastXp;
    private final Timer xpTimer = new Timer(1800000);
    private final int maxDeaths = 15;
    private static boolean leaving;
    private int lastCAngle;

    public boolean onStart() {
        lastCAngle = Camera.getCompassAngle();
        lastPos = Players.getLocal().getLocation();
        lastXp = Skills.getCurrentExp(Skills.DUNGEONEERING);
        return true;
    }

    @Override
    public int loop() {
        try {
            if (leaving && !MyPlayer.get().isInCombat()) {
                leaveDungeon();
                return 500;
            }
            if (!posTimer.isRunning()) {
                posTimer.reset();
                if (lastPos.equals(Players.getLocal().getLocation())) {
                    leaveDungeon();
                }
                lastPos = Players.getLocal().getLocation();
            }
            if (!xpTimer.isRunning()) {
                xpTimer.reset();
                if (Skills.getCurrentExp(Skills.DUNGEONEERING) == lastXp) {
                    leaveDungeon();
                }

            }
            if (Dungeon.timesDied() >= maxDeaths) {
                leaveDungeon();
            }
            if (Camera.getCompassAngle() == lastCAngle &&
                !(Explore.getBossRoom() != null && Explore.getBossRoom().contains(
                        MyPlayer.location()))) {
                switch (Random.nextInt(0, 2)) {
                    case 0:
                        Camera.setCompassAngle(Camera.getCompassAngle() + Random.nextInt(165, 175));
                        break;
                    case 1:
                        Camera.setCompassAngle(Camera.getCompassAngle() - Random.nextInt(165, 175));
                        break;
                }
            }
            lastCAngle = Camera.getCompassAngle();
        } catch (Exception ignored) {
        }
        return 10000;
    }

    public void leaveDungeon() {
        leaving = true;
        Component yes = Widgets.getComponent(236, 1);
        Component leave = Widgets.getComponent(939, 33);
        if (yes.isValid())
            yes.click();
        else if (Widgets.canContinue())
            Widgets.clickContinue();
        else if (leave.isValid() && Game.getCurrentTab() == Game.TAB_QUESTS && Explore.inDungeon())
            leave.click();
        else if (Game.getCurrentTab() != Game.TAB_QUESTS && Explore.inDungeon()) {
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
        else {
            Game.openTab(Game.TAB_INVENTORY);
            DungeonMain.timesAborted++;
            leaving = false;
        }
    }

    public static boolean leaving() {
        return leaving;
    }
}
