package nz.artedungeon.dungeon;

import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.wrappers.Area;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Player;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.DungeonCommon;
import nz.artedungeon.dungeon.doors.Door;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.misc.GameConstants;
import nz.uberutils.methods.MyMovement;


// TODO: Auto-generated Javadoc
public class MyPlayer extends DungeonCommon
{
    public static Room currentRoom() {
        if (Explore.getRooms().indexOf(currentRoom) != -1)
            return Explore.getRooms().get(Explore.getRooms().indexOf(currentRoom));
        else
            return null;
    }

    public static void setCurrentRoom(Room currentRoom) {
        MyPlayer.currentRoom = currentRoom;
    }

    public static Room lastRoom() {
        return lastRoom;
    }

    public static void setLastRoom(Room lastRoom) {
        MyPlayer.lastRoom = lastRoom;
    }

    public static Door lastDoorOpened() {
        return lastDoorOpended;
    }

    public static void setLastDoorOpended(Door lastDoorOpended) {
        MyPlayer.lastDoorOpended = lastDoorOpended;
    }

    public static ItemHandler.Style combatStyle() {
        return combatStyle;
    }

    public static void setCombatStyle(ItemHandler.Style combatStyle) {
        MyPlayer.combatStyle = combatStyle;
    }

    public static int getComplexity() {
        return complexity;
    }

    public static void setComplexity(int complexity) {
        MyPlayer.complexity = complexity;
    }

    public static boolean teleBack() {
        return teleBack;
    }

    public static void setTeleBack(boolean teleBack) {
        MyPlayer.teleBack = teleBack;
    }

    public static int timesDied() {
        return timesDied;
    }

    public static void setTimesDied(int timesDied) {
        MyPlayer.timesDied = timesDied;
    }

    public static int tokensGained() {
        return tokensGained;
    }

    public static void addTokensGained(int amount) {
        tokensGained += amount;
    }

    private static Room currentRoom;
    private static Room lastRoom;
    private static Door lastDoorOpended;
    private static ItemHandler.Style combatStyle = ItemHandler.Style.MELEE;
    private static int complexity = 1;
    private static int tokensGained;
    private static boolean teleBack;
    public static final int safeHP = 75;

    private static int timesDied = 0;

    /**
     * Instantiates a new player.
     *
     * @param parent instance of main script
     */
    public MyPlayer(DungeonMain parent) {
        super(parent);
    }

    /**
     * Cast spell.
     *
     * @param spell    the spell
     * @param castType the cast type
     * @return true, if successful
     */
    public static boolean castSpell(int spell, String castType) {
        if (Game.getCurrentTab() != Game.TAB_MAGIC) {
            Game.openTab(Game.TAB_MAGIC);
            for (int i = 0; i < 100; i++) {
                sleep(20);
                if (Game.getCurrentTab() == Game.TAB_MAGIC) {
                    break;
                }
            }
            sleep(random(150, 250));
        }
        return Game.getCurrentTab() == Game.TAB_MAGIC && Widgets.get(950).getComponent(spell).interact(castType);
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
     */
    public static void eat() {
        if (Inventory.containsOneOf(GameConstants.FOODS)) {
            Mouse.move(Inventory.getItem(GameConstants.FOODS).getComponent().getAbsLocation());
            Mouse.click(true);
        }
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
    public static void attack(Npc enemy) {
        MyMovement.turnTo(enemy);
        if (enemy != null && MyPlayer.get().getInteracting() == null) {
            if (enemy.interact("Attack")) {
                int timeout = 0;
                while (!MyPlayer.get().isInCombat() && ++timeout <= 20) {
                    sleep(100);
                }
            }
        }
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
     * @return true, if in combat
     */
    public static boolean inCombat() {
        return interacting() != null;
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
     * Does the inventory contain food
     * @return true if the inventory contains food
     */
    public static boolean hasFood() {
        return Inventory.containsOneOf(GameConstants.FOODS);
    }
}
