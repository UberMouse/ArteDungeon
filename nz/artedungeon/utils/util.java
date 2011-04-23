package nz.artedungeon.utils;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.uberutils.helpers.Utils;

// TODO: Auto-generated Javadoc
public class Util extends nz.uberutils.helpers.Utils
{
    /**
     * Check if tile is in current room
     *
     * @param tile to check
     * @return true if it is
     */
    public static boolean tileInRoom(Tile tile) {
        return Calculations.distanceTo(tile) <= 12;
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