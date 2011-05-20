package nz.artedungeon.utils;

import nz.artedungeon.common.Plugin;
import nz.uberutils.helpers.Utils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/20/11
 * Time: 7:33 AM
 * Package: nz.artedungeon.utils;
 */
public class PluginFactory
{
    private        Map<String, Class> plugins  = new HashMap<String, Class>();
    private static PluginFactory      instance = null;

    public void setPlugins(final ArrayList<Plugin> plugins) {
        this.plugins.clear();
        for (Plugin p : plugins) {
            this.plugins.put(p.getClass().getSimpleName(), p.getClass());
        }
    }

    public Plugin createPlugin(String name) {
        for (String pName : plugins.keySet()) {
            if (pName.equals(name)) {
                Class pluginClass = plugins.get(pName);
                try {
                    Constructor productConstructor = pluginClass.getDeclaredConstructor();
                    return (Plugin) productConstructor.newInstance();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    public static PluginFactory instance() {
        if (instance == null)
            instance = new PluginFactory();
        return instance;
    }
}
