package nz.artedungeon.bosses;

import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Npc;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.uberutils.methods.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 1/29/11
 * Time: 11:53 PM
 */
public class Default extends Plugin
{
    public boolean isValid() {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc Npc) {
                return Explore.getBossRoom().contains(Npc);
            }
        }) != null && Objects.getNearest(GameConstants.FINISHEDLADDERS) == null;
    }

    public String getStatus() {
        return "Killing uncoded boss: " + Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc Npc) {
                return Explore.getBossRoom().contains(Npc);
            }
        }).getName();
    }

    public String getAuthor() {
        return "UberMouse";
    }

    public String getName() {
        return "Default Boss";
    }

    public int loop() {
        if (MyPlayer.hp() < 60)
            MyPlayer.eat();
        Npc boss = Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc Npc) {
                return Explore.getBossRoom().contains(Npc);
            }
        });
        if (boss != null && !MyPlayer.isInteracting()) {
            MyMovement.turnTo(boss);
            boss.interact("Attack");
        }
        return Random.nextInt(500, 800);
    }

    public void start() {
    }
}
