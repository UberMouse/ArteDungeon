package nz.uberutils.helpers;

import com.rsbuddy.script.methods.Combat;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Settings;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Timer;
import com.rsbuddy.script.wrappers.Item;
import nz.uberutils.methods.MyEquipment;
import nz.uberutils.methods.MyInventory;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/19/11
 * Time: 8:17 PM
 * Package: nz.uberutils.helpers;
 */
public class SpecialAttack
{
    private static String primaryWeapon;
    private static String specialWeapon;
    private static int specEnergy;
    private static int specAt = 101;
    private static boolean useSecondaryWeapon;
    private static final int SETTING_SPECIAL_ENERGY = 300;

    private static final int[] amountUsage = {10, 25, 33, 35, 45, 50, 55, 60, 80, 85, 100};
    private static final String[][] weapons = {{"Rune thrownaxe", "Rod of ivandis"},
                                               {"Dragon Dagger",
                                                "Dragon dagger (p)",
                                                "Dragon dagger (p+)",
                                                "Dragon dagger (p++)",
                                                "Dragon Mace",
                                                "Dragon Spear",
                                                "Dragon longsword",
                                                "Rune claws"},
                                               {"Dragon Halberd"},
                                               {"Magic Longbow"},
                                               {"Magic Composite Bow"},
                                               {"Dragon Claws",
                                                "Abyssal Whip",
                                                "Granite Maul",
                                                "Darklight",
                                                "Barrelchest Anchor",
                                                "Armadyl Godsword"},
                                               {"Magic Shortbow"},
                                               {"Dragon Scimitar",
                                                "Dragon 2H Sword",
                                                "Zamorak Godsword",
                                                "Korasi's sword"},
                                               {"Dorgeshuun Crossbow",
                                                "Bone Dagger",
                                                "Bone Dagger (p+)",
                                                "Bone Dagger (p++)"},
                                               {"Brine Sabre"},
                                               {"Bandos Godsword",
                                                "Dragon Battleaxe",
                                                "Dragon Hatchet",
                                                "Seercull Bow",
                                                "Excalibur",
                                                "Enhanced excalibur",
                                                "Ancient Mace",
                                                "Saradomin sword"}};


    /**
     * Set primary weapon name
     *
     * @param name name of primary weapon
     */
    public static void setPrimaryWeapon(String name) {
        primaryWeapon = name;
    }

    /**
     * Set special weapon name
     *
     * @param name name of special weapon
     */
    public static void setSpecialWeapon(String name) {
        specialWeapon = name;
    }

    /**
     * Set percentage of special energy special attack uses
     *
     * @param percent percent used
     */
    public static void setSpecEnergy(int percent) {
        specEnergy = percent;
    }

    /**
     * Set percentage of special energy to use special at
     *
     * @param percent percent to set to
     */
    public static void setSpecAt(int percent) {
        specAt = percent;
    }

    /**
     * Set whether to use secondary weapon for special or not
     *
     * @param use whether to use secondary weapon or not
     */
    public static void setUseSecondaryWeapon(boolean use) {
        useSecondaryWeapon = use;
    }

    /**
     * Should use special attack
     *
     * @return true if special attack should be used
     */
    public static boolean shouldSpec() {
        return getSpecialEnergy() >= specAt || (useSecondaryWeapon && MyInventory.contains(primaryWeapon));
    }

    private static boolean canSpec() {
        return getSpecialEnergy() >= specEnergy;
    }

    public static void setSpecValues(String weapon) {
        for (int i = 0; i < weapons.length; i++) {
            for (int j = 0; j < weapons[i].length; j++) {
                if (weapons[i][j].equalsIgnoreCase(weapon)) {
                    specAt = amountUsage[i];
                    specEnergy = amountUsage[i];
                }
            }
        }
    }

    public static void doSpecial() {
        if (useSecondaryWeapon && MyInventory.contains(specialWeapon)) {
            Game.openTab(Game.TAB_INVENTORY);
            MyInventory.getItem(specialWeapon).interact("wield");
            for (int i = 0; i <= 15 && MyInventory.contains(specialWeapon); i++)
                Task.sleep(100);
        }
        else if (getSpecialEnergy() >= specEnergy && !Combat.isSpecialEnabled()) {
            Timer fail = new Timer(4000);
            while (getSpecialEnergy() >= specEnergy && fail.isRunning()) {
                Game.openTab(Game.TAB_ATTACK);
                if (!Combat.isSpecialEnabled()) {
                    Widgets.getComponent(884, 4).click();
                    for (int i = 0; i < 15 && !Combat.isSpecialEnabled(); i++)
                        Utils.sleep(100);
                }
                Utils.sleep(100);
                if (!MyPlayer.inCombat())
                    break;
            }
        }
        else if (useSecondaryWeapon && MyInventory.contains(primaryWeapon)) {
            Game.openTab(Game.TAB_INVENTORY);
            MyInventory.getItem(primaryWeapon).interact("wield");
            for (int i = 0; i <= 15 && MyInventory.contains(primaryWeapon); i++)
                Task.sleep(100);
        }
    }

    /**
     * Get current special energy
     *
     * @return special energy (0-100)
     */
    public static int getSpecialEnergy() {
        return Settings.get(SETTING_SPECIAL_ENERGY) / 10;
    }

    /**
     * Call to have the SpecialAttack class automatically setup up the special attack values and weapons
     */
    public static void setUpWeapons() {
        String prim;
        SpecialAttack.setSpecAt(101);
        SpecialAttack.setSpecEnergy(100);
        Game.openTab(Game.TAB_EQUIPMENT);
        if (MyEquipment.getItem(MyEquipment.WEAPON).getId() > -1) {
            prim = MyEquipment.getItem(MyEquipment.WEAPON).getName();
            primaryWeapon = prim;
            String name = prim;
            if (name.contains(">"))
                name = name.split(">")[1];
            for (int i = 0; i < weapons.length; i++) {
                for (int j = 0; j < weapons[i].length; j++) {
                    if (weapons[i][j].equalsIgnoreCase(name)) {
                        specAt = amountUsage[i];
                        specEnergy = amountUsage[i];
                    }
                }
            }
        }
        Game.openTab(Game.TAB_INVENTORY);
        for (Item item : MyInventory.getItems()) {
            String name = item.getName();
            if (name.contains(">"))
                name = name.split(">")[1];
            for (int i = 0; i < weapons.length; i++) {
                for (int j = 0; j < weapons[i].length; j++) {
                    if (weapons[i][j].equalsIgnoreCase(name)) {
                        specialWeapon = item.getName();
                        specAt = amountUsage[i];
                        specEnergy = amountUsage[i];
                        useSecondaryWeapon = true;
                    }
                }
            }
        }
    }
}