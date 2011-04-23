package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.AStar;
import nz.uberutils.methods.MyMovement;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/24/11
 * Time: 11:01 PM
 * Package: nz.artedungeon.puzzles;
 */
public class ColoredFerret extends PuzzlePlugin
{
    public static int[] FERRET = {12165, 12197, 12169, 12171, 12173}, PAD = {54364, 54372, 54380, 54388, 54396};
    final int[] BAD_PLATES = {54365,
                              54367,
                              54369,
                              54371,
                              54373,
                              54375,
                              54377,
                              54379,
                              54381,
                              54383,
                              54385,
                              54387,
                              54389,
                              54391,
                              54393,
                              54395,
                              54397,
                              54399,
                              54401,
                              54403};
    final int[] RED_TILE = {54364, 54366, 54368, 54370}, BLUE_TILE = {54372, 54374, 54376, 54378}, GREEN_TILE = {54380,
                                                                                                                 54382,
                                                                                                                 54384,
                                                                                                                 54386}, YELLOW_TILE = {
            54388,
            54390,
            54392,
            54394}, ORANGE_TILE = {54396, 54398, 54400, 54402};
    final int[][] COLORED_PLATES = {BLUE_TILE, RED_TILE, YELLOW_TILE, GREEN_TILE, ORANGE_TILE};
    private final int[] COLORED_FERRETS = {12167, 12165, 12171, 12169, 12173};
    AStar distAStar = new AStar()
    {

        private double heuristic(Node start, Node end) {
            double dx = start.x - end.x;
            double dy = start.y - end.y;
            if (dx < 0)
                dx = -dx;
            if (dy < 0)
                dy = -dy;
            return dx + dy;
            //return dx < dy ? dy : dx;
            ////double diagonal = dx > dy ? dy : dx;
            ////double manhattan = dx + dy;
            ////return 1.41421356 * diagonal + (manhattan - 2 * diagonal);
        }

        private Tile[] path(Node end, int base_x, int base_y) {
            LinkedList<Tile> tempPath = new LinkedList<Tile>();
            Node p = end;
            while (p != null) {
                tempPath.addFirst(p.toTile(base_x, base_y));
                p = p.prev;
            }
            LinkedList<Tile> path = new LinkedList<Tile>();
            int x = MyPlayer.get().getLocation().getX(),
                    y = MyPlayer.get().getLocation().getY();
            for (int i = 0; i < tempPath.size(); i++) {
                Tile t = tempPath.get(i);
                if ((i > 0 && t.getX() != x && t.getY() != y) || i == tempPath.size() - 1) {
                    Tile t2 = tempPath.get(--i);
                    path.add(t2);
                    x = t2.getX();
                    y = t2.getY();
                }
            }
            return path.toArray(new Tile[path.size()]);
        }

        @Override
        public java.util.List<Node> customSuccessors(Node t) {
            java.util.List<Node> tiles = new LinkedList<Node>();
            int[][] flags = Walking.getCollisionFlags(Game.getFloorLevel());
            Tile offset = Walking.getCollisionOffset(Game.getFloorLevel());
            int off_x = offset.getX();
            int off_y = offset.getY();
            int x = t.x, y = t.y;
            int f_x = x - off_x, f_y = y - off_y;
            int here = flags[f_x][f_y];
            Tile ctile = t.toTile(Game.getMapBase().getX(), Game.getMapBase().getY());
            int tile_x = ctile.getX(), tile_y = ctile.getY();
            Tile ftile = getNextFerret().getLocation();
            int fx = ftile.getX(), fy = ftile.getY();
            if (f_y > 0) {
                if ((here & WALL_SOUTH) == 0 &&
                    (flags[f_x][f_y - 1] & BLOCKED) == 0 &&
                    Math.abs(fx - (x)) > 2 &&
                    Math.abs(fy - (y - 1)) > 2) {
                    tiles.add(new Node(x, y - 1));
                }
            }
            if (f_x > 0) {
                if ((here & WALL_WEST) == 0 &&
                    (flags[f_x - 1][f_y] & BLOCKED) == 0 &&
                    Math.abs(fx - (x - 1)) > 2 &&
                    Math.abs(fy - (y)) > 2) {
                    tiles.add(new Node(x - 1, y));
                }
            }
            if (f_y < 103) {
                if ((here & WALL_NORTH) == 0 &&
                    (flags[f_x][f_y + 1] & BLOCKED) == 0 &&
                    Math.abs(fx - (x)) > 2 &&
                    Math.abs(fy - (y + 1)) > 2) {
                    tiles.add(new Node(x, y + 1));
                }
            }
            if (f_x < 103) {
                if ((here & WALL_EAST) == 0 &&
                    (flags[f_x + 1][f_y] & BLOCKED) == 0 &&
                    Math.abs(fx - (x + 1)) > 2 &&
                    Math.abs(fy - (y)) > 2) {
                    tiles.add(new Node(x + 1, y));
                }
            }
            /*if (f_x > 0 && f_y > 0 && (here & (WALL_SOUTH_WEST | WALL_SOUTH | WALL_WEST)) == 0
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
            }*/
            return tiles;
        }
    };
    AStar ferretAStar = new AStar()
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
                if ((here & WALL_SOUTH) == 0 &&
                    (flags[f_x][f_y - 1] & BLOCKED) == 0 &&
                    (here & WALL_NORTH) == 0 &&
                    (flags[f_x][f_y + 1] & BLOCKED) == 0 &&
                    (flags[f_x][f_y + 1] & WALL_NORTH) == 0 &&
                    (flags[f_x][f_y + 2] & BLOCKED) == 0) {
                    tiles.add(new CustomNode(x, y - 1, x, y + 1));
                }
            }
            if (f_x > 0) {
                if ((here & WALL_WEST) == 0 &&
                    (flags[f_x - 1][f_y] & BLOCKED) == 0 &&
                    (here & WALL_EAST) == 0 &&
                    (flags[f_x + 1][f_y] & BLOCKED) == 0 &&
                    (flags[f_x + 1][f_y] & WALL_EAST) == 0 &&
                    (flags[f_x + 2][f_y] & BLOCKED) == 0) {
                    tiles.add(new CustomNode(x - 1, y, x + 1, y));
                }
            }
            if (f_y < 103) {
                if ((here & WALL_SOUTH) == 0 &&
                    (flags[f_x][f_y - 1] & BLOCKED) == 0 &&
                    (here & WALL_NORTH) == 0 &&
                    (flags[f_x][f_y + 1] & BLOCKED) == 0 &&
                    (flags[f_x][f_y - 1] & WALL_SOUTH) == 0 &&
                    (flags[f_x][f_y - 2] & BLOCKED) == 0) {
                    tiles.add(new CustomNode(x, y + 1, x, y - 1));
                }
            }
            if (f_x < 103) {
                if ((here & WALL_WEST) == 0 &&
                    (flags[f_x - 1][f_y] & BLOCKED) == 0 &&
                    (here & WALL_EAST) == 0 &&
                    (flags[f_x + 1][f_y] & BLOCKED) == 0 &&
                    (flags[f_x - 1][f_y] & WALL_WEST) == 0 &&
                    (flags[f_x - 2][f_y] & BLOCKED) == 0) {
                    tiles.add(new CustomNode(x + 1, y, x - 1, y));
                }
            }
            return tiles;
        }
    };

    public boolean isValid() {
        return Npcs.getNearest(FERRET) != null && Util.tileInRoom(Npcs.getNearest(FERRET).getLocation());
    }

    public String getStatus() {
        return "Solving: Coloured Ferrets";
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Coloured Ferrets";
    }

    @Override
    public int loop() {
        //        Npc ferret = getNextFerret();
        //        if (!MyPlayer.isMoving() && !ferret.isMoving()) {
        //            Tile[] ferretPath = ferretAStar.findPath(ferret.getLocation(), getPad(ferret).getLocation());
        //            if (ferretPath == null) {
        //                if (ferret.isOnScreen())
        //                    ferret.interact("Scare");
        //                else
        //                    MyMovement.turnTo(ferret);
        //                return Random.nextInt(400, 800);
        //            }
        //            Tile[] distPath = distAStar.findPath(MyPlayer.get().getLocation(), ferretPath[0]);
        //            if (distPath == null) {
        //                if (ferret.isOnScreen())
        //                    ferret.interact("Scare");
        //                else
        //                    MyMovement.turnTo(ferret);
        //                return Random.nextInt(400, 800);
        //            }
        //            Tile next = distPath[0];
        //            if (Calculations.isTileOnScreen(next)) {
        //                next.interact("Walk here");
        //            }
        //            else {
        //                MyCamera.turnTo(next);
        //                if (!Calculations.isTileOnScreen(next))
        //                    MyMovement.turnTo(ferret);
        //            }
        //        }
        //        return Random.nextInt(400, 800);
        Tile safeTile = null;
        while (Npcs.getNearest(COLORED_FERRETS) != null) {
            for (int I = 0; I < COLORED_FERRETS.length; I++) {
                int failCount = 0;
                Npc ferret = Npcs.getNearest(COLORED_FERRETS[I]);
                GameObject plate = MyPlayer.currentRoom().getNearestObject(COLORED_PLATES[I]);
                if (ferret == null || plate == null)
                    continue;
                safeTile = plate.getLocation();
                while (ferret != null && !ferret.getLocation().equals(safeTile)) {
                    Tile scareCheck = null, xCheck = null, yCheck = null;
                    boolean xBlocked = true, yBlocked = true;
                    int fX = ferret.getLocation().getX(), fY = ferret.getLocation().getY();
                    if (fX > safeTile.getX()) {
                        scareCheck = new Tile(fX - 1, fY);
                        xCheck = new Tile(fX + 2, fY);
                        if (!isGoodTile(xCheck, BAD_PLATES))
                            xCheck = new Tile(fX + 1, fY);
                    }
                    else if (fX < safeTile.getX()) {
                        scareCheck = new Tile(fX + 1, fY);
                        xCheck = new Tile(fX - 2, fY);
                        if (!isGoodTile(xCheck, BAD_PLATES))
                            xCheck = new Tile(fX - 1, fY);
                    }
                    if (isGoodTile(scareCheck, BAD_PLATES) && isGoodTile(xCheck, BAD_PLATES))
                        xBlocked = false;
                    if (fY > safeTile.getY()) {
                        scareCheck = new Tile(fX, fY - 1);
                        yCheck = new Tile(fX, fY + 2);
                        if (!isGoodTile(yCheck, BAD_PLATES))
                            yCheck = new Tile(fX, fY + 1);
                    }
                    else if (fY < safeTile.getY()) {
                        scareCheck = new Tile(fX, fY + 1);
                        yCheck = new Tile(fX, fY - 2);
                        if (!isGoodTile(yCheck, BAD_PLATES))
                            yCheck = new Tile(fX, fY - 1);
                    }
                    if (isGoodTile(scareCheck, BAD_PLATES) && isGoodTile(yCheck, BAD_PLATES))
                        yBlocked = false;
                    if (!xBlocked) {
                        if (!MyPlayer.location().equals(xCheck)) {
                            MyMovement.turnTo(xCheck);
                            nz.uberutils.helpers.Utils.debug("walking");
                            xCheck.interact("walk");
                            failCount = 0;
                        }
                        else
                            failCount++;
                    }
                    else if (!yBlocked) {
                        if (!MyPlayer.location().equals(yCheck)) {
                            MyMovement.turnTo(yCheck);
                            nz.uberutils.helpers.Utils.debug("walking");
                            yCheck.interact("walk");
                            failCount = 0;
                        }
                        else
                            failCount++;
                    }
                    else {
                        MyMovement.turnTo(ferret);
                        ferret.interact("scare");
                        nz.uberutils.helpers.Utils.waitUntilMoving(5);
                        nz.uberutils.helpers.Utils.waitUntilStopped(5);
                    }
                    sleep(500, 800);
                    failCount++;
                    while (ferret.isMoving()) {
                        sleep(300, 500);
                    }
                    if (failCount > 8) {
                        MyMovement.turnTo(ferret.getLocation());
                        nz.uberutils.helpers.Utils.waitUntilMoving(5);
                        nz.uberutils.helpers.Utils.waitUntilStopped(5);
                        failCount = 0;
                    }
                }
            }
        }
        return 1;
    }

    public GameObject getPad(Npc n) {
        return getPad(n.getId());
    }

    public GameObject getPad(int i) {
        for (int j = 0; j < FERRET.length; j++)
            if (PAD[j] == i)
                return Objects.getNearest(PAD[j]);
        return null;
    }

    public Npc getNextFerret() {
        for (int i : FERRET)
            if (Npcs.getNearest(i) != null)
                return Npcs.getNearest(i);
        return null;
    }

    private boolean isGoodTile(Tile t, int... badIDs) {
        if (t == null || !MyPlayer.currentRoom().contains(t))
            return false;
        for (GameObject o : Objects.getAllAt(t)) {
            if (nz.uberutils.helpers.Utils.arrayContains(badIDs, o.getId()))
                return false;
        }
        return ((nz.uberutils.helpers.Utils.getCollisionFlagAtTile(t) & GameConstants.WALL) == 0 &&
                (nz.uberutils.helpers.Utils.getCollisionFlagAtTile(t) & 0x100) == 0);
    }
}
