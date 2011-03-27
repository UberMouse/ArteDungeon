package nz.uberdungeon.utils;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.RSBuddyCommon;


// TODO: Auto-generated Javadoc
public class MyMovement extends RSBuddyCommon
{

    /**
     * Instantiates a new movement.
     *
     * @param parent instance of main script
     */
    public MyMovement(DungeonMain parent) {
        super(parent);
    }

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
            MyCamera.turnTo(tile, RSBuddyCommon.random(10, 25));
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

}
