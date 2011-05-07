package nz.uberutils.paint.abstracts;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/4/11
 * Time: 7:15 PM
 * Package: nz.uberutils.paint.abstracts;
 */
public abstract class PGame extends PComponent
{
    public abstract void mouseMoved(MouseEvent e);

    public abstract void mouseClicked(MouseEvent e);

    public abstract void keyPressed(KeyEvent e);
}
