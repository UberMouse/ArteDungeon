package nz.uberutils.methods;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;
import nz.uberutils.helpers.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/6/11
 * Time: 9:21 PM
 * Package: nz.artedungeon.utils;
 */
public class MyObjects
{
    public static GameObject getTopAt(Tile tile, int... id) {
        GameObject[] objects = Objects.getAllAt(tile);
        for (GameObject object : objects) {
            for (int doorID : id)
                if (object.getId() == doorID)
                    return object;
        }
        return null;
    }

    public static GameObject getTopAt(Tile tile, int[][] ids) {
        for (int[] subIds : ids)
            if (getTopAt(tile, subIds) != null)
                return getTopAt(tile, subIds);
        return null;
    }

    public static GameObject getNearestTo(Tile one, int... ids) {
        double dist = 99999.99;
        GameObject bestObject = null;
        if (one != null) {
            for (GameObject o : Objects.getLoaded(new Filter<GameObject>()
            {
                public boolean accept(GameObject gameObject) {
                    return Utils.canReach(gameObject.getLocation());
                }
            })) {
                Tile oT = o.getLocation();
                double oDist = Calculations.distanceBetween(oT, one);
                if (oDist < dist) {
                    dist = oDist;
                    bestObject = o;
                }
            }
        }
        return bestObject;
    }
}
