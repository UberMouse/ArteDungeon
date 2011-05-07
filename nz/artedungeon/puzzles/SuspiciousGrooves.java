package nz.artedungeon.puzzles;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.task.Task;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.MyPlayer;
import nz.uberutils.methods.MyMovement;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/3/11
 * Time: 2:58 PM
 * Package: nz.artedungeon.puzzles;
 */
public class SuspiciousGrooves extends PuzzlePlugin
{
    private static final Tile[] path = new Tile[3];
    private static final ArrayList<Tile> badGrooves = new ArrayList<Tile>();
    private static Tile lastGroove;
    private static int[] curRowIds = GameConstants.SUSPICOUS_GROOVES_ROW_1;
    private static int curRow = 0;

    @Override
    public boolean isValid() {
        return Objects.getNearest(GameConstants.SUSPICOUS_GROOVES_ROW_1) != null &&
               Util.tileInRoom(Objects.getNearest(GameConstants.SUSPICOUS_GROOVES_ROW_1).getLocation());
    }

    @Override
    public String getStatus() {
        return "Solving: Suspicious Grooves";
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Suspicious Grooves";
    }

    public boolean isPossible() {
        return false;
    }

    @Override
    public int loop() {
        GameObject groove = Objects.getNearest(new Filter<GameObject>()
        {

            public boolean accept(GameObject gameObject) {
                return Util.arrayContains(curRowIds, gameObject.getId()) &&
                       !badGrooves.contains(gameObject.getLocation());
            }
        });
        MyMovement.turnTo(groove);
        if (groove.interact("Step-onto")) {
            for (int i = 0; i <= 15 && !MyPlayer.location().equals(groove.getLocation()); i++)
                Task.sleep(100);
            for (int i = 0; i <= 10 && MyPlayer.location().equals(groove.getLocation()); i++)
                Task.sleep(100);
            if (MyPlayer.location().equals(groove.getLocation())) {
                path[curRow] = groove.getLocation();
                curRow++;
                switch (curRow) {
                    case 1:
                        curRowIds = GameConstants.SUSPICOUS_GROOVES_ROW_2;
                        break;
                    case 2:
                        curRowIds = GameConstants.SUSPICOUS_GROOVES_ROW_3;
                        break;
                    default:
                        solved = true;
                        break;
                }
            }
        }
        return 1000;
    }

    public void messageReceived(MessageEvent e) {
        if (e.isAutomated()) {
            if (e.getMessage().contains("trap gets"))
                badGrooves.add(lastGroove);
        }
    }
}
