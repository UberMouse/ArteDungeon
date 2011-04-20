package nz.artedungeon.utils;

import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.uberutils.helpers.Utils;

// TODO: Auto-generated Javadoc
public class util extends Utils
{
    /**
     * Check if tile is in current room
     *
     * @param tile to check
     * @return true if it is
     */
    public static boolean tileInRoom(Tile tile) {
        AStar pf = new AStar()
        {
            @Override
            public java.util.List<AStar.Node> customSuccessors(Node t) {
                java.util.LinkedList<Node> path = new java.util.LinkedList<Node>();
                for (Tile tile : getSurroundingTiles(t.toTile(Game.getMapBase().getX(), Game.getMapBase().getY()),
                                                     true)) {
                    if ((getCollisionFlagAtTile(tile) & GameConstants.WALL) == 0) {
                        path.add(new Node(tile.getX() - Game.getMapBase().getX(),
                                          tile.getY() - Game.getMapBase().getY()));
                    }
                }
                return path;
            }
        };
        return pf.findPath(MyPlayer.location(), tile) != null;
    }

    /**
     * Click random tile in the current room on the map
     */
    public static void clickRandomTileOnMap() {
        Tile[] tiles = MyPlayer.currentRoom().getArea().getTileArray();
        Tile randTile = tiles[Random.nextInt(0, tiles.length)];
        randTile.clickOnMap();
    }
}