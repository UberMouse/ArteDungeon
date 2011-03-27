package nz.uberdungeon.puzzles;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;
import nz.uberdungeon.common.Plugin;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.utils.MyCamera;
import nz.uberdungeon.utils.util;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/23/11
 * Time: 9:01 PM
 * Package: nz.uberdungeon.puzzles;
 */
public class FlipTiles extends Plugin
{
    static final int YELLOW = 49641, GREEN = 49638;

    boolean[][] grid;
    boolean[] imbueTop = null;

    public boolean isValid() {
        grid = getGrid();
        return grid != null && !isSolved(grid);
    }

    public String getStatus() {
        return "Solving: Flip Tiles";
    }

    public String getAuthor() {
        return "Zippy[Taw]";
    }

    public String getName() {
        return "Flip Tiles";
    }

    @Override
    public int loop() {
        if (MyPlayer.needToEat())
            MyPlayer.eat();
        if (!MyPlayer.isMoving() && MyPlayer.isInteracting() && MyPlayer.get().getAnimation() == -1) {
            grid = getGrid();
            Tile corner = getCorner();
            if (imbueTop != null) {
                for (int i = 0; i < 5; i++)
                    if (imbueTop[i]) {
                        GameObject panel = getPanelAt(corner.getX() + i, corner.getY());
                        if (panel.isOnScreen()) {
                            panel.interact("Imbue");
                            waitForFlip();
                            imbueTop[i] = false;
                        }
                        else {
                            MyCamera.turnTo(panel);
                            return 1000;
                        }
                        return 500;
                    }
                imbueTop = null;
            }
            else {
                for (int r = 0; r < 4; r++) {
                    for (int c = 0; c < 5; c++) {
                        if (grid[r][c]) {
                            GameObject panel = getPanelAt(corner.getX() + c, corner.getY() + r + 1);
                            if (panel.isOnScreen()) {
                                panel.interact("Imbue");
                                waitForFlip();
                            }
                            else {
                                MyCamera.turnTo(panel);
                                return 1000;
                            }
                            return 500;
                        }
                    }
                }
                imbueTop = toBoolArr(getCounterpart(toInt(grid[4])));
                log("imbue top");
            }
        }
        return Random.nextInt(400, 700);
    }

    public void waitForFlip() {
        for (int i = 0; i < 100 && MyPlayer.get().getAnimation() == -1; i++) {
            sleep(20);
        }
        for (int i = 0; i < 100 && MyPlayer.get().getAnimation() != -1; i++) {
            sleep(10);
        }
    }

    public void onRepaint(Graphics g) {
        if (grid != null) {
            Tile corner = getCorner();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    highlight(g, new Tile(corner.getX() + j, corner.getY() + i),
                              (grid[i][j] ? new Color(255, 0, 0, 100) : new Color(0, 255, 0, 100)));
                }
            }
        }
    }

    private void highlight(Graphics g, Tile t, Color fill) {
        Point pn = Calculations.tileToScreen(t, 0, 0, 0);
        Point px = Calculations.tileToScreen(t, 1, 0, 0);
        Point py = Calculations.tileToScreen(t, 0, 1, 0);
        Point pxy = Calculations.tileToScreen(t, 1, 1, 0);
        if (py.x != -1 && pxy.x != -1 && px.x != -1 && pn.x != -1) {
            g.setColor(fill);
            g.fillPolygon(new int[]{py.x, pxy.x, px.x, pn.x},
                          new int[]{py.y, pxy.y, px.y, pn.y}, 4);
        }
    }

    public int toInt(boolean[] row) {
        int ret = 0;
        int[] arr = {0x1, 0x2, 0x4, 0x8, 0x10};
        for (int i = 0; i < row.length; i++)
            if (row[i]) ret += arr[i];
        return ret;
    }

    public int getCounterpart(int i) {
        if (i == bin("10001"))
            return bin("11000");
        if (i == bin("01010"))
            return bin("10010");
        if (i == bin("11100"))
            return bin("01000");
        if (i == bin("00111"))
            return bin("00010");
        if (i == bin("10110"))
            return bin("00001");
        if (i == bin("01101"))
            return bin("10000");
        if (i == bin("11011"))
            return bin("00100");
        return 0;
    }

    public int bin(String s) {
        return Integer.parseInt(s, 2);
    }

    public boolean[] toBoolArr(int num) {
        boolean[] b = new boolean[5];
        int[] arr = {0x1, 0x2, 0x4, 0x8, 0x10};
        for (int i = 0; i < 5; i++) {
            b[i] = ((num & arr[i]) != 0);
        }
        return b;
    }

    public GameObject getPanelAt(int x, int y) {
        return getPanelAt(new Tile(x, y));
    }

    public GameObject getPanelAt(Tile t) {
        for (GameObject o : Objects.getAllAt(t))
            if (o.getId() == YELLOW || o.getId() == GREEN)
                return o;
        return null;
    }

    public Tile getCorner() {
        int x = 0, y = 0;
        for (GameObject o : Objects.getLoaded(new Filter<GameObject>()
        {
            public boolean accept(GameObject o) {
                return MyPlayer.currentRoom().contains(o) && (o.getId() == YELLOW || o.getId() == GREEN);
            }
        })) {
            if (x == 0 || o.getLocation().getX() < x)
                x = o.getLocation().getX();
            if (y == 0 || o.getLocation().getY() < y)
                y = o.getLocation().getY();
        }
        return new Tile(x, y);
    }

    public boolean[][] getGrid() {
        boolean[][] g = new boolean[5][5];
        GameObject[] panels = Objects.getLoaded(new Filter<GameObject>()
        {
            public boolean accept(GameObject o) {
                return util.tileInRoom(o.getLocation()) && (o.getId() == YELLOW || o.getId() == GREEN);
            }
        });
        if (panels.length == 0) return null;
        Tile corner = getCorner();
        int x = corner.getX(), y = corner.getY();
        for (GameObject o : panels) {
            if (o.getLocation().getX() - x < 5 && o.getLocation().getY() - y < 5
                && o.getLocation().getX() - x >= 0 && o.getLocation().getY() - y >= 0)
                g[o.getLocation().getY() - y][o.getLocation().getX() - x] = (o.getId() == GREEN);
        }
        return g;
    }

    public boolean isSolved(boolean[][] grid) {
        boolean fill = grid[0][0];
        for (boolean[] bool : grid)
            for (boolean b : bool)
                if (b != fill)
                    return false;
        return true;
    }
}
