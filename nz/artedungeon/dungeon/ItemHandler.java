package nz.artedungeon.dungeon;

import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.wrappers.Item;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.RSBuddyCommon;
import nz.uberutils.methods.MyEquipment;
import nz.uberutils.methods.MyInventory;

import java.util.Arrays;


public class ItemHandler extends RSBuddyCommon
{

    private static enum MELEE_TIERS
    {
        NOVITE,
        BATHUS,
        MARMAROS,
        KRATONITE,
        FRACTITE,
        ZEPHYRIUM,
        ARGONITE,
        KATAGON,
        GORGONITE,
        PROMETHIUM,
        PRIMAL,
        NOVALUE;

        public static MELEE_TIERS toTier(String str) {
            try {
                return valueOf(str);
            } catch (Exception ex) {
                return NOVALUE;
            }
        }
    }

    private static enum RANGED_TIERS_ARMOUR
    {
        PROTOLEATHER,
        SUBLEATHER,
        PARALEATHER,
        ARCHLEATHER,
        DROMOLEATHER,
        SPINOLEATHER,
        GALILEATHER,
        STEGOLEATHER,
        MEGALEATHER,
        TYRANOLEATHER,
        SAGITTARIAN,
        NOVALUE;

        public static RANGED_TIERS_ARMOUR toTier(String str) {
            try {
                return valueOf(str);
            } catch (Exception ex) {
                return NOVALUE;
            }
        }
    }

    private static enum RANGED_TIERS_WEAPON
    {
        TANGLE_GUM,
        SEEPING_ELM,
        BLOOD_SPINDLE,
        UTUKU,
        SPINEBEAM,
        BOVISTRANGLR,
        THIGAT,
        CORPSETHORN,
        ENTGALLOW,
        GRAVE_CREEPER,
        SAGITTARIAN,
        NOVALUE;

        public static RANGED_TIERS_WEAPON toTier(String str) {
            try {
                return valueOf(str);
            } catch (Exception ex) {
                return NOVALUE;
            }
        }
    }

    private static enum MAGE_TIERS
    {
        SALVE,
        WILDERCRESS,
        BLIGHTLEAF,
        ROSEBLOOD,
        BRYLL,
        DUSKWEED,
        SOULBELL,
        ECTO,
        RUNIC,
        SPIRITBLOOM,
        CELESTIAL,
        NOVALUE;

        public static MAGE_TIERS toTier(String str) {
            try {
                return valueOf(str);
            } catch (Exception ex) {
                return NOVALUE;
            }
        }
    }

    /**
     * Instantiates a new item handler.
     *
     * @param parent instance of main script
     */
    public ItemHandler(DungeonMain parent) {
        super(parent);
    }

    /**
     * Checks if item is bound.
     *
     * @param EquipmentIndex the Equipment index to check
     * @return true, if is bound
     */
    public static boolean isBound(int EquipmentIndex) {
        Item equipItem = MyEquipment.getItem(EquipmentIndex);
        if (equipItem.getId() != -1)
            return equipItem.getName().contains("(b)");
        return false;
    }

    /**
     * Un bind item.
     *
     * @param EquipmentIndex the Equipment index to unbind
     */
    public static void unBind(int EquipmentIndex) {
        if (Game.getCurrentTab() != Game.TAB_EQUIPMENT)
            Game.openTab(Game.TAB_EQUIPMENT);
        int equippedItemID = Equipment.getItem(EquipmentIndex).getId();
        int timeout = 0;
        MyEquipment.getItem(EquipmentIndex).interact("Remove");
        while (!Inventory.contains(equippedItemID)) {
            MyEquipment.getItem(EquipmentIndex).interact("Remove");
            sleep(random(500, 700));
            Game.openTab(Game.TAB_INVENTORY);
            sleep(random(500, 700));
        }
        while (!Inventory.getItem(equippedItemID).interact("Destroy"))
            sleep(random(500, 700));
        while (!Widgets.getComponent(94, 3).isValid())
            sleep(100);
        if (Widgets.getComponent(94, 3).click()) {
            while (Inventory.contains(equippedItemID) && ++timeout <= 15)
                sleep(100);
        }
        Game.openTab(Game.TAB_EQUIPMENT);
        sleep(random(500, 700));
        Game.openTab((Game.TAB_INVENTORY));
    }

    /**
     * Bind item.
     *
     * @param itemID the item id of the item
     */
    public static void bind(int itemID) {
        Item item = MyInventory.getItem(itemID);
        if (item != null) {
            while (!Menu.click("Bind") && !MyInventory.getItem(itemID).getName().contains("(b)")) {
                Mouse.move((int) item.getComponent().getBoundingRect().getCenterX(),
                           (int) item.getComponent().getBoundingRect().getCenterY());
                Mouse.moveRandomly(1, 4);
                Menu.click("Bind");
                sleep(100);
            }
        }
    }

    /**
     * Gets the items name with (b) removed and parsed into format used by Equipable.
     *
     * @param item the items name
     * @return the name
     */
    public static String getItemName(String item) {
        if (item == null || item.length() < 1)
            return "";
        item = item.replace(" (b)", "");
        String[] itemSplit = item.split(" ");
        itemSplit = Arrays.copyOfRange(itemSplit,
                                       (item.toLowerCase().matches(".*(shortbow|longbow).*") &&
                                        item.split(" ").length == 3) ? 2 : 1,
                                       itemSplit.length);
        item = "";
        for (String str : itemSplit)
            item += "_" + str.trim().toUpperCase().replaceAll("[0-9]", "");
        if (item.length() < 1)
            return "";
        if (item.contains(">"))
            item = item.split(">")[1];
        item = item.substring(1);
        return item;
    }

    /**
     * Should this item be equiped
     *
     * @param toEquip        the Equipable to be checked
     * @param EquipmentIndex the equipment spot it goes in
     * @return true if should be equiped
     */
    public static boolean shouldEquip(Equipable toEquip, int EquipmentIndex) {
        Equipable toCheck = null;
        if (toEquip.getLocation() == Equipable.Location.NOVALUE || toEquip.getTier() == -1)
            return false;
        if (toEquip.getName().contains("(b)"))
            return true;
        else if (toEquip.getEquipmentIndex() == MyEquipment.AMMO)
            return false;
        if (EquipmentIndex == -1) {
            return false;
        }
        if (MyEquipment.getItem(EquipmentIndex).getId() != -1) {
            toCheck = new Equipable(MyEquipment.getItem(EquipmentIndex).getName());
            if (toEquip.getItem() != toCheck.getItem() || toEquip.getTier() <= toCheck.getTier()) {
                return false;
            }
        }
        if (toEquip.getStyle() == MyPlayer.combatStyle() &&
            toEquip.canWield() &&
            toEquip.getEquipmentIndex() == EquipmentIndex) {
            if (MyEquipment.getItem(MyEquipment.WEAPON).getId() != -1) {
                if ((new Equipable(MyEquipment.getItem(MyEquipment.WEAPON).getName()).twoHanded() &&
                     toEquip.getEquipmentIndex() == MyEquipment.SHIELD))
                    return false;
                else if (MyEquipment.getItem(MyEquipment.SHIELD).getId() != -1 && toEquip.twoHanded())
                    return false;
            }
            return true;

        }
        return false;
    }

    /**
     * Returns tier parsed from item name
     *
     * @param name full name of item
     * @return tier parsed
     */
    public static int getTier(String name) {
        String[] split = Arrays.copyOfRange(name.split(" "),
                                            0,
                                            (name.toLowerCase().matches(".*(shortbow|longbow).*") &&
                                             name.split(" ").length == 3) ? 2 : 1);
        String tier = "";
        for (String string : split)
            tier += string + "_";
        tier = tier.substring(0, tier.length() - 1);
        if (tier.contains(">"))
            tier = tier.split(">")[1];
        tier = tier.toUpperCase();
        if (MELEE_TIERS.toTier(tier) != MELEE_TIERS.NOVALUE)
            return MELEE_TIERS.valueOf(tier).ordinal() + 1;
        if (RANGED_TIERS_ARMOUR.toTier(tier) != RANGED_TIERS_ARMOUR.NOVALUE)
            return RANGED_TIERS_ARMOUR.valueOf(tier).ordinal() + 1;
        if (RANGED_TIERS_WEAPON.toTier(tier) != RANGED_TIERS_WEAPON.NOVALUE)
            return RANGED_TIERS_WEAPON.valueOf(tier).ordinal() + 1;
        if (MAGE_TIERS.toTier(tier) != MAGE_TIERS.NOVALUE)
            return MAGE_TIERS.valueOf(tier).ordinal() + 1;
        return -1;
    }

    public static enum Style
    {
        MELEE, RANGED, MAGIC, UNKNOWN
    }

    public static Style MELEE = ItemHandler.Style.MELEE;
    public static Style RANGED = ItemHandler.Style.RANGED;
    public static Style MAGIC = ItemHandler.Style.MAGIC;
    public static Style UNKNOWN = ItemHandler.Style.UNKNOWN;
}