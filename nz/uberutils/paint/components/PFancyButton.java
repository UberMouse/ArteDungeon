package nz.uberutils.paint.components;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/2/11
 * Time: 10:54 AM
 * Package: nz.uberutils.paint.components;
 */
public class PFancyButton extends PButton
{
    private ColorScheme colorScheme;
    private Font font = null;
    private FontMetrics metrics;
    protected boolean hovered = false;

    public PFancyButton(int x, int y, String text) {
        this(x, y, text, PFancyButton.ColorScheme.GRAPHITE);
    }

    public PFancyButton(int x, int y, int width, int height, String text, ColorScheme colorScheme, Font font) {
        super(x, y, text);
        this.width = width;
        this.height = height;
        this.font = font;
        this.colorScheme = colorScheme;
    }

    public PFancyButton(int x, int y, int width, int height, String text, ColorScheme colorScheme) {
        this(x, y, width, height, text, colorScheme, null);
    }

    public PFancyButton(int x, int y, String text, ColorScheme colorScheme) {
        this(x, y, text, colorScheme, null);
    }

    public PFancyButton(int x, int y, String text, ColorScheme colorScheme, Font font) {
        this(x, y, -1, -1, text, colorScheme, null);
    }

    @Override
    public void repaint(Graphics2D g) {
        if (hoverArea == null) {
            hoverArea = getBounds(g, text);
        }
        if (font == null)
            font = g.getFont();
        if (height == -1 || width == -1) {
            Rectangle2D bounds = font.getStringBounds(text, g.getFontRenderContext());
            height = (height == -1) ? (int) bounds.getHeight() + 5 : height;
            width = (width == -1) ? (int) bounds.getWidth() + 5 : width;
        }
        Paint p = g.getPaint();
        metrics = g.getFontMetrics(font);
        if (!hovered)
            g.setPaint(new GradientPaint(x, y, colorScheme.main1, x, y + height, colorScheme.main2));
        else
            g.setPaint(new GradientPaint(x, y, colorScheme.main2, x, y + height, colorScheme.main1));
        g.fillRect(x, y, width, height);

        g.setPaint(p);
        g.setColor(colorScheme.main2);
        g.drawRect(x, y, width - 1, height);

        g.setColor(colorScheme.text);
        g.setFont(font);
        g.drawString(text,
                     (x + (width / 2) - (metrics.stringWidth(text) / 2)),
                     y + (height / 2) + (int) (metrics.getMaxAscent() / (font.getSize() / 4.5)));

        g.setColor(new Color(255, 255, 255, 65));
        g.fillRect(x + 1, (y + 1), width - 2, (int) (height / 2.333));
    }

    public enum ColorScheme
    {
        GRAPHITE(new Color(55, 55, 55, 240),
                 new Color(15, 15, 15, 240),
                 new Color(200, 200, 200, 85),
                 new Color(80, 80, 80, 85),
                 new Color(200, 200, 200)),
        LIME(new Color(0, 187, 37, 240),
             new Color(0, 128, 28),
             new Color(51, 255, 51, 120),
             new Color(153, 255, 51, 120),
             new Color(255, 153, 51)),
        HOT_PINK(new Color(255, 102, 204, 240),
                 new Color(255, 0, 170),
                 new Color(200, 200, 200, 80),
                 new Color(100, 100, 100, 85),
                 new Color(110, 255, 110)),
        WHITE(new Color(200, 200, 200, 240),
              new Color(180, 180, 180, 240),
              new Color(255, 255, 255, 85),
              new Color(200, 200, 200, 85),
              new Color(51, 51, 51));

        ColorScheme(Color mainColor1, Color mainColor2, Color highlightColor1, Color highlightColor2, Color textColor) {
            main1 = mainColor1;
            main2 = mainColor2;
            highlight1 = highlightColor1;
            highlight2 = highlightColor2;
            text = textColor;
        }

        private Color main1, main2, highlight1, highlight2, text;
    }

    public void setHovered(boolean hover) {
        hovered = hover;
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        setHovered(pointInButton(mouseEvent.getPoint()));
    }
}
