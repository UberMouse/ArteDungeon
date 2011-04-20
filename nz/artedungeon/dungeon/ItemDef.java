package nz.artedungeon.dungeon;

import com.rsbuddy.script.wrappers.GroundItem;
import nz.artedungeon.dungeon.rooms.Room;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/9/11
 * Time: 6:31 PM
 * Package: nz.artedungeon.dungeon;
 */
public class ItemDef
{
    private final int id;
    private final String name;
    private final Room parent;

    public ItemDef(GroundItem item, Room parent) {
        id = item.getItem().getId();
        name = item.getItem().getName();
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Room getParent() {
        return parent;
    }
}
