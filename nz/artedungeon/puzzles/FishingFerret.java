package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.wrappers.*;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.uberutils.methods.MyInventory;
import nz.uberutils.methods.MyMovement;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/19/11
 * Time: 11:20 AM
 * Package: nz.artedungeon.puzzles;
 */
public class FishingFerret extends Plugin
{
    static final int TILE = 49546, HOLE = 49549, PAD = 49555, FISHING_SPOT = 49561, FIRE = 49940;
    AStar astar = new AStar();

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
        if (MyPlayer.get().getHpPercent() < 50) {
            // EAT FOOD
        }
        if (MyPlayer.get().isIdle() && !getFerret().isMoving()) {
            if (inventoryCount("vile fish") < getFishNeeded(getFerret().getLocation(), getPad().getLocation())) {
                if (Objects.getNearest("fishing spot").isOnScreen()) {
                    Objects.getNearest("fishing spot").interact("fish");
                }
                else {
                    MyMovement.turnTo(Objects.getNearest("fishing spot"));
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
            if (x != x2) x += (x < x2 ? 1 : -1);
            if (y != y2) y += (y < y2 ? 1 : -1);
            if (Objects.getTopAt(new Tile(x + 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() != TILE
                && Objects.getTopAt(new Tile(x + 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() != PAD)
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
            if (i.getName().toLowerCase().contains(name.toLowerCase())) return i;
        }
        return null;
    }

    public Item inventoryGet(String name) {
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) return i;
        }
        return null;
    }

    public boolean inventoryContains(String name) {
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) return true;
        }
        return false;
    }

    public int inventoryCount(String name) {
        int count = 0;
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) count++;
        }
        return count;
    }

    public GroundItem nearestGroundItem(String name) {
        for (GroundItem i : GroundItems.getLoaded()) {
            if (i.getItem().getName().toLowerCase().contains(name.toLowerCase())) return i;
        }
        return null;
    }

    static class AStar
    {

        static private class Node
        {

            public int x, y;
            public Node prev;
            public double g, f;

            public Node(int x, int y) {
                this.x = x;
                this.y = y;
                g = f = 0;
            }

            public Node(int x, int y, int g) {
                this.x = x;
                this.y = y;
                this.g = this.f = g;
            }

            @Override
            public int hashCode() {
                return (x << 4) | y;
            }

            @Override
            public boolean equals(Object o) {
                if (o instanceof Node) {
                    Node n = (Node) o;
                    return x == n.x && y == n.y;
                }
                return false;
            }

            @Override
            public String toString() {
                return "(" + x + "," + y + ")";
            }

            public Tile toTile(int baseX, int baseY) {
                return new Tile(x + baseX, y + baseY);
            }

        }


        private int off_x, off_y;

        public Tile[] findPath(Tile start, Tile end) {
            //int base_x = Game.getMapBase().getX(), base_y = Game.getMapBase().getY();
            int curr_x = start.getX(), curr_y = start.getY();
            int dest_x = end.getX(), dest_y = end.getY();

            // loaded region only

            // structs
            HashSet<Node> open = new HashSet<Node>();
            HashSet<Node> closed = new HashSet<Node>();
            Node curr = new Node(curr_x, curr_y);
            Node dest = new Node(dest_x, dest_y);

            curr.f = heuristic(curr, dest);
            open.add(curr);

            // search
            while (!open.isEmpty()) {
                curr = lowest_f(open);
                if (curr.equals(dest)) {
                    // reconstruct from pred tree
                    return path(curr, 0, 0);
                }
                open.remove(curr);
                closed.add(curr);
                for (Node next : successors(curr)) {
                    if (!closed.contains(next)) {
                        double t = curr.g + next.g + dist(curr, next);
                        boolean use_t = false;
                        if (!open.contains(next)) {
                            open.add(next);
                            use_t = true;
                        }
                        else if (t < next.g) {
                            use_t = true;
                        }
                        if (use_t) {
                            next.prev = curr;
                            next.g = t;
                            next.f = t + heuristic(next, dest);
                        }
                    }
                }
            }

            // no path
            return null;
        }

        private double heuristic(Node start, Node end) {
            double dx = start.x - end.x;
            double dy = start.y - end.y;
            if (dx < 0) dx = -dx;
            if (dy < 0) dy = -dy;
            return dx < dy ? dy : dx;
            //double diagonal = dx > dy ? dy : dx;
            //double manhattan = dx + dy;
            //return 1.41421356 * diagonal + (manhattan - 2 * diagonal);
        }

        private double dist(Node start, Node end) {
            if (start.x != end.x && start.y != end.y) {
                return 1.41421356;
            }
            else {
                return 1.0;
            }
        }

        private Node lowest_f(Set<Node> open) {
            Node best = null;
            for (Node t : open) {
                if (best == null || t.f < best.f) {
                    best = t;
                }
            }
            return best;
        }

        private Tile[] path(Node end, int base_x, int base_y) {
            LinkedList<Tile> path = new LinkedList<Tile>();
            Node p = end;
            while (p != null) {
                path.addFirst(p.toTile(base_x, base_y));
                p = p.prev;
            }
            return path.toArray(new Tile[path.size()]);
        }

        private java.util.List<Node> successors(Node t) {
            LinkedList<Node> tiles = new LinkedList<Node>();
            int x = t.x, y = t.y;
            try {
                if (Objects.getTopAt(new Tile(x + 1, y), Objects.TYPE_FLOOR_DECORATION).getId() == TILE
                    || Objects.getTopAt(new Tile(x + 1, y), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x + 1, y));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x + 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE
                    || Objects.getTopAt(new Tile(x + 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x + 1, y + 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE
                    || Objects.getTopAt(new Tile(x, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x, y + 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x - 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE
                    || Objects.getTopAt(new Tile(x - 1, y + 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x - 1, y + 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x - 1, y), Objects.TYPE_FLOOR_DECORATION).getId() == TILE
                    || Objects.getTopAt(new Tile(x - 1, y), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x - 1, y));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x - 1, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE
                    || Objects.getTopAt(new Tile(x - 1, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x - 1, y - 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE
                    || Objects.getTopAt(new Tile(x, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x, y - 1));
            } catch (Exception e) {
            }
            try {
                if (Objects.getTopAt(new Tile(x + 1, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == TILE
                    || Objects.getTopAt(new Tile(x + 1, y - 1), Objects.TYPE_FLOOR_DECORATION).getId() == PAD)
                    tiles.add(new Node(x + 1, y - 1));
            } catch (Exception e) {
            }
            return tiles;
        }

    }
}
