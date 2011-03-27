package nz.uberdungeon.utils;

import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.task.LoopTask;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GroundItem;
import com.rsbuddy.script.wrappers.Npc;
import nz.uberdungeon.dungeon.EnemyDef;
import nz.uberdungeon.dungeon.Explore;
import nz.uberdungeon.dungeon.ItemDef;
import nz.uberdungeon.dungeon.MyPlayer;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/9/11
 * Time: 7:02 PM
 * Package: nz.uberdungeon.utils;
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
                    return util.tileInRoom(groundItem.getLocation());
                }
            });
            ArrayList<ItemDef> itemDefs = new ArrayList<ItemDef>();
            for (GroundItem item : items)
                itemDefs.add(new ItemDef(item, MyPlayer.currentRoom()));
            Explore.getRooms().get(Explore.getRooms().indexOf(MyPlayer.currentRoom())).setGroundItems(itemDefs);
            Npc[] enemies = Npcs.getLoaded(new Filter<Npc>()
            {

                public boolean accept(Npc npc) {
                    return util.tileInRoom(npc.getLocation());
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
