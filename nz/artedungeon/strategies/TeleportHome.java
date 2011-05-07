package nz.artedungeon.strategies;

import com.rsbuddy.script.wrappers.Npc;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.dungeon.doors.Door;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.utils.Pathfinding;
import nz.uberutils.methods.MyMovement;

public class TeleportHome extends Strategy
{

    public TeleportHome(DungeonMain parent) {
        super(parent);
    }


    public int execute() {
        if (MyPlayer.get().isInCombat() && !MyPlayer.inCombat()) {
            Npc enemy = MyPlayer.currentRoom().getNearestEnemy();
            MyMovement.turnTo(enemy);
            if (enemy != null)
                enemy.interact("Attack");
        }
        if (MyPlayer.get().getAnimation() != -1 ||
            (MyPlayer.get().isInCombat() && MyPlayer.currentRoom().getNearestEnemy() != null) ||
            MyPlayer.currentRoom().getItem() != null)
            return random(400, 600);
        MyPlayer.castSpell(24, "Cast");
        int timeout = 0;
        if (Explore.getStartRoom().contains(MyPlayer.location()))
            parent.teleportFailSafe++;
        else
            parent.teleportFailSafe = 0;
        while (MyPlayer.get().getAnimation() != 4852 && ++timeout <= 30 && MyPlayer.currentRoom().getItem() == null) {
            sleep(100);
        }
        return random(400, 600);
    }


    public boolean isValid() {        //TODO change so it only waits for combat if enemies in room
        Pathfinding pf = new Pathfinding(parent);
        Room[] pathToHome = pf.findPath(MyPlayer.currentRoom(), Explore.getStartRoom());
        Room[] pathFromHome = null;
        Room[] pathFromCur = null;
        if (getDoor() != null) {
            pathFromHome = pf.findPath(Explore.getStartRoom(), getDoor().getParent());
            pathFromCur = pf.findPath(MyPlayer.currentRoom(), getDoor().getParent());
        }
        if (MyPlayer.currentRoom().getNearestDoor() != null)
            return false;
        if (pathToHome != null && pathToHome.length <= 3)
            return false;
        if (pathFromCur != null && pathFromHome != null && pathFromCur.length <= (pathFromHome.length + 1))
            return true;
        return false;
    }


    public void reset() {

    }

    public String getStatus() {
        return (MyPlayer.inCombat()) ? "Killing enemies" : "Teleporting to home room";
    }

    private Door getDoor() {
        for (Door door : Explore.getDoors()) {
            if (door.canOpen() && !door.isOpen())
                return door;
        }
        return null;
    }
}
