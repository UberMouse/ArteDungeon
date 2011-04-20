package nz.artedungeon.dungeon.doors;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Inventory;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.dungeon.Dungeon;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.util;
import nz.uberutils.methods.MyMovement;
import nz.uberutils.methods.MyObjects;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/1/11
 * Time: 5:42 PM
 * Package: nz.artedungeon.dungeon.doors;
 */
public class KeyDoor extends Door
{

    /**
     * Instantiates a new door.
     *
     * @param door   the door
     * @param parent instance of main script
     */
    public KeyDoor(GameObject door, DungeonMain parent) {
        super(door, KEY, parent);
    }

    @Override
    public boolean canOpen() {
        if (locked && open)
            open = false;
        if (open && connector > -1)
            return false;
        if (!locked)
            return true;
        if (util.tileInRoom(location) && Objects.getTopAt(location, Objects.TYPE_INTERACTIVE) == null)
            locked = false;
        for (int i = 0; i < GameConstants.KEY_DOORS.length; i++) {
            for (int l = 0; l < GameConstants.KEY_DOORS[i].length; l++) {
                if (id == GameConstants.KEY_DOORS[i][l]
                    && Inventory.contains(GameConstants.KEYS[i][l]))
                    return true;
            }
        }
        return false;
    }

    public void unlock() {
        if(!MyPlayer.currentRoom().contains(this))
            return;
        MyPlayer.setLastDoorOpended(this);
        MyMovement.turnTo(location);
        int timeout = 0;
        while (Calculations.distanceTo(location) > 4 && MyPlayer.currentRoom().getItem() == null && ++timeout <= 15)
            sleep(100);
        GameObject object = MyObjects.getTopAt(location, id);
        if (object.interact(getAction(object))) {
            timeout = 0;
            while (Objects.getTopAt(location, Objects.TYPE_INTERACTIVE) != null && ++timeout <= 15)
                sleep(100);
            if (Objects.getTopAt(location, Objects.TYPE_INTERACTIVE) == null) {
                locked = false;
                sleep(random(400,600));
            }
        }
    }

    @Override
    public void open() {
        if(!MyPlayer.currentRoom().contains(this))
            return;
        if (locked && open)
            open = false;
        if (!locked) {
            MyMovement.turnTo(location);
            int timeout = 0;
            while (Calculations.distanceTo(location) > 4 && MyPlayer.currentRoom().getItem() == null && ++timeout <= 15)
                sleep(100);
            if (Objects.getTopAt(location, Objects.TYPE_INTERACTIVE) != null) {
                locked = true;
                return;
            }
            else {
                MyPlayer.setLastDoorOpended(this);
                GameObject door = Objects.getNearest(id + Dungeon.doorDifferential());
                if (door.interact(getAction(door))) {
                    timeout = 0;
                    while (MyPlayer.currentRoom().contains(MyPlayer.location()) && ++timeout <= 150) {
                        sleep(10);
                        if (Game.getClientState() == 11)
                            break;
                    }
                }
            }
        }
        else {
            unlock();
        }
    }
}
