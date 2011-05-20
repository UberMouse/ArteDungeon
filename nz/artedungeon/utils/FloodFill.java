package nz.artedungeon.utils;

import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.RSBuddyCommon;
import nz.artedungeon.misc.GameConstants;

import java.util.ArrayList;



public class FloodFill extends RSBuddyCommon
{

    /**
     * Instantiates a new flood fill.
     *
     * @param parent instance of main script
     */
    public FloodFill(DungeonMain parent) {
        super(parent);
    }

    private final ArrayList<Tile> tiles = new ArrayList<Tile>();
    private int z = 0;

    /**
     * Fills current room.
     *
     * @param location the location to start from
     * @return the Tile[]'s found
     */
    public Tile[] fill(Tile location) {
        Tile[] tiles = getRoomTiles(location);
        for (Tile tile : tiles) {
            Tile[] checkTiles = Util.getSurroundingTiles(tile, true);
            for (Tile checkTile : checkTiles) {
                if (!this.tiles.contains(checkTile))
                    this.tiles.add(checkTile);
            }
        }
        Tile[] returnTiles = new Tile[this.tiles.size()];
        for (int i = 0; i < this.tiles.size(); i++)
            returnTiles[i] = this.tiles.get(i);
        return returnTiles;
    }

    /**
     * Gets the room tiles.
     *
     * @param location the location to start from
     * @return the room tiles
     */
    private Tile[] getRoomTiles(Tile location) {
        z++;
        if ((Util.getCollisionFlagAtTile(location) & GameConstants.WALL) != 0) {
            Tile[] tempTiles = new Tile[tiles.size()];
            for (int i = 0; i < tiles.size(); i++) {
                tempTiles[i] = tiles.get(i);
            }
            return tempTiles;
        }
        Tile[] checkTiles = Util.getSurroundingTiles(location, true);
        int[] flagTiles = Util.getSurroundingCollisionFlags(location, true);
        for (int i = 0; i < checkTiles.length; i++) {
            if ((flagTiles[i] & GameConstants.WALL) == 0 && z <= 1100) {
                if (!tiles.contains(checkTiles[i])) {
                    tiles.add(checkTiles[i]);
                    getRoomTiles(checkTiles[i]);
                }
            }
        }
        Tile[] tempTiles = new Tile[tiles.size()];
        for (int i = 0; i < tiles.size(); i++) {
            tempTiles[i] = tiles.get(i);
        }
        return tempTiles;
    }

    /**
     * Clear tile list.
     */
    public void clear() {
        tiles.clear();
    }

    /**
     * Fills room and returns only tiles on edge
     *
     * @param location the location to start from
     * @return the Tile[] array
     */
    public Tile[] edgeTiles(Tile location) {
        if (tiles.size() < 1)
            fill(location);
        ArrayList<Tile> edgeTiles = new ArrayList<Tile>();
        for (Tile tile : tiles) {
            if ((Util.getCollisionFlagAtTile(location) & GameConstants.WALL) != 0)
                continue;
            Tile[] checkTiles = Util.getSurroundingTiles(location, true);
            int[] flagTiles = Util.getSurroundingCollisionFlags(location, true);
            for (int i = 0; i < checkTiles.length; i++) {
                if ((flagTiles[i] & GameConstants.WALL) == 0)
                    continue;
                if ((flagTiles[i] & GameConstants.WALL) != 0) {
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
     * Gets the tiles in Tile[] form.
     *
     * @return the tiles
     */
    public Tile[] getTiles() {
        Tile[] returnTiles = new Tile[tiles.size()];
        for (int i = 0; i < tiles.size(); i++)
            returnTiles[i] = tiles.get(i);
        return returnTiles;
    }
}