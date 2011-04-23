package nz.artedungeon.dungeon.doors;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.dungeon.rooms.Puzzle;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyMovement;
import nz.uberutils.methods.MyObjects;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/1/11
 * Time: 2:54 PM
 * Package: nz.artedungeon.dungeon;
 */
public class Normal extends Door
{

    /**
     * Instantiates a new door.
     *
     * @param door   the door
     * @param parent instance of main script
     */
    public Normal(GameObject door, DungeonMain parent) {
        super(door, NORMAL, parent);
    }

    @Override
    public boolean canOpen() {
        if (open && connector > -1)
            return false;
        else if (open && connector == -1)
            open = false;
        else if (!open && connector > -1)
            connector = -1;
        if (getParent().getType() == Room.PUZZLE) {
            Puzzle room = (Puzzle) getParent();
            return room.isSolved();
        }
        return !(getParent().getDoorAt(Util.getNearestNonWallTile(location)) != null &&
                 !getParent().getDoorAt(Util.getNearestNonWallTile(location)).isOpen());
    }

    @Override
    public void open() {
        if (!MyPlayer.currentRoom().contains(this))
            return;
        MyPlayer.setLastDoorOpended(this);
        MyMovement.turnTo(location);
        int timeout = 0;
        while (Calculations.distanceTo(location) > 4 && MyPlayer.currentRoom().getItem() == null && ++timeout <= 15)
            sleep(100);
        GameObject object = MyObjects.getTopAt(location, GameConstants.BASIC_DOORS);
        if (object == null) {
            int index = Explore.getDoors().indexOf(this);
            for (GameObject doorObject : Objects.getAllAt(location)) {
                int doorId = doorObject.getId();
                if (Util.arrayContains(GameConstants.KEY_DOORS, doorId))
                    Explore.getDoors().set(index, new Key(doorObject, parent));
                if (Util.arrayContains(GameConstants.STANDARD_DOORS, doorId))
                    Explore.getDoors().set(index, new Normal(doorObject, parent));
                if (Util.arrayContains(GameConstants.SKILL_DOORS, doorId))
                    Explore.getDoors().set(index, new Skill(doorObject, parent));
            }
            return;
        }
        if (object.interact(getAction(object))) {
            timeout = 0;
            while (MyPlayer.currentRoom().contains(MyPlayer.location()) && ++timeout <= 150) {
                sleep(10);
                if (Game.getClientState() == 11)
                    break;
            }
        }
    }
}
