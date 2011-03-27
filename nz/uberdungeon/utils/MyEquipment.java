package nz.uberdungeon.utils;

import com.rsbuddy.script.methods.Equipment;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.wrappers.Item;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.DungeonCommon;


public class MyEquipment extends DungeonCommon
{

    public MyEquipment(DungeonMain parent) {
        super(parent);
    }

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
        return getItems().length;
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

}
