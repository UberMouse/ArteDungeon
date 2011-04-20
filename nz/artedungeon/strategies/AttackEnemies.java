package nz.artedungeon.strategies;

import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.Enemy;
import nz.artedungeon.dungeon.MyPlayer;

public class AttackEnemies extends Strategy
{

    public AttackEnemies(DungeonMain parent) {
        super(parent);
    }


    public int execute() {
        if (!MyPlayer.inCombat()) {
            Enemy.setNPC(MyPlayer.currentRoom().getHighestPriorityEnemy());
            MyPlayer.attack(Enemy.getNPC());
        }
        return random(400, 600);
    }


    public boolean isValid() {
        return MyPlayer.currentRoom() != null &&
               MyPlayer.currentRoom().getHighestPriorityEnemy() != null &&
               MyPlayer.currentRoom().hasUnOpenGuardianDoor();
    }


    public void reset() {

    }

    public String getStatus() {
        if (Enemy.getNPC() != null)
            return "Killing " + Enemy.getNPC().getName();
        return "Attacking enemies";
    }

}
