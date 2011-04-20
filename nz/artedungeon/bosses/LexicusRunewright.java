package nz.artedungeon.bosses;

import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.util.Timer;
import com.rsbuddy.script.wrappers.Npc;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.Enemy;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.utils.util;
import nz.uberutils.methods.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/15/11
 * Time: 7:04 PM
 * Package: nz.artedungeon.bosses;
 */
public class LexicusRunewright extends Plugin
{
    private boolean avoidBooks;
    private final Timer bookResetTimer = new Timer(3500);

    private static enum State
    {
        ATTACK("Attacking"), DODGE_BOOKS("Dodging books"), KILLING_BOOKS("Killing books");

        private State(String name) {
            get = name;
        }

        private final String get;

        public String toString() {
            return get;
        }
    }

    private State getState() {
        if (avoidBooks)
            return State.DODGE_BOOKS;
        if (MyPlayer.currentRoom().getNearestNpc("tome of .*") != null)
            return State.KILLING_BOOKS;
        return State.ATTACK;
    }

    @Override
    public boolean isValid() {
        return MyPlayer.currentRoom().getNearestNpc(".*runewright") != null;
    }

    @Override
    public String getStatus() {
        return "Killing Lexicus Runewright: " + getState().toString();
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Lexicus Runewright";
    }

    @Override
    public int loop() {
        if (MyPlayer.needToEat())
            MyPlayer.eat();
        Npc runewright = MyPlayer.currentRoom().getNearestNpc(".*runewright");
        if (runewright.getMessage() != null && runewright.getMessage().contains("barrage")) {
            avoidBooks = true;
            bookResetTimer.reset();
        }
        if (!bookResetTimer.isRunning())
            avoidBooks = false;
        switch (getState()) {
            case ATTACK:
                if (!MyPlayer.inCombat()) {
                    Enemy.setNPC(runewright);
                    MyMovement.turnTo(runewright);
                    Enemy.interact("attack");
                }
                break;
            case KILLING_BOOKS:
                Npc book = MyPlayer.currentRoom().getNearestNpc("tome of .*");
                if (book != null) {
                    if (!MyPlayer.inCombat() || !MyPlayer.interacting().equals(book)) {
                        Enemy.setEnemy(book);
                        MyMovement.turnTo(book);
                        Enemy.interact("attack");
                    }
                }
                break;
            case DODGE_BOOKS:
                util.clickRandomTileOnMap();
                break;
        }
        return Random.nextInt(400, 500);
    }
}
