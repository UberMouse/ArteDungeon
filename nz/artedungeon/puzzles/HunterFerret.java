package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyMovement;
import nz.uberutils.methods.MyObjects;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 7:20 PM
 * Package: nz.artedungeon.puzzles;
 */
public class HunterFerret extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Hunter ferret";
    }

    @Override
    public boolean isValid() {
        return Npcs.getNearest(GameConstants.HUNTER_FERRET) != null &&
               Util.tileInRoom(Npcs.getNearest(GameConstants.HUNTER_FERRET).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Hunter Ferret";
    }

    @Override
    public int loop() {
        final int[] CORNERS = {50663, 51762, 54785};
        final int DRY_LOGS = 17377, TRAPS = 17378, SET_TRAPS = 49592, COMPLETED_TRAPS = 49593;
        final Room cur = MyPlayer.currentRoom();
        Tile safeTile = null;
        while (cur.getNearestObject(COMPLETED_TRAPS) == null) {
            Npc ferret = cur.getNearestNpc(GameConstants.HUNTER_FERRET);
            GameObject branchesSource = cur.getNearestObject(49589, 49590, 49591, 54005);
            int trapsSet = Objects.getLoaded(new Filter<GameObject>()
            {
                public boolean accept(GameObject gameObject) {
                    return cur.contains(gameObject) && gameObject.getId() == SET_TRAPS;
                }
            }).length;
            if (trapsSet < 3) {
                if (Inventory.getCount(DRY_LOGS, TRAPS) < 3 - trapsSet) {
                    MyMovement.turnTo(branchesSource);
                    if (branchesSource.interact("Chop"))
                        Util.waitToStop();
                }
                else if (Inventory.contains(TRAPS)) {
                    Tile testCorner = null;
                    safeTile = null;
                    if (Calculations.distanceTo(MyPlayer.curArea().getCentralTile()) > 6) {
                        MyMovement.turnTo(MyPlayer.curArea().getCentralTile());
                        Util.waitToStop();
                    }
                    for (GameObject corner : cur.getObjects(CORNERS)) {
                        Tile cornerTile = corner.getLocation();
                        Tile test = MyObjects.getNearestTo(cornerTile, SET_TRAPS).getLocation();
                        if (test == null || Calculations.distanceBetween(cornerTile, test) > 3) {
                            testCorner = cornerTile;
                            break;
                        }
                    }
                    if (testCorner != null) {
                        Tile newTile = null;
                        for (Tile t : Util.getDiagonalTiles(testCorner)) {
                            if (cur.contains(t) && Objects.getTopAt(t) == null) {
                                newTile = t;
                                break;
                            }
                        }
                        if (newTile != null) {
                            for (Tile t : Util.getSurroundingTiles(newTile, false)) {
                                if (Objects.getTopAt(t) == null) {
                                    safeTile = t;
                                    break;
                                }
                            }
                            if (safeTile != null && Inventory.contains(TRAPS)) {
                                while (!MyPlayer.location().equals(safeTile)) {
                                    MyMovement.turnTo(safeTile);
                                    if (!Inventory.contains(TRAPS)) {
                                        if (!Inventory.contains(DRY_LOGS))
                                            break;
                                        Inventory.getItem(DRY_LOGS).click(true);
                                        sleep(400, 700);
                                    }
                                    Util.waitToStop();
                                }
                                if (ridItem(TRAPS, "Lay")) {
                                    sleep(1000, 1300);
                                    Util.waitToStop();
                                }
                            }
                        }
                    }
                }
                else if (Inventory.contains(DRY_LOGS)) {
                    Inventory.getItem(DRY_LOGS).click(true);
                    sleep(700, 900);
                }
            }
            else {
                if (ferret != null)
                    safeTile = ferret.getLocation();
                MyMovement.turnTo(safeTile);
            }
            Util.waitToStop();
        }
        while (cur.getNearestObject(COMPLETED_TRAPS) != null) {
            if (cur.getNearestObject(COMPLETED_TRAPS).interact("Pick-up"))
                Util.waitToStop();
            sleep(400, 600);
        }
        return 1;
    }

    private boolean ridItem(int itemID, String action) {
		if (!Inventory.contains(itemID))
			return true;
		int iCount = Inventory.getCount(itemID);
		for (int i = 0; i < 6; i++) {
			Item item = Inventory.getItem(itemID);
			if (item != null && Inventory.getCount(itemID) == iCount) {
				item.interact(action);
				sleep(1000, 1300);
			} else break;
			sleep(100, 200);
		}
		return iCount > Inventory.getCount(itemID);
	}
}
