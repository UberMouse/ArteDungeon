package nz.uberdungeon.utils;

import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.wrappers.Item;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.DungeonCommon;


public class MyInventory extends DungeonCommon
{

    public MyInventory(DungeonMain parent) {
        super(parent);
    }

    public static Item[] getItems() {
        Item[] items = new Item[getCount()];
        int idx = 0;
        for (Item item : Inventory.getItems()) {
            if (item != null && item.getId() > 0) {
                items[idx] = item;
                idx++;
            }
        }
        return items;
    }

    private static int getCount() {
        int count = 0;
        for (Item item : Inventory.getItems()) {
            if (item.getId() > 0)
                count++;
        }
        return count;
    }

    public static Item getItem(int id) {
        return Inventory.getItem(id);
    }

}
