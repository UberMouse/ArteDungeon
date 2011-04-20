package nz.uberutils.helpers.tasks;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.script.methods.Environment;
import com.rsbuddy.script.task.LoopTask;
import nz.uberutils.helpers.Utils;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/18/11
 * Time: 5:59 PM
 * Package: nz.uberutils.helpers.tasks;
 */
public class ImageThread extends LoopTask implements MessageListener
{
    private static boolean hourly;
    private static boolean levelup;
    private static long startTime;
    private static String name;
    private static int hour = 1;
    private static boolean firstRun = true;
    private static boolean onFinish;

    public ImageThread(String name, boolean hourly, boolean levelup, boolean onFinish) {
        startTime = System.currentTimeMillis();
        ImageThread.hourly = hourly;
        ImageThread.levelup = levelup;
        ImageThread.name = name;
        ImageThread.onFinish = onFinish;
    }

    public ImageThread(String name) {
        this(name, true, true, true);
    }

    @Override
    public int loop() {
        if (hourly && !firstRun) {
            saveImage(hour + "h");
            hour++;
        }
        if (firstRun)
            firstRun = false;
        return 3600000;
    }

    public boolean takeScreenShot() {
        return false;
    }

    public void messageReceived(MessageEvent messageEvent) {
        if (levelup) {
            if (messageEvent.isAutomated()) {
                if (messageEvent.getMessage().contains("You've just advanced"))
                    saveImage("levelup");
            }
        }
    }

    public void saveImage(String name) {
        try {
            Utils.saveImage(Environment.takeScreenshot(true),
                            Environment.getStorageDirectory().getCanonicalPath() +
                            "\\artebots\\" +
                            ImageThread.name +
                            "-" +
                            name +
                            "-" +
                            String.valueOf(Utils.random(1, 9999)) +
                            ".png",
                            "png");
        } catch (IOException ignored) {
        }
    }

    public void onFinish() {
        if(onFinish)
            saveImage("end");
    }
}
