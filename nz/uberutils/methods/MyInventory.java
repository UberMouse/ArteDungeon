package nz.uberutils.methods;

import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.wrappers.Item;


public class MyInventory
{
    public static Item[] getItems(boolean cached) {
        Item[] items = new Item[getCount()];
        int idx = 0;
        for (Item item : (cached) ? Inventory.getCachedItems() : Inventory.getItems()) {
            if (item != null && item.getId() > 0) {
                items[idx] = item;
                idx++;
            }
        }
        return items;
    }

    public static Item[] getItems() {
        return getItems(false);
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

    /**
     * Check if inventory contains item
     * @param name name of item to check for
     * @param cached use cached inventory
     * @return true if item in inventory
     */
    public static boolean contains(String name, boolean cached) {
        for (Item item : getItems(cached)) {
            if (item.getName().toLowerCase().contains(name.toLowerCase()))
                return true;
        }
        return false;
    }

    /**
     * Check if inventory contains item
     * @param name name of item to check for
     * @return true if item in inventory
     */
    public static boolean contains(String name) {
        return contains(name, false);
    }

    /**
     * Get item in inventory
     * @param name name of item to get
     * @return Item if found or null if no item was found
     */
    public static Item getItem(String name) {
        for (Item i : getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) return i;
        }
        return null;
    }
}
