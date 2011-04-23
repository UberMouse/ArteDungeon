package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 6:53 PM
 * Package: nz.artedungeon.puzzles;
 */
public class BloodFountain extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Blood fountain";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.DRY_BLOOD_FOUNTAIN) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.DRY_BLOOD_FOUNTAIN).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Blood fountain";
    }

    @Override
    public int loop() {
        final int[] RUBBLE = {54130, 54131, 54132, 54133, 54134, 541325, 54138, 54139, 54140, 54141, 54142};
        final int[] PILLARS = {54118, 54122, 54123, 54124, 54125, 54126};
        GameObject rubble = null, pillar = null;
        while (MyPlayer.currentRoom().getNearestObject(54114, 54115, 54116, 54117) == null) {
            if (MyPlayer.needToEat() && MyPlayer.hasFood())
                MyPlayer.eat();
            if (MyPlayer.get().getAnimation() != -1)
                return 100;
            rubble = MyPlayer.currentRoom().getNearestObject(RUBBLE);
            pillar = MyPlayer.currentRoom().getNearestObject(PILLARS);
            if (rubble != null && pillar != null) {
                if (Calculations.distanceTo(rubble) < Calculations.distanceTo(pillar) ?
                    rubble.interact("Mine") :
                    pillar.interact("Fix")) {
                    for (int i = 0; i < 15 && MyPlayer.get().getAnimation() == -1; i++)
                        Utils.sleep(100);
                }
            }
            else if (rubble == null) {
                if (pillar.interact("Fix"))
                    for (int i = 0; i < 15 && MyPlayer.get().getAnimation() == -1; i++)
                        Utils.sleep(100);
            }
            else {
                if (rubble.interact("Mine"))
                    for (int i = 0; i < 15 && MyPlayer.get().getAnimation() == -1; i++)
                        Utils.sleep(100);
            }
        }
        return 1;
    }
}
