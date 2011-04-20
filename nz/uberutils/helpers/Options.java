package nz.uberutils.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/3/11
 * Time: 10:38 AM
 * Package: nz.artezombies.misc;
 */
public class Options
{
    private static Preferences prefs = Preferences.userRoot().node("UberOptions");
    private static final Map<String, String> stringDefaults = new HashMap<String, String>();
    private static final Map<String, Integer> intDefaults = new HashMap<String, Integer>();
    private static final Map<String, Long> longDefaults = new HashMap<String, Long>();
    private static final Map<String, Boolean> booleanDefaults = new HashMap<String, Boolean>();
    private static final Map<String, Float> floatDefaults = new HashMap<String, Float>();
    private static final Map<String, Double> doubleDefaults = new HashMap<String, Double>();
    private static final Map<String, byte[]> byteDefaults = new HashMap<String, byte[]>();

    public static void setNode(String name) {
        prefs = Preferences.userRoot().node(name);
    }

    public static void save() {
        try {
            prefs.flush();
        } catch (BackingStoreException ignored) {
        }
    }

    public static void flip(String name) {
        putBoolean(name, !getBoolean(name));
    }

    public static void addOption(String s, String s1) {
        stringDefaults.put(s, s1);
    }

    public static void addOption(String s, int i) {
        intDefaults.put(s, i);
    }

    public static void addOption(String s, long l) {
        longDefaults.put(s, l);
    }

    public static void addOption(String s, boolean b) {
        booleanDefaults.put(s, b);
    }

    public static void addOption(String s, float f) {
        floatDefaults.put(s, f);
    }

    public static void addOption(String s, double d) {
        doubleDefaults.put(s, d);
    }

    public static void addOption(String s, byte[] bytes) {
        byteDefaults.put(s, bytes);
    }

    public static void put(String s, String s1) {
        prefs.put(s, s1);
        stringDefaults.put(s, s1);
        save();
    }

    public static String get(String s) {
        return prefs.get(s, stringDefaults.get(s));
    }

    public static void putInt(String s, int i) {
        prefs.putInt(s, i);
        intDefaults.put(s, i);
        save();
    }

    public static int getInt(String s) {
        return prefs.getInt(s, intDefaults.get(s));
    }

    public static void putLong(String s, long l) {
        prefs.putLong(s, l);
        longDefaults.put(s, l);
        save();
    }

    public static long getLong(String s) {
        return prefs.getLong(s, longDefaults.get(s));
    }

    public static void putBoolean(String s, boolean b) {
        prefs.putBoolean(s, b);
        booleanDefaults.put(s, b);
        save();
    }

    public static boolean getBoolean(String s) {
        return prefs.getBoolean(s, booleanDefaults.get(s));
    }

    public static void putFloat(String s, float v) {
        prefs.putFloat(s, v);
        floatDefaults.put(s, v);
        save();
    }

    public static float getFloat(String s) {
        return prefs.getFloat(s, floatDefaults.get(s));
    }

    public static void putDouble(String s, double v) {
        prefs.putDouble(s, v);
        doubleDefaults.put(s, v);
        save();
    }

    public static double getDouble(String s) {
        return prefs.getDouble(s, doubleDefaults.get(s));
    }

    public static void putByteArray(String s, byte[] bytes) {
        prefs.putByteArray(s, bytes);
        byteDefaults.put(s, bytes);
        save();
    }

    public static byte[] getByteArray(String s) {
        return prefs.getByteArray(s, byteDefaults.get(s));
    }

    public static boolean containsKey(String s) {
        try {
            for (String key : prefs.keys()) {
                if (key.toLowerCase().equals(s.toLowerCase()))
                    return true;
            }
        } catch (BackingStoreException ignored) {
        }
        return false;
    }
}