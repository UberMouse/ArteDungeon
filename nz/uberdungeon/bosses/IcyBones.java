package nz.uberdungeon.bosses;


import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Npc;
import nz.uberdungeon.common.Plugin;
import nz.uberdungeon.dungeon.EnemyDef;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.misc.GameConstants;
import nz.uberdungeon.utils.MyCombat;
import nz.uberdungeon.utils.MyMovement;

public class IcyBones extends Plugin
{
    public boolean isValid() {
        return Npcs.getNearest(GameConstants.ICY_BONES) != null;
    }


    public int loop() {
        if (MyPlayer.hp() < 60)
            MyPlayer.eat();
        Npc boss = Npcs.getNearest(GameConstants.ICY_BONES);
        MyCombat.doPrayerFor(new EnemyDef(boss));
        if (boss != null && !MyPlayer.isInteracting()) {
            MyMovement.turnTo(boss);
            boss.interact("Attack");
        }
        return Random.nextInt(500, 800);
    }

    public String getStatus() {
        return "Killing " + getName();
    }

    public String getAuthor() {
        return "UberMouse";
    }

    public String getName() {
        return "Icy Bones";
    }

}
