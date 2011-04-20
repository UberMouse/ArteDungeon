package nz.uberutils.paint.components;

import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/2/11
 * Time: 11:22 AM
 * Package: nz.uberutils.paint.components;
 */
public class PLabel extends PComponent
{
    protected String text;

    public PLabel(int x, int y, String text) {
        super();
        this.x = x;
        this.y = y;
        this.text = text;
    }

    @Override
    public void repaint(Graphics2D g) {
        g.drawString(text, x, y);
    }
}
