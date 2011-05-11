package nz.uberutils.wrappers;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/8/11
 * Time: 7:05 PM
 * Package: nz.uberutils.wrappers;
 */
public class BankItem
{
    private final int id;
    private final String name;
    private final int quantity;

    public BankItem(int id, int quantity) {
        this(id, null, quantity);
    }

    public BankItem(String name, int quantity) {
        this(-1, null, quantity);
    }

    public BankItem(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }
}
