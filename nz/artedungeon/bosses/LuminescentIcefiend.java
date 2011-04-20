package nz.artedungeon.bosses;


import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.EnemyDef;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.utils.MyCombat;
import nz.artedungeon.utils.util;
import nz.uberutils.methods.MyMovement;


public class LuminescentIcefiend extends Plugin
{
    static final int BOSS[] = {9912, 9913, 9914, 9915, 9916, 9917, 9918, 9919,
                               9920, 9921, 9922, 9923, 9924, 9925, 9926, 9927, 9928},
            PILLAR = 51300, SPEC_ATTACK = 13338;

    public boolean isValid() {
        return Npcs.getNearest(BOSS) != null && MyPlayer.currentRoom().contains(Npcs.getNearest(BOSS));
    }

    public String getStatus() {
        return "Killing the " + getName();
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Luminescent Ice Fiend";
    }

    @Override
    public int loop() {
        Npc boss = Npcs.getNearest(BOSS);
        MyCombat.doPrayerFor(new EnemyDef(boss));
        if (boss.getAnimation() == SPEC_ATTACK) {
            if (Walking.getEnergy() > 10)
                Walking.setRun(true);
            //getNextCorner().clickOnMap();
            util.clickRandomTileOnMap();
            sleep(Random.nextInt(50, 100));
        }
        else {
            if (MyPlayer.needToEat())
                MyPlayer.eat();
            if (!MyPlayer.inCombat()) {
                if (boss.isOnScreen())
                    boss.interact("Attack");
                else
                    MyMovement.turnTo(boss);
            }
        }
        return Random.nextInt(400, 800);
    }

    public Tile getRoomCenter() {
        GameObject[] pillars = Objects.getLoaded(new Filter<GameObject>()
        {
            public boolean accept(GameObject o) {
                return o.getId() == PILLAR && MyPlayer.currentRoom().contains(o);
            }
        });
        int x = 0, y = 0;
        for (GameObject o : pillars) {
            x += o.getLocation().getX() / pillars.length;
            y += o.getLocation().getY() / pillars.length;
        }
        return new Tile(x, y);
    }

    public Tile getNextCorner() {
        final Tile c = getRoomCenter();
        final boolean east = (c.getX() - MyPlayer.get().getLocation().getX() > 0),
                north = (c.getY() - MyPlayer.get().getLocation().getY() > 0);
        return Objects.getNearest(new Filter<GameObject>()
        {
            public boolean accept(GameObject o) {
                boolean e = (c.getX() - o.getLocation().getX() > 0),
                        n = (c.getY() - o.getLocation().getY() > 0);
                return o.getId() == PILLAR && MyPlayer.currentRoom().contains(o)
                       && (e != north) && (n == east);
            }
        }).getLocation();
    }
}
