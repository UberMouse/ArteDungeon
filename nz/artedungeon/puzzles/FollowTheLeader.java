package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Widgets;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.Component;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Widget;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.utils.util;
import nz.uberutils.methods.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/19/11
 * Time: 9:48 PM
 * Package: nz.artedungeon.puzzles;
 */
public class FollowTheLeader extends Plugin
{
    final int WAVE = 863, NOD = 855, SHAKE = 856, LAUGH = 861, CRY = 860;
    final int STATUE = 10966, PAD = 52206;

    int lastEmote = -1;

    public boolean isValid() {
        return Npcs.getNearest(STATUE) != null && util.tileInRoom(Npcs.getNearest(STATUE).getLocation());
    }

    public String getStatus() {
        return "Solving: Follow The Leader";
    }

    @Override
    public String getAuthor() {
        return "Zippy";
    }

    @Override
    public String getName() {
        return "Follow The Leader";
    }

    @Override
    public int loop() {
        if (!MyPlayer.get().isMoving()) {
            Npc statue = Npcs.getNearest(10966);
            if (statue != null) {
                GameObject pad = null;
                for (GameObject o : Objects.getLoaded(new Filter<GameObject>()
                {
                    public boolean accept(GameObject o) {
                        return o.getId() == PAD;
                    }
                })) {
                    if (Calculations.distanceBetween(statue.getLocation(), o.getLocation()) <= 4) {
                        pad = o;
                        break;
                    }
                }
                if (pad != null) {
                    if (!pad.getLocation().equals(MyPlayer.get().getLocation())) {
                        lastEmote = -1;
                        if (!pad.isOnScreen())
                            MyMovement.turnTo(pad);
                        else {
                            pad.interact("Walk here");
                            while (MyPlayer.get().isIdle() && isActive())
                                sleep(50);
                            while (MyPlayer.get().isMoving() && isActive())
                                sleep(50);
                        }
                    }
                    else {
                        Widget w = getEmoteWidget();
                        if (w != null) {
                            for (Component c : w.getComponents())
                                if (c.containsText(getEmoteName())) {
                                    c.click();
                                    lastEmote = -1;
                                }
                        }
                        for (int i = 0; i < 100 && statue.getAnimation() == -1; i++) {
                            sleep(50);
                        }
                        for (int anim : new int[]{LAUGH, CRY, WAVE, NOD, SHAKE}) {
                            if (statue.getAnimation() == anim) {
                                lastEmote = anim;
                                return 0;
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }

    public String getEmoteName() {
        switch (lastEmote) {
            case WAVE:
                return "Wave";
            case NOD:
                return "Nod";
            case SHAKE:
                return "Shake";
            case LAUGH:
                return "Laugh";
            case CRY:
                return "Cry";
        }
        return "Idle";
    }

    public Widget getEmoteWidget() {
        for (Widget w : Widgets.getLoaded()) {
            if (w.containsText("Perform an action")
                && w.containsText("Wave")
                && w.containsText("Shake")
                && w.containsText("Nod")
                && w.containsText("Laugh")
                && w.containsText("Cry"))
                return w;
        }
        return null;
    }
}
