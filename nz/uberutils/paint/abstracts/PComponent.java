package nz.uberutils.paint.abstracts;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/1/11
 * Time: 11:00 PM
 * Package: nz.uberutils.paint.abstracts;
 */
public abstract class PComponent
{
    protected int x = -1;
    protected int y = -1;
    protected int width = -1;
    protected int height = -1;
    protected boolean forcePaint;
    protected boolean forceMouse;
    protected boolean forceKeys;

    public PComponent() {
        onStart();
    }

    public boolean shouldPaint() {
        return x > -1 || y > -1;
    }

    public boolean shouldHandleMouse() {
        return true;
    }

    public boolean shouldHandleKeys() {
        return true;
    }

    public abstract void repaint(Graphics2D g);

    public void mouseClicked(MouseEvent mouseEvent) {
    }

    public void mousePressed(MouseEvent mouseEvent) {
    }

    public void mouseReleased(MouseEvent mouseEvent) {
    }

    public void mouseEntered(MouseEvent mouseEvent) {
    }

    public void mouseExited(MouseEvent mouseEvent) {
    }

    public void mouseDragged(MouseEvent mouseEvent) {
    }

    public void mouseMoved(MouseEvent mouseEvent) {
    }

    public void keyTyped(KeyEvent keyEvent) {
    }

    public void keyPressed(KeyEvent keyEvent) {
    }

    public void keyReleased(KeyEvent keyEvent) {
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    protected Rectangle2D getBounds(Graphics2D g, String text) {
        Rectangle2D bounds = g.getFont().getStringBounds(text, g.getFontRenderContext());
        return new Rectangle((int) (x),
                             (int) (y),
                             (width == -1) ? (int) bounds.getWidth() + 5 : width,
                             (height == -1) ? (int) bounds.getHeight() + 5 : height);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof PComponent))
            return false;
        PComponent comp = (PComponent) object;
        return comp.getX() == x &&
               comp.getY() == y &&
               comp.getWidth() == width &&
               comp.getHeight() == height &&
               comp.getClass().getSimpleName().equals(getClass().getSimpleName());
    }

    public void setForcePaint(boolean force) {
        forcePaint = force;
    }

    public void setForceMouse(boolean force) {
        forceMouse = force;
    }

    public void setForceKeys(boolean force) {
        forceKeys = force;
    }

    public boolean forcePaint() {
        return forcePaint;
    }

    public boolean forceMouse() {
        return forceMouse;
    }

    public boolean forceKeys() {
        return forceKeys;
    }

    public void onStart() {

    }
}
