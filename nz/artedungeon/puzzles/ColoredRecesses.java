package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Timer;
import com.rsbuddy.script.wrappers.Area;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyInventory;
import nz.uberutils.methods.MyMovement;
import nz.uberutils.methods.MyNpcs;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 6:57 PM
 * Package: nz.artedungeon.puzzles;
 */
public class ColoredRecesses extends PuzzlePlugin
{
    final int[] BLUE_TILES = {54504, 54546, 54623}, GREEN_TILES = {54506, 54548, 54625}, YELLOW_TILES = {54508,
                                                                                                         54550,
                                                                                                         54627}, VIOLET_TILES = {
            54510,
            54552,
            54629};
    final int[][] COLOR_TILES = {BLUE_TILES, GREEN_TILES, YELLOW_TILES, VIOLET_TILES};
    final int[] COLOR_VIALS = {19869, 19871, 19873, 19875};
    final String[] COLOR_NAMES = {"Blue", "Green", "Yellow", "Violet"};
    private Tile safeTile;
    GameObject tObj = null;
    private Timer stuckTimer = new Timer(5000);
    Tile oldLoc = null;

    @Override
    public String getStatus() {
        return "Solving: Colored Recesses";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.RECESS_FOUNTAINS) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.RECESS_FOUNTAINS).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Colored Recesses";
    }

    @Override
    public int loop() {
        Toolkit.getDefaultToolkit().beep();
        if(oldLoc == null)
            oldLoc = MyPlayer.location();
        if(!stuckTimer.isRunning() && oldLoc.equals(MyPlayer.location()))
            Util.clickRandomTileOnMap();
        else if(!oldLoc.equals(MyPlayer.location()))
            stuckTimer.reset();
        oldLoc = MyPlayer.location();
        Area fountain = MyPlayer.currentRoom().getNearestObject(GameConstants.RECESS_FOUNTAINS).getArea();
        GameObject shelves = MyPlayer.currentRoom().getNearestObject(35241, 35242, 35243, 35245);
        boolean blocksAligned = true;
        o:
        for (int[] color : COLOR_TILES) {
            for (GameObject tObj : MyPlayer.currentRoom().getObjects(color)) {
                if (MyNpcs.getTopAt(tObj.getLocation()) == null) {
                    blocksAligned = false;

                    break o;
                }
            }
        }
        if (blocksAligned) {
            while (!Inventory.containsAll(COLOR_VIALS) && !solved) {
                String color = "";
                Tile sTile = shelves.getLocation();
                for (String col : COLOR_NAMES) {
                    if (Inventory.getItemID(col + " vial") == -1) {
                        color = col;
                        break;
                    }
                }
                if (Widgets.get(238).isValid()) {
                    Util.getWidgetWithText(color).click();
                }
                else if (Calculations.distanceTo(sTile) > 3) {
                    MyMovement.turnTo(sTile);
                }
                else if (shelves.interact("Take-bottle")) {
                    Util.waitToStop();
                }
            }
           while(Inventory.containsOneOf(COLOR_VIALS) && !solved) {
                for (int I = 0; I < COLOR_TILES.length; I++) {
                    GameObject tObj = MyPlayer.currentRoom().getNearestObject(COLOR_TILES[I]);
                    if (tObj == null)
                        continue;
                    safeTile = tObj.getLocation();
                    Npc block = MyNpcs.getTopAt(safeTile);
                    if (block != null && Inventory.contains(COLOR_VIALS[I])) {
                        MyMovement.turnTo(block);
                        MyInventory.useItem(Inventory.getItem(COLOR_VIALS[I]), block);
                        Util.waitToStop();
                    }
                }
                sleep(200, 400);
            }
        }
        else {
            for (int[] color : COLOR_TILES) {
                if (tObj == null || MyNpcs.getTopAt(tObj.getLocation()) != null)
                    tObj = MyPlayer.currentRoom().getNearestObject(color);
                if (tObj == null)
                    continue;
            }
            safeTile = tObj.getLocation();
            Npc push = null;
            int dist = 999999999;
            for (Npc n : Npcs.getLoaded()) {
                if (Calculations.distanceBetween(n.getLocation(), tObj.getLocation()) < dist) {
                    push = n;
                    dist = (int) Calculations.distanceBetween(n.getLocation(), tObj.getLocation());
                }
            }
            if (push != null && MyNpcs.getTopAt(safeTile) == null) {
                boolean xBlocked = true, yBlocked = true;
                Tile checkX1 = null, checkX2 = null, checkY1 = null, checkY2 = null, pT = push.getLocation();
                int mY = 0, mX = 0, pX = pT.getX(), pY = pT.getY();
                if (pX < safeTile.getX()) {
                    checkX1 = new Tile(pX + 1, pY);
                    checkX2 = new Tile(pX + 2, pY);
                    mX = 1;
                }
                else if (pX > safeTile.getX()) {
                    checkX1 = new Tile(pX - 1, pY);
                    checkX2 = new Tile(pX - 2, pY);
                    mX = -1;
                }
                if (checkX1 != null) {
                    xBlocked = false;
                    Npc block = MyNpcs.getTopAt(checkX1);
                    if (block == null)
                        MyNpcs.getTopAt(checkX2);
                    if (block != null || fountain.contains(checkX1, checkX2)) {
                        log("The X pull is blocked!");
                        xBlocked = true;
                    }
                }
                if (pY < safeTile.getY()) {
                    checkY1 = new Tile(pX, pY + 1);
                    checkY2 = new Tile(pX, pY + 2);
                    mY = 1;
                }
                else if (pY > safeTile.getY()) {
                    checkY1 = new Tile(pX, pY - 1);
                    checkY2 = new Tile(pX, pY - 2);
                    mY = -1;
                }
                if (checkY1 != null) {
                    yBlocked = false;
                    Npc block = MyNpcs.getTopAt(checkY1);
                    if (block == null)
                        MyNpcs.getTopAt(checkY2);
                    if (block != null || fountain.contains(checkY1, checkY2)) {
                        log("The Y pull is blocked!");
                        yBlocked = true;
                    }
                }
                Tile pullLoc = null;
                if (!xBlocked)
                    pullLoc = new Tile(pT.getX() + mX, pT.getY());
                else if (!yBlocked)
                    pullLoc = new Tile(pT.getX(), pT.getY() + mY);
                else {
                    log("No good pull tile found, trying another statue!");
                }
                if (MyPlayer.location().equals(pullLoc)) {
                    if (push.interact("Pull")) {
                        for (int i = 0; i < 15 && MyPlayer.get().getAnimation() == -1; i++)
                            Task.sleep(100);
                        for (int i = 0; i < 15 && MyPlayer.get().getAnimation() != -1; i++)
                            Task.sleep(100);
                    }
                    else
                        sleep(200, 400);
                }
                else
                    MyMovement.walkTo(pullLoc);
            }


        }
        return 1;
    }
}
