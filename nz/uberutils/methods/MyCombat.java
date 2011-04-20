package nz.uberutils.methods;

import com.rsbuddy.script.methods.Combat;
import com.rsbuddy.script.methods.Widgets;
import nz.uberutils.helpers.Utils;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/10/11
 * Time: 11:57 AM
 * Package: nz.uberutils.methods;
 */
public class MyCombat
{
    public static void toggleQuickPrayers() {
        if (Widgets.getComponent(749, 5).click()) {
            for (int i = 0; i < 15 && !Combat.isQuickPrayerOn(); i++)
                Utils.sleep(100);
            Utils.sleep(600, 700);
        }
    }
}
