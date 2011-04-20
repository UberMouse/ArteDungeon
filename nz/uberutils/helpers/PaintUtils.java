package nz.uberutils.helpers;

import com.rsbuddy.client.RSObject;
import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/28/11
 * Time: 8:52 PM
 * Package: nz.uberutils.helpers;
 */
public class PaintUtils
{
    /**
     * To be used to draw a certain model on screen
     *
     * @param g Graphics
     * @param o The RSObject you want to 'paint'
     * @param c The Color of the fill By Kimbers
     */
    public static void drawModel(Graphics g, GameObject o, Color c, String s,
                          Color tc) {
        Polygon[] model = o.getModel().getTriangles();
        Point point = Calculations.tileToScreen(o.getLocation());
        for (Polygon p : model) {
            g.setColor(c);
            g.fillPolygon(p);
            g.setColor(c.darker());
            g.drawPolygon(p);
        }

        g.setColor(tc);
        g.drawString(s, point.x - 75, point.y - 35);
    }

    /**
     * To be used to draw a certain model on screen
     *
     * @param g Graphics
     * @param o The RSNPC you want to 'paint'
     * @param c The Color of the fill By Kimbers
     */
    public static void drawModel(Graphics g, Npc o, Color c, String s, Color tc) {
        Polygon[] model = o.getModel().getTriangles();
        Point point = Calculations.tileToScreen(o.getLocation());
        for (Polygon p : model) {
            g.setColor(c);
            g.fillPolygon(p);
            g.setColor(c.darker());
            g.drawPolygon(p);
        }

        g.setColor(tc);
        g.drawString(s, point.x - 75, point.y - 35);
    }

    /**
     * Draws a tile with the passed color on the passed instance of
     * <code>Graphics</code>.
     *
     * @param render       The instance of <code>Graphics</code> you want to draw on.
     * @param tile         The instance of the tile you want to draw.
     * @param color        The color you want the drawn tile to be.
     * @param outlineColor The color you want the outline of the tile to be
     * @author Gnarly
     */
    public static void drawTile(Graphics render, Tile tile, Color color,
                         Color outlineColor) {
        Point southwest = Calculations.tileToScreen(tile, 0, 0, 0);
        Point southeast = Calculations.tileToScreen(new Tile(tile.getX() + 1, tile.getY()), 0, 0, 0);
        Point northwest = Calculations.tileToScreen(new Tile(tile.getX(), tile.getY() + 1), 0, 0, 0);
        Point northeast = Calculations.tileToScreen(new Tile(tile.getX() + 1, tile.getY() + 1), 0, 0, 0);

        if (Calculations.isPointOnScreen(southwest) &&
            Calculations.isPointOnScreen(southeast) &&
            Calculations.isPointOnScreen(northwest) &&
            Calculations.isPointOnScreen(northeast)) {
            render.setColor(outlineColor);
            render.drawPolygon(new int[]{(int) northwest.getX(),
                                         (int) northeast.getX(),
                                         (int) southeast.getX(),
                                         (int) southwest.getX()},
                               new int[]{(int) northwest.getY(),
                                         (int) northeast.getY(),
                                         (int) southeast.getY(),
                                         (int) southwest.getY()},
                               4);
            render.setColor(color);
            render.fillPolygon(new int[]{(int) northwest.getX(),
                                         (int) northeast.getX(),
                                         (int) southeast.getX(),
                                         (int) southwest.getX()},
                               new int[]{(int) northwest.getY(),
                                         (int) northeast.getY(),
                                         (int) southeast.getY(),
                                         (int) southwest.getY()},
                               4);
        }
    }
}
