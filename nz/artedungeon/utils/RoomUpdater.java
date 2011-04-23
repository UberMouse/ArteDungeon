package nz.artedungeon.utils;

import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.task.LoopTask;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GroundItem;
import com.rsbuddy.script.wrappers.Npc;
import nz.artedungeon.dungeon.EnemyDef;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.ItemDef;
import nz.artedungeon.dungeon.MyPlayer;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/9/11
 * Time: 7:02 PM
 * Package: nz.artedungeon.utils;
 */
public class RoomUpdater extends LoopTask
{
    @Override
    public int loop() {
        try {
            if (MyPlayer.currentRoom() == null || !MyPlayer.currentRoom().contains(MyPlayer.location()))
                return 100;
            GroundItem[] items = GroundItems.getLoaded(new Filter<GroundItem>()
            {

                public boolean accept(GroundItem groundItem) {
                    return Util.tileInRoom(groundItem.getLocation());
                }
            });
            ArrayList<ItemDef> itemDefs = new ArrayList<ItemDef>();
            for (GroundItem item : items)
                itemDefs.add(new ItemDef(item, MyPlayer.currentRoom()));
            Explore.getRooms().get(Explore.getRooms().indexOf(MyPlayer.currentRoom())).setGroundItems(itemDefs);
            Npc[] enemies = Npcs.getLoaded(new Filter<Npc>()
            {

                public boolean accept(Npc npc) {
                    return Util.tileInRoom(npc.getLocation()) &&
                           npc.getActions() != null &&
                           npc.getActions().length > 0 &&
                           npc.getActions()[0] != null &&
                           npc.getActions()[0].contains("Attack");
                }
            });
            ArrayList<EnemyDef> enemyDefs = new ArrayList<EnemyDef>();
            for (Npc npc : enemies)
                enemyDefs.add(new EnemyDef(npc, MyPlayer.currentRoom()));
            Explore.getRooms().get(Explore.getRooms().indexOf(MyPlayer.currentRoom())).setEnemies(enemyDefs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 2500;
    }
}
