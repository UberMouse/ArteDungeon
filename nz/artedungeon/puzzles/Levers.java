package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Mouse;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/23/11
 * Time: 8:43 PM
 * Package: nz.artedungeon.puzzles;
 */
public class Levers extends PuzzlePlugin
{

    public boolean isValid() {
        return Objects.getNearest(GameConstants.LEVERS) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.LEVERS).getLocation());
    }

    public String getStatus() {
        return "Solving: Levers";
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Levers";
    }

    public int loop() {
        Mouse.setSpeed(1);
        GameObject lever;
        if (getLevers().length == 5)
            lever = getFarthestLever();
        else
            lever = Objects.getNearest(GameConstants.LEVERS);
        if (lever != null) {
            if (lever.isOnScreen())
                lever.interact("Pull");
            else
                lever.getLocation().clickOnMap();
        }
        return Random.nextInt(50, 100);
    }

    public GameObject[] getLevers() {
        return Objects.getLoaded(new Filter<GameObject>()
        {
            public boolean accept(GameObject o) {
                return Utils.arrayContains(GameConstants.LEVERS, o.getId());
            }
        });
    }

    public GameObject getFarthestLever() {
        GameObject[] levers = getLevers();
        double[] arr = new double[levers.length];
        double max = 0;
        GameObject obj = null;
        for (int i = 0; i < levers.length; i++) {
            double min = 999;
            for (int j = 0; j < levers.length; j++) {
                if (i != j && Calculations.distanceBetween(levers[i].getLocation(), levers[j].getLocation()) < min)
                    min = Calculations.distanceBetween(levers[i].getLocation(), levers[j].getLocation());
            }
            if (min > max) {
                max = min;
                obj = levers[i];
            }
        }
        return obj;
    }
}
