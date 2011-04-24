package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyObjects;


/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 7:44 PM
 * Package: nz.artedungeon.puzzles;
 */
public class StrangeFlowers extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Strange Flowers";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.CENTER_FLOWERS) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.CENTER_FLOWERS).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Strange Flowers";
    }

    @Override
    public int loop() {
        final int[] C_FLOWERS = {35507, 35523, 35562, 35569};
        final String[] F_COLORS = {"blue", "purple", "red", "yellow"};

        GameObject centerFlower = MyPlayer.currentRoom().getNearestObject(GameConstants.CENTER_FLOWERS);
        Util.debug(Calculations.isReachable(centerFlower.getLocation(), true));
        if (centerFlower != null) {
            //            if (flowerCut) {
            //                waitToAnimate();
            //                waitToStop(true);
            //                sleep(500, 800);
            //                walkToMap(centerFlower.getLocation(), 1);
            //                flowerCut = false;
            //            }
            //            else
            if (Calculations.isReachable(centerFlower.getLocation(), true)) {
                if (centerFlower.interact("Uproot"))
                    Util.waitToStop();
            }
            else {
                int fID = centerFlower.getId();
                for (int I = 0; I < C_FLOWERS.length; I++) {
                    if (fID == C_FLOWERS[I]) {
                        String color = F_COLORS[I];
                        GameObject flower = MyObjects.getReachableObject("Strange " + color + " plant");
                        if (flower != null) {
                            if (flower.interact("Chop"))
                                Util.waitToStop();
                        }
                        break;
                    }
                }
            }
        }
        sleep(400, 800);
        return 1;
    }
}
