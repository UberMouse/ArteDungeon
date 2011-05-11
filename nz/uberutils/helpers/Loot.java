package nz.uberutils.helpers;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GroundItem;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Tile;
import nz.uberutils.helpers.tasks.PriceThread;
import nz.uberutils.methods.MyInventory;
import nz.uberutils.methods.MyMovement;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/27/11
 * Time: 3:28 PM
 * Package: nz.artezombies.utils;
 */
public class Loot
{
    private static final Map<Integer, Integer> lootCounts = new HashMap<Integer, Integer>();
    private static final Map<Integer, String> lootNames = new HashMap<Integer, String>();

    public static int totalPrice = 0;

    private static Filter<GroundItem> filter = new Filter<GroundItem>()
    {
        public boolean accept(GroundItem groundItem) {
            return true;
        }
    };

    public static void takeLoot(Filter<GroundItem> filter) {
        GroundItem loot = GroundItems.getNearest(filter);
        boolean stackable = false;
        for (GroundItem g : GroundItems.getLoaded(filter))
            if (g.getItem().getDefinition().getStackType() == 1)
                stackable = true;
        int count = 0;
        try {
            for (Item i : MyInventory.getItems())
                if (MyPlayer.isEdible(i))
                    count++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Inventory.isFull() && !stackable && count <= 5)
            return;
        if (count > 5)
            MyPlayer.eat();
        String name = loot.getItem().getName();
        int id = loot.getItem().getId();
        MyMovement.turnTo(loot.getLocation());
        if (loot.interact("Take " + name)) {
            if (lootCounts.containsKey(id)) {
                lootCounts.put(id, lootCounts.get(id) + 1);
            }
            else {
                lootCounts.put(id, 0);
                lootNames.put(id, name);
            }
            int add = 0;
            if (PriceThread.priceForId(id) > 0)
                add = PriceThread.priceForId(id) * loot.getItem().getStackSize();
            totalPrice += add;
            int iCount = Inventory.getCount();
            for (int i = 0; i < 15 && iCount == Inventory.getCount(); i++)
                Utils.sleep(100);
        }
    }

    public static void takeLoot(final int... ids) {
        takeLoot(new Filter<GroundItem>()
        {
            public boolean accept(GroundItem t) {
                if (Utils.arrayContains(ids, t.getItem().getId()))
                    return true;
                else
                    return false;
            }

        });
    }

    public static void takeLootTile(Tile tile, Filter<GroundItem> filter) {

    }

    public static void takeLootTile(Tile tile, final int... ids) {
        takeLootTile(tile, new Filter<GroundItem>()
        {

            public boolean accept(GroundItem t) {
                if (Utils.arrayContains(ids, t.getItem().getId()))
                    return true;
                else
                    return false;
            }

        });
    }

    public static void repaint(Graphics2D g) {
        GroundItem[] items = GroundItems.getLoaded(filter);
        int[] ids = new int[items.length];
        int offset = 0;
        ArrayList<Tile> tiles = new ArrayList<Tile>();
        for (int i = 0; i < items.length; i++) {
            GroundItem item = items[i];
            if (!tiles.contains(item.getLocation()))
                tiles.add(item.getLocation());
            ids[i] = item.getItem().getId();
        }
        for (Tile tile : tiles) {
            PaintUtils.drawTile(g, tile, new Color(0, 51, 204, 50), Color.WHITE);
            for (GroundItem item : GroundItems.getAllAt(tile)) {
                if (Utils.arrayContains(ids, item.getItem().getId())) {
                    lootInfo(g, item, offset);
                    offset += 15;
                }
            }
            offset = 0;
        }
    }

    public static void lootInfo(Graphics2D g, GroundItem loot, int offset) {
        if (!Calculations.isTileOnScreen(loot.getLocation()))
            return;
        String lootString = loot.getItem().getName() + " (" + loot.getItem().getStackSize() + " * ";
        lootString += (PriceThread.priceForId(loot.getItem().getId()) == -1) ?
                      "Calculating.." :
                      (PriceThread.priceForId(loot.getItem().getId()) == -2) ?
                      "Unknown" :
                      PriceThread.priceForId(loot.getItem().getId());
        lootString += ")";
        final Color color1 = new Color(0, 51, 204, 50);
        final Color color2 = new Color(0, 0, 0);
        final Color color3 = new Color(255, 255, 255);

        final BasicStroke stroke1 = new BasicStroke(1);

        final Font font1 = new Font("Arial", 0, 9);
        final FontMetrics fm = g.getFontMetrics(font1);
        final int stringWidth = fm.stringWidth(lootString) + 6;
        final Point point = Calculations.tileToScreen(loot.getLocation(), 0.5, 0.5, 0);

        g.setColor(color1);
        g.fillRect(point.x, point.y + offset, stringWidth, 15);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRect(point.x, point.y + offset, stringWidth, 15);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString(lootString, point.x + 3, point.y + 10 + offset);
    }

    public static boolean isPaintValid() {
        return GroundItems.getLoaded(filter).length > 0;
    }

    private static String idToName(int id) {
        return lootNames.containsKey(id) ? lootNames.get(id) : null;
    }

    public static void addLoot(int id, String name, int price) {
        lootNames.put(id, name);
        lootCounts.put(id, 0);
        PriceThread.addPrice(id, price);
    }

    public static void setFilter(Filter<GroundItem> filter) {
        Loot.filter = filter;
    }

    public static boolean shouldLoot() {
        boolean stackable = false;
        for (GroundItem g : GroundItems.getLoaded(filter))
            if (g.getItem().getDefinition().getStackType() == 1 && MyInventory.contains(g.getItem().getName()))
                stackable = true;
        int count = 0;
        try {
            for (Item i : MyInventory.getItems())
                if (MyPlayer.isEdible(i))
                    count++;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GroundItems.getNearest(filter) != null && (!Inventory.isFull() || stackable || count > 5);
    }
}