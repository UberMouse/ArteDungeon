package nz.artedungeon.bosses;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.utils.Util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/1/11
 * Time: 6:08 PM
 * Package: nz.artedungeon.bosses;
 */
public class NightGazerKhighorahk extends Plugin
{
    private static final int[] SPECIAL_ANIMS = {13426, 13427, 13428, 13429};

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public int loop() {
        final Room cur = MyPlayer.currentRoom();
        final Npc khighorahk = cur.getNearestNpc("Khighorahk");
        if (khighorahk != null) {
            boolean specialActive = Util.arrayContains(SPECIAL_ANIMS, khighorahk.getAnimation());
            if (specialActive && Calculations.distanceTo(khighorahk.getLocation()) < 6) {
                Tile safeTile = Util.safePillar(khighorahk.getLocation());
                if (safeTile != null) {
                    safeTile.clickOnMap();
                    Util.waitUntilMoving(10);
                    for (int i = 0; i < 7; i++) {
                        GameObject pillar = cur.getNearestObject(49265);
                        if (pillar != null && Calculations.distanceTo(pillar) < 4) {
                            if (pillar.interact("Light")) {
                                sleep(800, 1100);
                                if (MyPlayer.get().getAnimation() != 13355 || MyPlayer.get().getAnimation() != 13354)
                                    pillar.interact("Light");
                                break;
                            }
                        }
                        if (Walking.isRunEnabled()) {
                            sleep(200, 300);
                        }
                        else if (Calculations.distanceTo(safeTile) < 2 ||
                                 Calculations.distanceTo(khighorahk.getLocation()) > 8) {
                            break;
                        }
                        if (Calculations.distanceTo(safeTile) < 3 && khighorahk.getAnimation() != 13429) {
                            sleep(400, 600);
                            break;
                        }
                        if (!Util.arrayContains(new int[]{13426, 13427, 13429}, khighorahk.getAnimation()))
                            break;
                        sleep(200, 250);
                    }
                    for (int c = 0; c < 4; c++) {
                        if (khighorahk.interact("Attack")) {
                            for (int v = 0; v <= 15 && !MyPlayer.inCombat(); v++)
                                sleep(100);
                            break;
                        }
                        sleep(500, 800);
                    }
                    safeTile = null;
                }
            }
            else {
                GameObject[] litPillars = cur.getObjects(49266, 49267);
                if (litPillars != null && litPillars.length < 2) {
                    if (Objects.getNearest(49265).interact("Light"))
                        sleep(800, 1200);
                }
                if (khighorahk.interact("Attack"))
                    for (int v = 0; v <= 15 && !MyPlayer.inCombat(); v++)
                        sleep(100);
            }
        }
        return Util.random(100, 200);
    }
}
