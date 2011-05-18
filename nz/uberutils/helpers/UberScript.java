package nz.uberutils.helpers;

import com.rsbuddy.event.listeners.PaintListener;
import com.rsbuddy.script.ActiveScript;
import com.rsbuddy.script.Manifest;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Mouse;
import nz.uberutils.arte.ArteNotifier;
import nz.uberutils.helpers.tasks.ImageThread;
import nz.uberutils.helpers.tasks.PriceThread;
import nz.uberutils.paint.PaintController;
import nz.uberutils.paint.abstracts.PComponent;
import nz.uberutils.paint.components.PColumnLayout;
import nz.uberutils.paint.components.PFancyButton;
import nz.uberutils.paint.components.PFrame;
import nz.uberutils.paint.components.PSkill;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/17/11
 * Time: 5:13 PM
 * Package: nz.uberutils.helpers;
 */
public abstract class UberScript extends ActiveScript implements PaintListener,
                                                                 KeyListener,
                                                                 MouseListener,
                                                                 MouseMotionListener
{
    protected boolean showPaint = true;
    protected String status = "Starting...";
    protected final ArrayList<Strategy> stratagies = new ArrayList<Strategy>();
    protected final ArrayList<Skill> skills = new ArrayList<Skill>();
    protected final String name = getClass().getAnnotation(Manifest.class).name();
    protected int threadId;
    protected String[] infoColumnValues;
    protected String[] infoColumnData;

    private final PFrame infoFrame = new PFrame("info")
    {
        public boolean shouldPaint() {
            return menuIndex == 0;
        }

        public boolean shouldHandleMouse() {
            return shouldPaint();
        }
    };
    private final PFrame optionFrame = new PFrame("options")
    {
        public boolean shouldPaint() {
            return menuIndex == 1;
        }

        public boolean shouldHandleMouse() {
            return shouldPaint();
        }
    };
    public int menuIndex = 0;

    public void togglePaint() {
        showPaint = !showPaint;
    }

    public boolean onStart() {
        Options.setNode(name);
        getContainer().submit(new PriceThread());
        getContainer().submit(new ArteNotifier(14310, true));
        getContainer().submit(new ImageThread(name));
        PaintController.clearComps();
        PaintController.reset();
        PaintController.addComponent(new PFancyButton(8, 450, "ArteBots", PFancyButton.ColorScheme.GRAPHITE)
        {
            public void onPress() {
                Utils.openURL("http://artebots.com");
            }
        });
        PaintController.addComponent(new PFancyButton(59, 450, "Feedback", PFancyButton.ColorScheme.GRAPHITE)
        {
            public void onPress() {
                Utils.openURL("http://rsbuddy.com/forum/showthread.php?t=" + threadId);
            }
        });
        PaintController.addComponent(infoFrame);
        PaintController.addComponent(optionFrame);
        PaintController.addComponent(new PFancyButton(440, 345, "Toggle paint", PFancyButton.ColorScheme.GRAPHITE)
        {
            public void onStart() {
                forceMouse = true;
                forcePaint = true;
            }

            public void onPress() {
                togglePaint();
                PaintController.toggleEvents();
            }
        });
        PaintController.addComponent(new PFancyButton(440, 365, 73, -1, "Info", PFancyButton.ColorScheme.GRAPHITE)
        {
            public void onStart() {
                setHovered(true);
            }

            public void onPress() {
                menuIndex = 0;
            }

            public void mouseMoved(MouseEvent mouseEvent) {
                setHovered(pointInButton(mouseEvent.getPoint()) || menuIndex == 0);
            }
        });
        PaintController.addComponent(new PFancyButton(440, 385, 73, -1, "Options", PFancyButton.ColorScheme.GRAPHITE)
        {
            public void onPress() {
                menuIndex = 1;
            }

            public void mouseMoved(MouseEvent mouseEvent) {
                setHovered(pointInButton(mouseEvent.getPoint()) || menuIndex == 1);
            }
        });
        PaintController.startTimer();
        return true;
    }

    public void onFinish() {
        Options.save();
    }

    @Override
    public int loop() {
        try {
            Mouse.setSpeed(Utils.random(1, 2));
            miscLoop();
            for (Strategy strategy : stratagies) {
                if (strategy.isValid()) {
                    status = strategy.getStatus();
                    strategy.execute();
                    return Utils.random(400, 500);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    protected void miscLoop() {

    }

    public void keyTyped(KeyEvent keyEvent) {
        PaintController.keyTyped(keyEvent);
    }

    public void keyPressed(KeyEvent keyEvent) {
        PaintController.keyPressed(keyEvent);
    }

    public void keyReleased(KeyEvent keyEvent) {
        PaintController.keyReleased(keyEvent);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        PaintController.mouseClicked(mouseEvent);
    }

    public void mousePressed(MouseEvent mouseEvent) {
        PaintController.mousePressed(mouseEvent);
    }

    public void mouseReleased(MouseEvent mouseEvent) {
        PaintController.mouseReleased(mouseEvent);
    }

    public void mouseEntered(MouseEvent mouseEvent) {
        PaintController.mouseEntered(mouseEvent);
    }

    public void mouseExited(MouseEvent mouseEvent) {
        PaintController.mouseExited(mouseEvent);
    }

    public void mouseDragged(MouseEvent mouseEvent) {
        PaintController.mouseDragged(mouseEvent);
    }

    public void mouseMoved(MouseEvent mouseEvent) {
        PaintController.mouseMoved(mouseEvent);
    }

    public void onRepaint(Graphics graphics) {
        if (!Game.isLoggedIn())
            return;
        try {
            Graphics2D g = (Graphics2D) graphics;
            PComponent clayout = null;
            try {
                clayout = new PColumnLayout(215,
                                            354,
                                            infoColumnData,
                                            infoColumnValues,
                                            new Font("Arial", 0, 9),
                                            PColumnLayout.ColorScheme.GRAPHITE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (showPaint) {
                Paint p = g.getPaint();
                g.setPaint(new GradientPaint(7, 345, new Color(55, 55, 55, 240), 512, 472, new Color(15, 15, 15, 240)));
                g.fillRect(7, 345, 505, 127);
                g.setPaint(p);
            }
            if (clayout != null)
                infoFrame.addComponent(clayout);
            PaintController.onRepaint(graphics);
            if (clayout != null)
                infoFrame.removeComponent(clayout);
            if (!showPaint)
                return;
            String infoTxt = name + " - " + "v" + getClass().getAnnotation(Manifest.class).version();
            g.drawString(infoTxt, 512 - SwingUtilities.computeStringWidth(g.getFontMetrics(g.getFont()), infoTxt), 468);
            if (Loot.isPaintValid() && Options.getBoolean("pickupLoot"))
                Loot.repaint(g);

            int offset = 0;
            for (Skill skill : skills) {
                if (skill.xpGained() > 0) {
                    PSkill skillComp = new PSkill(8, 346 + offset, skill.getSkill(), PSkill.ColorScheme.GRAPHITE);
                    if (!infoFrame.containsComponent(skillComp)) {
                        infoFrame.addComponent(skillComp);
                    }
                    offset += 20;
                }
            }
        } catch (Exception ignored) {
        }
    }
}
