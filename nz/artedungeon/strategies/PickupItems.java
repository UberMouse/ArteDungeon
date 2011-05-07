package nz.artedungeon.strategies;

import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.wrappers.GroundItem;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.uberutils.methods.MyGroundItems;
import nz.uberutils.methods.MyMovement;


public class PickupItems extends Strategy
{

    public PickupItems(DungeonMain parent) {
        super(parent);
    }


    public int execute() {
        GroundItem pickup = MyPlayer.currentRoom().getItem();
        if (Inventory.isFull()) {
            for (int id : GameConstants.FOODS) {
                if (Inventory.contains(id))
                    Inventory.getItem(id).interact("eat");
            }
        }
        if (pickup != null && !MyPlayer.isMoving()) {
            MyMovement.turnTo(pickup.getLocation());
            if (MyGroundItems.itemInteract(pickup, "take")) {
                debug("Picking up item: " + pickup.getItem().getName());
                int timeout = 0;
                int count = Inventory.getCount();
                while (count == Inventory.getCount() && ++timeout <= 15 && !MyPlayer.inCombat()) {
                    sleep(100);
                    if (MyPlayer.isMoving()) {
                        timeout = 0;
                    }
                }
            }
        }
        return random(400, 600);
    }


    public boolean isValid() {
        return MyPlayer.currentRoom() != null &&
               MyPlayer.currentRoom().getItem() != null &&
               !MyPlayer.isInteracting();
    }


    public void reset() {

    }

    public String getStatus() {
        return "Picking up " + MyPlayer.currentRoom().getItem().getItem().getName();
    }

}
