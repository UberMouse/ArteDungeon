package nz.uberdungeon.utils;

import com.rsbuddy.script.methods.Combat;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Skills;
import com.rsbuddy.script.methods.Widgets;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.RSBuddyCommon;
import nz.uberdungeon.dungeon.EnemyDef;
import nz.uberdungeon.dungeon.Equipable;
import nz.uberdungeon.dungeon.MyPlayer;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/13/11
 * Time: 8:22 PM
 * Package: nz.uberdungeon.utils;
 */
public class MyCombat extends RSBuddyCommon
{
    public static final int PARENT_IFACE = 884;
    public static final int STYLE_ONE_IFACE = 11;
    public static final int STYLE_TWO_IFACE = 12;
    public static final int STYLE_THREE_IFACE = 13;
    public static final int STYLE_FOUR_IFACE = 14;

    private static enum Weapons
    {
        RAPIER(new String[]{"stab", "stab", "slash", "stab"}),
        WARHAMMER(new String[]{"crush", "crush", "crush"}),
        LONGSWORD(new String[]{"slash", "slash", "stab", "slash"}),
        BATTLEAXE(new String[]{"slash", "slash", "slash", "crush"}),
        SPEAR(new String[]{"stab", "slash", "crush", "stab"}),
        MAUL(new String[]{"crush", "crush", "crush"}),
        H_SWORD(new String[]{"slash", "slash", "crush", "slash"}),
        LONGBOW(new String[]{"range"}),
        SHORTBOW(new String[]{"range"}),
        NOVALUE(new String[]{});
        ;
        private String[] ATTACK_STYLES;

        private Weapons(String[] attackStyles) {
            ATTACK_STYLES = attackStyles;
        }

        public static Weapons toValue(String value) {
            try {
                return Weapons.valueOf(value);
            } catch (Exception ignored) {
                return NOVALUE;
            }
        }

        public String[] attackStyles() {
            return ATTACK_STYLES;
        }
    }

    /**
     * Instantiates a new RSBuddyCommon.
     *
     * @param parent instance of main script
     */
    public MyCombat(DungeonMain parent) {
        super(parent);
    }

    /**
     * Get attack styles of current weapon
     * @return String[] attack styles
     */
    public static String[] getAttackStyles() {
        return Weapons.toValue(new Equipable(MyEquipment.getItem(MyEquipment.WEAPON).getName()).getItemName())
                      .attackStyles();
    }

    /**
     * Get attack actions of current weapon (Stab, luncge, smash, block etc)
     * @return String[] attack actions
     */
    public static String[] getAttackActions() {
        ArrayList<String> actions = new ArrayList<String>();
        try {
            actions.add(Widgets.getComponent(PARENT_IFACE, STYLE_ONE_IFACE).getActions()[0]);
            actions.add(Widgets.getComponent(PARENT_IFACE, STYLE_TWO_IFACE).getActions()[0]);
            actions.add(Widgets.getComponent(PARENT_IFACE, STYLE_THREE_IFACE).getActions()[0]);
            actions.add(Widgets.getComponent(PARENT_IFACE, STYLE_FOUR_IFACE).getActions()[0]);
        } catch (NullPointerException ignored) {
        }
        return actions.toArray(new String[0]);
    }

    /**
     * Check if have style
     * @param style String to check
     * @return true if have style
     */
    public static boolean haveStyle(String style) {
        return getStyleIndex(style) != -1;
    }

    /**
     * Get current attack style
     * @return current attack style
     */
    public static String currentAttackStyle() {
        return getAttackStyles()[Combat.getFightMode()];
    }

    /**
     * Get current attack action
     * @return current attack action
     */
    public static String currentAttackAction() {
        return getAttackActions()[Combat.getFightMode()];
    }

    /**
     * Set new attack style
     * @param style to be set
     */
    public static void setAttackStyle(String style) {
        if (haveStyle(style)) {
            Game.openTab(Game.TAB_ATTACK);
            for (int i = 0; Game.getCurrentTab() != Game.TAB_ATTACK && i <= 15; i++)
                sleep(100);
            Widgets.getComponent(PARENT_IFACE, getStyleIndex(style)).click();
            sleep(500, 600);
            Game.openTab(Game.TAB_INVENTORY);
        }
    }

    /**
     * Get Widget index of attack style
     * @param style to get index of
     * @return int index of attack style
     */
    public static int getStyleIndex(String style) {
        String[] styles = getAttackStyles();
        for (int i = 0; i < styles.length; i++)
            if (styles[i].equalsIgnoreCase(style))
                return 11 + i;
        return -1;
    }

    /**
     * Get Widget index of attack action
     * @param action to get index of
     * @return int index of attack action
     */
    public static int getActionIndex(String action) {
        String[] actions = getAttackActions();
        for (int i = 0; i < actions.length; i++)
            if (actions[i].equalsIgnoreCase(action))
                return 11 + i;
        return -1;
    }

    public static boolean isBestPrayerForEnemyOn(EnemyDef enemy) {
        return Combat.isPrayerOn(styleToPrayer(enemy.recommendedPrayer()));
    }

    public static void setBestPrayerForEnemy(EnemyDef enemy) {
        Combat.setPrayer(styleToPrayer(enemy.recommendedPrayer()), true);
    }

    public static boolean canUsePrayer(Combat.Prayer prayer) {
        return Skills.getCurrentLevel(Skills.PRAYER) >= prayer.getRequiredLevel();
    }

    /**
     * Turn on best prayer for enemy if canUsePrayer and not prayer is on
     * @param enemy enemy to check against
     */
    public static void doPrayerFor(EnemyDef enemy) {
        if(!MyPlayer.usePrayers())
            return;
        Combat.Prayer prayer = styleToPrayer(enemy.recommendedPrayer());
        if (canUsePrayer(prayer) && !Combat.isPrayerOn(prayer))
            Combat.setPrayer(prayer, true);
    }

    /**
     * Converts attack style int too Prayer
     * @param style
     * @return
     */
    public static Combat.Prayer styleToPrayer(int style) {
        switch (style) {
            case 0:
                return Combat.Prayer.PROTECT_FROM_MELEE;
            case 1:
                return Combat.Prayer.PROTECT_FROM_MISSILES;
            case 2:
                return Combat.Prayer.PROTECT_FROM_MAGIC;
        }
        return null;
    }
}
