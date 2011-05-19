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
public class MyNpcs
{
    public static Npc getTopAt(final Tile tile) {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                return npc.getLocation().equals(tile);
            }
        });
    }

    /**
     * Gets nearest Npc to Player in Room (With regex or .contains)
     *
     * @param name the name of the <tt>Npc</tt> to search for
     * @return <tt>Npc</tt>, if one is found, else <tt>null</tt>
     */
    public static Npc getNearest(final String name) {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc Npc) {
                return Npc != null &&
                       (Npc.getName().toLowerCase().matches(name.toLowerCase()) ||
                        Npc.getName().toLowerCase().contains(name)) &&
                       Npc.getHpPercent() > 0;
            }
        });
    }
}
