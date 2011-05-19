package nz.artedungeon.bosses;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.EnemyDef;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.MyCombat;


public class GluttonousBehemoth extends Plugin
{


    public int loop() {
        try {
            MyCombat.doPrayerFor(new EnemyDef(Npcs.getNearest(GameConstants.GLUTTONOUS)));
            GameObject carcass = Objects.getNearest(49283);
            if (carcass == null)
                return Random.nextInt(400, 500);
            Tile carcassTile = carcass.getLocation();
            Tile north = new Tile(carcassTile.getX(), carcassTile.getY() + 5);
            Tile south = new Tile(carcassTile.getX(), carcassTile.getY() - 5);
            Tile west = new Tile(carcassTile.getX() - 5, carcassTile.getY());
            Tile east = new Tile(carcassTile.getX() + 5, carcassTile.getY());
            Tile[] array = {north, south, west, east};
            int count = 0;
            for (Tile tile : array) {
                if (Npcs.getNearest(GameConstants.GLUTTONOUS).getLocation().equals(tile)) {
                    Tile walkTile = null;
                    switch (count) {
                        case 0:
                            walkTile = new Tile(carcassTile.getX(), carcassTile.getY() + 2);
                            break;
                        case 1:
                            walkTile = new Tile(carcassTile.getX(), carcassTile.getY() - 2);
                            break;
                        case 2:
                            walkTile = new Tile(carcassTile.getX() - 2, carcassTile.getY());
                            break;
                        case 3:
                            walkTile = new Tile(carcassTile.getX() + 2, carcassTile.getY());
                            break;
                    }
                    if (!walkTile.equals(MyPlayer.location())) {
                        if (Calculations.isTileOnScreen(walkTile))
                            walkTile.interact("Walk");
                        else
                            walkTile.clickOnMap();
                    }
                }
                count++;
            }
            if (MyPlayer.hp() < 60)
                MyPlayer.eat();
            if (!MyPlayer.inCombat())
                Npcs.getNearest(GameConstants.GLUTTONOUS).getLocation().interact("attack");

        } catch (Exception ignored) {
        }
        return Random.nextInt(500, 700);
    }

    public String getStatus() {
        return "Killing the " + getName();
    }

    public String getAuthor() {
        return "UberMouse";
    }

    public String getName() {
        return "Gluttonous Behemoth";
    }


    public boolean isValid() {
        return Objects.getNearest(49283) != null && Npcs.getNearest(GameConstants.INROOM_ENEMY_FILTER) != null;
    }

}
