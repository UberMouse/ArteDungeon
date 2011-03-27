package nz.uberdungeon.strategies;

import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.methods.Skills;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.GroundItem;
import nz.uberdungeon.DungeonMain;
import nz.uberdungeon.common.Strategy;
import nz.uberdungeon.dungeon.*;
import nz.uberdungeon.misc.GameConstants;
import nz.uberdungeon.utils.MyEquipment;


public class DungeonStart extends Strategy
{
    private boolean setup;
    private boolean firstRun = true;
    private final int TOOLKIT_COMPONENT = 254;
    private final int TOOLKIT_ID = 19650;

    public DungeonStart(DungeonMain parent) {
        super(parent);
    }

    public int execute() {
        if (firstRun) {
            parent.clearAll();
            Dungeon.start();
            int timeout = 0;
            while (Objects.getNearest(GameConstants.EXITLADDERS) == null && ++timeout <= 30)
                sleep(100);
            parent.inDungeon = true;
            Explore.setExit(false);
            Game.openTab(Game.TAB_EQUIPMENT);
            sleep(random(400, 500));
            Game.openTab(Game.TAB_INVENTORY);
            if (MyEquipment.getItem(MyEquipment.WEAPON) != null) {
                Equipable wep = new Equipable(MyEquipment.getItem(MyEquipment.WEAPON).getName());
                switch (wep.getStyle()) {
                    case MELEE:
                        MyPlayer.setCombatStyle(ItemHandler.MELEE);
                        break;
                    case MAGIC:
                        MyPlayer.setCombatStyle(ItemHandler.MAGIC);
                        break;
                    case RANGED:
                        MyPlayer.setCombatStyle(ItemHandler.RANGED);
                }
            }
            GameObject exit = Objects.getNearest(GameConstants.EXITLADDERS);
            if (exit != null) {
                if (exit.getId() == 51156) {
                    Dungeon.setFloorType("Frozen");
                    Dungeon.setDoorDifferential(145);
                }
                else if (exit.getId() == 50604) {
                    Dungeon.setFloorType("Abandoned");
                    Dungeon.setDoorDifferential(209);
                }
                else if (exit.getId() == 51704) {
                    Dungeon.setFloorType("Furnished");
                    Dungeon.setDoorDifferential(273);
                    //Explore.setExit(true);
                }
            }
            if (Skills.getCurrentLevel(Skills.CONSTITUTION) < 30)
                MyPlayer.setPickupLowLevelFood(true);
            else
                MyPlayer.setPickupLowLevelFood(false);
            MyPlayer.setCurrentRoom(Explore.newRoom());
            Explore.setStartRoom(MyPlayer.currentRoom());
            while (!Explore.getStartRoom().contains(MyPlayer.location())) {
                parent.clearAll();
                MyPlayer.setCurrentRoom(Explore.newRoom());
                Explore.setStartRoom(MyPlayer.currentRoom());
            }
            firstRun = false;
//            if (MyPlayer.getComplexity() < 5)
            setup = true;
            return random(400, 600);
        }
//        else {
//            if (getNearestLoot() == null && Inventory.contains(17678))
//                setup = true;
//            else if (getNearestLoot() != null) {
//                GroundItem loot = getNearestLoot();
//                MyMovement.turnTo(loot.getLocation());
//                int timeout = 0;
//                while (Calculations.distanceTo(loot) > 4 && ++timeout <= 15)
//                    sleep(100);
//                MyGroundItems.itemInteract(loot, "take");
//                int count = Inventory.getCount();
//                timeout = 0;
//                while (Inventory.getCount() == count && ++timeout <= 15)
//                    sleep(100);
//            }
//            else if (!Inventory.contains(TOOLKIT_ID)) {
//                Npc smuggler = Npcs.getNearest(GameConstants.SMUGGLER);
//                MyMovement.turnTo(smuggler);
//                int timeout = 0;
//                while (Calculations.distanceTo(smuggler) > 4 && ++timeout <= 15)
//                    sleep(100);
//                if (!Widgets.getComponent(GameConstants.STORE_COMPONENT, 0).isValid()) {
//                    debug("ffs");
//                    smuggler.interact("Trade");
//                }
//                else {
//                    timeout = 0;
//                    while (!Widgets.getComponent(GameConstants.STORE_COMPONENT).isValid() && ++timeout <= 15)
//                        sleep(100);
//                    PaintComponent toolkit = Widgets.getComponent(GameConstants.STORE_COMPONENT,
//                                                             GameConstants.STORE_SUB_COMPONENT)
//                                               .getComponent(TOOLKIT_COMPONENT);
//                    PaintComponent storeSubComp = Widgets.getComponent(GameConstants.STORE_COMPONENT,
//                                                                  GameConstants.STORE_SUB_COMPONENT);
//                    Rectangle storeSubBounds = storeSubComp.getBoundingRect();
//                    Mouse.move((int) storeSubBounds.getCenterX(), (int) storeSubBounds.getCenterY());
//                    for (int i = 0; i < 7; i++)
//                        Mouse.scroll(false);
//                    if (toolkit != null && toolkit.isValid() && Inventory.getCount(true, GameConstants.COINS) >= 1320)
//                        toolkit.interact("Buy 1");
//                    else {
//                        for (Item item : Inventory.getItems()) {
//                            Equipable check = new Equipable(item.getName());
//                            if (check.getStyle() != ItemHandler.UNKNOWN &&
//                                !ItemHandler.shouldEquip(check, check.getEquipmentIndex())) {
//                                item.interact("Sell 50");
//                            }
//                        }
//                    }
//                }
//            }
//            else {
//                Item toolkit = Inventory.getItem(TOOLKIT_ID);
//                if (toolkit != null)
//                    toolkit.interact("Open");
//            }
//        }
        return random(400, 600);
    }

    public boolean isValid() {
        return !setup;
    }

    public void reset() {
        setup = false;
        firstRun = true;
    }

    public String getStatus() {
        return "Starting Dungeon";
    }

    private GroundItem getNearestLoot() {
        return GroundItems.getNearest(new Filter<GroundItem>()
        {
            public boolean accept(GroundItem groundItem) {
                return new Equipable(groundItem.getItem().getName()).getStyle() != ItemHandler.UNKNOWN ||
                       groundItem.getItem().getId() == GameConstants.COINS;
            }
        });
    }
}
