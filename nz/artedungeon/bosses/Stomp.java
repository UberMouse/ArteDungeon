package nz.artedungeon.bosses;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.GroundItem;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.Enemy;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/19/11
 * Time: 4:26 PM
 * Package: nz.artedungeon.bosses;
 */
public class Stomp extends Plugin
{
    private int chargedLodeStones = 0;

    private static enum State
    {
        ATTACK("Attacking"),
        DODGE_ROCKS("Dodging falling debris"),
        CHARGING_LODESTONES("Charging Lodestones"),
        PICKING_UP_CRYSTALS("Picking up crystals"),
        EAT("Eating");

        private State(String name) {
            get = name;
        }

        private final String get;

        public String toString() {
            return get;
        }
    }

    private State getState() {
        if (Objects.getTopAt(MyPlayer.location()) != null &&
            Util.arrayContains(GameConstants.STOMP_DEBRIS_SHADOWS, Objects.getTopAt(MyPlayer.location()).getId()))
            return State.DODGE_ROCKS;
        if (MyPlayer.needToEat() && MyPlayer.hasFood())
            return State.EAT;
        for (int i = 0; i < GameConstants.STOMP_LODESTONES.length; i++) {
            final int x = i;
            if (Objects.getNearest(GameConstants.STOMP_LODESTONES[i]) != null) {
                int count = Objects.getLoaded(new Filter<GameObject>()
                {
                    public boolean accept(GameObject gameObject) {
                        return Util.arrayContains(GameConstants.STOMP_LODESTONES[x], gameObject.getId());
                    }
                }).length;
                switch (i) {
                    case 0:
                        if (Inventory.getCount(GameConstants.STOMP_CRYSTAL_RED) >= count)
                            return State.CHARGING_LODESTONES;
                        break;
                    case 1:
                        if (Inventory.getCount(GameConstants.STOMP_CRYSTAL_GREEN) >= count)
                            return State.CHARGING_LODESTONES;
                        break;
                    case 2:
                        if (Inventory.getCount(GameConstants.STOMP_CRYSTAL_BLUE) >= count)
                            return State.CHARGING_LODESTONES;
                        break;
                }
            }
        }
        if (GroundItems.getNearest(GameConstants.STOMP_CRYSTALS) != null)
            return State.PICKING_UP_CRYSTALS;
        return State.ATTACK;
    }

    @Override
    public String getStatus() {
        return "Killing " + getName() + ": " + getState().toString();
    }

    @Override
    public boolean isValid() {
        return MyPlayer.currentRoom().getNearestNpc("stomp") != null;
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Stomp";
    }

    @Override
    public int loop() {
        Npc stomp = MyPlayer.currentRoom().getNearestNpc("stomp");
        switch (getState()) {
            case EAT:
                    MyPlayer.eat();
                break;
            case PICKING_UP_CRYSTALS:
                GroundItem crystal = GroundItems.getNearest(GameConstants.STOMP_CRYSTALS);
                if (crystal != null && !MyPlayer.isMoving()) {
                    MyMovement.turnTo(crystal.getLocation());
                    crystal.interact("take");
                }
                break;
            case CHARGING_LODESTONES:
                GameObject lodestone = null;
                for (int[] lodestones : GameConstants.STOMP_LODESTONES) {
                    lodestone = Objects.getNearest(lodestones);
                    if (lodestone != null)
                        break;
                }
                if (lodestone != null && !MyPlayer.isMoving()) {
                    MyMovement.turnTo(lodestone);
                    lodestone.click();
                }
                break;
            case DODGE_ROCKS:
                Tile[] surrounding = Util.getSurroundingTiles(MyPlayer.location(), true);
                for (Tile tile : surrounding)
                    if (Objects.getTopAt(tile) == null ||
                        !Util.arrayContains(GameConstants.STOMP_DEBRIS_SHADOWS,
                                            Objects.getTopAt(MyPlayer.location()).getId())) {
                        tile.interact("walk");
                        break;
                    }
                break;
            case ATTACK:
                if (!MyPlayer.isInteracting()) {
                    Enemy.setEnemy(stomp);
                    MyMovement.turnTo(Enemy.getNPC());
                    Enemy.interact("attack");
                }
                break;
        }
        return Random.nextInt(300, 500);
    }

    public void messageReceived(MessageEvent event) {
        if (event.getMessage().contains("the crystal into"))
            chargedLodeStones++;
        if (event.getMessage().contains("enters a defensive"))
            chargedLodeStones = 0;
    }
}
