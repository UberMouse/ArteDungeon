package nz.uberutils.methods;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.uberutils.helpers.MyPlayer;
import nz.uberutils.helpers.Utils;



public class MyMovement
{
    /**
     * Turn to object.
     *
     * @param object the object
     */
    public static void turnTo(GameObject object) {
        if (object == null)
            return;
        turnTo(object.getLocation());
    }

    /**
     * Turn to npc.
     *
     * @param npc the npc
     */
    public static void turnTo(Npc npc) {
        if (npc == null)
            return;
        turnTo(npc.getLocation());
    }

    /**
     * Turn to tile.
     *
     * @param tile the tile
     */
    public static void turnTo(Tile tile) {
        if (tile == null)
            return;
        if (!Calculations.isTileOnScreen(tile)) {
            if (Calculations.distanceTo(tile) > 6)
                Walking.getTileOnMap(tile).clickOnMap();
            MyCamera.turnTo(tile, Random.nextInt(10, 25));
            if (!Calculations.isTileOnScreen(tile))
                Walking.getTileOnMap(tile).clickOnMap();
        }
    }

    /**
     * Reverse path.
     *
     * @param other the path to reverse
     * @return the reversed path
     */
    public static Tile[] reversePath(Tile[] other) {
        Tile[] t = new Tile[other.length];
        for (int i = 0; i < t.length; i++) {
            t[i] = other[other.length - i - 1];
        }
        return t;
    }

    public static void walkTo(Tile tile) {
        MyMovement.turnTo(tile);
        if (!MyPlayer.isMoving() && Calculations.isTileOnScreen(tile))
            if(tile.interact("Walk"))
                Utils.waitToStop();
    }

    public static void walkTo(GameObject object) {
        walkTo(object.getLocation());
    }

    public static void walkTo(Npc n) {
        walkTo(n.getLocation());
    }

}
