package nz.uberutils.methods;

import com.rsbuddy.script.methods.Equipment;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.wrappers.Item;


public class MyEquipment
{
    public static final int ITEM_SLOTS = Equipment.NUM_SLOTS;
    public static final int INTERFACE_Equipment = Equipment.WIDGET;
    public static final int HELMET = Equipment.HELMET;
    public static final int CAPE = Equipment.CAPE;
    public static final int NECK = Equipment.NECK;
    public static final int WEAPON = Equipment.WEAPON;
    public static final int BODY = Equipment.BODY;
    public static final int SHIELD = Equipment.SHIELD;
    public static final int LEGS = Equipment.LEGS;
    public static final int HANDS = Equipment.HANDS;
    public static final int FEET = Equipment.FEET;
    public static final int RING = Equipment.RING;
    public static final int AMMO = Equipment.AMMO;

    public static Item[] getItems() {
        Item[] items = new Item[getCount()];
        int idx = 0;
        for (Item item : Equipment.getItems()) {
            if (item.getId() > 0) {
                items[idx] = item;
                idx++;
            }
        }
        return items;
    }

    private static int getCount() {
        int count = 0;
        for (Item item : Equipment.getItems()) {
            if (item.getId() > 0)
                count++;
        }
        return count;
    }

    public static Item[] getCachedItems() {
        return Equipment.getCachedItems();
    }

    /**
     * Gets the item from Equipment cache.
     *
     * @param EquipmentIndex the Equipment index
     * @return the item
     */
    public static Item getItem(int EquipmentIndex) {
        return new Item(Widgets.getComponent(INTERFACE_Equipment, EquipmentIndex));
    }

    /**
     * Check if cached equipment contains item
     *
     * @param name name of item to check for
     * @return true if equipment contains item
     */
    public static boolean contains(String name) {
        return contains(name, false);
    }

    /**
     * Check if equipment contains item
     *
     * @param name   name of item to check for
     * @param cached check cached equipment or not
     * @return true if equipment contains item
     */
    public static boolean contains(String name, boolean cached) {
        for (Item item : (cached) ? getCachedItems() : Equipment.getItems()) {
            if (item.getName().toLowerCase().contains(name.toLowerCase()))
                return true;
        }
        return false;
    }
}
