package nz.uberdungeon.paint;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/7/11
 * Time: 5:17 PM
 * Package: nz.uberdungeon.paint;
 */
public class PaintButton extends PaintComponent
{
    private final int x;
    private final int y;
    private Rectangle boundingBox = null;
    private String text;
    private boolean toggled;
    private final String[] flipText;
    private int curIndex = 0;
    private String curText;
    public int startIndex;

    public PaintButton(int x, int y, String text, String[] flipText) {
        this.x = x;
        this.y = y;
        this.text = text;
        this.flipText = flipText;
        onStart();
        if (flipText != null)
            curText = flipText[startIndex];
        curIndex = startIndex;
    }

    public PaintButton(int x, int y, String text) {
        this(x, y, text,  null);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        if (boundingBox != null && boundingBox.contains(mouseEvent.getPoint())) {
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
        if (boundingBox == null) {
            Rectangle2D bounds = g.getFontMetrics().getStringBounds(text, g);
            boundingBox = new Rectangle(x,
                                        y - (int) bounds.getHeight(),
                                        (int) bounds.getWidth(),
                                        (int) bounds.getHeight());
        }
        g.drawString(text, x, y);
    }

    public boolean getData() {
        return toggled;
    }

    /**
     * Function called onClick override to control button functionality
     */
    public void onPress() {

    }

    public void toggle() {
        toggled = !toggled;
    }

    /**
     * Set button text
     * @param text new text to set to
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Called on button creation
     */
    public void onStart() {

    }
}
