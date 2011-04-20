package nz.artedungeon.bosses;

import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.util.Timer;
import com.rsbuddy.script.wrappers.Area;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.dungeon.Enemy;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.utils.util;
import nz.uberutils.methods.MyMovement;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/18/11
 * Time: 9:01 PM
 * Package: nz.artedungeon.bosses;
 */
public class RiftSplitter extends Plugin
{
    private final ArrayList<Portal> portals = new ArrayList<Portal>();

    private static enum State
    {
        ATTACK("Attacking"), DODGE_PORTALS("Dodging a portal"), EAT("Eating");

        private State(String name) {
            get = name;
        }

        private final String get;

        public String toString() {
            return get;
        }
    }

    private State getState() {
        if (inPortal(MyPlayer.location()) || (Walking.getDestination() != null && inPortal(Walking.getDestination())))
            return State.DODGE_PORTALS;
        if (MyPlayer.needToEat() && MyPlayer.hasFood())
            return State.EAT;
        return State.ATTACK;
    }

    @Override
    public String getStatus() {
        return "Killing " + getName() + ": " + getState().toString();
    }

    @Override
    public boolean isValid() {
        return MyPlayer.currentRoom().getNearestNpc(".*riftsplitter") != null;
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Har'Lakk the Riftsplitter";
    }

    @Override
    public int loop() {
        Npc riftsplitter = MyPlayer.currentRoom().getNearestNpc(".*riftsplitter");
        if (riftsplitter.getMessage() != null) {
            String msg = riftsplitter.getMessage();
            if (msg.contains("miasma") || msg.contains("flame portal") || msg.contains("cut you"))
                portals.add(new Portal(MyPlayer.location()));
        }
        switch (getState()) {
            case DODGE_PORTALS:
                if (Walking.getDestination() != null && inPortal(Walking.getDestination()) || !MyPlayer.isMoving())
                    util.clickRandomTileOnMap();
                break;
            case EAT:
                MyPlayer.eat();
                break;
            case ATTACK:
                if (!MyPlayer.inCombat()) {
                    Enemy.setEnemy(riftsplitter);
                    MyMovement.turnTo(Enemy.getNPC());
                    Enemy.interact("attack");
                }
                break;
        }
        return Random.nextInt(300, 500);
    }

    public boolean inPortal(Tile location) {
        for (Portal portal : portals)
            if (portal.inPortal(location))
                return true;
        return false;
    }

    private class Portal
    {
        private final Area area;
        private final Timer lifeTimer = new Timer(10000);

        public Portal(Tile location) {
            area = new Area(new Tile(location.getX() + 1, location.getY() + 1),
                            new Tile(location.getX() - 1, location.getY() - 1));
        }

        public boolean inPortal(Tile location) {
            if (lifeTimer.isRunning())
                return area.contains(location);
            else
                return false;
        }
    }
}