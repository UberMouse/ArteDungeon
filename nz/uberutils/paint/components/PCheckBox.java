package nz.uberutils.paint.components;

import nz.uberutils.helpers.Utils;
import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/29/11
 * Time: 11:01 PM
 * Package: nz.uberutils.paint.components;
 */
public class PCheckBox extends PComponent
{
    public boolean state;
    private String text;
    //    private static final Color ON = new Color(0, 255, 0, 130);
    //    private static final Color ON_HOVER = new Color(0, 180, 0, 80);
    //    private static final Color OFF = new Color(255, 0, 0, 130);
    //    private static final Color OFF_HOVER = new Color(180, 0, 0, 100);
    private static final Color OFF = new Color(55, 55, 55, 240);
    private static final Color OFF_HOVER = new Color(25, 25, 25, 240);
    private static final Color ON = new Color(200, 200, 200, 85);
    private static final Color ON_HOVER = new Color(110, 110, 110, 85);
    private Color curCol;
    private Rectangle2D hoverArea = null;
    private int seperator;

    public PCheckBox(int x, int y, String text, boolean state, int seperator) {
        super();
        this.x = x;
        this.y = y;
        this.text = text;
        this.state = state;
        width = 12;
        height = 12;
        curCol = state ? ON : OFF;
        this.seperator = seperator;
    }

    public PCheckBox(int x, int y, String text, boolean state) {
        this(x, y, text, state, 3);
    }

    public PCheckBox(int x, int y, String text) {
        this(x, y, text, false, 3);
    }

    public PCheckBox(int x, int y, boolean state) {
        this(x, y, "", state, 3);
    }

    public PCheckBox(int x, int y) {
        this(x, y, "", false, 3);
    }

    @Override
    public void repaint(Graphics2D g) {
        int tmpx = x;
        int tmpy = y;
        if (!text.equals("")) {
            g.drawString(text, x, y);
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
            tmpx += seperator + bounds.getWidth();
            tmpy -= bounds.getHeight() / 1.4;
        }
        if (hoverArea == null) {
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
            int height = (bounds.getHeight() > this.height) ? (int) bounds.getHeight() : this.height;
            hoverArea = new Rectangle(x, tmpy - 2, (int) bounds.getWidth() + (seperator + width) + 2, height);
        }
        g.setColor(Color.WHITE);
        g.drawRect(tmpx - 1, tmpy - 1, height + 1, width + 1);
        g.setColor(curCol);
        g.fillRect(tmpx, tmpy, height, width);
    }

    public void mouseClicked(MouseEvent e) {
        if (hoverArea.contains(e.getPoint())) {
            state = !state;
            onPress();
            curCol = state ? ON : OFF;
            mouseMoved(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (hoverArea.contains(e.getPoint()))
            curCol = state ? ON_HOVER : OFF_HOVER;
        else
            curCol = state ? ON : OFF;
    }

    public void onPress() {
    }
}
