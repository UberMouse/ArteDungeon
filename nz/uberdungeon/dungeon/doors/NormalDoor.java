package nz.uberdungeon.dungeon.doors;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.dungeon.Explore;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.dungeon.rooms.PuzzleRoom;
import nz.uberdungeon.dungeon.rooms.Room;
import nz.uberdungeon.misc.GameConstants;
import nz.uberdungeon.utils.MyMovement;
import nz.uberdungeon.utils.MyObjects;
import nz.uberdungeon.utils.util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/1/11
 * Time: 2:54 PM
 * Package: nz.uberdungeon.dungeon;
 */
public class NormalDoor extends Door
{

    /**
     * Instantiates a new door.
     *
     * @param door   the door
     * @param parent instance of main script
     */
    public NormalDoor(GameObject door, DungeonMain parent) {
        super(door, NORMAL, parent);
    }

    @Override
    public boolean canOpen() {
        if (open && connector > -1)
            return false;
        if (getParent().getType() == Room.PUZZLE) {
            PuzzleRoom room = (PuzzleRoom) getParent();
            return room.isSolved();
        }
        if (getParent().getDoorAt(util.getNearestNonWallTile(location)) != null)
            return false;
        return true;
    }

    @Override
    public void open() {
        if(!MyPlayer.currentRoom().contains(this))
            return;
        debug(MyPlayer.currentRoom().contains(this));
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
                if (util.arrayContains(GameConstants.KEY_DOORS, doorId))
                    Explore.getDoors().set(index, new KeyDoor(doorObject, parent));
                if (util.arrayContains(GameConstants.STANDARD_DOORS, doorId))
                    Explore.getDoors().set(index, new NormalDoor(doorObject, parent));
                if (util.arrayContains(GameConstants.SKILL_DOORS, doorId))
                    Explore.getDoors().set(index, new SkillDoor(doorObject, parent));
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
