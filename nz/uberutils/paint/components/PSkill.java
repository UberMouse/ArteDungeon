package nz.uberutils.paint.components;

import com.rsbuddy.script.methods.Skills;
import nz.uberutils.helpers.Skill;
import nz.uberutils.methods.MySkills;
import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/2/11
 * Time: 5:35 PM
 * Package: nz.uberutils.paint.components;
 */
public class PSkill extends PComponent {
    private final int skill;
    private final Rectangle2D hoverArea;
    private boolean skillToggle;
    private Point m = new Point(-1, -1);
    public final Skill skillInfo;
    private final ColorScheme scheme;
    private final String name;

    public PSkill(int x, int y, int skill, ColorScheme scheme) {
        this(x, y, 200, 18, skill, scheme);
    }

    public PSkill(int x, int y, int width, int height, int skill, ColorScheme scheme) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.skill = skill;
        skillInfo = new Skill(skill);
        hoverArea = new Rectangle(x, y, width, height);
        this.scheme = scheme;
        name = Skills.SKILL_NAMES[skill];
    }


    @Override
    public void repaint(Graphics2D g) {
        drawProgressBar(g, skill, x, y, width, height, (skillToggle || hoverArea.contains(m)));
    }

    /**
     * Draws a progress bar using the co-ordinates, dimensions and skill provided.
     * This also displays current level in the skill, percent till the next level &
     * exp needed to reach the next level
     *
     * @param g     An instance of Graphics to paint to.
     * @param skill The number of the skill wanting to display. E.g Skills.MAGIC
     * @param x     The "x" co-ordinate.
     * @param y     The "y" co-ordinate.
     * @param w     The width of the progress bar.
     * @param h     The height of the progress bar.
     * @param hover If you want extra info to be displayed (Usually for when the skill is "Hovered")
     * @author Rudday, UberMouse
     */
    public void drawProgressBar(Graphics g, int skill, int x, int y, int w, int h, boolean hover) {
        Graphics2D g2 = (Graphics2D) g;
        Paint p = g2.getPaint();
        GradientPaint grad1 = new GradientPaint(x, y, scheme.firstGradient1,
                x, y + h + 3, scheme.firstGradient2);
        GradientPaint grad2 = new GradientPaint(x, y, scheme.secondGradient1,
                x, y + h + 3, scheme.secondGradient2);
        g2.setPaint(grad1);
        g2.fillRect(x, y, w, h);
        g2.setPaint(grad2);
        g2.fillRect(x, y, (int) (w * (MySkills.getPercentToNextLevel(skill) / 100.0)), h);
        g2.setColor(scheme.text);
        g2.drawRect(x, y, w, h);
        g2.setFont(new Font("Arial", 0, 9));
        String progress = MySkills.getPercentToNextLevel(skill)
                + "% to " + ((MySkills.getRealLevel(skill) + 1 <= 120) ? MySkills.getRealLevel(skill) + 1 : 120)
                + " " + name + " | "
                + MySkills.getExpToNextLevel(skill)
                + " XPTL";
        g2.drawString(progress,
                x + ((w - (g.getFontMetrics().stringWidth(progress))) / 2),
                (int) (y + ((g.getFontMetrics().getHeight() / 2) + (h / 4) * 1.75)));
        g2.setPaint(p);
        if (hover) {
            g2.setColor(scheme.hover);
            g2.fillRect(x, y - (height + 2), width, height);
            g2.setColor(scheme.text);
            g2.drawRect(x, y - (height + 2), width, height);
            progress = "TTL: " +
                    skillInfo.timeToLevel() +
                    " XPG: " +
                    skillInfo.xpGained() +
                    " XPPH: " +
                    skillInfo.xpPH() + " LG: " + skillInfo.levelsGained();
            g2.drawString(progress, x + ((w - (g.getFontMetrics().stringWidth(progress))) / 2),
                    (int) ((y - (height + 2)) + ((g.getFontMetrics().getHeight() / 2) + (h / 4) * 1.75)));
        }
    }

    public void mouseClicked(MouseEvent e) {
        if (hoverArea.contains(e.getPoint()))
            skillToggle = !skillToggle;
    }

    public void mouseMoved(MouseEvent e) {
        m = e.getPoint();
    }

    public static enum ColorScheme {
        GRAPHITE(new Color(55, 55, 55, 240),
                new Color(15, 15, 15, 240),
                new Color(200, 200, 200, 85),
                new Color(80, 80, 80, 85),
                new Color(55, 55, 55, 240),
                new Color(200, 200, 200)),
        LIME(new Color(0, 187, 37, 240),
                new Color(0, 128, 28),
                new Color(51, 255, 51, 120),
                new Color(153, 255, 51, 120),
                new Color(0, 187, 37, 240),
                new Color(255, 153, 51)),
        HOT_PINK(new Color(255, 102, 204, 240),
                new Color(255, 0, 170),
                new Color(200, 200, 200, 80),
                new Color(100, 100, 100, 85),
                new Color(255, 102, 204, 240),
                new Color(110, 255, 110)),
        WHITE(new Color(200, 200, 200, 240),
                new Color(180, 180, 180, 240),
                new Color(255, 255, 255, 85),
                new Color(200, 200, 200, 85),
                new Color(200, 200, 200, 240),
                new Color(51, 51, 51)),
        GREENRED(new Color(181, 55, 55, 255),
                new Color(207, 104, 103, 255),
                new Color(90, 168, 51, 255),
                new Color(134, 205, 99, 255),
                new Color(181, 55, 55, 255),
                Color.BLACK);

        private Color firstGradient1, firstGradient2, secondGradient1, secondGradient2, hover, text;

        ColorScheme(Color first, Color second, Color third, Color fourth, Color hover, Color text) {
            firstGradient1 = first;
            firstGradient2 = second;
            secondGradient1 = third;
            secondGradient2 = fourth;
            this.hover = hover;
            this.text = text;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        if (!super.equals(o))
            return false;

        PSkill pSkill = (PSkill) o;

        return skill == pSkill.skill &&
                (name != null ? name.equals(pSkill.name) : pSkill.name == null) &&
                scheme == pSkill.scheme;

    }

    @Override
    public int hashCode() {
        int result = skill;
        result = 31 * result + (scheme != null ? scheme.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
