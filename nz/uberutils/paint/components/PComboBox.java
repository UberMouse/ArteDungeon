package nz.uberutils.paint.components;

import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/2/11
 * Time: 10:55 AM
 * Package: nz.uberutils.paint.components;
 */
public class PComboBox extends PComponent
{
    private ColorScheme colorScheme;
    private Font font;
    private boolean hovered = false;
    private FontMetrics metrics;
    private String[] content;

    private boolean expanded = false;

    public int currentIndex;
    public boolean setIndex;
    public int hoveredIndex = -1;

    public PComboBox(int x, int y, String[] content, ColorScheme colorScheme, Font font) {
        super();
        this.x = x;
        this.y = y;
        this.font = font;
        this.content = content;
        this.colorScheme = colorScheme;
        if(!setIndex)
            currentIndex = 0;
        hoveredIndex = currentIndex;
    }

    public PComboBox(int x, int y, String[] content, ColorScheme colorScheme) {
        this(x, y, content, colorScheme, null);
    }

    public Rectangle getBoundsForLine(int lineIndex) {
        if (metrics == null)
            return new Rectangle(-1, -1, -1, -1);
        int x = this.x + 4;
        int width = this.width - 8;
        int height = metrics.getMaxAscent() + 2;
        int y = this.y + 5 + height;
        for (int i = 0; i != lineIndex; i++) {
            y += metrics.getMaxAscent() + 2;
        }
        return new Rectangle(x - 1, y - 1, width + 2, height + 2);
    }

    public void hoverUnderPoint(Point point) {
        for (int i = 0; i < getContent().length; i++) {
            if (getBoundsForLine(i).contains(point)) {
                hoveredIndex = i;
                return;
            }
        }
    }

    public void setSelectedUnderPoint(Point point) {
        if (!expanded)
            return;
        for (int i = 0; i < getContent().length; i++) {
            if (getBoundsForLine(i).contains(point)) {
                currentIndex = i;
                hoveredIndex = currentIndex;
                onChange();
                return;
            }
        }
    }

    public String[] getContent() {
        return content;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
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

    private int getContentHeight() {
        int height = 0;
        if (content != null) {
            for (String i : content) {
                height += metrics.getMaxAscent() + 2;
            }
        }
        return height + this.height;
    }

    private Polygon generateArrow(boolean up) {
        Polygon poly = new Polygon();
        if (!up) {
            poly.addPoint(x + width + 4, y + 4);
            poly.addPoint(x + width + height - 4, y + 4);
            poly.addPoint(x + width + (height / 2), y + height - 4);
        }
        else {
            poly.addPoint(x + width + (height / 2), y + 4);
            poly.addPoint(x + width + 4, y + height - 4);
            poly.addPoint(x + width + height - 4, y + height - 4);
        }
        return poly;
    }

    public boolean pointInComboBox(Point point) {
        return new Rectangle(x, y, width + height, height).contains(point) ||
               new Rectangle(x, y, width, getContentHeight()).contains(point);
    }

    public void toggle() {
        expanded = !expanded;
    }

    @Override
    public void repaint(Graphics2D g) {
        if (font == null)
            font = g.getFont();
        if (height == -1 || width == -1) {
            String largestString = "";
            int offset = 0;
            for (String str : content) {
                if (str.length() > largestString.length())
                    largestString = str;
            }
            Rectangle2D bounds = g.getFont().getStringBounds(largestString, g.getFontRenderContext());
            height = (int) bounds.getHeight() + 5;
            width = (int) bounds.getWidth() + 10;
        }
        Paint p = g.getPaint();

        metrics = g.getFontMetrics(font);

        g.setPaint(new GradientPaint(x - 8,
                                     y - 8,
                                     new Color(230, 230, 230, 180),
                                     x - 8,
                                     y + 16,
                                     new Color(200, 200, 200, 180)));
        g.fillRect(x, y, width, height);

        g.setPaint(new GradientPaint(x,
                                     y,
                                     colorScheme.main1,
                                     x,
                                     !expanded ? y + height : y + getContentHeight(),
                                     colorScheme.main2));
        g.fillRect(x + 4, y + 4, width - 8, !expanded ? height - 8 : getContentHeight());


        g.setPaint(new GradientPaint(x, y, colorScheme.main1, x, y + height, colorScheme.main2));
        g.fillRect(x + width, y, height, height);

        g.setPaint(new GradientPaint(x, y, colorScheme.highlight1, x, y + height, colorScheme.highlight2));
        g.fill(generateArrow(expanded));

        g.setPaint(p);
        g.setFont(font);
        if (expanded) {
            Rectangle b = getBoundsForLine(hoveredIndex);
            g.setPaint(new GradientPaint(b.x,
                                         b.y,
                                         colorScheme.highlight1,
                                         b.x,
                                         b.y + b.height,
                                         colorScheme.highlight2));
            g.fill(getBoundsForLine(hoveredIndex));
            int sY = y + metrics.getMaxAscent() + 3;
            for (int i = 0; i < content.length; i++) {
                sY += metrics.getMaxAscent() + 2;
                g.setColor(colorScheme.text);
                g.drawString(content[i], (x + (width / 2) - (metrics.stringWidth(content[i]) / 2)), sY);
            }
        }
        else {
            g.setColor(colorScheme.text);
            hoveredIndex = currentIndex;
            if (content != null) {
                if (content[currentIndex] != null) {
                    g.drawString(content[currentIndex],
                                 (x + (width / 2) - (metrics.stringWidth(content[currentIndex]) / 2)),
                                 y + (height / 2) + (int) (metrics.getMaxAscent() / (font.getSize() / 4.5)));

                }
            }
        }

        g.setColor(new Color(255, 255, 255, 65));
        g.fillRect(x + 1, (y + 1), width - 2, (int) (height / 2.333));
    }

    public void onChange() {

    }

    public void mousePressed(MouseEvent mouseEvent) {
        if (pointInComboBox(mouseEvent.getPoint())) {
            setSelectedUnderPoint(mouseEvent.getPoint());
            toggle();
        }
        else {
            setExpanded(false);
        }
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        if (pointInComboBox(mouseEvent.getPoint())) {
            hoverUnderPoint(mouseEvent.getPoint());
        }
        else if (isExpanded()) {
            toggle();
        }
    }
}
