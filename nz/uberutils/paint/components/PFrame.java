package nz.uberutils.paint.components;

import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/1/11
 * Time: 11:02 PM
 * Package: nz.uberutils.paint.components;
 */
public class PFrame extends PComponent
{
    private ArrayList<PComponent> components = new ArrayList<PComponent>();
    private boolean shouldDraw = true;
    private boolean shouldHandleMouse = true;
    private boolean shouldHandleKeys = true;
    private final String name;
    private Font font;

    public PFrame(String name) {
        this(name, null);
    }

    public PFrame(String name, Font font) {
        this.name = name;
        this.font = font;
    }

    public void addComponent(PComponent component) {
        components.add(component);
    }

    public void removeComponent(PComponent component) {
        components.remove(component);
    }

    public boolean containsComponent(PComponent component) {
        return components.contains(component);
    }

    public void toggleEvents() {
        shouldDraw = !shouldDraw;
        shouldHandleKeys = !shouldHandleKeys;
        shouldHandleMouse = !shouldHandleMouse;
    }

    public void toggleDrawing() {
        shouldDraw = !shouldDraw;
    }

    public void toggleMouseHandling() {
        shouldHandleKeys = !shouldHandleKeys;
    }

    public void toggleKeyHandling() {
        shouldHandleMouse = !shouldHandleMouse;
    }

    public void setDrawing(boolean draw) {
        shouldDraw = draw;
    }

    public void setMouseHandling(boolean handle) {
        shouldHandleMouse = handle;
    }

    public void setKeyHandling(boolean handle) {
        shouldHandleKeys = handle;
    }

    @Override
    public boolean shouldPaint() {
        return true;
    }

    @Override
    public boolean forceMouse() {
        for (PComponent comp : components)
            if (comp.forceMouse())
                return true;
        return false;
    }

    public void repaint(Graphics2D g) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldDraw) {
                if (comp.shouldPaint()) {
                    Font oldFont = g.getFont();
                    Color oldColor = g.getColor();
                    if (font != null)
                        g.setFont(font);
                    comp.repaint(g);
                    g.setFont(oldFont);
                    g.setColor(oldColor);
                }
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forcePaint()) {
                            Font oldFont = g.getFont();
                            Color oldColor = g.getColor();
                            if (font != null)
                                g.setFont(font);
                            component.repaint(g);
                            g.setFont(oldFont);
                            g.setColor(oldColor);
                        }
                }
                else if (comp.forcePaint()) {
                    Font oldFont = g.getFont();
                    Color oldColor = g.getColor();
                    if (font != null)
                        g.setFont(font);
                    comp.repaint(g);
                    g.setFont(oldFont);
                    g.setColor(oldColor);
                }
            }
        }
    }

    public void keyTyped(KeyEvent keyEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleKeys) {
                if (comp.shouldHandleKeys())
                    comp.keyTyped(keyEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceKeys())
                            component.keyTyped(keyEvent);
                }
                else if (comp.forceKeys())
                    comp.keyTyped(keyEvent);
            }
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleKeys) {
                if (comp.shouldHandleKeys())
                    comp.keyPressed(keyEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceKeys())
                            component.keyPressed(keyEvent);
                }
                else if (comp.forceKeys())
                    comp.keyPressed(keyEvent);
            }
        }
    }

    public void keyReleased(KeyEvent keyEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleKeys) {
                if (comp.shouldHandleKeys())
                    comp.keyReleased(keyEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceKeys())
                            component.keyReleased(keyEvent);
                }
                else if (comp.forceKeys())
                    comp.keyReleased(keyEvent);
            }
        }
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleMouse) {
                if (comp.shouldHandleMouse())
                    comp.mouseClicked(mouseEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceMouse())
                            component.mouseClicked(mouseEvent);
                }
                else if (comp.forceMouse())
                    comp.mouseClicked(mouseEvent);
            }
        }
    }

    public void mousePressed(MouseEvent mouseEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleMouse) {
                if (comp.shouldHandleMouse())
                    comp.mousePressed(mouseEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceMouse())
                            component.mousePressed(mouseEvent);
                }
                else if (comp.forceMouse())
                    comp.mousePressed(mouseEvent);
            }
        }
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleMouse) {
                if (comp.shouldHandleMouse())
                    comp.mouseReleased(mouseEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceMouse())
                            component.mouseReleased(mouseEvent);
                }
                else if (comp.forceMouse())
                    comp.mouseReleased(mouseEvent);
            }
        }
    }

    public void mouseEntered(MouseEvent mouseEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleMouse) {
                if (comp.shouldHandleMouse())
                    comp.mouseEntered(mouseEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceMouse())
                            component.mouseEntered(mouseEvent);
                }
                else if (comp.forceMouse())
                    comp.mouseEntered(mouseEvent);
            }
        }
    }

    public void mouseExited(MouseEvent mouseEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleMouse) {
                if (comp.shouldHandleMouse())
                    comp.mouseExited(mouseEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceMouse())
                            component.mouseExited(mouseEvent);
                }
                else if (comp.forceMouse())
                    comp.mouseExited(mouseEvent);
            }
        }
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleMouse) {
                if (comp.shouldHandleMouse())
                    comp.mouseDragged(mouseEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceMouse())
                            component.mouseDragged(mouseEvent);
                }
                else if (comp.forceMouse())
                    comp.mouseDragged(mouseEvent);
            }
        }
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        Iterator<PComponent> it = components.iterator();
        while (it.hasNext()) {
            PComponent comp = it.next();
            if (shouldHandleMouse) {
                if (comp.shouldHandleMouse())
                    comp.mouseMoved(mouseEvent);
            }
            else {
                if (comp instanceof PFrame) {
                    for (PComponent component : ((PFrame) comp).getComponents())
                        if (component.forceMouse())
                            component.mouseMoved(mouseEvent);
                }
                else if (comp.forceMouse())
                    comp.mouseMoved(mouseEvent);
            }
        }
    }

    public String getName() {
        return name;
    }

    public boolean equals(Object object) {
        return !(object == null || !(object instanceof PFrame)) && name.equals(((PFrame) object).getName());
    }

    public void clearComps() {
        components.clear();
    }

    public void reset() {
        setDrawing(true);
        setKeyHandling(true);
        setMouseHandling(true);
        clearComps();
    }

    public ArrayList<PComponent> getComponents() {
        return components;
    }
}
