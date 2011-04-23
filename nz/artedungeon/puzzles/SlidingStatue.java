package nz.artedungeon.puzzles;

import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.AStar;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/18/11
 * Time: 7:24 AM
 * Package: nz.artedungeon.puzzles;
 */
public class SlidingStatue extends PuzzlePlugin implements PaintListener
{
    private static final int[] SLIDING_STATUE = {10954, 10955, 10956, 10957},
            STATIC_STATUE = {10942, 10943, 10944, 10945},
            STATUE = {10954, 10955, 10956, 10957, 10942, 10943, 10944, 10945};
    private static final int SLIDING_1 = 10954, SLIDING_2 = 10955, SLIDING_3 = 10956, SLIDING_4 = 10957,
            STATIC_1 = 10942, STATIC_2 = 10943, STATIC_3 = 10944, STATIC_4 = 10945;
    private static final int RUG = 51317;
    AStar pf = new AStar()
    {
        class CustomNode extends Node
        {
            public int x2, y2;

            public CustomNode(int x, int y, int x2, int y2) {
                super(x, y);
                this.x2 = x2;
                this.y2 = y2;
            }

            public Tile toTile2(int baseX, int baseY) {
                return new Tile(x2 + baseX, y2 + baseY);
            }
        }

        private Tile[] path(Node end, int base_x, int base_y) {
            java.util.LinkedList<Tile> path = new java.util.LinkedList<Tile>();
            CustomNode p = (CustomNode) end;
            while (p.prev != null) {
                path.addFirst(p.toTile2(base_x, base_y));
                p = (CustomNode) p.prev;
            }
            return path.toArray(new Tile[path.size()]);
        }

        @Override
        public java.util.List<Node> customSuccessors(Node t) {
            java.util.LinkedList<Node> tiles = new java.util.LinkedList<Node>();
            int x = t.x, y = t.y;
            int f_x = x - off_x, f_y = y - off_y;
            int here = flags[f_x][f_y];
            Tile ctile = t.toTile(Game.getMapBase().getX(), Game.getMapBase().getY());
            int tile_x = ctile.getX(), tile_y = ctile.getY();
            if (f_y > 0) {
                if ((here & WALL_SOUTH) == 0
                    && (flags[f_x][f_y - 1] & BLOCKED) == 0
                    && (here & WALL_NORTH) == 0
                    && (flags[f_x][f_y + 1] & BLOCKED) == 0
                    && Objects.getTopAt(new Tile(ctile.getX(), ctile.getY() - 1)) != null
                    && Objects.getTopAt(new Tile(ctile.getX(), ctile.getY() - 1)).getId() == RUG) {
                    tiles.add(new CustomNode(x, y - 1, x, y + 1));
                }
            }
            if (f_x > 0) {
                if ((here & WALL_WEST) == 0
                    && (flags[f_x - 1][f_y] & BLOCKED) == 0
                    && (here & WALL_EAST) == 0
                    && (flags[f_x + 1][f_y] & BLOCKED) == 0
                    && Objects.getTopAt(new Tile(ctile.getX() - 1, ctile.getY())) != null
                    && Objects.getTopAt(new Tile(ctile.getX() - 1, ctile.getY())).getId() == RUG) {
                    tiles.add(new CustomNode(x - 1, y, x + 1, y));
                }
            }
            if (f_y < 103) {
                if ((here & WALL_SOUTH) == 0
                    && (flags[f_x][f_y - 1] & BLOCKED) == 0
                    && (here & WALL_NORTH) == 0
                    && (flags[f_x][f_y + 1] & BLOCKED) == 0
                    && Objects.getTopAt(new Tile(ctile.getX(), ctile.getY() + 1)) != null
                    && Objects.getTopAt(new Tile(ctile.getX(), ctile.getY() + 1)).getId() == RUG) {
                    tiles.add(new CustomNode(x, y + 1, x, y - 1));
                }
            }
            if (f_x < 103) {
                if ((here & WALL_WEST) == 0
                    && (flags[f_x - 1][f_y] & BLOCKED) == 0
                    && (here & WALL_EAST) == 0
                    && (flags[f_x + 1][f_y] & BLOCKED) == 0
                    && Objects.getTopAt(new Tile(ctile.getX() + 1, ctile.getY())) != null
                    && Objects.getTopAt(new Tile(ctile.getX() + 1, ctile.getY())).getId() == RUG) {
                    tiles.add(new CustomNode(x + 1, y, x - 1, y));
                }
            }
            return tiles;
        }
    };

    public boolean isValid() {
        return getNextStatue() != null;
    }

    public String getStatus() {
        return "Solving: Sliding Statues";
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Sliding Statues";
    }

    public Tile getSolution(Npc n) {
        Npc stat = getStatic(n.getId());
        return new Tile(stat.getLocation().getX() + (verticalSymmetry() ? 0 : getOffset()),
                        stat.getLocation().getY() + (verticalSymmetry() ? getOffset() : 0));
    }

    public Npc getNextStatue() {
        if (!isSolved(Npcs.getNearest(SLIDING_1)) && Util.tileInRoom(Npcs.getNearest(SLIDING_1).getLocation()))
            return Npcs.getNearest(SLIDING_1);
        else if (!isSolved(Npcs.getNearest(SLIDING_2)) && Util.tileInRoom(Npcs.getNearest(SLIDING_2).getLocation()))
            return Npcs.getNearest(SLIDING_2);
        else if (!isSolved(Npcs.getNearest(SLIDING_3)) && Util.tileInRoom(Npcs.getNearest(SLIDING_3).getLocation()))
            return Npcs.getNearest(SLIDING_3);
        else if (!isSolved(Npcs.getNearest(SLIDING_4)) && Util.tileInRoom(Npcs.getNearest(SLIDING_4).getLocation()))
            return Npcs.getNearest(SLIDING_4);
        return null;
    }

    public boolean isSolved(Npc n) {
        if (n == null) return true;
        //if(!MyPlayer.currentRoom.contains(n))return true;
        for (int i = 0; i < SLIDING_STATUE.length; i++) {
            if (SLIDING_STATUE[i] == n.getId()) {
                if (Npcs.getNearest(STATIC_STATUE[i]) != null) {
                    return (verticalSymmetry()
                            && Math.abs(Npcs.getNearest(STATIC_STATUE[i]).getLocation().getY()
                                        - n.getLocation().getY()) == 7
                            && Npcs.getNearest(STATIC_STATUE[i]).getLocation().getX() == n.getLocation().getX())
                           || (!verticalSymmetry()
                               && Math.abs(Npcs.getNearest(STATIC_STATUE[i]).getLocation().getX()
                                           - n.getLocation().getX()) == 7
                               && Npcs.getNearest(STATIC_STATUE[i]).getLocation().getY() == n.getLocation().getY());
                }
            }
        }
        return false;
    }


    public Npc getSliding(int staticId) {
        for (int i = 0; i < STATIC_STATUE.length; i++) {
            if (STATIC_STATUE[i] == staticId) {
                return Npcs.getNearest(SLIDING_STATUE[i]);
            }
        }
        return null;
    }

    public Npc getStatic(int slidingId) {
        for (int i = 0; i < SLIDING_STATUE.length; i++) {
            if (SLIDING_STATUE[i] == slidingId) {
                return Npcs.getNearest(STATIC_STATUE[i]);
            }
        }
        return null;
    }

    public boolean verticalSymmetry() {
        return Math.round(Npcs.getNearest(STATIC_STATUE).getOrientation() / 90.0) % 2 == 1;
    }

    public int getOffset() {
        if (Math.round(Npcs.getNearest(STATIC_STATUE).getOrientation() / 90.0) > 1) return -7;
        return 7;
    }

    public void onRepaint(Graphics g) {
        for (Tile t : pf.findPath(getNextStatue().getLocation(), getSolution(getNextStatue())))
            highlight(g, t, Color.RED);
    }

    private void highlight(Graphics g, Tile t, Color fill) {
        Point pn = Calculations.tileToScreen(t, 0, 0, 0);
        Point px = Calculations.tileToScreen(t, 1, 0, 0);
        Point py = Calculations.tileToScreen(t, 0, 1, 0);
        Point pxy = Calculations.tileToScreen(t, 1, 1, 0);
        if (py.x != -1 && pxy.x != -1 && px.x != -1 && pn.x != -1) {
            g.setColor(fill);
            g.fillPolygon(new int[]{py.x, pxy.x, px.x, pn.x},
                          new int[]{py.y, pxy.y, px.y, pn.y}, 4);
        }
    }

    @Override
    public int loop() {
        if (!MyPlayer.isMoving() && !MyPlayer.isInteracting()) {
            Tile[] path = pf.findPath(getNextStatue().getLocation(), getSolution(getNextStatue()));
            if (path.length > 0) {
                if (!MyPlayer.get().getLocation().equals(path[0])) {
                    if (Calculations.isTileOnScreen(path[0])) {
                        path[0].interact("Walk here");
                    }
                    else {
                        path[0].clickOnMap();
                    }
                }
                else {
                    getNextStatue().interact("Push");
                }
            }
        }
        return Random.nextInt(400, 700);
    }
}
