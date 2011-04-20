package nz.artedungeon.bosses;


import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.util.Random;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.EnemyDef;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.MyCombat;

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
