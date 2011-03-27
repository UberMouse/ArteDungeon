package nz.uberdungeon.utils;

import com.rsbuddy.script.methods.Skills;

import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/4/11
 * Time: 2:19 PM
 * Package: nz.uberdungeon.utils;
 */
public class Skill
{
    private final long startime;
    private final int startxp;
    private final int startLevel;
    private final int skillint;
    private int lastxp;
    private int lastLevel;
    private final String name;

    /**
     * Constructs a new Skill
     *
     * @param skillInt the index of the Skill
     */
    public Skill(int skillInt) {
        startime = System.currentTimeMillis();
        startxp = Skills.getCurrentExp(skillInt);
        startLevel = Skills.getCurrentLevel(skillInt);
        skillint = skillInt;
        lastxp = startxp;
        lastLevel = startLevel;
        name = Skills.SKILL_NAMES[skillint];
    }

    public int xpGained() {
        return Skills.getCurrentExp(skillint) - startxp;
    }

    public int levelsGained() {
        return Skills.getCurrentLevel(skillint) - startLevel;
    }

    public int xpTL() {
        return Skills.getExpToNextLevel(skillint);
    }

    public int percentTL() {
        return Skills.getPercentToNextLevel(skillint);
    }

    public int xpPH() {
        return (int) ((xpGained()) * 3600000D / (System.currentTimeMillis() - startime));
    }

    public int curLevel() {
        return Skills.getCurrentLevel(skillint);
    }

    public int curXP() {
        return Skills.getCurrentExp(skillint);
    }

    public String timeToLevel() {
        String TTL = "Calculating..";
        long ttlCalculations;
        if (xpPH() != 0) {
            ttlCalculations = (long) (xpTL() * 3600000D) / xpPH();
            TTL = getTime(ttlCalculations);
        }
        return TTL;
    }

    public String getName() {
        return name;
    }

    public void drawSkill(Graphics2D g, int x, int y) {
        g.drawString(name +
                     ": Gained " +
                     xpGained() +
                     " P/H " +
                     xpPH() +
                     " | Level " +
                     curLevel() +
                     " (" +
                     levelsGained() +
                     ")", x, y);
    }

    private String getTime(long millis) {
        long time = millis / 1000;
        String seconds = Integer.toString((int) (time % 60));
        String minutes = Integer.toString((int) ((time % 3600) / 60));
        String hours = Integer.toString((int) (time / 3600));
        for (int i = 0; i < 2; i++) {
            if (seconds.length() < 2) {
                seconds = "0" + seconds;
            }
            if (minutes.length() < 2) {
                minutes = "0" + minutes;
            }
            if (hours.length() < 2) {
                hours = "0" + hours;
            }
        }
        String returnThis = hours + ":" + minutes + ":" + seconds;
        return returnThis;
    }

    public int getXpMinusLast() {
        int returnXP = curXP() - lastxp;
        lastxp = curXP();
        return returnXP;
    }

    public int getLevelMinusLast() {
        int returnLevel = curLevel() - lastLevel;
        lastLevel = curLevel();
        return returnLevel;
    }
}
