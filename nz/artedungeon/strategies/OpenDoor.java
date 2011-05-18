package nz.artedungeon.strategies;

import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.dungeon.doors.Door;
import nz.uberutils.helpers.Utils;

public class OpenDoor extends Strategy
{

    public OpenDoor(DungeonMain parent) {
        super(parent);
    }


    public int execute() {
        Door toOpen = MyPlayer.currentRoom().getNearestDoor();
        if (toOpen == null)
            return random(400, 600);

        toOpen.open();
        return random(400, 600);
    }


    public boolean isValid() {
        return MyPlayer.currentRoom().getNearestDoor() != null;
    }


    public void reset() {

    }

    public String getStatus() {
        return "Opening " + MyPlayer.currentRoom().getNearestDoor().getName() + " door";
    }

}
