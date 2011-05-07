package nz.uberutils.paint.components;

import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/7/11
 * Time: 5:17 PM
 * Package: nz.artedungeon.paint;
 */
public class PButton extends PComponent
{
    protected String text;
    protected boolean toggled;
    protected Rectangle2D hoverArea = null;
    protected Point mouse = new Point(-1, -1);
    protected final String[] flipText;
    protected int curIndex = 0;
    protected String curText;
    protected int startIndex;

    protected PButton() {
        super();
        flipText = null;
    }

    public PButton(int x, int y, String text, String[] flipText) {
        super();
        this.x = x;
        this.y = y;
        this.text = text;
        this.flipText = flipText;
        onStart();
        if (flipText != null)
            curText = flipText[startIndex];
        curIndex = startIndex;
    }

    public PButton(int x, int y, String text) {
        this(x, y, text,  null);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (hoverArea != null && hoverArea.contains(mouseEvent.getPoint())) {
            toggled = !toggled;
            onPress();
            if (flipText != null) {
                curIndex++;
                if (curIndex > flipText.length - 1)
                    curIndex = 0;
                curText = flipText[curIndex];
            }
        }
    }

    public void repaint(Graphics2D g) {
        String text = (flipText != null) ? this.text + curText : this.text;
        if (hoverArea == null) {
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
            hoverArea = new Rectangle(x,
                                      y - (int) bounds.getHeight(),
                                      (int) bounds.getWidth(),
                                      (int) bounds.getHeight());
        }
        g.drawString(text, x, y);
    }

    public boolean getData() {
        return toggled;
    }

    public void onPress() {

    }

    public void toggle() {
        toggled = !toggled;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        mouse = mouseEvent.getPoint();
    }

    public boolean pointInButton(Point point) {
        if (hoverArea != null) {
            return hoverArea.contains(point);
        }
        return false;
    }
}
