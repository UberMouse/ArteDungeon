package nz.uberutils.paint.components;

import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/24/11
 * Time: 7:01 PM
 * Package: nz.uberutils.paint.components;
 */
public class PNumInput extends PComponent
{
    private final int FLOOR;
    private final int ROOF;
    private int curIndex;
    private final int START_INDEX;
    private final ColorScheme SCHEME;
    private Rectangle2D up = null;
    private Rectangle2D down = null;
    private Rectangle2D input = null;

    public PNumInput(int x, int y, int floor, int roof, int startIndex, ColorScheme scheme) {
        this.x = x;
        this.y = y;
        this.FLOOR = floor;
        this.ROOF = roof;
        this.START_INDEX = startIndex;
        this.SCHEME = scheme;
    }

    public PNumInput(int x, int y, int roof, int startIndex, ColorScheme scheme) {
        this(x, y, 0, roof, startIndex, scheme);
    }

    public PNumInput(int x, int y, int roof, ColorScheme scheme) {
        this(x, y, 0, roof, 0, scheme);
    }

    public PNumInput(int x, int y, int roof) {
        this(x, y, 0, roof, 0, ColorScheme.GRAPHITE);
    }

    @Override
    public void repaint(Graphics2D g) {
        if (up == null || down == null || input == null) {

        }
    }

    public static enum ColorScheme
    {
        GRAPHITE(new Color(55, 55, 55, 240), new Color(15, 15, 15, 240), new Color(200, 200, 200)),
        LIME(new Color(0, 187, 37, 240), new Color(0, 128, 28), new Color(255, 153, 51)),
        HOT_PINK(new Color(255, 102, 204, 240), new Color(255, 0, 170), new Color(110, 255, 110)),
        WHITE(new Color(200, 200, 200, 240), new Color(180, 180, 180, 240), new Color(51, 51, 51));

        ColorScheme(Color mainColor1, Color mainColor2, Color textColor) {
            main1 = mainColor1;
            main2 = mainColor2;
            text = textColor;
        }

        private Color main1, main2, text;
    }
}
