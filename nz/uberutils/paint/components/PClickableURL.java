package nz.uberutils.paint.components;

import nz.uberutils.helpers.Utils;

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
 * Package: nz.artedungeon.paint;
 */
public class PClickableURL extends PButton
{
    private String url;
    private static final Font italicArial = new Font("Arial", Font.ITALIC, 13);
    private static final Color white = new Color(255, 255, 255);
    private static final Color grey = new Color(150, 150, 150);

    /**
     * Creates a new PClickableURL
     *
     * @param url       the url to open on click
     * @param shortName the name to display the URL as
     * @param x         the x co-ord to draw at
     * @param y         the y co-ord to draw at
     */
    public PClickableURL(String url, String shortName, int x, int y) {
        super(x, y, shortName);
        this.url = url;
    }

    /**
     * Creates a new PClickableURL
     *
     * @param url the url to open on click and display name
     * @param x   the x co-ord to draw at
     * @param y   the y co-ord to draw at
     */
    public PClickableURL(String url, int x, int y) {
        super(x, y, url);
        this.url = url;
    }

    public void repaint(Graphics2D g) {
        if (hoverArea == null) {
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
            hoverArea = new Rectangle(x,
                                      y - (int) bounds.getHeight(),
                                      (int) bounds.getWidth(),
                                      (int) bounds.getHeight());
        }
        AttributedString as = new AttributedString(text);
        as.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 0, text.length() - 1);
        as.addAttribute(TextAttribute.FONT, italicArial);
        Rectangle2D bounds = italicArial.getStringBounds(text, g.getFontRenderContext());
        hoverArea = new Rectangle(x, y - (int) bounds.getHeight(), (int) bounds.getWidth(), (int) bounds.getHeight());
        if (hoverArea.contains(mouse)) {
            g.setColor(grey);
        }
        else
            g.setColor(white);
        new TextLayout(as.getIterator(), g.getFontRenderContext()).draw(g, (float) x, (float) y);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (hoverArea != null && hoverArea.contains(mouseEvent.getPoint())) {
            Utils.openURL(url);
        }
    }
}