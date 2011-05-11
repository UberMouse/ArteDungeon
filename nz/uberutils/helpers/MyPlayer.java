package nz.uberutils.helpers;

import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.*;
import nz.uberutils.methods.MyInventory;
import nz.uberutils.methods.MyMovement;


// TODO: Auto-generated Javadoc
public class MyPlayer
{
    /**
     * Enum for storing potions and there attributes
     */
    private static enum Potions
    {
        EXTREME_ATTACK("Extreme attack", Skills.ATTACK),
        EXTREME_STRENGTH("Extreme strength", Skills.STRENGTH),
        EXTREME_DEFENCE("Extreme defence", Skills.DEFENSE),
        SUPER_ATTACK("Super attack", Skills.ATTACK),
        SUPER_STRENGTH("Super strength", Skills.STRENGTH),
        SUPER_DEFENCE("Super defence", Skills.DEFENSE),
        ATTACK_POTION("Attack potion", Skills.ATTACK),
        STRENGTH_POTION("Strength potion", Skills.STRENGTH),
        DEFENCE_POTION("Defence potion", Skills.DEFENSE),
        COMBAT_POTION("Combat potion", Skills.ATTACK),
        OVERLOAD("Overload (", Skills.ATTACK);
        private String name;
        private int skill;

        Potions(String name, int skill) {
            this.name = name;
            this.skill = skill;
        }

        public String getName() {
            return name;
        }

        public int skill() {
            return skill;
        }
    }

    /**
     * Location.
     *
     * @return the player location
     */
    public static Tile location() {
        return MyPlayer.get().getLocation();
    }

    /**
     * Eat food.
     *
     * @param food the food ids
     */
    public static void eat(int[] food) {
        if (Inventory.containsOneOf(food))
            Inventory.getItem(food).interact("Eat");
    }

    /**
     * Eat food.
     */
    public static void eat() {
        Item food = edibleItem();
        if (food != null)
            food.click(true);
    }

    /**
     * Returns player hp percent.
     *
     * @return hp percent remaining
     */
    public static int hp() {
        return Combat.getHealth();
    }

    /**
     * In area.
     *
     * @param area the area
     * @return true, if successful
     */
    public static boolean inArea(Area area) {
        return area.contains(MyPlayer.get().getLocation());
    }

    /**
     * Attack.
     *
     * @param enemy the enemy
     */
    public static boolean attack(Npc enemy) {
        MyMovement.turnTo(enemy);
        if (enemy != null && MyPlayer.get().getInteracting() == null) {
            if (enemy.interact("Attack")) {
                return true;
            }
            else
                return false;
        }
        return false;
    }

    /**
     * Get prayer points.
     *
     * @return current prayer points
     */
    public static int prayer() {
        return Combat.getPrayerPoints();
    }

    /**
     * In combat.
     *
     * @param multi in multi combat area894
     * @return true, if in combat
     */
    public static boolean inCombat(boolean multi) {
        if (multi)
            return MyPlayer.isInteracting() && MyPlayer.interacting().getHpPercent() > 0;
        else
            return MyPlayer.isInteracting() &&
                   MyPlayer.interacting().getHpPercent() > 0 &&
                   (getInteractor() != null && interacting().equals(getInteractor()) || getInteractor() == null);
    }

    /**
     * In combat.
     *
     * @return true, if in combat
     */
    public static boolean inCombat() {
        return inCombat(false);
    }

    /**
     * Checks if is interacting.
     *
     * @return true, if is interacting
     */
    public static boolean isInteracting() {
        return (MyPlayer.get().getInteracting() != null);
    }

    /**
     * Return interacting NPC as Npc.
     *
     * @return the Npc
     */
    public static Npc interacting() {
        return (Npc) MyPlayer.get().getInteracting();
    }

    /**
     * Gets the player.
     *
     * @return the player RSPlayer
     */
    public static Player get() {
        return (Player) Players.getLocal();
    }

    /**
     * Checks if player is moving.
     *
     * @return true, if is moving
     */
    public static boolean isMoving() {
        return get().isMoving();
    }

    /**
     * Generic level to eat at
     *
     * @return true, if player hp is too low
     */
    public static boolean needToEat() {
        return hp() < 50;
    }

    /**
     * Check if inventory contains potions
     *
     * @return true if any potions were found
     */
    public static boolean hasPotions() {
        for (Potions potion : Potions.values())
            if (MyInventory.contains(potion.getName()))
                return true;
        return false;
    }

    /**
     * Drink any potions in inventory for unboosted skills
     */
    public static void drinkPotions() {
        if (!hasPotions())
            return;
        for (Potions potion : Potions.values())
            if (!Utils.isBoosted(potion.skill()) && MyInventory.contains(potion.getName())) {
                MyInventory.getItem(potion.getName()).click(true);
                Utils.sleep(Utils.random(400, 500));
            }
    }

    /**
     * Should repot
     *
     * @return true if should repot
     */
    public static boolean shouldPot() {
        if (!hasPotions())
            return false;
        for (Potions potion : Potions.values()) {
            if (!Utils.isBoosted(potion.skill()) && MyInventory.contains(potion.getName(), true))
                return true;
        }
        return false;
    }

    /**
     * Gets edible item from Inventory
     *
     * @return Item if one is found, else null
     */
    public static Item edibleItem() {
        Item[] is = MyInventory.getItems(true);
        for (Item i : is) {
            if (isEdible(i))
                return i;
        }
        return null;
    }

    public static boolean isEdible(Item item) {
        if (item.getComponent().getActions() == null || item.getComponent().getActions()[0] == null)
            return false;
        return item.getComponent().getActions()[0].contains("Eat");
    }

    /**
     * Gets npc interating with you
     *
     * @return npc interacting with local player
     */
    public static Npc getInteractor() {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                if (npc.getInteracting() == null)
                    return false;
                return npc.getInteracting().equals(get());
            }
        });
    }
}
