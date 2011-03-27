package nz.uberdungeon.puzzles;

import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.*;
import nz.uberdungeon.common.Plugin;
import nz.uberdungeon.dungeon.Enemy;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.utils.MyEquipment;
import nz.uberdungeon.utils.MyInventory;
import nz.uberdungeon.utils.MyMovement;
import nz.uberdungeon.utils.util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/19/11
 * Time: 11:26 AM
 * Package: nz.uberdungeon.puzzles;
 */
public class ThreeStatueWeapon extends Plugin
{
    int CRUMBLING_WALL[] = {49647}, PICKAXE[] = {16295, 16297, 16299},
            CHISEL = 17444, HAMMER = 17883, STONE_BLOCK = 17415,
            STATUE_BOW = 11030, STATUE_STAFF = 11033, STATUE_SWORD = 11027, STATUE_NONE = 11012,
            STONE_SWORD = 17416, STONE_BOW = 17418, STONE_STAFF = 17420;
    Filter<Npc> enemyFilter = new Filter<Npc>()
    {
        public boolean accept(Npc npc) {
            return !npc.getName().equals("Statue") && util.tileInRoom(npc.getLocation());
        }
    };

    public boolean isValid() {
        if (Npcs.getNearest(STATUE_NONE) != null
            && util.tileInRoom(Npcs.getNearest(STATUE_NONE).getLocation())) {
            if (inventoryContains("Chisel") || nearestGroundItem("Chisel") != null) {
                return (inventoryContains("Pickaxe") || equipmentContains("Pickaxe"))
                       && inventoryContains("Hammer");
            }
        }
        return false;
    }

    public String getStatus() {
        return "Solving: Three Statue Weapon";
    }

    public String getAuthor() {
        return "Zippy";
    }

    public String getName() {
        return "Three Statue Weapon";
    }

    @Override
    public int loop() {
        if (MyPlayer.get().getHpPercent() < 50) {
            // EAT FOOD
        }
        if (!MyPlayer.get().isInCombat()) {
            if (Npcs.getNearest(enemyFilter) != null) {
                Npc enemy = Npcs.getNearest(enemyFilter);
                MyMovement.turnTo(enemy);
                Enemy.setEnemy(enemy);
                Enemy.interact("Attack");
                return Random.nextInt(500, 1000);
            }
            if (MyPlayer.get().isIdle()) {
                if (!inventoryContains("Chisel")) {
                    GroundItem chisel = nearestGroundItem("Chisel");
                    if (chisel != null) {
                        if (chisel.isOnScreen()) {
                            chisel.interact("Take");
                        }
                        else {
                            MyMovement.turnTo(chisel.getLocation());
                        }
                    }
                }
                Npc next = Npcs.getNearest(STATUE_NONE);
                if (next != null) {
                    String nextWep = getNecessaryWeapon(next);
                    if (clickInterface(nextWep + ".")) return Random.nextInt(500, 1000);
                    if (!inventoryContains("stone " + nextWep)) {
                        if (!inventoryContains("Stone block")) {
                            GameObject wall = Objects.getNearest("crumbling wall");
                            if (wall != null) {
                                if (wall.isOnScreen()) {
                                    wall.interact("Mine");
                                }
                                else {
                                    MyMovement.turnTo(wall);
                                }
                            }
                        }
                        else {
                            getInventoryItem("Stone block").interact("Carve");
                        }
                    }
                    else {
                        MyMovement.turnTo(next);
                        next.interact("Arm");
                    }
                }
                return Random.nextInt(500, 1000);
            }
        }
        return 0;
    }

    public boolean clickInterface(String txt) {
        for (Widget w : Widgets.getLoaded()) {
            if (w.containsText(txt)) {
                for (Component c : w.getComponents()) {
                    if (c.getText().equalsIgnoreCase(txt)) {
                        c.click();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public String getNecessaryWeapon(final Npc n) {
        boolean sword = Npcs.getNearest(STATUE_SWORD) != null;
        boolean bow = Npcs.getNearest(STATUE_BOW) != null;
        boolean staff = Npcs.getNearest(STATUE_STAFF) != null;

        if (!sword) return "Sword";
        if (!staff) return "Staff";
        if (!bow) return "Bow";
        return "null";
    }

    public Item getInventoryItem(String name) {
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) return i;
        }
        return null;
    }

    public boolean inventoryContains(String name) {
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) return true;
        }
        return false;
    }

    public boolean equipmentContains(String name) {
        for (Item i : MyEquipment.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) return true;
        }
        return false;
    }

    public GroundItem nearestGroundItem(String name) {
        for (GroundItem i : GroundItems.getLoaded()) {
            if (i.getItem().getName().toLowerCase().contains(name.toLowerCase())) return i;
        }
        return null;
    }
}
