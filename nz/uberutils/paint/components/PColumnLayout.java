package nz.uberutils.paint.components;

import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/3/11
 * Time: 1:11 AM
 * Package: nz.uberutils.paint.components;
 */
public class PColumnLayout extends PComponent
{
    private final int offsetIncrement;
    private final String[] leftColumn;
    private final String[] rightColumn;
    private final ColorScheme scheme;
    private Font font;

    public PColumnLayout(int x, int y, int offsetIncrement, String[] leftColumn, String[] rightColumn, Font font) {
        this(x, y, offsetIncrement, leftColumn, rightColumn, font, null);
    }

    public PColumnLayout(int x, int y, int offsetIncrement, String[] leftColumn, String[] rightColumn) {
        this(x, y, offsetIncrement, leftColumn, rightColumn, null, null);
    }

    public PColumnLayout(int x, int y, String[] leftColumn, String[] rightColumn) {
        this(x, y, 5, leftColumn, rightColumn, null, null);
    }

    public PColumnLayout(int x, int y, String[] leftColumn, String[] rightColumn, Font font) {
        this(x, y, 5, leftColumn, rightColumn, font);
    }

    public PColumnLayout(int x,
                         int y,
                         int offsetIncrement,
                         String[] leftColumn,
                         String[] rightColumn,
                         Font font,
                         ColorScheme scheme) {
        this.x = x;
        this.y = y;
        this.offsetIncrement = offsetIncrement;
        this.leftColumn = leftColumn;
        this.rightColumn = rightColumn;
        this.font = font;
        this.scheme = scheme;
    }

    public PColumnLayout(int x,
                         int y,
                         int offsetIncrement,
                         String[] leftColumn,
                         String[] rightColumn,
                         ColorScheme scheme) {
        this(x, y, offsetIncrement, leftColumn, rightColumn, null, scheme);
    }

    public PColumnLayout(int x, int y, String[] leftColumn, String[] rightColumn, ColorScheme scheme) {
        this(x, y, 5, leftColumn, rightColumn, null, scheme);
    }

    public PColumnLayout(int x, int y, String[] leftColumn, String[] rightColumn, Font font, ColorScheme scheme) {
        this(x, y, 5, leftColumn, rightColumn, font, scheme);
    }

    @Override
    public void repaint(Graphics2D g) {
        if (font == null)
            font = g.getFont();
        if (scheme != null)
            g.setColor(scheme.text);
        g.setFont(font);
        String largestString = "";
        int offset = 0;
        for (String str : leftColumn) {
            if(str == null)
                continue;
            if (str.length() > largestString.length())
                largestString = str;
            g.drawString(str, x, y + offset);
            offset += g.getFont().getStringBounds(str, g.getFontRenderContext()).getHeight();
        }
        offset = 0;
        Rectangle2D bounds = g.getFont().getStringBounds(largestString, g.getFontRenderContext());
        for (String str : rightColumn) {
            if (str != null) {
                g.drawString(str, (int) (x + bounds.getWidth() + offsetIncrement), y + offset);
                offset += g.getFont().getStringBounds(str, g.getFontRenderContext()).getHeight();
            }
        }
    }

    public static enum ColorScheme
    {
        GRAPHITE(new Color(200, 200, 200)),
        LIME(new Color(255, 153, 51)),
        HOT_PINK(new Color(110, 255, 110)),
        WHITE(new Color(51, 51, 51));
        private Color text;

        ColorScheme(Color text) {
            this.text = text;
        }
    }
}
