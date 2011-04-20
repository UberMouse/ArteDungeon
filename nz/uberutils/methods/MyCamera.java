package nz.uberutils.methods;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Camera;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/20/11
 * Time: 2:10 PM
 * Package: nz.artedungeon.utils;
 */
public class MyCamera
{
    /**
     * Turns the Camera to specified <tt>Tile</tt>
     * @param tile the <tt>Tile</tt> to turn to
     * @param deviation max amount of deviation to add to angle
     */
    public static void turnTo(Tile tile, int deviation) {
        int angle = Camera.getAngleTo(tile);
        if(deviation < 16)
            deviation = 16;
        if (angle > Camera.getCompassAngle()) {
            int times = Camera.getAngleTo(Camera.getAngleTo(tile)) / 15;
            for (int i = 0; i < times; i++) {
                Camera.setCompassAngle(Camera.getCompassAngle() + 15);
                if (Calculations.isTileOnScreen(tile))
                    break;
            }
            Camera.setCompassAngle(Camera.getCompassAngle() + Random.nextInt(15, deviation));
        }
        else {
            int times = Camera.getAngleTo(Camera.getAngleTo(tile)) / 15;
            for (int i = 0; i < times; i++) {
                Camera.setCompassAngle(Camera.getCompassAngle() - 15);
                if (Calculations.isTileOnScreen(tile))
                    break;
            }
            Camera.setCompassAngle(Camera.getCompassAngle() - Random.nextInt(15, deviation));
        }
    }

    /**
     * Turns the Camera to specified <tt>Tile</tt>
     * @param tile the <tt>Tile</tt> to turn to
     */
    public static void turnTo(Tile tile) {
        turnTo(tile, 0);
    }

    /**
     * Turns the Camera to specified <tt>Tile</tt>
     * @param npc the <tt>Npc</tt> to turn to
     */
    public static void turnTo(Npc npc) {
        turnTo(npc.getLocation());
    }

    /**
     * Turns the Camera to specified <tt>Tile</tt>
     * @param npc the <tt>Npc</tt> to turn to
     * @param deviation max amount of deviation to add to angle
     */
    public static void turnTo(Npc npc, int deviation) {
        turnTo(npc.getLocation(), deviation);
    }

    /**
     * Turns the Camera to specified <tt>Tile</tt>
     * @param object the <tt>GameObject</tt> to turn to
     */
    public static void turnTo(GameObject object) {
        turnTo(object.getLocation());
    }

    /**
     * Turns the Camera to specified <tt>Tile</tt>
     * @param object the <tt>GameObject</tt> to turn to
     * @param deviation max amount of deviation to add to angle
     */
    public static void turnTo(GameObject object, int deviation) {
        turnTo(object.getLocation(), deviation);
    }
}
