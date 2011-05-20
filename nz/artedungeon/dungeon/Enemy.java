package nz.artedungeon.dungeon;

import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.DungeonCommon;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;



public class Enemy extends DungeonCommon
{

    /**
     * Instantiates a new enemy.
     *
     * @param parent instance of main script
     */
    public Enemy(DungeonMain parent) {
        super(parent);
    }

    private static Npc npc;

    /**
     * Gets the NPC.
     *
     * @return the npc
     */
    public static Npc getNPC() {
        return npc;
    }

    /**
     * Sets the NPC.
     *
     * @param npc the new npv
     */
    public static void setNPC(Npc npc) {
        Enemy.npc = npc;
    }

    /**
     * Get enemy hp percent.
     *
     * @return hp percent
     */
    private static int hp() {
        return (npc != null) ? npc.getHpPercent() : 0;
    }

    /**
     * Location.
     *
     * @return the enemy location
     */
    public static Tile location() {
        return (npc != null) ? npc.getLocation() : null;
    }

    /**
     * Checks if current enemy is valid.
     *
     * @return true, if is valid
     */
    public static boolean isValid() {
        return (npc != null && hp() > 0 && MyPlayer.currentRoom().contains(npc));
    }

    /**
     * Do action.
     *
     * @param action the action
     * @return true, if successful
     */
    public static boolean interact(String action) {
        return (npc != null) ? npc.interact(action) : null;
    }

    /**
     * Pick enemy using ids.
     *
     * @param ids the valid ids
     */
    public static Npc pickEnemy(final int... ids) {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                if (npc.getHpPercent() == 0)
                    return false;

                return !(npc.getInteracting() != null && !npc.getInteracting().equals(MyPlayer.get())) &&
                       Util.arrayContains(ids, npc.getId());

            }
        });
    }

    /**
     * Pick enemy using names.
     *
     * @param names the valid names
     */
    public static Npc pickEnemy(final String... names) {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                if (npc.getHpPercent() == 0)
                    return false;

                return !(npc.getInteracting() != null && !npc.getInteracting().equals(MyPlayer.get())) &&
                       Util.arrayContains(names, true, npc.getName());

            }
        });
    }

    /**
     * Pick enemy using generic filter.
     */
    public static Npc pickEnemy() {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                if (npc.getHpPercent() == 0)
                    return false;

                return !(npc.getInteracting() != null && !npc.getInteracting().equals(MyPlayer.get())) &&
                       !Util.arrayContains(GameConstants.NONARGRESSIVE_NPCS, npc.getId());

            }
        });
    }

    /**
     * Set enemy using generic filter.
     */
    public static void setEnemy() {
        npc = pickEnemy();
    }

    /**
     * Set enemy using ids.
     *
     * @param names the valid names
     */
    public static void setEnemy(final String... names) {
        npc = pickEnemy(names);
    }

    /**
     * Set enemy using ids.
     *
     * @param ids the valid ids
     */
    public static void setEnemy(final int... ids) {
        npc = pickEnemy(ids);
    }

    /**
     * Set enemy using supplied Npc.
     *
     * @param npc the npc to set it to
     */
    public static void setEnemy(Npc npc) {
        Enemy.npc = npc;
    }

    /**
     * Checks if is dead.
     *
     * @return true, if is dead
     */
    public static boolean isDead() {
        return hp() == 0;
    }
}
