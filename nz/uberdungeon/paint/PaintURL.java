package nz.uberdungeon.paint;

import nz.uberdungeon.utils.util;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.text.AttributedString;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/25/11
 * Time: 8:54 PM
 * Package: nz.uberdungeon.paint;
 */
public class PaintURL extends PaintComponent
{
    private String url;
    private String shortName = null;
    private int x;
    private int y;
    private Point mouse = new Point(-1, -1);
    private Rectangle2D hoverArea = new Rectangle(-1, -1);
    private static final Font font1 = new Font("Arial", Font.ITALIC, 13);
    private static final Color white = new Color(255, 255, 255);
    private static final Color grey = new Color(150, 150, 150);

    /**
     * Creates a new PaintURL
     *
     * @param url       the url to open on click
     * @param shortName the name to display the URL as
     * @param x         the x co-ord to draw at
     * @param y         the y co-ord to draw at
     */
    public PaintURL(String url, String shortName, int x, int y) {
        this.url = url;
        this.shortName = shortName;
        this.x = x;
        this.y = y;
    }

    /**
     * Creates a new PaintURL
     *
     * @param url the url to open on click and display name
     * @param x   the x co-ord to draw at
     * @param y   the y co-ord to draw at
     */
    public PaintURL(String url, int x, int y) {
        this.url = url;
        this.x = x;
        this.y = y;
    }

    public void repaint(Graphics2D g) {
        String name = (shortName != null) ? shortName : url;
        AttributedString as = new AttributedString(name);
        as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, name.length() - 1);
        as.addAttribute(TextAttribute.FONT, font1);
        Rectangle2D bounds = font1.getStringBounds(name, g.getFontRenderContext());
        hoverArea = new Rectangle(x,
                                  y - (int) bounds.getHeight(),
                                  (int) bounds.getWidth(),
                                  (int) bounds.getHeight());
        if (hoverArea.contains(mouse)) {
            g.setColor(grey);
        }
        else
            g.setColor(white);
        new TextLayout(as.getIterator(), g.getFontRenderContext()).draw(g, (float) x, (float) y);
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        mouse = mouseEvent.getPoint();
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (hoverArea.contains(mouseEvent.getPoint())) {
            util.openURL(url);
        }
    }
}