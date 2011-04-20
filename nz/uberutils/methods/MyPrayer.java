package nz.uberutils.methods;

import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Settings;
import com.rsbuddy.script.methods.Widgets;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/29/11
 * Time: 6:38 PM
 * Package: nz.uberutils.methods;
 */
public class MyPrayer
{

    /**
     * Advanced Prayer support. The difference between the old client api and this
     * new version, is that this version uses the settings class instead of widgets.
     * This creates far more stable scripts, without checking the prayer tab
     * regularly.
     *
     * @author Joris
     */
    public enum Prayer
    {
        // Regular prayers
        THICK_SKIN(0, 0, 1, "Thick skin", true),
        BURST_OF_STRENGTH(1, 1, 4, "Burst of strength", true),
        CLARITY_OF_THOUGHT(2, 2, 7, "Clarity of thought", true),
        SHARP_EYE(3, 18, 8, "Sharp eye", true),
        MYSTIC_WILL(4, 19, 9, "Mystic will", true),
        ROCK_SKIN(5, 3, 10, "Rock skin", true),
        SUPERHUMAN_STRENGTH(6, 4, 13, "Superhuman strength", true),
        IMPROVED_REFLEXES(7, 5, 16, "Improved reflexes", true),
        RAPID_RESTORE(8, 6, 19, "Rapad restore", true),
        RAPID_HEAL(9, 7, 22, "Rapid heal", true),
        PROTECT_ITEM_REGULAR(10, 8, 25, "Protect item", true),
        HAWK_EYE(11, 20, 26, "Hawk eye", true),
        MYSTIC_LORE(12, 21, 27, "Mystic lore", true),
        STEEL_SKIN(13, 9, 28, "Steel skin", true),
        ULTIMATE_STRENGTH(14, 10, 31, "Ultimate strength", true),
        INCREDIBLE_REFLEXES(15, 11, 34, "Incredible reflexes", true),
        PROTECT_FROM_SUMMONING(16, 24, 35, "Protect from summoning", true),
        PROTECT_FROM_MAGIC(17, 12, 37, "Protect from magic", true),
        PROTECT_FROM_MISSILES(18, 13, 40, "Protect from missiles", true),
        PROTECT_FROM_MELEE(19, 14, 43, "Protect from melee", true),
        EAGLE_EYE(20, 22, 44, "Eagle eye", true),
        MYSTIC_MIGHT(21, 23, 45, "Mystic might", true),
        RETRIBUTION(22, 15, 46, "Retribution", true),
        REDEMPTION(23, 16, 49, "Redemption", true),
        SMITE(24, 17, 52, "Smite", true),
        CHIVALRY(25, 25, 60, "Chivalry", true),
        RAPID_RENEWAL(26, 27, 65, "Rapid renewal", true),
        PIETY(27, 26, 70, "Piety", true),
        RIGOUR(28, 28, 74, "Rigour", true),
        AUGURY(29, 29, 77, "Augury", true),

        // Curses
        PROTECT_ITEM_CURSE(0, 0, 50, "Protect item", false),
        SAP_WARRIOR(1, 1, 50, "Sap warrior", false),
        SAP_RANGER(2, 2, 52, "Sap ranger", false),
        SAP_MAGE(3, 3, 54, "Sap mage", false),
        SAP_SPIRIT(4, 4, 56, "Sap spirit", false),
        BERSERKER(5, 5, 59, "Berserker", false),
        DEFLECT_SUMMONING(6, 6, 62, "Deflect summoning", false),
        DEFLECT_MAGIC(7, 7, 65, "Deflect magic", false),
        DEFLECT_MISSILE(8, 8, 68, "Deflect missile", false),
        DEFLECT_MELEE(9, 9, 71, "Deflect melee", false),
        LEECH_ATTACK(10, 10, 74, "Leach attack", false),
        LEECH_RANGE(11, 11, 76, "Leach range", false),
        LEECH_MAGIC(12, 12, 78, "Leach magic", false),
        LEECH_DEFENCE(13, 13, 80, "Leach defence", false),
        LEECH_STRENGTH(14, 14, 82, "Leach strength", false),
        LEECH_ENERGY(15, 15, 84, "Leach energy", false),
        LEECH_SPECIAL_ATTACK(16, 16, 86, "Leech special attack", false),
        WRATH(17, 17, 89, "Wrath", false),
        SOUL_SPLIT(18, 18, 92, "Soul split", false),
        TURMOIL(19, 19, 95, "Turmoil", false);

        final private int index;
        final private int settingIndex;
        final private int level;
        final private String name;
        final private boolean regular;

        /**
         * An standard prayer, can be a regular prayer or a curse.
         *
         * @param index        index of widgets menu.
         * @param settingIndex index of settings.
         * @param level        level required to use prayer.
         * @param name         name of prayer.
         * @param regular      is prayer regular or a curse.
         */
        Prayer(final int index, final int settingIndex, final int level, final String name, final boolean regular) {
            this.index = index;
            this.settingIndex = settingIndex;
            this.level = level;
            this.name = name;
            this.regular = regular;
        }

        /**
         * Returns the index of the prayers ordered in widgets.
         *
         * @return index
         */
        public int getIndex() {
            return index;
        }

        /**
         * Returns the index of the prayers ordered in the settings class.
         *
         * @return index of settings class
         */
        public int getSettingIndex() {
            return settingIndex;
        }

        /**
         * Required level of the prayer.
         *
         * @return required level
         */
        public int getRequiredLevel() {
            return level;
        }

        /**
         * Is current prayer regular.
         *
         * @return true if current prayer is regular; otherwise false.
         */
        public boolean isRegularPrayer() {
            return regular;
        }

        /**
         * Will activate/deactivate prayer based upon the paramater activate. This
         * doesn't open up the prayer screen, so there is need to check if prayer is
         * selected in your script.
         *
         * @param activate activate prayer; otherwise deactivate the prayer.
         * @return true if it interacted with prayer.
         */
        public boolean setActivated(final boolean activate) {
            if (isUsingRegularPrayers() == isRegularPrayer() && activate != isSelected()) {
                if (Game.getCurrentTab() != Game.TAB_PRAYER) {
                    Game.openTab(Game.TAB_PRAYER);
                    try {
                        Thread.sleep(700);
                    } catch (InterruptedException e) {
                    }
                }
                if (Widgets.getComponent(271, 8)
                           .getComponent(getIndex())
                           .interact(activate ? "Activate" : "Deactivate")) {
                    for (int i = 0; i < 30; i++) {
                        if (isSelected()) {
                            return true;
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                        }
                    }
                    return true;
                }

            }
            return false;
        }

        /**
         * Checks if current prayer is selected.
         *
         * @return true if current prayer is selected; otherwise false.
         */
        public boolean isSelected() {
            for (Prayer selected : getSelectedPrayers()) {
                if (selected == this) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return name + " - " + (regular ? "Regular" : "Curse") + " (Level: " + getRequiredLevel() + ")";
        }
    }

    /**
     * Gets all activated prayers.
     *
     * @return all activate prayers; it'll never return null.
     */
    public static Prayer[] getSelectedPrayers() {
        final boolean regular = isUsingRegularPrayers();
        int number = regular ? Settings.get(1395) : Settings.get(1582);
        int power = 536870912; // = Math.pow(2, 29)
        final ArrayList<Integer> selected = new ArrayList<Integer>();
        for (int i = 29; i >= 0; i--) {
            if (power <= number) {
                selected.add(i);
                number -= power;
            }
            power /= 2;
        }
        final ArrayList<Prayer> prayers = new ArrayList<Prayer>();
        for (int index : selected) {
            for (Prayer prayer : Prayer.values()) {
                if (prayer.getSettingIndex() == index && prayer.isRegularPrayer() == regular) {
                    prayers.add(prayer);
                }
            }
        }
        return prayers.toArray(new Prayer[prayers.size()]);
    }

    /**
     * Will check for for all prayers with equal name, if found it'll return the
     * prayer, which belongs to the name.
     *
     * @param name of the prayer
     * @return the Prayer, if one of the names matches; otherwise null.
     */
    public static Prayer parsePrayer(final String name) {
        for (Prayer prayer : Prayer.values()) {
            if (prayer.name().equals(name)) {
                return prayer;
            }
        }
        return null;
    }

    /**
     * Check if is using regulary prayers.
     *
     * @return true if is using regular prayers; false if using curses.
     */
    public static boolean isUsingRegularPrayers() {
        return Settings.get(1584) % 2 == 0;
    }
}
