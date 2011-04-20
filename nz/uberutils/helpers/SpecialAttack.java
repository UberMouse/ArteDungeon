package nz.uberutils.helpers;

import com.rsbuddy.script.methods.Combat;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Settings;
import com.rsbuddy.script.methods.Widgets;
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

    public static boolean canSpec() {
        return getSpecialEnergy() >= specEnergy;
    }

    public static void doSpecial() {
        if (useSecondaryWeapon && MyInventory.contains(specialWeapon)) {
            Game.openTab(Game.TAB_INVENTORY);
            MyInventory.getItem(specialWeapon).interact("wield");
        }
        else if (getSpecialEnergy() >= specEnergy && !Combat.isSpecialEnabled()) {
            while (getSpecialEnergy() >= specEnergy && MyPlayer.inCombat()) {
                Game.openTab(Game.TAB_ATTACK);
                if (!Combat.isSpecialEnabled()) {
                    Widgets.getComponent(884, 4).click();
                    for (int i = 0; i < 15 && !Combat.isSpecialEnabled(); i++)
                        Utils.sleep(100);
                }
                Utils.sleep(100);
            }
        }
        else if (useSecondaryWeapon && MyInventory.contains(primaryWeapon)) {
            Game.openTab(Game.TAB_INVENTORY);
            MyInventory.getItem(primaryWeapon).interact("wield");
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
}
