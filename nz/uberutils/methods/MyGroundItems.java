package nz.uberutils.methods;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Menu;
import com.rsbuddy.script.methods.Mouse;
import com.rsbuddy.script.wrappers.GroundItem;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/19/11
 * Time: 11:03 AM
 * Package: nz.artedungeon.utils;
 */
public class MyGroundItems
{
    /* Attempts to interact with the specified GroundItem using the
     * specified String
     * If the item is not found under the cursor at ground height, the cursor
     * is moved to table height.
     * @param i GroundItem to interact with
     * @param s action to do
     * @return true if successfully clicked
     */
    public static boolean itemInteract(GroundItem i, String s) {
        s = s.toLowerCase();
        Point p1 = Calculations.tileToScreen(i.getLocation(), .5, .5, 0),
                p2 = Calculations.tileToScreen(i.getLocation(), .5, .5, -400);
        if (p1.x != 0 || p1.y != 0) {
            Mouse.move(p1);
            if (Menu.contains(s + " " + i.getItem().getName()))
                return Menu.click(s + " " + i.getItem().getName());
        }
        if (p2.x != 0 || p2.y != 0) {
            Mouse.move(p2);
            if (Menu.contains(s + " " + i.getItem().getName())) {
                return Menu.click(s + " " + i.getItem().getName());
            }
        }
        return false;
    }
}
