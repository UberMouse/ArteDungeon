package nz.artedungeon.common;

import nz.artedungeon.DungeonMain;

public abstract class Strategy extends RSBuddyCommon
{

    /**
     * Instantiates a new strategy.
     *
     * @param parent instance of main script
     */
    public Strategy(DungeonMain parent) {
        super(parent);
    }

    /**
     * Executes strategy.
     */
    public abstract int execute();

    /**
     * Checks if valid.
     *
     * @return true, if is valid
     */
    public abstract boolean isValid();

    /**
     * Resets strategy for new dungeon.
     */
    public abstract void reset();

    /**
     * Returns status
     *
     * @return status
     */
    public abstract String getStatus();
}
