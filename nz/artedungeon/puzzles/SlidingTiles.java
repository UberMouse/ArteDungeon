package nz.artedungeon.puzzles;

import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.common.PuzzlePlugin;
import nz.artedungeon.dungeon.MyPlayer;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.Util;
import nz.uberutils.methods.MyNpcs;
import nz.uberutils.methods.MyObjects;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/21/11
 * Time: 9:03 PM
 * Package: nz.artedungeon.puzzles;
 */
public class SlidingTiles extends PuzzlePlugin
{
    final int[] TILE_1 = {12125, 12133, 12141, 12149}, TILE_2 = {12126, 12134, 12142, 12150}, TILE_3 = {12127,
                                                                                                        12135,
                                                                                                        12143,
                                                                                                        12151};
    final int[] TILE_4 = {12128, 12136, 12144, 12152}, TILE_5 = {12129, 12137, 12145, 12153}, TILE_6 = {12130,
                                                                                                        12138,
                                                                                                        12146,
                                                                                                        12154};
    final int[] TILE_7 = {12131, 12139, 12147, 12155}, TILE_8 = {12132, 12140, 12148, 12156}, TILE_9 = null;
    final int[][] ID_ROW_1 = {TILE_1, TILE_2, TILE_3};
    final int[][] ID_ROW_2 = {TILE_4, TILE_5, TILE_6};
    final int[][] ID_ROW_3 = {TILE_7, TILE_8, TILE_9};
    final int[][][] ID_SPOTS = {ID_ROW_1, ID_ROW_2, ID_ROW_3};
    final int[] BACKGROUNDS = {54321, 54322, 54323};

    @Override
    public String getStatus() {
        return "Solving: Sliding tiles";
    }

    @Override
    public boolean isValid() {
        if (Npcs.getNearest(GameConstants.SLIDERS) != null)
            return Util.tileInRoom(Npcs.getNearest(GameConstants.SLIDERS).getLocation());
        return false;
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Sliding Tiles";
    }

    @Override
    public int loop() {
        Tile start = MyObjects.getNearestTo(new Tile(0, 0), BACKGROUNDS).getLocation();
        if (start == null)
            return -1;
        final int sX = start.getX(), sY = start.getY();
        final Tile[] ROW_1 = {new Tile(sX + 1, sY + 5), new Tile(sX + 3, sY + 5), new Tile(sX + 5, sY + 5)};
        final Tile[] ROW_2 = {new Tile(sX + 1, sY + 3), new Tile(sX + 3, sY + 3), new Tile(sX + 5, sY + 3)};
        final Tile[] ROW_3 = {new Tile(sX + 1, sY + 1), new Tile(sX + 3, sY + 1), new Tile(sX + 5, sY + 1)};
        Tile safeTile = null;
        final Tile[][] SLIDE_SPOTS = {ROW_1, ROW_2, ROW_3};
        while (MyNpcs.getTopAt(ROW_3[2]) != null) {
            if (MyPlayer.needToEat() && MyPlayer.hasFood())
                MyPlayer.eat();
            o:
            for (int I = 0; I < SLIDE_SPOTS.length; I++) {
                for (int J = 0; J < SLIDE_SPOTS[I].length; J++) {
                    Tile t = SLIDE_SPOTS[I][J];
                    if (MyNpcs.getTopAt(t) == null) {
                        safeTile = t;
                        safeTile.interact("move");
                        Npc nextSlide = MyPlayer.currentRoom().getNearestNpc(ID_SPOTS[I][J]);
                        if (nextSlide.interact("move")) {
                            while (nextSlide.isMoving())
                                sleep(200, 300);
                        }
                        break o;
                    }
                }
            }
            sleep(200, 300);
        }
        return 1;
    }
}
