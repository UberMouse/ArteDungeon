package nz.artedungeon.puzzles;

import nz.artedungeon.common.Plugin;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 3/3/11
 * Time: 2:58 PM
 * Package: nz.artedungeon.puzzles;
 */
public class SuspiciousGrooves extends Plugin
{
    @Override
    public boolean isValid() {
        return false;
    }

    @Override
    public String getStatus() {
        return "Solving: Suspicious Grooves";
    }

    @Override
    public String getAuthor() {
        return "UberMouse";
    }

    @Override
    public String getName() {
        return "Suspicious Grooves";
    }

    @Override
    public int loop() {
        return 0;
    }
}
