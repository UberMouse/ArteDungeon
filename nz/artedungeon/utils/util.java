package nz.artedungeon.utils;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.dungeon.MyPlayer;
import nz.uberutils.helpers.Utils;


public class Util extends Utils
{
    /**
     * Check if tile is in current room
     *
     * @param tile to check
     * @return true if it is
     */
    public static boolean tileInRoom(Tile tile) {
        return MyPlayer.curArea() != null && MyPlayer.curArea().contains(tile);
    }

    /**
     * Click random tile in the current room on the map
     */
    public static void clickRandomTileOnMap() {
        Tile[] tiles = MyPlayer.currentRoom().getArea().getTileArray();
        Tile randTile = tiles[Random.nextInt(0, tiles.length)];
        randTile.clickOnMap();
    }

    public static boolean isMembers() {
        return DungeonMain.members;
    }

    public static Tile safePillar(Tile bossTile) {
        for (GameObject o : MyPlayer.currentRoom().getObjects(49265, 49266, 49267)) {
            if (o == null)
                continue;
            Tile t = o.getLocation();
            if (Calculations.distanceTo(t) < 10 &&
                Calculations.distanceTo(t) > 3 &&
                Calculations.distanceBetween(t, bossTile) > 6)
                return t;
        }
        return null;
    }
}