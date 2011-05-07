package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.wrappers.GameObject;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 7:11 PM
 * Package: nz.artedungeon.puzzles;
 */
public class FremennikCamp extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Fremennik Camp";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.FREMMY_CRATES) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.FREMMY_CRATES).getLocation());
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Fremennik Camp";
    }

    @Override
    public int loop() {
        if (!Util.isMembers())
            canSolve = false;
        Room cur = MyPlayer.currentRoom();
        GameObject bars = cur.getNearestObject(GameConstants.BARCRATE_CAMP);
        GameObject logs = cur.getNearestObject(GameConstants.LOGCRATE_CAMP);
        GameObject fish = cur.getNearestObject(GameConstants.FISHCRATE_CAMP);
        GameObject toDo = (bars != null) ? bars : (logs != null) ? logs : (fish != null) ? fish : null;
        if (toDo != null) {
            MyMovement.turnTo(toDo);
            if (toDo.click())
                for (int i = 0; i <= 15 && Objects.getNearest(toDo.getId()) != null; i++) {
                    if(MyPlayer.isMoving())
                        i = 0;
                    Task.sleep(100);
                }
        }
        else
            solved = true;
        return 1000;
    }
}
