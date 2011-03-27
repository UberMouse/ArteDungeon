package nz.uberdungeon.strategies;

import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.Strategy;
import nz.uberdungeon.dungeon.Explore;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.utils.util;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/17/11
 * Time: 11:48 PM
 * Package: nz.uberdungeon.strategies;
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
//            if ((Boolean) util.callMethod(boss, "isValid")) {
//                util.callMethod(boss, "startupMessage");
//                if(MyPlayer.inCombat()) {
//
//                }
//                status = (String) util.callMethod(boss, "getStatus");
//                return (Integer) util.callMethod(boss, "loop");
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
