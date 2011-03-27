package nz.uberdungeon.bosses;


import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.util.Random;
import nz.uberdungeon.common.Plugin;
import nz.uberdungeon.dungeon.EnemyDef;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.misc.GameConstants;
import nz.uberdungeon.utils.MyCombat;

public class BloodChiller extends Plugin
{


    public boolean isValid() {
        return Npcs.getNearest(GameConstants.BLOOD_CHILLERS) != null;
    }


    public int loop() {
        MyCombat.doPrayerFor(new EnemyDef(Npcs.getNearest(GameConstants.BLOOD_CHILLERS)));
        if (MyPlayer.hp() < 60)
            MyPlayer.eat();
        return Random.nextInt(500, 800);
    }

    public String getStatus() {
        return "Killing " + getName();
    }

    public String getAuthor() {
        return "UberMouse";
    }

    public String getName() {
        return "To'Kash the blood chiller";
    }

}
