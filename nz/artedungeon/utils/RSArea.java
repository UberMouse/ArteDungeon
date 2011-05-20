package nz.artedungeon.utils;

import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.wrappers.Area;
import com.rsbuddy.script.wrappers.Tile;

import java.util.ArrayList;



public class RSArea extends Area
{
    private final Tile[] tiles;

    /**
     * Instantiates a new nz.artedungeon.utils.RSArea
     *
     * @param tiles the tiles that form the area
     */
    public RSArea(Tile[] tiles) {
        super(tiles);
        this.tiles = tiles;
    }

    /**
     * Contains location.
     *
     * @param x the x
     * @param y the y
     * @return true, if successful
     */
    public boolean contains(int x, int y) {
        return contains(new Tile(x, y));
    }

    /**
     * Contains Tiles.
     *
     * @param tiles the tiles
     * @return true, if successful
     */
    public boolean contains(Tile... tiles) {
        Tile[] areaTiles = getRect().getTileArray();
        for (Tile check : tiles) {
            for (Tile space : areaTiles) {
                if (check.equals(space)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the tile array.
     *
     * @return the tile array
     */
    public Tile[] getTileArray() {
        return getRect().getTileArray();
    }

    /**
     * Gets the central tile.
     *
     * @return the central tile
     */
    public Tile getCentralTile() {
        if (tiles.length < 1)
            return null;
        int totalX = 0, totalY = 0;
        for (Tile tile : getRect().getTileArray()) {
            totalX += tile.getX();
            totalY += tile.getY();
        }
        return new Tile(Math.round(totalX / tiles.length), Math.round(totalY / tiles.length));
    }

    private static final int WALL = 0x200000;

    public Tile[] getEdgeTiles() {
        ArrayList<Tile> edgeTiles = new ArrayList<Tile>();
        for (Tile tile : tiles) {
            int[][] flags = Walking.getCollisionFlags(Game.getFloorLevel());
            int x = tile.getX();
            int y = tile.getY();
            int xOff = x - Game.getMapBase().getX() - Walking.getCollisionOffset(Game.getFloorLevel()).getX();
            int yOff = y - Game.getMapBase().getY() - Walking.getCollisionOffset(Game.getFloorLevel()).getY();
            if (!(xOff > -1 && yOff > -1 && xOff < 110 && yOff < 110))
                continue;
            if ((flags[xOff][yOff] & WALL) != 0)
                continue;
            Tile north = new Tile(x, y + 1);
            int fNorth = flags[xOff][yOff + 1];
            Tile northEast = new Tile(x + 1, y + 1);
            int fNorthEast = flags[xOff + 1][yOff + 1];
            Tile east = new Tile(x + 1, y);
            int fEast = flags[xOff + 1][yOff];
            Tile southEast = new Tile(x + 1, y - 1);
            int fSouthEast = flags[xOff + 1][yOff - 1];
            Tile south = new Tile(x, y - 1);
            int fSouth = flags[xOff][yOff - 1];
            Tile southWest = new Tile(x - 1, y - 1);
            int fSouthWest = flags[xOff - 1][yOff - 1];
            Tile west = new Tile(x - 1, y);
            int fWest = flags[xOff - 1][yOff];
            Tile northWest = new Tile(x - 1, y + 1);
            int fNorthWest = flags[xOff - 1][yOff + 1];
            Tile[] checkTiles = {north, northEast, east, southEast, south, southWest, west, northWest};
            int[] flagTiles = {fNorth, fNorthEast, fEast, fSouthEast, fSouth, fSouthWest, fWest, fNorthWest};
            for (int i = 0; i < checkTiles.length; i++) {
                if ((flagTiles[i] & WALL) == 0)
                    continue;
                if ((flagTiles[i] & WALL) != 0) {
                    Tile check = checkTiles[i];
                    if (!edgeTiles.contains(check)) {
                        edgeTiles.add(check);
                    }
                }
            }
        }
        Tile[] areaTiles = new Tile[edgeTiles.size()];
        for (int i = 0; i < edgeTiles.size(); i++)
            areaTiles[i] = edgeTiles.get(i);
        return areaTiles;
    }

    /**
     * Creates new Area covering the entire room.
     *
     * @return Area covering room
     */
    public Area getRect() {
        int xMin = 999999999, xMax = -1, yMin = 99999999, yMax = -1;
        for (Tile tile : tiles) {
            if (tile.getX() > xMax)
                xMax = tile.getX();
            else if (tile.getX() < xMin)
                xMin = tile.getX();
            else if (tile.getY() > yMax)
                yMax = tile.getY();
            else if (tile.getY() < yMin)
                yMin = tile.getY();
        }
        return new Area(new Tile(xMin, yMin), new Tile(xMax, yMax));
    }
}
