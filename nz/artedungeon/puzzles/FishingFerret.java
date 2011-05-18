package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.wrappers.*;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.uberutils.helpers.AStar;
import nz.uberutils.helpers.Utils;
import nz.uberutils.methods.MyInventory;
import nz.uberutils.methods.MyMovement;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/19/11
 * Time: 11:20 AM
 * Package: nz.artedungeon.puzzles;
 */
public class FishingFerret extends PuzzlePlugin
{
    static final int TILE = 49546, HOLE = 49549, PAD = 49555, FISHING_SPOT = 49561, FIRE = 49940;
    AStar astar = new AStar()
    {
        public java.util.List<Node> customSuccessors(Node t) {
            LinkedList<Node> tiles = new LinkedList<Node>();
            int x = t.x, y = t.y;
            try {
                if (Objects.getTopAt(new Tile(x + 1, y), Objects.TYPE_FLOOR_DECORATION).getId() == TILE ||
                    Objects.getTopAt(new Tile(x + 1, y), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x + 1, y));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x + 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE ||
                    Objects.getTopAt(new Tile(x + 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x + 1, y + 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE ||
                    Objects.getTopAt(new Tile(x, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x, y + 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x - 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE ||
                    Objects.getTopAt(new Tile(x - 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x - 1, y + 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x - 1, y), Objects.TYPE_FLOOR_DECORATION).getId() == TILE ||
                    Objects.getTopAt(new Tile(x - 1, y), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x - 1, y));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x - 1, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE ||
                    Objects.getTopAt(new Tile(x - 1, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x - 1, y - 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE ||
                    Objects.getTopAt(new Tile(x, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x, y - 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x + 1, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE ||
                    Objects.getTopAt(new Tile(x + 1, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x + 1, y - 1));
            } catch (Exception e) {
            }
            return tiles;
        }

    };

    public boolean isValid() {
        if (getFerret() != null && getPad() != null) {
            if (getFerret().getLocation().equals(getPad().getLocation()))
                return false;
            if (Calculations.distanceTo(Objects.getNearest(TILE, HOLE, PAD)) < 7) {
                if (inventoryContains("fishing rod") && inventoryContains("feather") || inventoryContains("vile fish"))
                    return true;
            }
        }
        return false;
    }

    public String getStatus() {
        return "Solving: Fishing Ferret";
    }

    public String getAuthor() {
        return "Zippy";
    }

    public String getName() {
        return "Fishing Ferret";
    }

    public int loop() {
        if (MyPlayer.get().isIdle() && !getFerret().isMoving()) {
            if (inventoryCount("vile fish") < getFishNeeded(getFerret().getLocation(), getPad().getLocation())) {

                if (Objects.getNearest("ishing spot").isOnScreen()) {
                    Objects.getNearest("ishing spot").interact("fish");
                }
                else {
                    MyMovement.turnTo(Objects.getNearest("ishing spot"));
                }
                return 1000;
            }
            if (inventoryContains("raw vile fish")) {
                if (Objects.getNearest("fire").isOnScreen()) {
                    Inventory.useItem(inventoryGet("raw vile fish"), Objects.getNearest("fire"));
                }
                else {
                    MyMovement.turnTo(Objects.getNearest("fire"));
                }
                return 1000;
            }
            Tile t = getNextTile(getFerretPath(), getPad().getLocation());
            GameObject tile = Objects.getTopAt(t, Objects.TYPE_FLOOR_DECORATION);
            MyMovement.turnTo(tile);
            Inventory.useItem(inventoryGet("vile fish"), tile);
            return 1500;
        }
        return 0;
    }

    public int getFishNeeded(Tile start, Tile end) {
        Tile cur = start;
        int count = 0;
        while (!cur.equals(end)) {
            cur = getNextTile(astar.findPath(cur, end), cur);
            count++;
        }
        return count;
    }

    public Tile getNextTile(Tile[] path, Tile start) {
        for (int i = path.length - 1; i >= 0; i--)
            if (canReach(start, path[i]))
                return path[i];
        return null;
    }

    public boolean canReach(Tile start, Tile end) {
        int x = start.getX(), y = start.getY(), x2 = end.getX(), y2 = end.getY();
        while ((x != x2 || y != y2) && isActive() && isValid()) {
            if (x != x2)
                x += (x < x2 ? 1 : -1);
            if (y != y2)
                y += (y < y2 ? 1 : -1);
            if (Objects.getTopAt(new Tile(x + 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() != TILE &&
                Objects.getTopAt(new Tile(x + 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() != PAD)
                return false;
        }
        return true;
    }

    public Tile[] getFerretPath() {
        return astar.findPath(getFerret().getLocation(), getPad().getLocation());
    }

    public GameObject getPad() {
        return Objects.getNearest(PAD);
    }

    public Npc getFerret() {
        return Npcs.getNearest("Ferret");
    }

    public Item getInventoryItem(String name) {
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase()))
                return i;
        }
        return null;
    }

    public Item inventoryGet(String name) {
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase()))
                return i;
        }
        return null;
    }

    public boolean inventoryContains(String name) {
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase()))
                return true;
        }
        return false;
    }

    public int inventoryCount(String name) {
        int count = 0;
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase()))
                count++;
        }
        return count;
    }

    public GroundItem nearestGroundItem(String name) {
        for (GroundItem i : GroundItems.getLoaded()) {
            if (i.getItem().getName().toLowerCase().contains(name.toLowerCase()))
                return i;
        }
        return null;
    }
}