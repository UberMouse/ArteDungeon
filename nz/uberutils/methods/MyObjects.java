package nz.uberutils.methods;

import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;

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
}
