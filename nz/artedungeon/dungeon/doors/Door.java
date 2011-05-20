package nz.artedungeon.dungeon.doors;


import com.rsbuddy.script.methods.Calculations;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Tile;
import nz.artedungeon.DungeonMain;
import nz.artedungeon.common.DungeonCommon;
import nz.artedungeon.dungeon.Explore;
import nz.artedungeon.dungeon.rooms.Room;
import nz.artedungeon.utils.Util;



public abstract class Door extends DungeonCommon
{
    protected final int id;
    protected final Tile location;
    protected boolean locked;
    protected boolean open;
    protected String name;
    protected int connector = -1;
    protected final Type type;
    protected GameObject object;

    protected static enum Type
    {
        KEY, SKILL, NORMAL
    }

    public static final Type KEY = Type.KEY;
    public static final Type SKILL = Type.SKILL;
    public static final Type NORMAL = Type.NORMAL;

    /**
     * Instantiates a new door.
     *
     * @param door   the door
     * @param type   the Type of door
     * @param parent instance of main script
     */
    public Door(GameObject door, Type type, DungeonMain parent) {
        super(parent);
        object = door;
        id = door.getId();
        location = door.getLocation();
        this.type = type;
        if (door.getDef() != null) {
            if (type != SKILL)
                locked = !type.equals(Type.NORMAL);
            name = door.getDef().getName().replace(" door", "");
        }
    }

    /**
     * Get the GameObject of the door
     * @return GameObject door
     */
    public GameObject getObject() {
        return object;
    }

    /**
     * Sets door GameObject to new one
     * @param model new GameObject
     */
    public void setObject(GameObject model) {
        this.object = model;
    }

    /**
     * Gets the iD.
     *
     * @return the iD
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the location.
     *
     * @return the location
     */
    public Tile getLocation() {
        return location;
    }

    /**
     * Checks if is locked.
     *
     * @return true, if is locked
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Checks if is open.
     *
     * @return true, if is open
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the connector.
     *
     * @return the connector
     */
    public Room getConnector() {
        if (connector == -1)
            return null;
        else
            return Explore.getRooms().get(connector);
    }

    /**
     * Sets the connector.
     *
     * @param connector the new connector
     */
    public void setConnector(Room connector) {
        this.connector = Explore.getRooms().indexOf(connector);
    }

    /**
     * Sets the open state.
     *
     * @param open the new state
     */
    public void setOpen(boolean open) {
        this.open = open;
    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Object#equals(java.lang.Object)
      */
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return o instanceof Door && (((Door) o).id == id && ((Door) o).getLocation().equals(location));

    }

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Object#hashCode()
      */
    public int hashCode() {
        return (location != null ? location.hashCode() : 0);
    }

    /**
     * Can open.
     *
     * @return true, if successful
     */
    public abstract boolean canOpen();

    /*
      * (non-Javadoc)
      *
      * @see java.lang.Object#toString()
      */
    public String toString() {
        return name + ": " + locked + " Location: " + location.toString();
    }

    /**
     * Checks if is visible.
     *
     * @return true, if is visible
     */
    public boolean isVisible() {
        return Calculations.isTileOnScreen(location);
    }

    /**
     * Open door.
     */
    public abstract void open();

    /**
     * Gets the parent.
     *
     * @return the parent
     */
    public Room getParent() {
        for (Room room : Explore.getRooms()) {
            if (room.contains(this))
                return room;
        }
        return null;
    }

    /**
     * Get action that should be done for GameObject
     * @param object to check actions of
     * @return String correct action to do to open door
     */
    public String getAction(GameObject object) {
        if (object == null || object.getDef() == null)
            return null;
        String[] actions = object.getDef().getActions();
        if (Util.arrayContains(actions, true, "unlock"))
            return "unlock";
        if (Util.arrayContains(actions, true, "burn"))
            return "burn";
        if (Util.arrayContains(actions, true, "mine"))
            return "mine";
        if (Util.arrayContains(actions, true, "open"))
            return "open";
        if (Util.arrayContains(actions, true, "enter"))
            return "enter";
        return actions[0];
    }

    /**
     * Return Type of door
     * @return Type type
     */
    public Type getType() {
        return type;
    }
}
