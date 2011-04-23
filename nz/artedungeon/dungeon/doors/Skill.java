package nz.artedungeon.dungeon.doors;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.Options;
import nz.uberutils.methods.MyMovement;
import nz.uberutils.methods.MyObjects;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/1/11
 * Time: 5:43 PM
 * Package: nz.artedungeon.dungeon.doors;
 */
public class Skill extends Door implements MessageListener
{
    private boolean canOpen = true;

    /**
     * Instantiates a new door.
     *
     * @param door   the door
     * @param parent instance of main script
     */
    public Skill(GameObject door, DungeonMain parent) {
        super(door, SKILL, parent);
    }

    @Override
    public boolean canOpen() {
        return !open &&
               canOpen &&
               !(Util.arrayContains(GameConstants.DARK_SPIRIT, id) && !Options.getBoolean("prayDoors"));
    }

    @Override
    public void open() {
        if (!Util.tileInRoom(Util.getNearestNonWallTile(location)))
            return;
        MyPlayer.setLastDoorOpended(this);
        MyMovement.turnTo(location);
        int timeout = 0;
        while (Calculations.distanceTo(location) > 4 && MyPlayer.currentRoom().getItem() == null && ++timeout <= 15)
            sleep(100);
        GameObject object = MyObjects.getTopAt(location, id);
        if (object == null)
            object = MyObjects.getTopAt(location, GameConstants.BASIC_DOORS);
        if (object != null && object.interact(getAction(object))) {
            timeout = 0;
            while (Objects.getNearest(id) != null && ++timeout <= 15)
                sleep(100);
            if (Objects.getTopAt(location, Objects.TYPE_INTERACTIVE) == null)
                open = true;
        }
        else if (object == null)
            open = true;
    }

    public void messageReceived(MessageEvent messageEvent) {
        if (messageEvent.getMessage().contains("level of"))
            canOpen = false;
    }
}
