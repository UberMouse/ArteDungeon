package nz.uberutils.methods;

import org.rsbuddy.tabs.Inventory;
import org.rsbuddy.widgets.Bank;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/17/11
 * Time: 6:38 PM
 * Package: nz.uberutils.methods;
 */
public class MyBanking
{
    public static boolean makeInventoryCount(final int id, final int count) {
        if (!Bank.isOpen())
            return false;
        if (count != 0) {
            int invCount = Inventory.getCount(id);
            if (invCount != count) {
                if (invCount < count)
                    return Bank.withdraw(id, count - invCount);
                else
                    return Bank.deposit(id, invCount - count);
            }
            else
                return true;
        }
        else {
            if (Bank.getCount(id) > 0)
                return Bank.withdraw(id, 0);
            else
                return Inventory.getCount(id) > 0;
        }
    }
}
