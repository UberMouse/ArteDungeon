package nz.artedungeon.strategies;

import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.utils.Pathfinding;

public class WalkToBoss extends Strategy
{

    public WalkToBoss(DungeonMain parent) {
        super(parent);
    }


    public int execute() {
        Pathfinding bfs = new Pathfinding(parent);
        Room[] path = bfs.findPath(MyPlayer.currentRoom(), Explore.getBossRoom());
        if (path != null) {
            bfs.walkPath(path);
        }
        else {
            Explore.setExit(true);
            Explore.setBossRoom(null);
            parent.timesAborted++;
        }
        return random(400, 600);
    }


    public boolean isValid() {
        return (Explore.getBossRoom() != null && !Explore.getBossRoom().contains(MyPlayer.location()));
    }


    public void reset() {

    }

    public String getStatus() {
        return "Walking to boss";
    }

}
