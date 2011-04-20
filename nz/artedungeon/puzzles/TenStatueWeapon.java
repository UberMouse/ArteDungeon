package nz.artedungeon.puzzles;

import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.Component;
import com.rsbuddy.script.wrappers.*;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.Enemy;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.utils.util;
import nz.uberutils.methods.MyEquipment;
import nz.uberutils.methods.MyInventory;
import nz.uberutils.methods.MyMovement;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/23/11
 * Time: 8:49 PM
 * Package: nz.artedungeon.puzzles;
 */
public class TenStatueWeapon extends Plugin implements PaintListener
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
            if (inventoryContains("Chisel") || GroundItems.getNearest(CHISEL) != null) {
                return (inventoryContains("Pickaxe") || equipmentContains("Pickaxe"))
                       && inventoryContains("Hammer");
            }
        }
        return false;
    }

    public String getStatus() {
        return "Solving: Ten Statue Weapon";
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Ten Statue Weapon";
    }

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
                    GroundItem chisel = GroundItems.getNearest(CHISEL);
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
                            GameObject wall = Objects.getNearest(CRUMBLING_WALL);
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

    public void onRepaint(Graphics g) {
        try {
            final Npc n = Npcs.getNearest(STATUE_NONE);
            highlight(g, n.getLocation(), new Color(0, 255, 0, 100));
            highlight(g, Npcs.getNearest(new Filter<Npc>()
            {
                public boolean accept(Npc npc) {
                    if (n.getLocation().getX() == npc.getLocation().getX()
                        || n.getLocation().getY() == npc.getLocation().getY())
                        if (npc.getId() == STATUE_BOW || npc.getId() == STATUE_SWORD || npc.getId() == STATUE_STAFF)
                            return true;
                    return false;
                }
            }).getLocation(), new Color(255, 0, 0, 100));
        } catch (Exception ignored) {
        }
    }

    private void highlight(Graphics g, Tile t, Color fill) {
        Point pn = Calculations.tileToScreen(t, 0, 0, 0);
        Point px = Calculations.tileToScreen(t, 1, 0, 0);
        Point py = Calculations.tileToScreen(t, 0, 1, 0);
        Point pxy = Calculations.tileToScreen(t, 1, 1, 0);
        if (py.x != -1 && pxy.x != -1 && px.x != -1 && pn.x != -1) {
            g.setColor(fill);
            g.fillPolygon(new int[]{py.x, pxy.x, px.x, pn.x},
                          new int[]{py.y, pxy.y, px.y, pn.y}, 4);
        }
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
        final Tile t = n.getLocation();
        Npc nearest = Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                if (n.getLocation().getX() == npc.getLocation().getX()
                    || n.getLocation().getY() == npc.getLocation().getY())
                    if (npc.getId() == STATUE_BOW || npc.getId() == STATUE_SWORD || npc.getId() == STATUE_STAFF)
                        return true;
                return false;
            }
        });
        if (nearest.getId() == STATUE_BOW) return "Sword";
        if (nearest.getId() == STATUE_SWORD) return "Staff";
        if (nearest.getId() == STATUE_STAFF) return "Bow";
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
