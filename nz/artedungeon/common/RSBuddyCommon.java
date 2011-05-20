package nz.artedungeon.common;

import com.rsbuddy.script.util.Random;
import nz.artedungeon.DungeonMain;
import nz.uberutils.helpers.Utils;



public class RSBuddyCommon
{
    protected static DungeonMain parent;

    /**
     * Instantiates a new RSBuddyCommon.
     *
     * @param parent instance of main script
     */
    public RSBuddyCommon(DungeonMain parent) {
        RSBuddyCommon.parent = parent;
    }

    /**
     * Debugs text along with function caller and line numbers.
     *
     * @param text the text
     */
    public static void debug(Object text) {
        if (DungeonMain.debug) {
            Utils.debug(text);
        }
    }

    /**
     * Sleep script.
     *
     * @param min the min time
     * @param max the max time
     */
    protected static void sleep(int min, int max) {
        parent.sleep(random(min, max));
    }

    /**
     * Sleep script.
     *
     * @param time the time
     */
    protected static void sleep(int time) {
        parent.sleep(time);
    }

    /**
     * Generate random number.
     *
     * @param min the min value
     * @param max the max value
     * @return the random number
     */
    protected static int random(int min, int max) {
        return Random.nextInt(min, max);
    }
}
