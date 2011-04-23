package nz.artedungeon.puzzles;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.common.PuzzlePlugin;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/23/11
 * Time: 2:16 AM
 * Package: nz.artedungeon.puzzles;
 */
public class Maze extends PuzzlePlugin
{
    private boolean mazeActive;
    private Tile[] barriers;
    private int[] currentPath;
    int MAZE_BARRIER = 49341;

    @Override
    public String getStatus() {
        return "Solving: Maze";
    }

    @Override
    public boolean isValid() {
        return Objects.getNearest(MAZE_BARRIER) != null &&
               Calculations.distanceTo(Objects.getNearest(MAZE_BARRIER)) <= 8;
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Maze";
    }

    public boolean isPossible() {
        return false;
    }

    @Override
    public int loop() {
        //        int[] centerPath = null, edgePath = null;
        //        Tile ORIGIN = new Tile(0, 0);
        //        boolean NS = false;
        //        int mX = 1, mY = 1;
        //        final int[] FINISHED_CHESTS = {49348, 49349, 49350, 54408};
        //        GameObject chest = MyPlayer.currentRoom()
        //                                   .getNearestObject(49345, 49346, 49347, 49348, 49349, 49350, 54407, 54408);
        //        GameObject lever = MyPlayer.currentRoom().getNearestObject(49351, 49352, 49353, 54409);
        //        Tile hT = null;
        //        if (chest == null || lever == null)
        //            return -1;
        //        Tile cTile = chest.getLocation();
        //        Tile sTile = lever.getLocation();
        //        if (cTile.getX() - sTile.getX() < -4) {
        //            mX = 1;
        //            mY = 1;
        //            hT = MyPlayer.currentRoom().getArea().getNearestTile(ORIGIN);
        //        }
        //        if (cTile.getX() - sTile.getX() > 4) {
        //            mX = -1;
        //            mY = -1;
        //            hT = MyPlayer.currentRoom().getArea().getNearestTile(new Tile(20000, 20000));
        //        }
        //        if (cTile.getY() - sTile.getY() < -4) {
        //            mX = -1;
        //            mY = 1;
        //            hT = MyPlayer.currentRoom().getArea().getNearestTile(new Tile(20000, 0));
        //            NS = true;
        //        }
        //        if (cTile.getY() - sTile.getY() > 4) {
        //            mX = 1;
        //            mY = -1;
        //            hT = MyPlayer.currentRoom().getArea().getNearestTile(new Tile(0, 20000));
        //            NS = true;
        //        }
        //        int hX = 0, hY = 0;
        //        if (hT == null)
        //            return -1;
        //        hX = hT.getX();
        //        hY = hT.getY();
        //        Tile[] barriersNS = new Tile[]{new Tile(hX + 14 * mX, hY + 5 * mY),
        //                                       new Tile(hX + 14 * mX, hY + 7 * mY),
        //                                       new Tile(hX + 14 * mX, hY + 12 * mY),
        //                                       new Tile(hX + 13 * mX, hY + 8 * mY),
        //                                       new Tile(hX + 13 * mX, hY + 6 * mY),
        //                                       new Tile(hX + 11 * mX, hY + 2 * mY),
        //                                       new Tile(hX + 11 * mX, hY + 7 * mY),
        //                                       new Tile(hX + 10 * mX, hY + 3 * mY),
        //                                       new Tile(hX + 10 * mX, hY + 8 * mY),
        //                                       new Tile(hX + 10 * mX, hY + 13 * mY),
        //                                       new Tile(hX + 8 * mX, hY * mY),
        //                                       new Tile(hX + 8 * mX, hY + 4 * mY),
        //                                       new Tile(hX + 8 * mX, hY + 10 * mY),
        //                                       new Tile(hX + 8 * mX, hY + 14 * mY),
        //                                       new Tile(hX + 7 * mX, hY + 5 * mY),
        //                                       new Tile(hX + 6 * mX, hY + 3 * mY),
        //                                       new Tile(hX + 6 * mX, hY + 11 * mY),
        //                                       new Tile(hX + 5 * mX, hY + 2 * mY),
        //                                       new Tile(hX + 5 * mX, hY + 7 * mY),
        //                                       new Tile(hX + 5 * mX, hY + 12 * mY),
        //                                       new Tile(hX + 4 * mX, hY + 6 * mY),
        //                                       new Tile(hX + 4 * mX, hY + 9 * mY),
        //                                       new Tile(hX + 4 * mX, hY + 13 * mY),
        //                                       new Tile(hX + 4 * mX, hY + 14 * mY),
        //                                       new Tile(hX + 3 * mX, hY + 9 * mY),
        //                                       new Tile(hX + 2 * mX, hY + 7 * mY),
        //                                       new Tile(hX * mX, hY + 3 * mY),
        //                                       new Tile(hX * mX, hY + 9 * mY),
        //                                       new Tile(hX * mX, hY + 11 * mY)};
        //        Tile[] barriersEW = new Tile[]{new Tile(hX + 5 * mX, hY + 14 * mY),
        //                                       new Tile(hX + 7 * mX, hY + 14 * mY),
        //                                       new Tile(hX + 12 * mX, hY + 14 * mY),
        //                                       new Tile(hX + 8 * mX, hY + 13 * mY),
        //                                       new Tile(hX + 6 * mX, hY + 12 * mY),
        //                                       new Tile(hX + 2 * mX, hY + 11 * mY),
        //                                       new Tile(hX + 7 * mX, hY + 11 * mY),
        //                                       new Tile(hX + 3 * mX, hY + 10 * mY),
        //                                       new Tile(hX + 8 * mX, hY + 10 * mY),
        //                                       new Tile(hX + 13 * mX, hY + 10 * mY),
        //                                       new Tile(hX * mX, hY + 8 * mY),
        //                                       new Tile(hX + 4 * mX, hY + 8 * mY),
        //                                       new Tile(hX + 10 * mX, hY + 8 * mY),
        //                                       new Tile(hX + 14 * mX, hY + 8 * mY),
        //                                       new Tile(hX + 5 * mX, hY + 7 * mY),
        //                                       new Tile(hX + 3 * mX, hY + 6 * mY),
        //                                       new Tile(hX + 11 * mX, hY + 6 * mY),
        //                                       new Tile(hX + 2 * mX, hY + 5 * mY),
        //                                       new Tile(hX + 7 * mX, hY + 5 * mY),
        //                                       new Tile(hX + 12 * mX, hY + 5 * mY),
        //                                       new Tile(hX + 6 * mX, hY + 4 * mY),
        //                                       new Tile(hX + 9 * mX, hY + 4 * mY),
        //                                       new Tile(hX + 13 * mX, hY + 4 * mY),
        //                                       new Tile(hX + 14 * mX, hY + 4 * mY),
        //                                       new Tile(hX + 9 * mX, hY + 3 * mY),
        //                                       new Tile(hX + 7 * mX, hY + 2 * mY),
        //                                       new Tile(hX + 3 * mX, hY * mY),
        //                                       new Tile(hX + 9 * mX, hY * mY),
        //                                       new Tile(hX + 11 * mX, hY * mY)};
        //        GameObject test1, test2, test3, test4, test5;
        //        if (NS) {
        //            barriers = barriersNS;
        //            test1 = Objects.getTopAt(new Tile(hX + 4 * mX, hY + 5 * mY));
        //            test2 = Objects.getTopAt(new Tile(hX + 9 * mX, hY + 2 * mY));
        //            test3 = Objects.getTopAt(new Tile(hX + 5 * mX, hY + 2 * mY));
        //            test4 = Objects.getTopAt(new Tile(hX + 3 * mX, hY + 5 * mY));
        //            test5 = Objects.getTopAt(new Tile(hX + 3 * mX, hY * mY));
        //        }
        //        else {
        //            barriers = barriersEW;
        //            test1 = Objects.getTopAt(new Tile(hX + 5 * mX, hY + 4 * mY));
        //            test2 = Objects.getTopAt(new Tile(hX + 2 * mX, hY + 9 * mY));
        //            test3 = Objects.getTopAt(new Tile(hX + 2 * mX, hY + 5 * mY));
        //            test4 = Objects.getTopAt(new Tile(hX + 5 * mX, hY + 3 * mY));
        //            test5 = Objects.getTopAt(new Tile(hX + 1 * mX, hY + 3 * mY));
        //        }
        //        final int[] OCCULT_BLOCKS = {54412, 54413, 54414, 54415, 54416};
        //        if (test1 != null && (test1.getId() > 49359 && test1.getId() < 49375) ||
        //            Util.arrayContains(OCCULT_BLOCKS, test1.getId())) {
        //            centerPath = new int[]{14, 29, 27, 26, 16, 7, 15};
        //            edgePath = new int[]{15, 7, 16, 26, 27, 0, 29, 10, 4, 1, 0};
        //        }
        //        else if (test3 != null && (test3.getId() > 49359 && test3.getId() < 49375) ||
        //                 Util.arrayContains(OCCULT_BLOCKS, test3.getId())) {
        //            centerPath = new int[]{14, 26, 16, 8, 4, 10, 20, 7, 13};
        //            edgePath = new int[]{15, 7, 20, 10, 1, 0, 0, 11, 4, 8, 16, 26, 29, 0};
        //        }
        //        else if (test4 != null && (test4.getId() > 49359 && test4.getId() < 49375) ||
        //                 Util.arrayContains(OCCULT_BLOCKS, test4.getId())) {
        //            centerPath = new int[]{14, 24, 29, 26, 10, 4, 8, 7, 15};
        //            edgePath = new int[]{13, 7, 8, 4, 1, 0, 1, 10, 26, 27, 0, 0};
        //        }
        //        else if (test2 != null && (test2.getId() > 49359 && test2.getId() < 49375) ||
        //                 Util.arrayContains(OCCULT_BLOCKS, test2.getId())) {
        //            centerPath = new int[]{14, 1, 11, 27, 29, 26, 16, 22, 13};
        //            edgePath = new int[]{15, 22, 16, 26, 29, 0, 27, 11, 0, 0};
        //        }
        //        else if (test5 != null && (test5.getId() > 49359 && test5.getId() < 49375) ||
        //                 Util.arrayContains(OCCULT_BLOCKS, test5.getId())) {
        //            centerPath = new int[]{14, 29, 27, 4, 8, 22, 15};
        //            edgePath = new int[]{15, 22, 8, 4, 1, 0, 0, 11, 27, 0};
        //        }
        //        else
        //            return -1;
        //        while (MyPlayer.currentRoom().getNearestObject(FINISHED_CHESTS) == null && !mazeActive) {
        //            Objects.getTopAt(sTile).interact("Pull");
        //        }
        //        mazeActive = false;
        //        while (MyPlayer.currentRoom().getNearestObject(FINISHED_CHESTS) == null &&
        //               MyPlayer.currentRoom().contains(MyPlayer.location())) {
        //            if (Calculations.distanceTo(sTile) < 4) {
        //                currentPath = centerPath;
        //            }
        //            else if (Calculations.distanceTo(cTile) < 3) {
        //                currentPath = edgePath;
        //            }
        //            else
        //                return -1;
        //            for (int I = 0; I < currentPath.length; I++) {
        //                if (currentPath[I] == 0) {
        //                    getBestDoor();
        //                    if (nearDoor == null)
        //                        return -1;
        //                    GameObject door = Objects.getTopAt(nearDoor);
        //                    safeTile = door == null ? nearDoor : door.getArea().getNearestTile(myLoc());
        //                    walkToMap(safeTile, 1);
        //                    waitToStart(false);
        //                    waitToStop(false);
        //                    if (isEdgeTile(myLoc()) && Calculations.distanceTo(safeTile) < 3) {
        //                        puzzleSolved = true;
        //                        break;
        //                    }
        //                }
        //                else {
        //                    safeTile = barriers[currentPath[I] - 1];
        //                    if (currentPath == edgePath)
        //                        getBestDoor();
        //                    Timer barrierTimer = new Timer(random(45000, 90000));
        //                    o:
        //                    while (player().getAnimation() != 9516 || Calculations.distanceTo(safeTile) > 2) {
        //                        if (!barrierTimer.isRunning()) {
        //                            failReason = "We got stuck";
        //                            return -1;
        //                        }
        //                        if (isDead()) {
        //                            teleBack();
        //                        }
        //                        else if (failSafe()) {
        //                            return 2;
        //                        }
        //                        if (player().getAnimation() == -1 && doObjAction(Objects.getTopAt(safeTile), "Go-through")) {
        //                            while (player().isMoving() && player().getAnimation() == -1) {
        //                                sleep(100, 200);
        //                            }
        //                            for (int i = 0; i < 8; i++) {
        //                                if (player().getAnimation() == 9516 && Calculations.distanceTo(safeTile) < 3)
        //                                    break o;
        //                                sleep(200, 300);
        //                            }
        //                        }
        //                        else
        //                            while (!Calculations.tileOnScreen(safeTile) &&
        //                                   player().isMoving() &&
        //                                   player().getAnimation() == -1) {
        //                                sleep(200, 300);
        //                            }
        //                    }
        //                    waitToStop(true);
        //                    while (player().getAnimation() == 9516) {
        //                        sleep(300, 400);
        //                    }
        //                }
        //            }
        //            GameObject closedChest = MyPlayer.currentRoom().getNearestObject(49345, 49346, 49347, 54407);
        //            if (closedChest != null && Calculations.distanceTo(closedChest) < 3) {
        //                while (MyPlayer.currentRoom().getNearestObject(49345, 49346, 49347, 54407) != null) {
        //                    if (isDead()) {
        //                        teleBack();
        //                    }
        //                    else if (failSafe()) {
        //                        return -1;
        //                    }
        //                    doObjAction(MyPlayer.currentRoom().getNearestObject(49345, 49346, 49347, 54407), "Open");
        //                    waitToStop(false);
        //                }
        //                getKey();
        //                if (nearItem != null) {
        //                    int keyID = nearItem.getItem().getId();
        //                    for (int c = 0; c < 10; c++) {
        //                        if (inventory.contains(keyID))
        //                            break;
        //                        pickUpItem(nearItem);
        //                        sleep(400, 600);
        //                    }
        //                }
        //                waitToStop(false);
        //                getBestDoor();
        //            }
        //        }
        //        puzzleRepeat = true;
        //        return 1;
        return 1;
    }

    public void messageReceived(MessageEvent e) {
        if (e.getMessage().contains("both the chest in"))
            mazeActive = true;
    }
}
