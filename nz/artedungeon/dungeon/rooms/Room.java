package nz.artedungeon.dungeon.rooms;

import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.methods.GroundItems;
import com.rsbuddy.script.methods.Npcs;
import com.rsbuddy.script.methods.Objects;
import com.rsbuddy.script.util.Filter;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.GroundItem;
import com.rsbuddy.script.wrappers.Npc;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.DungeonCommon;
import nz.artedungeon.dungeon.*;
import nz.artedungeon.dungeon.doors.Door;
import nz.artedungeon.misc.GameConstants;
import nz.artedungeon.utils.RSArea;
import nz.artedungeon.utils.Util;
import nz.uberutils.helpers.Options;

import java.util.ArrayList;
import java.util.LinkedList;



public abstract class Room extends DungeonCommon
{
    protected final RSArea area;
    protected LinkedList<Door> doors = new LinkedList<Door>();
    protected final ArrayList<ItemDef> groundItems = new ArrayList<ItemDef>();
    protected final ArrayList<EnemyDef> enemies = new ArrayList<EnemyDef>();
    protected Room parent;
    protected final Type type;

    public static enum Type
    {
        NORMAL, BOSS, PUZZLE
    }

    public static final Type NORMAL = Type.NORMAL;
    public static final Type BOSS = Type.BOSS;
    public static final Type PUZZLE = Type.PUZZLE;

    /**
     * Instantiates a new room.
     *
     * @param area   the area
     * @param doors  the doors
     * @param parent instance of main script
     */
    public Room(RSArea area, LinkedList<Door> doors, Type type, DungeonMain parent) {
        super(parent);
        this.area = area;
        this.doors = doors;
        this.type = type;
    }

    /**
     * Gets the area.
     *
     * @return the area
     */
    public RSArea getArea() {
        return area;
    }

    /**
     * Contains GameObject.
     *
     * @param object the object
     * @return true, if successful
     */
    public boolean contains(GameObject object) {
        return area.contains(object.getLocation());
    }

    /**
     * Contains NPC.
     *
     * @param npc the npc
     * @return true, if successful
     */
    public boolean contains(Npc npc) {
        return area.contains(npc.getLocation());
    }

    /**
     * Contains GroundItem.
     *
     * @param item the item
     * @return true, if successful
     */
    boolean contains(GroundItem item) {
        return area.contains(item.getLocation());
    }

    /**
     * Contains Tile.
     *
     * @param tile the tile
     * @return true, if successful
     */
    public boolean contains(Tile tile) {
        return area.contains(tile);
    }

    /**
     * Contains Door.
     *
     * @param door the door
     * @return true, if successful
     */
    public boolean contains(Door door) {
        return area.contains(door.getLocation());
    }

    /**
     * Contains any keys.
     *
     * @return true, if successful
     */
    public boolean containsKeys() {
        for (int[] keys : GameConstants.KEYS) {
            for (int key : keys) {
                for (ItemDef groundItem : groundItems) {
                    if (groundItem.getId() == key)
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Gets the doors.
     *
     * @return the doors
     */
    public LinkedList<Door> getDoors() {
        return doors;
    }

    /**
     * Number of doors.
     *
     * @return the int
     */
    int numDoors() {
        return doors.size();
    }

    /**
     * Center tile.
     *
     * @return the rS tile
     */
    Tile centerTile() {
        return area.getCentralTile();
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Object#equals(java.lang.Object)
      */
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Room))
            return false;
        Room room = (Room) o;
        return !(centerTile() == null || room.centerTile() == null) && (centerTile().equals(room.centerTile()));
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Object#hashCode()
      */
    public int hashCode() {
        return (area != null ? centerTile().hashCode() : 0);
    }

    /**
     * Gets the nearest door to player.
     *
     * @return the nearest door
     */
    public Door getNearestDoor() {
        Door closest = null;
        for (Door door : doors) {
            if (contains(door)) {
                if (door.canOpen() && !door.isOpen()) {
                    if (closest == null)
                        closest = door;
                    else {
                        if (Calculations.distanceTo(door.getLocation()) <
                            Calculations.distanceTo(closest.getLocation()))
                            closest = door;
                    }
                }
            }
        }
        return closest;
    }

    /**
     * Gets the closest door of any type.
     *
     * @return the closest door
     */
    public Door getClosestDoorAll() {
        int distance = 999999;
        Door closestDoor = null;
        for (Door door : doors) {
            int dTo = Calculations.distanceTo(door.getLocation());
            if (dTo < distance) {
                distance = dTo;
                closestDoor = door;
            }
        }
        return closestDoor;
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Object#toString()
      */
    public String toString() {
        return "Room " +
               Explore.getRooms().lastIndexOf(this) +
               " NumDoors:" +
               numDoors() +
               " Center: " +
               centerTile().toString();
    }

    /**
     * Gets the nearest enemy in room.
     *
     * @return the nearest enemy
     */
    public Npc getNearestEnemy() {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc t) {
                return !Util.arrayContains(GameConstants.NONARGRESSIVE_NPCS, t.getId()) &&
                       contains(t) &&
                       t.getHpPercent() > 0;
            }

        });
    }

    /**
     * Gets highest priority enemy in room
     *
     * @return Npc
     */
    public Npc getHighestPriorityEnemy() {
        Npc[] allNpcsInRoom = Npcs.getLoaded(new Filter<Npc>()
        {
            public boolean accept(Npc npc) {
                return !Util.arrayContains(GameConstants.NONARGRESSIVE_NPCS, npc.getId()) &&
                       contains(npc) &&
                       npc.getHpPercent() > 0;
            }
        });
        int highestPriority = -1;
        Npc highestPriorityNpc = null;
        for (Npc npc : allNpcsInRoom) {
            EnemyDef npcDef = new EnemyDef(npc, this);
            if (npcDef.getPriority() > highestPriority) {
                highestPriority = npcDef.getPriority();
                highestPriorityNpc = npc;
            }
        }
        return highestPriorityNpc;
    }

    /**
     * Gets highest priority enemy in room
     *
     * @return Npc
     */
    public Npc getHighestPriorityEnemy(Filter<Npc> filter) {
        Npc[] allNpcsInRoom = Npcs.getLoaded(filter);
        int highestPriority = -1;
        Npc highestPriorityNpc = null;
        for (Npc npc : allNpcsInRoom) {
            EnemyDef npcDef = new EnemyDef(npc, this);
            if (npcDef.getPriority() > highestPriority) {
                highestPriority = npcDef.getPriority();
                highestPriorityNpc = npc;
            }
        }
        return highestPriorityNpc;
    }

    /**
     * Returns nearest item in room
     *
     * @return the item
     * @author T1to1
     */
    public GroundItem getItem() {
        GroundItem item = GroundItems.getNearest(new Filter<GroundItem>()
        {
            public boolean accept(GroundItem t) {
                if (t == null)
                    return false;
                if (!area.contains(t.getLocation()))
                    return false;
                for (int[] j : GameConstants.LOOT_ITEMS) {
                    for (int i : j) {
                        if (i == t.getItem().getId()) {
                            return true;
                        }
                    }
                }
                if (Options.getBoolean("pickupLowLevelFood") &&
                    Util.arrayContains(GameConstants.FOODS, t.getItem().getId()))
                    return true;
                if (MyPlayer.getComplexity() > 4 &&
                    Util.arrayContains(GameConstants.COMPLEXITY_LOOT, t.getItem().getId()))
                    return true;
                if (t.getItem() != null &&
                    t.getItem().getName() != null &&
                    !MyPlayer.inCombat() &&
                    t.getItem().getName().contains("(b)"))
                    return true;
                Equipable equip = new Equipable(t.getItem().getName());
                return ItemHandler.shouldEquip(equip, equip.getEquipmentIndex());
            }
        });
        if (item != null)
            return item;
        else
            return null;
    }

    /**
     * Gets the connectors.
     *
     * @return the connectors
     */
    public ArrayList<Room> getConnectors() {
        ArrayList<Room> connectors = new ArrayList<Room>();
        int i = 0;
        for (Door door : doors) {
            if (door == null)
                continue;
            if (door.getConnector() != null) {
                connectors.add(door.getConnector());
            }
        }
        return connectors;
    }

    /**
     * Sets the parent.
     *
     * @param room the new parent
     */
    public void setParent(Room room) {
        parent = room;
    }

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    public Room getParent() {
        return parent;
    }

    /**
     * Gets the door for connector.
     *
     * @param connector the connector
     * @return the door for connector
     */
    public Door getDoorForConnector(Room connector) {
        for (Door door : doors) {
            if (door != null && door.getConnector() != null) {
                if (door.getConnector().equals(connector)) {
                    return door;
                }
            }
        }
        return null;
    }

    /*
     * Updates Door in door array
     * @param door the updated door
     */
    public void updateDoor(Door door) {
        doors.remove(door);
        doors.add(door);
    }

    /*
     * Returns closest door out of all doors to supplied door
     * @param door the door to find the closest door to
     */
    public Door getClosestDoorTo(Door door) {
        if (door == null)
            return null;
        Door returnDoor = null;
        double closest = Integer.MAX_VALUE;
        for (Door doorCheck : Explore.getDoors()) {
            if (Calculations.distanceBetween(door.getLocation(), doorCheck.getLocation()) < closest &&
                !door.equals(doorCheck)) {
                closest = Calculations.distanceBetween(door.getLocation(), doorCheck.getLocation());
                returnDoor = doorCheck;
            }
        }
        return returnDoor;
    }

    /**
     * Gets nearest GameObject to Player in Room
     *
     * @param ids the IDs of the GameObjects to search for
     * @return GameObject, if one is found, else null
     */
    public GameObject getNearestObject(final int... ids) {
        return Objects.getNearest(new Filter<GameObject>()
        {
            public boolean accept(GameObject gameObject) {
                return gameObject != null &&
                       nz.uberutils.helpers.Utils.arrayContains(ids, gameObject.getId()) &&
                       contains(gameObject);
            }
        });
    }

    /**
     * Gets nearest GameObject to Player in Room
     *
     * @param names the names of the GameObjects to search for
     * @return GameObject, if one is found, else null
     */
    public GameObject getNearestObject(final String... names) {
        return Objects.getNearest(new Filter<GameObject>()
        {
            public boolean accept(GameObject gameObject) {
                if (gameObject == null || gameObject.getDef() == null || gameObject.getDef().getName() == null)
                    return false;
                return nz.uberutils.helpers.Utils.arrayContains(names, gameObject.getDef().getName()) &&
                       contains(gameObject);
            }
        });
    }

    /**
     * Gets nearest Npc to Player in Room
     *
     * @param ids the ID of the Npc to search for
     * @return Npc, if one is found, else null
     */
    public Npc getNearestNpc(final int... ids) {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc Npc) {
                return Npc != null && nz.uberutils.helpers.Utils.arrayContains(ids, Npc.getId()) && contains(Npc);
            }
        });
    }

    public GameObject[] getObjects(final int... ids) {
        return Objects.getLoaded(new Filter<GameObject>()
        {

            public boolean accept(GameObject gameObject) {
                return contains(gameObject) && Util.arrayContains(ids, gameObject.getId());
            }
        });
    }

    public Npc getNearestNpc() {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc Npc) {
                return Npc != null;
            }
        });
    }

    /**
     * Gets nearest Npc to Player in Room
     *
     * @param name the name of the <tt>Npc</tt> to search for
     * @return <tt>Npc</tt>, if one is found, else <tt>null</tt>
     */
    public Npc getNearestNpc(final String name) {
        return Npcs.getNearest(new Filter<Npc>()
        {
            public boolean accept(Npc Npc) {
                return Npc != null &&
                       Npc.getName().toLowerCase().matches(name.toLowerCase()) &&
                       contains(Npc) &&
                       Npc.getHpPercent() > 0;
            }
        });
    }

    /**
     * Checks if room as an unOpen guardian door
     *
     * @return <tt>true</tt> if un open guardian door is found
     */
    public boolean hasUnOpenGuardianDoor() {
        for (Door door : doors) {
            if (door == null || door.getName() == null)
                continue;
            if (door.getName().contains("ardian") && !door.isOpen())
                return true;
        }
        return false;
    }

    /**
     * Gets nearest door in room to door
     *
     * @param door the <tt>Door</tt> to find closest <tt>Door</tt> to
     * @return <tt>Door</tt> closest <tt>Door</tt> in room to <tt>Door</tt>
     */
    public Door nearestDoorInRoomTo(Door door) {
        double dist = 99999;
        Door returnDoor = null;
        for (Door doorCheck : doors) {
            if (Calculations.distanceBetween(door.getLocation(), doorCheck.getLocation()) < dist) {
                dist = Calculations.distanceBetween(door.getLocation(), doorCheck.getLocation());
                returnDoor = doorCheck;
            }
        }
        return returnDoor;
    }

    /**
     * Gets door at tile
     *
     * @param tile the <tt>Tile</tt> to check
     * @return <tt>Door</tt> at <tt>Tile</tt> if one is found, else <tt>null</tt>
     */
    public Door getDoorAt(Tile tile) {
        for (Door door : doors) {
            if (door.getLocation().equals(tile))
                return door;
        }
        return null;
    }

    /**
     * Get <tt>Type</tt> of <tt>Door</tt>
     * Get Type of Door
     *
     * @return <tt>Type</tt> of the <tt>Door</tt>
     */
    public Type getType() {
        return type;
    }

    public boolean hasGroundItem(int... ids) {
        for (ItemDef item : groundItems)
            for (int id : ids)
                if (item.getId() == id)
                    return true;
        return false;
    }

    public boolean hasGroundItem(boolean contains, String... names) {
        for (ItemDef item : groundItems)
            for (String name : names)
                if ((contains) ? item.getName().contains(name) : item.getName().equals(name))
                    return true;
        return false;
    }

    public boolean hasGroundItem(String... names) {
        return hasGroundItem(false, names);
    }

    public void setGroundItems(ArrayList<ItemDef> items) {
        groundItems.clear();
        groundItems.addAll(items);
    }

    public boolean hasEnemy(int... ids) {
        for (EnemyDef enemy : enemies)
            for (int id : ids)
                if (enemy.id() == id)
                    return true;
        return false;
    }

    public boolean hasEnemy(boolean contains, String... names) {
        for (EnemyDef enemy : enemies)
            for (String name : names)
                if ((contains) ? enemy.name().contains(name) : enemy.name().equals(name))
                    return true;
        return false;
    }

    public boolean hasEnemy(String... names) {
        return hasGroundItem(false, names);
    }

    public boolean hasEnemies() {
        return enemies.size() > 0;
    }

    public void setEnemies(ArrayList<EnemyDef> items) {
        enemies.clear();
        enemies.addAll(items);
    }

    public void removeDoor(Door door) {
        doors.remove(door);
    }
}
