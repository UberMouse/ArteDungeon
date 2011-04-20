package nz.artedungeon.utils;

import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.wrappers.Tile;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class AStar
{
    public static final int WALL_NORTH_WEST = 0x1;
    public static final int WALL_NORTH = 0x2;
    public static final int WALL_NORTH_EAST = 0x4;
    public static final int WALL_EAST = 0x8;
    public static final int WALL_SOUTH_EAST = 0x10;
    public static final int WALL_SOUTH = 0x20;
    public static final int WALL_SOUTH_WEST = 0x40;
    public static final int WALL_WEST = 0x80;
    public static final int BLOCKED = 0x100;

    public class Node
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


    public int[][] flags;
    public int off_x, off_y;

    public Tile[] findPath(Tile start, Tile end) {
        int base_x = Game.getMapBase().getX(), base_y = Game.getMapBase().getY();
        int curr_x = start.getX() - base_x, curr_y = start.getY() - base_y;
        int dest_x = end.getX() - base_x, dest_y = end.getY() - base_y;

        // load client data
        flags = Walking.getCollisionFlags(Game.getFloorLevel());
        Tile offset = Walking.getCollisionOffset(Game.getFloorLevel());
        off_x = offset.getX();
        off_y = offset.getY();

        // loaded region only
        if (flags == null || curr_x < 0 || curr_y < 0 ||
            curr_x >= flags.length || curr_y >= flags.length ||
            dest_x < 0 || dest_y < 0 ||
            dest_x >= flags.length || dest_y >= flags.length) {
            return null;
        }

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
                return path(curr, base_x, base_y);
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

    //Override this method to use a custom successor list
    public java.util.List<Node> customSuccessors(Node t) {
        return null;
    }

    private java.util.List<Node> successors(Node t) {
        java.util.List<Node> tiles = customSuccessors(t);
        if (tiles != null) return tiles;
        tiles = new LinkedList<Node>();
        flags = Walking.getCollisionFlags(Game.getFloorLevel());
        Tile offset = Walking.getCollisionOffset(Game.getFloorLevel());
        off_x = offset.getX();
        off_y = offset.getY();
        int x = t.x, y = t.y;
        int f_x = x - off_x, f_y = y - off_y;
        int here = flags[f_x][f_y];
        Tile ctile = t.toTile(Game.getMapBase().getX(), Game.getMapBase().getY());
        int tile_x = ctile.getX(), tile_y = ctile.getY();
        if (f_y > 0) {
            if ((here & WALL_SOUTH) == 0
                && (flags[f_x][f_y - 1] & BLOCKED) == 0) {
                tiles.add(new Node(x, y - 1));
            }
        }
        if (f_x > 0) {
            if ((here & WALL_WEST) == 0
                && (flags[f_x - 1][f_y] & BLOCKED) == 0) {
                tiles.add(new Node(x - 1, y));
            }
        }
        if (f_y < 103) {
            if ((here & WALL_NORTH) == 0
                && (flags[f_x][f_y + 1] & BLOCKED) == 0) {
                tiles.add(new Node(x, y + 1));
            }
        }
        if (f_x < 103) {
            if ((here & WALL_EAST) == 0
                && (flags[f_x + 1][f_y] & BLOCKED) == 0) {
                tiles.add(new Node(x + 1, y));
            }
        }
        if (f_x > 0 && f_y > 0 && (here & (WALL_SOUTH_WEST | WALL_SOUTH | WALL_WEST)) == 0
            && (flags[f_x - 1][f_y - 1] & BLOCKED) == 0
            && (flags[f_x][f_y - 1] & (BLOCKED | WALL_WEST)) == 0
            && (flags[f_x - 1][f_y] & (BLOCKED | WALL_SOUTH)) == 0) {
            tiles.add(new Node(x - 1, y - 1));
        }
        if (f_x > 0 && f_y < 103 && (here & (WALL_NORTH_WEST | WALL_NORTH | WALL_WEST)) == 0
            && (flags[f_x - 1][f_y + 1] & BLOCKED) == 0
            && (flags[f_x][f_y + 1] & (BLOCKED | WALL_WEST)) == 0
            && (flags[f_x - 1][f_y] & (BLOCKED | WALL_NORTH)) == 0) {
            tiles.add(new Node(x - 1, y + 1));
        }
        if (f_x < 103 && f_y > 0 && (here & (WALL_SOUTH_EAST | WALL_SOUTH | WALL_EAST)) == 0
            && (flags[f_x + 1][f_y - 1] & BLOCKED) == 0
            && (flags[f_x][f_y - 1] & (BLOCKED | WALL_EAST)) == 0
            && (flags[f_x + 1][f_y] & (BLOCKED | WALL_SOUTH)) == 0) {
            tiles.add(new Node(x + 1, y - 1));
        }
        if (f_x > 0 && f_y < 103 && (here & (WALL_NORTH_EAST | WALL_NORTH | WALL_EAST)) == 0
            && (flags[f_x + 1][f_y + 1] & BLOCKED) == 0
            && (flags[f_x][f_y + 1] & (BLOCKED | WALL_EAST)) == 0
            && (flags[f_x + 1][f_y] & (BLOCKED | WALL_NORTH)) == 0) {
            tiles.add(new Node(x + 1, y + 1));
        }
        return tiles;
    }

}