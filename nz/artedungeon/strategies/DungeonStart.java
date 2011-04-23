package nz.artedungeon.strategies;

import com.rsbuddy.script.methods.*;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.GroundItem;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Npc;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.Strategy;
import nz.artedungeon.dungeon.*;
import nz.artedungeon.misc.GameConstants;
import nz.uberutils.helpers.Utils;
import nz.uberutils.methods.MyEquipment;
import nz.uberutils.methods.MyGroundItems;
import nz.uberutils.methods.MyInventory;
import nz.uberutils.methods.MyMovement;

import java.awt.*;


public class DungeonStart extends Strategy
{
    private boolean setup;
    private boolean firstRun = true;
    private final int TOOLKIT_COMPONENT = 469;
    private final int TOOLKIT_ID = 19650;
    private int failSafe = 0;

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
                    Dungeon.setDoorIdOffset(145);
                }
                else if (exit.getId() == 50604) {
                    Dungeon.setFloorType("Abandoned");
                    Dungeon.setDoorIdOffset(209);
                }
                else if (exit.getId() == 51704) {
                    Dungeon.setFloorType("Furnished");
                    Dungeon.setDoorIdOffset(273);
                }
            }
            MyPlayer.setCurrentRoom(Explore.newRoom());
            Explore.setStartRoom(MyPlayer.currentRoom());
            while (!Explore.getStartRoom().contains(MyPlayer.location())) {
                parent.clearAll();
                MyPlayer.setCurrentRoom(Explore.newRoom());
                Explore.setStartRoom(MyPlayer.currentRoom());
            }
            firstRun = false;
            if (MyPlayer.getComplexity() < 5)
                setup = true;
            return random(400, 600);
        }
        else {
            if (getNearestLoot() == null && Inventory.contains(17678))
                setup = true;
            else if (getNearestLoot() != null) {
                GroundItem loot = getNearestLoot();
                MyMovement.turnTo(loot.getLocation());
                int timeout = 0;
                while (Calculations.distanceTo(loot) > 4 && ++timeout <= 15)
                    sleep(100);
                MyGroundItems.itemInteract(loot, "take");
                int count = Inventory.getCount();
                timeout = 0;
                while (Inventory.getCount() == count && ++timeout <= 15)
                    sleep(100);
            }
            else if (!Inventory.contains(TOOLKIT_ID)) {
                Npc smuggler = Npcs.getNearest(GameConstants.SMUGGLER);
                MyMovement.turnTo(smuggler);
                int timeout = 0;
                while (Calculations.distanceTo(smuggler) > 4 && ++timeout <= 15)
                    sleep(100);
                if (!Widgets.getComponent(GameConstants.STORE_COMPONENT, 0).isValid()) {
                    if (smuggler.interact("Trade")) {
                        timeout = 0;
                        while (!Widgets.getComponent(GameConstants.STORE_COMPONENT).isValid() && ++timeout <= 15)
                            sleep(100);
                    }
                }
                else {
                    com.rsbuddy.script.wrappers.Component toolkit = Widgets.getComponent(GameConstants.STORE_COMPONENT,
                                                                                         GameConstants.STORE_SUB_COMPONENT)
                                                                           .getComponent(TOOLKIT_COMPONENT);
                    com.rsbuddy.script.wrappers.Component storeSubComp = Widgets.getComponent(GameConstants.STORE_COMPONENT,
                                                                                              GameConstants.STORE_SUB_COMPONENT);
                    if (toolkit != null && toolkit.isValid() && Inventory.getCount(true, GameConstants.COINS) >= 1320) {
                        Rectangle storeSubBounds = storeSubComp.getBoundingRect();
                        Mouse.move((int) storeSubBounds.getCenterX(), (int) storeSubBounds.getCenterY());
                        for (int i = 0; i < 7; i++) {
                            Mouse.scroll(false);
                        }
                        if (toolkit.interact("Buy"))
                            Widgets.getComponent(GameConstants.STORE_COMPONENT, 18).click();
                    }
                    else {
                        for (Item item : MyInventory.getItems(true)) {
                            Equipable check = new Equipable(item.getName());
                            Utils.debug(ItemHandler.shouldEquip(check, check.getEquipmentIndex()) +
                                        " " +
                                        check.getName());
                            if (check.getStyle() != ItemHandler.UNKNOWN &&
                                !ItemHandler.shouldEquip(check, check.getEquipmentIndex())) {
                                item.interact("Sell 50");
                            }
                        }
                        if (Inventory.getCount(true, GameConstants.COINS) <= 1320 && failSafe <= 5) {
                            Widgets.getComponent(GameConstants.STORE_COMPONENT, 18).click();
                            failSafe++;
                        }
                        else if (failSafe > 5) {
                            Widgets.getComponent(GameConstants.STORE_COMPONENT, 18).click();
                            Explore.setExit(true);
                            DungeonMain.timesAborted++;
                        }
                    }
                }
            }
            else {
                if (Widgets.getComponent(GameConstants.STORE_COMPONENT, 0).isValid())
                    Widgets.getComponent(GameConstants.STORE_COMPONENT, 18).click();
                Item toolkit = Inventory.getItem(TOOLKIT_ID);
                if (toolkit != null)
                    toolkit.interact("Open");
            }
        }
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
