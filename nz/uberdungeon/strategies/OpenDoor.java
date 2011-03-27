package nz.uberdungeon.strategies;

import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.Strategy;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.dungeon.doors.Door;

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
