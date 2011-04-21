package nz.artedungeon.strategies;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.GameObject;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.dungeon.doors.Door;
import nz.artedungeon.dungeon.doors.Normal;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.misc.GameConstants;


public class ChangeRoom extends Strategy
{

    public ChangeRoom(DungeonMain parent) {
        super(parent);
    }


    public int execute() {
        for (Room room : Explore.getRooms()) {
            if (room.contains(MyPlayer.location())) {
                MyPlayer.setLastRoom(MyPlayer.currentRoom());
                MyPlayer.setCurrentRoom(room);
                if (!MyPlayer.lastDoorOpened().isOpen() &&
                    Calculations.distanceTo(MyPlayer.lastDoorOpened().getLocation()) < 5) {
                    MyPlayer.lastDoorOpened().setOpen(true);
                    MyPlayer.lastRoom()
                            .updateDoor(MyPlayer.lastDoorOpened());
                    MyPlayer.lastDoorOpened().setConnector(MyPlayer.currentRoom());
                    Explore.getDoors().remove(MyPlayer.lastDoorOpened());
                    Explore.getDoors().add(MyPlayer.lastDoorOpened());
                }
                return random(400, 600);
            }
        }
        MyPlayer.setLastRoom(MyPlayer.currentRoom());
        MyPlayer.setCurrentRoom(Explore.newRoom());
        updateDoors();
        if (Objects.getNearest(GameConstants.BOSS_DOORS) != null && Explore.getBossRoom() == null) {
            Explore.setBossRoom(MyPlayer.currentRoom());
            parent.foundBoss = true;
            GameObject bossDoor = Objects.getNearest(GameConstants.BOSS_DOORS);
            Door tempDoor = new Normal(bossDoor, parent);
            tempDoor.setConnector(MyPlayer.lastRoom());
            tempDoor.setOpen(true);
            Explore.getDoors().add(tempDoor);
        }
        return random(400, 600);
    }

    public void updateDoors() {
        Door closestDoor = MyPlayer.currentRoom().getClosestDoorTo(MyPlayer.lastDoorOpened());
        if (closestDoor != null) {
            closestDoor.setOpen(true);
            closestDoor.setConnector(MyPlayer.lastRoom());
        }
        MyPlayer.lastDoorOpened().setOpen(true);
        MyPlayer.lastRoom()
                .updateDoor(MyPlayer.lastDoorOpened());
        MyPlayer.currentRoom().updateDoor(closestDoor);
        MyPlayer.lastDoorOpened().setConnector(MyPlayer.currentRoom());
        Explore.getDoors().remove(MyPlayer.lastDoorOpened());
        Explore.getDoors().add(MyPlayer.lastDoorOpened());
        Explore.getDoors().remove(closestDoor);
        Explore.getDoors().add(closestDoor);
    }

    public boolean isValid() {
        return MyPlayer.currentRoom() != null && !MyPlayer.currentRoom().contains(MyPlayer.location());
    }


    public void reset() {

    }

    public String getStatus() {
        return "Switching rooms";
    }

}
