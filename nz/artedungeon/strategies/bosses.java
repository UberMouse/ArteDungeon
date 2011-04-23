package nz.artedungeon.strategies;

import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.MyPlayer;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/17/11
 * Time: 11:48 PM
 * Package: nz.artedungeon.strategies;
 */
public class bosses extends Strategy
{
    private String status;

    /**
     * Instantiates a new strategy.
     *
     * @param parent instance of main script
     */
    public bosses(DungeonMain parent) {
        super(parent);
    }

    @Override
    public int execute() {
//        for (Object boss : DungeonMain.bosses) {
//            if ((Boolean) Util.callMethod(boss, "isValid")) {
//                Util.callMethod(boss, "startupMessage");
//                if(MyPlayer.inCombat()) {
//
//                }
//                status = (String) Util.callMethod(boss, "getStatus");
//                return (Integer) Util.callMethod(boss, "loop");
//            }
//        }
        return random(400, 600);
    }

    @Override
    public boolean isValid() {
        return Explore.getBossRoom() != null &&
               Explore.getBossRoom().contains(MyPlayer.location()) &&
               MyPlayer.currentRoom().equals(Explore.getBossRoom());
    }

    @Override
    public void reset() {
    }

    @Override
    public String getStatus() {
        return status;
    }
}
