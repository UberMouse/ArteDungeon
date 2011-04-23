package nz.uberutils.arte;

import com.rsbuddy.event.events.MessageEvent;
import com.rsbuddy.event.listeners.MessageListener;
import com.rsbuddy.script.methods.Players;
import com.rsbuddy.script.task.LoopTask;
import com.rsbuddy.script.util.Timer;
import nz.uberutils.helpers.Utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class ArteNotifier extends LoopTask implements MessageListener
{

    private static String feedbackUrl = "http://rsbuddy.com/forum/showthread.php?t=";
    private static String purchaseUrl = "http://artebots.com";
    private static boolean isLiteScript;
    private static String messageOfTheDay = "No news from Artemis Productions today!";
    private static String messageUrl = "http://artebots.com";
    private static final String artebots = "http://artebots.com";
    private static TrayIcon trayIcon = null;
    private static long startMillis, lastMillis;
    private static boolean gns = false, gnu = false, gnh = false;
    private static ActionListener iconListener;
    private static final Timer hTimer = new Timer(3600000), bTimer = new Timer(1800000);

    static boolean[] notifs = new boolean[6];
    String lvl, sender;
    boolean leveled = false, died = false, name = false;
    int hrs, bhrs;

    public static class Notifiers
    {
        static final int LEVEL_UPS = 0,
                HOURLY = 1,
                BIHOURLY = 2,
                NAME_HEARD = 3,
                DEATHS = 4,
                ON_FINISH = 5;
    }

    public ArteNotifier(int threadID, String purchaseURL, boolean lite,
                        boolean onLevelUp, boolean onHourly, boolean onHalfHourly,
                        boolean onNameHeard, boolean onDeath, boolean onFinish) {
        ArteNotifier.feedbackUrl = ArteNotifier.feedbackUrl + threadID;
        ArteNotifier.purchaseUrl = purchaseURL;
        isLiteScript = lite;
        setNotifs(onLevelUp, onHourly, onHalfHourly, onNameHeard, onDeath, onFinish);
    }

    public ArteNotifier(int threadID) {
        this(threadID, "", false, false, false, false, false, false, false);
    }

    public ArteNotifier(int threadID, boolean onLevelUp, boolean onHourly, boolean onHalfHourly,
                        boolean onNameHeard, boolean onDeath, boolean onFinish) {
        this(threadID, "", false, onLevelUp, onHourly, onHalfHourly, onNameHeard, onDeath, onFinish);
    }

    public ArteNotifier(int threadID, boolean onLevelUp) {
        this(threadID, "", false, onLevelUp, false, false, false, false, false);
    }

    public ArteNotifier(String feedbackURL, String purchaseURL, boolean lite,
                        boolean onLevelUp, boolean onHourly, boolean onHalfHourly,
                        boolean onNameHeard, boolean onDeath, boolean onFinish) {
        ArteNotifier.feedbackUrl = feedbackURL;
        ArteNotifier.purchaseUrl = purchaseURL;
        isLiteScript = lite;
        setNotifs(onLevelUp, onHourly, onHalfHourly, onNameHeard, onDeath, onFinish);
    }

    public ArteNotifier(String feedbackURL) {
        this(feedbackURL, "", false, false, false, false, false, false, false);
    }

    public ArteNotifier(String feedbackURL, boolean onLevelUp, boolean onHourly, boolean onHalfHourly,
                        boolean onNameHeard, boolean onDeath, boolean onFinish) {
        this(feedbackURL, "", false, onLevelUp, onHourly, onHalfHourly, onNameHeard, onDeath, onFinish);
    }

    public ArteNotifier(String feedbackURL, boolean onLevelUp) {
        this(feedbackURL, "", false, onLevelUp, false, false, false, false, false);
    }

    public boolean onStart() {
        URL url = null;
        URL urll;
        BufferedReader br = null;
        BufferedReader brl = null;
        try {
            url = new URL("http://artebots.com/notify/message");
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            messageOfTheDay = br.readLine();
            urll = new URL("http://artebots.com/notify/location");
            brl = new BufferedReader(new InputStreamReader(urll.openStream()));
            messageUrl = brl.readLine();
            addToTray();
            sendNotification("Starting script.", TrayIcon.MessageType.INFO);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        startMillis = System.currentTimeMillis();
        hrs = 0;
        bhrs = 0;
        return true;
    }

    private enum State
    {
        SLEEP
    }

    private State getState() {
        return State.SLEEP;
    }

    public int loop() {
        try {
            // GLOBAL NOTIFICATION
            if (System.currentTimeMillis() - startMillis >= 29000 && !gns) {
                sendNotification(messageOfTheDay, TrayIcon.MessageType.INFO);
                gns = true;
            }
            if (System.currentTimeMillis() - startMillis >= 59000 && !gnu) {
                messageUrl = "http://artebots.com";
                gnu = true;
            }
            // HOURLY NOTIFICATION
            if (!hTimer.isRunning() && notifs[Notifiers.HOURLY]) {
                hrs++;
                switch (hrs) {
                    case 1:
                        sendNotification("Running for 1 hour!", TrayIcon.MessageType.INFO);
                        break;
                    default:
                        sendNotification("Running for " + hrs + " hours!", TrayIcon.MessageType.INFO);
                        break;
                }
                hTimer.reset();
            }
            // BIHOURLY NOTIFICATION
            if (!bTimer.isRunning() && notifs[Notifiers.BIHOURLY]) {
                bhrs++;
                switch (bhrs) {
                    case 1:
                        sendNotification("Running for 30 minutes!", TrayIcon.MessageType.INFO);
                        break;
                    default:
                        String bihours = bhrs % 2 == 0 ? Integer.toString(bhrs / 2) : Integer.toString(bhrs / 2) + ".5";
                        if (bihours.equals("1"))
                            sendNotification("Running for 1 hour!", TrayIcon.MessageType.INFO);
                        else
                            sendNotification("Running for " + bihours + " hours!", TrayIcon.MessageType.INFO);
                        break;
                }
                bTimer.reset();
            }
            // DEATH NOTIFICATION
            if (died && notifs[Notifiers.DEATHS]) {
                sendNotification("Your character has died.", TrayIcon.MessageType.WARNING);
                died = false;
            }
            // LEVEL NOTIFICATION
            if (leveled && notifs[Notifiers.LEVEL_UPS]) {
                sendNotification("Gained an " + lvl + "!", TrayIcon.MessageType.INFO);
                leveled = false;
            }
            // NAMEHEARD NOTIFICATION
            if (name && notifs[Notifiers.NAME_HEARD]) {
                sendNotification("Your name was said by " + sender + ".", TrayIcon.MessageType.WARNING);
                name = false;
            }
            switch (getState()) {
                case SLEEP:
                    return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void onFinish() {
        // ON FINISH NOTIFICATION
        if (notifs[Notifiers.ON_FINISH]) {
            sendNotification("Script has stopped.", TrayIcon.MessageType.INFO);
            sleep(5000);
        }
        try {
            SystemTray.getSystemTray().remove(trayIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void messageReceived(MessageEvent e) {
        String s = e.getMessage();
        if (s.contains("You've just advanced a")) {
            lvl = s.substring(24, s.indexOf("level") - 2);
            leveled = true;
        }
        if (s.contains("Oh dear you are dead.")) {
            died = true;
        }
        if (s.contains(Players.getLocal().getName())
            && !e.getSender().equals(Players.getLocal().getName())) {
            name = true;
            sender = e.getSender();
        }
    }

    public static boolean addToTray() {
        try {
            if (SystemTray.isSupported()) {
                SystemTray tray = SystemTray.getSystemTray();
                URL url = null;
                Image image = null;
                url = new URL("http://wildimp.com/arte/server_connect.png");
                image = ImageIO.read(url);
                ActionListener listener = new ActionListener()
                {
                    public void actionPerformed(ActionEvent e) {
                        String s = ((MenuItem) (e.getSource())).getLabel();
                        if (s.equalsIgnoreCase("Leave feedback"))
                            Utils.openURL(feedbackUrl);
                        if (s.equalsIgnoreCase("Visit ArteBots.com"))
                            Utils.openURL(artebots);
                        if (s.equalsIgnoreCase("Purchase Pro"))
                            Utils.openURL(purchaseUrl);
                    }
                };
                PopupMenu popup = new PopupMenu();
                MenuItem fItem = new MenuItem("Leave feedback");
                fItem.addActionListener(listener);
                MenuItem pItem = null;
                if (isLiteScript) {
                    pItem = new MenuItem("Purchase Pro");
                    pItem.addActionListener(listener);
                }
                MenuItem dItem = new MenuItem("-");
                MenuItem aItem = new MenuItem("Visit ArteBots.com");
                aItem.addActionListener(listener);
                popup.add(fItem);
                if (isLiteScript)
                    popup.add(pItem);
                popup.add(dItem);
                popup.add(aItem);
                trayIcon = new TrayIcon(image, "Artemis Productions", popup);
                iconListener = new ActionListener()
                {
                    public void actionPerformed(ActionEvent e) {
                        Utils.openURL(messageUrl);
                    }
                };
                trayIcon.addActionListener(iconListener);
                tray.add(trayIcon);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void sendNotification(String text, TrayIcon.MessageType messageType) {
        try {
            trayIcon.displayMessage("Artemis Productions", text, messageType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setNotifs(boolean levelups, boolean hourly, boolean bihourly,
                                 boolean nameHeard, boolean deaths, boolean onFinish) {
        notifs[Notifiers.LEVEL_UPS] = levelups;
        notifs[Notifiers.BIHOURLY] = bihourly;
        notifs[Notifiers.HOURLY] = !bihourly && hourly;
        notifs[Notifiers.NAME_HEARD] = nameHeard;
        notifs[Notifiers.DEATHS] = deaths;
        notifs[Notifiers.ON_FINISH] = onFinish;
    }
}