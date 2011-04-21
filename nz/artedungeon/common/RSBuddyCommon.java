package nz.artedungeon.common;

import com.rsbuddy.script.util.Random;
import nz.artedungeon.DungeonMain;


// TODO: Auto-generated Javadoc
public class RSBuddyCommon
{
    protected static DungeonMain parent;

    /**
     * Instantiates a new RSBuddyCommon.
     *
     * @param parent instance of main script
     */
    public RSBuddyCommon(DungeonMain parent) {
        RSBuddyCommon.parent = parent;
    }

    /**
     * Debugs text along with function caller and line numbers.
     *
     * @param text the text
     */
    public static void debug(Object text) {
        if (DungeonMain.debug) {
            String className = getCurClassName();
            if (className.contains("$"))
                className = className.split("\\$")[1];
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            StackTraceElement stacktrace = stackTraceElements[2];
            String methodName = stacktrace.getMethodName();
            int lineNumber = stacktrace.getLineNumber();
            parent.log("["
                       + stackTraceElements[3].getClassName()
                       + "#"
                       + stackTraceElements[3].getMethodName()
                       + ":"
                       + stackTraceElements[3].getLineNumber()
                       + "] -> [" + className + "." + methodName + ":"
                       + lineNumber + "] -> " + text);
        }
    }

    public static String getLastMethod() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement stacktrace = stackTraceElements[3];
        return stacktrace.getMethodName();
    }

    /**
     * Gets the current class name.
     *
     * @return the currrent class name
     */
    private static String getCurClassName() {
        return (new CurClassNameGetter()).getClassName();
    }

    public static class CurClassNameGetter extends SecurityManager
    {

        /**
         * Gets the class name.
         *
         * @return the class name
         */
        public String getClassName() {
            return getClassContext()[3].getName();
        }
    }

    /**
     * Sleep script.
     *
     * @param min the min time
     * @param max the max time
     */
    protected static void sleep(int min, int max) {
        parent.sleep(random(min, max));
    }

    /**
     * Sleep script.
     *
     * @param time the time
     */
    protected static void sleep(int time) {
        parent.sleep(time);
    }

    /**
     * Generate random number.
     *
     * @param min the min value
     * @param max the max value
     * @return the random number
     */
    protected static int random(int min, int max) {
        return Random.nextInt(min, max);
    }
}
