package nz.uberutils.paint.components;

import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/2/11
 * Time: 12:24 AM
 * Package: nz.uberutils.paint.components;
 */
public class PDialogue extends PComponent
{
    private ColorScheme colorScheme;
    private Type type;
    private String title, content[];
    private Font tFont, cFont;
    private FontMetrics tMetrics, cMetrics;
    private Button buttons[];
    private boolean yesClick;
    private boolean disposed;

    public PDialogue(String title, String[] content, Font tFont, ColorScheme colorScheme, Type type) {
        this(-1, -1, -1, -1, title, content, tFont, colorScheme, type);
    }

    public PDialogue(int x, int y, int width, int height, String title, String[] content, Font tFont,
                     ColorScheme colorScheme, Type type) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.title = title;
        this.content = content;
        this.colorScheme = colorScheme;
        this.type = type;
        this.tFont = tFont;
        this.cFont = new Font(tFont.getName(), 0, tFont.getSize() / 1.25 >= 9 ? (int) (tFont.getSize() / 1.25) : 9);

        if (type == Type.WARNING)
            this.title = "WARNING: " + title;
        else if (type == Type.ERROR)
            this.title = "ERROR: " + title;
    }

    public int getContentHeight() {
        int barHeight = tMetrics.getMaxAscent() + 8;
        int height = barHeight;
        for (String i : content)
            height += cMetrics.getMaxAscent() + 2;
        return height + (barHeight - 4) + 20;
    }

    public int getContentWidth() {
        int width = tMetrics.stringWidth(title) + 12;
        for (String i : content) {
            int temp = cMetrics.stringWidth(i) + 12;
            if (temp > width)
                width = temp;
        }
        return width;
    }

    public boolean containsPoint(Point p) {
        return new Rectangle(x - 8, y - 8, width + 16, height + 16).contains(p);
    }

    public Button buttonWhichPointIsIn(Point point) {
        for (Button button : buttons)
            if (button.pointInButton(point))
                return button;
        return null;
    }

    public boolean pointInButton(Point point) {
        for (Button button : buttons) {
            if (button.pointInButton(point))
                return true;
        }
        return false;
    }

    public ButtonType getButtonClicked(Point point) {
        for (Button button : buttons) {
            if (button.pointInButton(point)) {
                return button.getType();
            }
        }
        return ButtonType.NULL;
    }

    public void setLocation(Point point) {
        setLocation(point.x, point.y);
    }

    public void setLocation(int x, int y) {
        if (this.x != x) {
            this.x = x;
            buttons = null;
        }
        if (this.y != y) {
            this.y = y;
            buttons = null;
        }
    }

    public void redrawButtons() {
        buttons = null;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public enum Type
    {
        WARNING, INFORMATION, ERROR, YES_NO, YES_NO_CANCEL
    }

    public enum ButtonType
    {
        YES("Yes"), NO("No"), CANCEL("Cancel"), OKAY("Ok"), NULL("null");

        ButtonType(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        String text;
    }

    public enum ColorScheme
    {
        GRAPHITE(new Color(55, 55, 55, 240),
                 new Color(15, 15, 15, 240),
                 new Color(200, 200, 200, 85),
                 new Color(80, 80, 80, 85),
                 new Color(200, 200, 200),
                 new Color(255, 255, 255),
                 new Color(200, 0, 0),
                 new Color(200, 200, 0)),
        LIME(new Color(0, 187, 37, 240),
             new Color(0, 128, 28),
             new Color(51, 255, 51, 120),
             new Color(153, 255, 51, 120),
             new Color(255, 153, 51),
             new Color(255, 128, 0),
             new Color(255, 38, 0),
             new Color(255, 217, 0)),
        HOT_PINK(new Color(255, 102, 204, 240),
                 new Color(255, 0, 170),
                 new Color(200, 200, 200, 80),
                 new Color(100, 100, 100, 85),
                 new Color(110, 255, 110),
                 new Color(0, 255, 0),
                 new Color(255, 0, 64),
                 new Color(186, 255, 0)),
        WHITE(new Color(200, 200, 200, 240),
              new Color(180, 180, 180, 240),
              new Color(255, 255, 255, 85),
              new Color(200, 200, 200, 85),
              new Color(51, 51, 51),
              new Color(0, 0, 0),
              new Color(255, 0, 0),
              new Color(255, 255, 0));

        ColorScheme(Color mainColor1,
                    Color mainColor2,
                    Color highlightColor1,
                    Color highlightColor2,
                    Color textColor,
                    Color normalTitleColor,
                    Color errorTitleColor,
                    Color warningTitleColor) {
            main1 = mainColor1;
            main2 = mainColor2;
            highlight1 = highlightColor1;
            highlight2 = highlightColor2;
            text = textColor;
            nTitle = normalTitleColor;
            eTitle = errorTitleColor;
            wTitle = warningTitleColor;
        }

        private Color main1, main2, highlight1, highlight2, text, nTitle, eTitle, wTitle;
    }

    public class Button extends PFancyButton
    {
        private ButtonType type;

        public Button(int x, int y, int width, int height, ButtonType type) {
            super(x, y, type.getText());
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.type = type;
        }

        public void paint(Graphics2D g) {
            if (hoverArea == null)
                hoverArea = getBounds(g, text);
            Paint p = g.getPaint();
            if (!hovered)
                g.setPaint(new GradientPaint(x, y, colorScheme.highlight1, x, y + height, colorScheme.highlight2));
            else
                g.setPaint(new GradientPaint(x, y, colorScheme.highlight2, x, y + height, colorScheme.highlight1));
            g.fillRect(x, y, width, height);

            g.setPaint(p);
            g.setColor(colorScheme.main2);
            g.drawRect(x, y, width - 1, height);

            g.setColor(colorScheme.text);
            g.setFont(cFont);
            g.drawString(type.getText(), (x + (width / 2) - (cMetrics.stringWidth(type.getText()) / 2)),
                         y + (height / 2) + (int) (cMetrics.getMaxAscent() / (cFont.getSize() / 4.5)));

            g.setColor(new Color(255, 255, 255, 65));
            g.fillRect(x + 1, (y + 1), width - 2, (int) (height / 2.333));
        }

        public ButtonType getType() {
            return type;
        }
    }

    @Override
    public void repaint(Graphics2D g) {
        if (disposed)
            return;

        Paint p = g.getPaint();

        g.setStroke(new BasicStroke());

        tMetrics = g.getFontMetrics(tFont);
        cMetrics = g.getFontMetrics(cFont);

        width = width == -1 ? getContentWidth() : width;
        height = height == -1 ? getContentHeight() : height;
        x = x == -1 ? (g.getClipBounds().width / 2) - (width / 2) : x;
        y = y == -1 ? (g.getClipBounds().height / 2) - (height / 2) : y;

        g.setPaint(new GradientPaint(x - 8,
                                     y - 8,
                                     new Color(230, 230, 230, 180),
                                     x - 8,
                                     y + 16,
                                     new Color(200, 200, 200, 180)));
        g.fillRect(x - 8, y - 8, width + 16, height + 16);

        g.setPaint(new GradientPaint(x, y, colorScheme.main1, x, y + height, colorScheme.main2));
        g.fillRect(x, y, width, height);

        g.setPaint(new GradientPaint(x,
                                     y,
                                     colorScheme.highlight1,
                                     x,
                                     y + tMetrics.getMaxAscent() + 8,
                                     colorScheme.highlight2));
        g.fillRect(x, y, width, tMetrics.getMaxAscent() + 8);

        g.setPaint(p);
        g.setColor(colorScheme.main2);
        g.drawRect(x, y, width - 1, tMetrics.getMaxAscent() + 8);

        g.setColor(type == Type.WARNING ?
                   colorScheme.wTitle :
                   type == Type.ERROR ? colorScheme.eTitle : colorScheme.nTitle);
        g.drawString(title, (x + (width / 2) - (tMetrics.stringWidth(title) / 2)) + 1,
                     y +
                     ((tMetrics.getMaxAscent() + 8) / 2) +
                     (int) (tMetrics.getMaxAscent() / (tFont.getSize() / 4.5)));

        g.setColor(new Color(255, 255, 255, 65));
        g.fillRect(x + 1, y + 1, width - 2, (int) ((tMetrics.getMaxAscent() + 8) / 2.333));

        int sY = y + tMetrics.getMaxAscent() + 8 + 4;
        for (String c : content) {
            sY += cMetrics.getMaxAscent() + 2;
            g.setFont(cFont);
            g.setColor(colorScheme.text);
            g.drawString(c, (x + (width / 2) - (cMetrics.stringWidth(c) / 2)) + 1, sY);
        }

        if (buttons == null) {
            if (type == Type.INFORMATION || type == Type.ERROR || type == Type.WARNING) {
                buttons = new Button[]{
                        new Button(x + 6, (y + (getContentHeight() - 24)), width - 12, 18, ButtonType.OKAY)
                };
            }
            else if (type == Type.YES_NO) {
                buttons = new Button[]{
                        new Button(x + 6, (y + (getContentHeight() - 24)), ((width - 12) / 2) - 3, 18, ButtonType.YES),
                        new Button(x + 6 + ((width - 12) / 2) + 3,
                                   (y + (getContentHeight() - 24)),
                                   ((width - 12) / 2) - 3,
                                   18,
                                   ButtonType.NO)
                };
            }
            else if (type == Type.YES_NO_CANCEL) {
                buttons = new Button[]{
                        new Button(x + 5, (y + (getContentHeight() - 24)), ((width - 12) / 3) - 3, 18, ButtonType.YES),
                        new Button(x + 5 + ((width - 12) / 3) + 3,
                                   (y + (getContentHeight() - 24)),
                                   ((width - 12) / 3) - 3,
                                   18,
                                   ButtonType.CANCEL),
                        new Button(x + 5 + (((width - 12) / 3) + 3) * 2,
                                   (y + (getContentHeight() - 24)),
                                   ((width - 12) / 3) - 3,
                                   18,
                                   ButtonType.NO)
                };
            }
        }

        if (buttons != null)
            for (Button button : buttons)
                button.paint(g);
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        if (disposed)
            return;
        for (Button button : getButtons()) {
            button.setHovered(button.pointInButton(mouseEvent.getPoint()));
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (disposed)
            return;
        for (Button button : getButtons()) {
            if (button.pointInButton(mouseEvent.getPoint())) {
                disposed = true;
                if (button.getType().getText().equals("Ok")) {
                    yesClick = true;
                    okClick();
                }
                else if (button.getType().getText().equals("Yes")) {
                    yesClick = true;
                    yesClick();
                }
                else if (button.getType().getText().equals("No"))
                    noClick();
                else if (button.getType().getText().equals("Cancel"))
                    cancelClick();
            }
        }

    }

    public void yesClick() {

    }

    public void noClick() {

    }

    public void cancelClick() {

    }

    public void okClick() {

    }
}