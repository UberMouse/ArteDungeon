package nz.artedungeon.strategies;

import com.rsbuddy.script.methods.Menu;
import com.rsbuddy.script.methods.Mouse;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/24/11
 * Time: 9:50 AM
 * Package: nz.artedungeon.strategies;
 */
public class MenuFailSafe extends Strategy
{
    /**
     * Instantiates a new strategy.
     *
     * @param parent instance of main script
     */
    public MenuFailSafe(DungeonMain parent) {
        super(parent);
    }

    @Override
    public int execute() {
        Mouse.moveRandomly(3000);
        return random(400, 600);
    }

    @Override
    public boolean isValid() {
        return Menu.isOpen() && Menu.getActions().length < 4;
    }

    @Override
    public void reset() {
    }

    @Override
    public String getStatus() {
        return "Fixing RSBuddy menu fails";
    }
}
