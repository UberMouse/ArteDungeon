package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.Plugin;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.dungeon.rooms.Puzzle;
import nz.artedungeon.dungeon.rooms.Room;
import nz.uberutils.helpers.Utils;
import nz.uberutils.methods.MyMovement;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/21/11
 * Time: 6:49 PM
 * Package: nz.artedungeon.puzzles;
 */
public class Monolith extends PuzzlePlugin
{
    @Override
    public String getStatus() {
        return "Solving: Monolith";
    }

    @Override
    public boolean isValid() {
        if (MyPlayer.currentRoom() != null && MyPlayer.currentRoom().contains(MyPlayer.location())) {
            if (MyPlayer.currentRoom().getType() == Room.Type.PUZZLE)
                return !((Puzzle) MyPlayer.currentRoom()).isSolved() &&
                       Npcs.getNearest("Monolith") != null &&
                       Calculations.distanceTo(Npcs.getNearest("Monolith")) < 8;
        }
        return Npcs.getNearest("Monolith") != null && Calculations.distanceTo(Npcs.getNearest("Monolith")) < 8;
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Monolith";
    }

    @Override
    public int loop() {
        final Room curRoom = MyPlayer.currentRoom();
        Filter<Npc> monolithShade = new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                if (npc != null && npc.getActions() != null) {
                    if (curRoom != null && curRoom.contains(npc.getLocation())) {
                        return !(npc.getInteracting() != null && npc.getInteracting().equals(MyPlayer.get())) &&
                               !npc.isInCombat() &&
                               Utils.arrayContains(npc.getActions(), "Attack");
                    }
                }
                return false;
            }
        };
        Tile safeTile;
        while (curRoom.getNearestNpc("Monolith") == null || curRoom.getNearestNpc("Monolith").getAnimation() != 13072) {
            Npc monolith = curRoom.getNearestNpc("Monolith");
            if (MyPlayer.needToEat() && MyPlayer.hasFood())
                MyPlayer.eat();
            if (monolith != null) {
                if (Npcs.getNearest(10978, 10979, 10980, 12176) == null) {
                    safeTile = monolith.getLocation();
                    if (!MyPlayer.isMoving() && Calculations.distanceTo(safeTile) > 3)
                        MyMovement.turnTo(safeTile);
                    if (monolith.getActions()[0].contains("tivate")) {
                        if (monolith.interact("Activate")) {
                            Utils.waitUntilMoving(5);
                            Utils.waitUntilStopped(5);
                        }
                    }
                }
                else {
                    Npc shade = Npcs.getNearest(monolithShade);
                    if (shade == null)
                        shade = curRoom.getNearestNpc();
                    if (shade != null) {
                        MyMovement.turnTo(shade);
                        if (shade.interact("Attack")) {
                            safeTile = null;
                            for (int i = 0; i < 15 && !MyPlayer.inCombat(); i++)
                                Utils.sleep(100);
                        }
                    }
                    else if (shade == null) {
                        safeTile = monolith.getLocation();
                        if (Calculations.distanceTo(monolith) > 2 && !MyPlayer.isMoving())
                            monolith.getLocation().clickOnMap();
                    }
                }
            }
            sleep(100, 200);
        }
        sleep(100, 200);
        solved = true;
        return 1;
    }
}
