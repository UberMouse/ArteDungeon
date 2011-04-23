package nz.uberutils.methods;

import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/21/11
 * Time: 9:15 PM
 * Package: nz.uberutils.methods;
 */
public class MyNpcs extends Npcs
{
    public static Npc getTopAt(final Tile tile) {
        return getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                return npc.getLocation().equals(tile);
            }
        });
    }
}
