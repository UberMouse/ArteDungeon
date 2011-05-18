package nz.artedungeon.bosses;

import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.Enemy;
import nz.artedungeon.dungeon.ItemHandler;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.MyCombat;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyCamera;
import nz.uberutils.methods.MyMovement;
import nz.uberutils.methods.MyNpcs;
import nz.uberutils.methods.MyPrayer;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/24/11
 * Time: 4:03 PM
 * Package: nz.artedungeon.bosses;
 */
public class SkeletalHorde extends Plugin
{
    private static enum State
    {
        BLOCK_TUNNEL("Blocking Tunnel"), HEAL("Healing"), AUTO_RETAILIATE("Turning off Auto Retailiate"), ATTACK(
            "Attacking Skeletons"), TALK("Talking to Skinweaver"), PICKUP_ARROWS("Picking up arrows"), EQUIP_ARROWS(
            "Equiping arrows"), WAIT("Waiting");

        private State(String name) {
            get = name;
        }

        private final String get;

        public String toString() {
            return get;
        }
    }

    private static boolean blockTunnel = false;
    private static boolean talkedToSkinWeaver = false;

    @Override
    public boolean isValid() {
        return MyNpcs.getNearest(".*skinweaver*") != null && !talkedToSkinWeaver;
    }

    @Override
    public String getStatus() {
        return "Killing Skeletal Horde: " + getState().toString();
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Skeletal Horde";
    }

    @Override
    public int loop() {
        if (MyPlayer.currentRoom().getNearestNpc(".*skinweaver.*") != null &&
            MyPlayer.currentRoom().getNearestNpc(".*skinweaver.*").getMessage() != null)
            blockTunnel = true;
        if (MyCombat.canUsePrayer((MyPrayer.isUsingRegularPrayers()) ?
                                  MyPrayer.Prayer.PROTECT_FROM_MISSILES :
                                  MyPrayer.Prayer.DEFLECT_MISSILE)) {
            MyPrayer.Prayer prayer = (MyPrayer.isUsingRegularPrayers()) ?
                                     MyPrayer.Prayer.PROTECT_FROM_MISSILES :
                                     MyPrayer.Prayer.DEFLECT_MISSILE;
            if (!prayer.isSelected())
                prayer.setActivated(true);
        }
        switch (getState()) {
            case AUTO_RETAILIATE:
                Combat.setAutoRetaliate(false);
                return Random.nextInt(500, 700);
            case ATTACK:
                attack();
                break;
            case HEAL:
                heal();
                break;
            case BLOCK_TUNNEL:
                blockTunnel();
                break;
            case TALK:
                talk();
                break;
        }
        return Random.nextInt(300, 500);
    }

    private void talk() {
        Npc skinweaver = MyPlayer.currentRoom().getNearestNpc(".*skinweaver.*");
        if (MyPlayer.isMoving() && Calculations.distanceTo(skinweaver) > 3)
            return;
        MyMovement.turnTo(skinweaver);
        if (skinweaver.isOnScreen() && skinweaver.interact("Talk")) {
            int timeout = 0;
            while (skinweaver.getMessage() == null && ++timeout <= 20)
                sleep(100);
            if (skinweaver.getMessage() != null && skinweaver.getMessage().contains("little danger in"))
                talkedToSkinWeaver = true;
            else if(Widgets.getComponent(242, 0) != null)
                talkedToSkinWeaver = true;
        }
    }

    private void blockTunnel() {
        GameObject tunnel = Objects.getNearest(GameConstants.SKELETAL_TUNNELS);
        if (MyPlayer.isMoving() && Calculations.distanceTo(tunnel) > 3)
            return;
        MyMovement.turnTo(tunnel);
        if (tunnel != null && tunnel.interact("block"))
            blockTunnel = false;
        else if (tunnel == null)
            blockTunnel = false;
    }

    private void heal() {
        Npc skinweaver = MyPlayer.currentRoom().getNearestNpc(".*skinweaver.*");
        if (MyPlayer.isMoving() && Calculations.distanceTo(skinweaver) > 3)
            return;
        Tile healTile = Util.getSurroundingTiles(skinweaver.getLocation())[3];
        if (!MyPlayer.location().equals(healTile)) {
            if (!Calculations.isTileOnScreen(healTile))
                healTile.clickOnMap();
            else
                healTile.interact("walk");
            if (MyPlayer.hp() < 30)
                MyPlayer.eat();
        }
        else {
            while (MyPlayer.hp() < 80 && MyPlayer.location().equals(healTile)) {
                if (MyPlayer.combatStyle() != ItemHandler.Style.MELEE)
                    Camera.setPitch(false);
                if (MyPlayer.interacting() == null && !MyPlayer.inCombat()) {
                    Enemy.setNPC(MyPlayer.currentRoom().getHighestPriorityEnemy(new Filter<Npc>()
                    {
                        public boolean accept(Npc npc) {
                            return MyPlayer.currentRoom().contains(npc) &&
                                   npc.getHpPercent() > 0 &&
                                   npc.getName().contains("elet");
                        }
                    }));  //TODO improve so it picks lowest hp enemy
                    MyCamera.turnTo(Enemy.getNPC());
                    Enemy.interact("attack");
                }
                sleep(Random.nextInt(400, 500));
            }
        }
    }

    private void attack() {
        Camera.setPitch(true);
        if (!MyPlayer.inCombat()) {
            Enemy.setNPC(MyPlayer.currentRoom().getHighestPriorityEnemy(new Filter<Npc>()
            {
                public boolean accept(Npc npc) {
                    return MyPlayer.currentRoom().contains(npc) &&
                           npc.getHpPercent() > 0 &&
                           npc.getName().contains("elet");
                }
            }));  //TODO improve so it picks lowest hp enemy
            MyMovement.turnTo(Enemy.getNPC());
            Enemy.interact("attack");
        }
    }

    private State getState() {
        if (Combat.isAutoRetaliateEnabled())
            return State.AUTO_RETAILIATE;
        else if (MyPlayer.needToEat())
            return State.HEAL;
        else if (blockTunnel)
            return State.BLOCK_TUNNEL;
        else if (MyPlayer.currentRoom().getNearestNpc(".*eleton.*") != null)
            return State.ATTACK;
        else
            return State.TALK;
    }

    public void reset() {
        talkedToSkinWeaver = false;
    }
}
