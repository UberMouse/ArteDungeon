package nz.uberutils.wrappers;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/8/11
 * Time: 7:04 PM
 * Package: nz.uberutils.wrappers;
 */
public class LootItem
{
    private final int id;
    private final String name;
    private final boolean lootInCombat;

    public LootItem(int id) {
        this(id, null, false);
    }

    public LootItem(String name) {
        this(-1, name, false);
    }

    public LootItem(int id, boolean lootInCombat) {
        this(id, null, lootInCombat);
    }

    public LootItem(String name, boolean lootInCombat) {
        this(-1, name, lootInCombat);
    }

    public LootItem(int id, String name, boolean lootInCombat) {
        this.id = id;
        this.name = name;
        this.lootInCombat = lootInCombat;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean lootInCombat() {
        return lootInCombat;
    }
}
