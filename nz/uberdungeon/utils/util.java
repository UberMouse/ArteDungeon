package nz.uberdungeon.utils;

import com.rsbuddy.script.methods.Equipment;
import com.rsbuddy.script.methods.Game;
import com.rsbuddy.script.methods.Mouse;
import com.rsbuddy.script.methods.Walking;
import com.rsbuddy.script.util.Random;
import com.rsbuddy.script.wrappers.GameObject;
import com.rsbuddy.script.wrappers.Item;
import com.rsbuddy.script.wrappers.Model;
import com.rsbuddy.script.wrappers.Tile;
import nz.uberdungeon.dungeon.MyPlayer;
import nz.uberdungeon.misc.GameConstants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
public class util
{

    /**
     * Check if array contains String(s) check.
     *
     * @param array     the array to check
     * @param contains, using .contains instead of .equals
     * @param check     the String(s) to check
     * @return true, if successful
     */
    public static boolean arrayContains(String[] array, boolean contains, String... check) {
        if (array == null || check == null || array.length < 1)
            return false;
        for (String i : array) {
            if (i == null)
                continue;
            for (String l : check) {
                if (l == null)
                    continue;
                if (contains) {
                    if (l.toLowerCase().contains(i.toLowerCase())) {
                        return true;
                    }
                }
                else {
                    if (i.toLowerCase().equals(l.toLowerCase()))
                        return true;
                }
            }
        }
        return false;
    }

    /**
     * Check if array contains String(s) check.
     *
     * @param array the array
     * @param check the check
     * @return true, if successful
     */
    public static boolean arrayContains(String[] array, String... check) {
        return arrayContains(array, false, check);
    }

    /**
     * Check if array contains int(s) check.
     *
     * @param array the array
     * @param check the check
     * @return true, if successful
     */
    public static boolean arrayContains(int[] array, int... check) {
        if (array == null || check == null || array.length < 1)
            return false;
        for (int i : array) {
            for (int l : check) {
                if (i == l)
                    return true;
            }
        }
        return false;
    }

    public static boolean arrayContains(int[][] arrays, int... check) {
        for (int[] array : arrays) {
            if (arrayContains(array, check))
                return true;
        }
        return false;
    }

    public static Tile getNearestNonWallTile(Tile tile) {
        return getNearestNonWallTile(tile, false);
    }

    public static Tile getNearestNonWallTile(Tile tile, boolean eightTiles) {
        Tile[] checkTiles = getSurroundingTiles(tile, eightTiles);
        int[] flags = getSurroundingCollisionFlags(tile, eightTiles);
        for (int i = 0; i < checkTiles.length; i++) {
            if ((flags[i] & GameConstants.WALL) == 0)
                return checkTiles[i];
//            else
//                getNearestNonWallTile(tile);
        }
        return null;
    }

    public static Tile[] getSurroundingTiles(Tile tile) {
        return getSurroundingTiles(tile, false);
    }

    public static Tile[] getSurroundingTiles(Tile tile, boolean eightTiles) {
        int x = tile.getX();
        int y = tile.getY();
        Tile north = new Tile(x, y + 1);
        Tile east = new Tile(x + 1, y);
        Tile south = new Tile(x, y - 1);
        Tile west = new Tile(x - 1, y);
        Tile northEast;
        Tile southEast;
        Tile southWest;
        Tile northWest;
        if (eightTiles) {
            northEast = new Tile(x + 1, y + 1);
            southEast = new Tile(x + 1, y - 1);
            southWest = new Tile(x - 1, y - 1);
            northWest = new Tile(x - 1, y + 1);
            return new Tile[]{north, northEast, east, southEast, south, southWest, west, northWest};
        }
        return new Tile[]{north, east, south, west};
    }

    public static int[] getSurrouncingCollisionFlags(Tile tile) {
        return getSurroundingCollisionFlags(tile, false);
    }

    public static int[] getSurroundingCollisionFlags(Tile tile, boolean eightTiles) {
        int[][] flags = Walking.getCollisionFlags(Game.getFloorLevel());
        int x = tile.getX();
        int y = tile.getY();
        int xOff = x - Game.getMapBase().getX() - Walking.getCollisionOffset(Game.getFloorLevel()).getX();
        int yOff = y - Game.getMapBase().getY() - Walking.getCollisionOffset(Game.getFloorLevel()).getY();
        int fNorth = flags[xOff][yOff + 1];
        int fEast = flags[xOff + 1][yOff];
        int fSouth = flags[xOff][yOff - 1];
        int fWest = flags[xOff - 1][yOff];
        int fNorthEast;
        int fSouthEast;
        int fSouthWest;
        int fNorthWest;
        if (eightTiles) {
            fNorthEast = flags[xOff + 1][yOff + 1];
            fSouthEast = flags[xOff + 1][yOff - 1];
            fSouthWest = flags[xOff - 1][yOff - 1];
            fNorthWest = flags[xOff - 1][yOff + 1];
            return new int[]{fNorth, fNorthEast, fEast, fSouthEast, fSouth, fSouthWest, fWest, fNorthWest};
        }
        return new int[]{fNorth, fEast, fSouth, fWest};
    }

    public static int getCollisionFlagAtTile(Tile tile) {
        int[][] flags = Walking.getCollisionFlags(Game.getFloorLevel());
        int x = tile.getX();
        int y = tile.getY();
        int xOff = x - Game.getMapBase().getX() - Walking.getCollisionOffset(Game.getFloorLevel()).getX();
        int yOff = y - Game.getMapBase().getY() - Walking.getCollisionOffset(Game.getFloorLevel()).getY();
        return flags[xOff][yOff];
    }

    public static boolean playerHas(String name) {
        return playerHas(name, false);
    }

    public static boolean playerHas(String name, boolean cached) {
        return inventoryContains(name) || equipmentContains(name, cached);
    }

    public static boolean inventoryContains(String name) {
        for (Item item : MyInventory.getItems()) {
            if (item.getName().toLowerCase().contains(name.toLowerCase()))
                return true;
        }
        return false;
    }

    public static boolean equipmentContains(String name, boolean cached) {
        for (Item item : (cached) ? MyEquipment.getCachedItems() : Equipment.getItems()) {
            if (item.getName().toLowerCase().contains(name.toLowerCase()))
                return true;
        }
        return false;
    }

    public static boolean equipmentContains(String name) {
        return equipmentContains(name, false);
    }

    public static Item getInventoryItem(String name) {
        for (Item i : MyInventory.getItems()) {
            if (i.getName().toLowerCase().contains(name.toLowerCase())) return i;
        }
        return null;
    }

    public static void saveImage(Image image, String location) {
        saveImage(image, location, null);
    }

    public static void saveImage(Image image, String location, String type) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                                                        image.getHeight(null), BufferedImage.TYPE_INT_RGB);

        //using "painter" we can draw in to "bufferedImage"
        Graphics2D painter = bufferedImage.createGraphics();

        //draw the "image" to the "bufferedImage"
        painter.drawImage(image, null, null);

        //the new image file
        File outputImg = new File(location);
        if (!outputImg.exists())
            try {
                outputImg.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        //write the image to the file
        try {
            ImageIO.write(bufferedImage, (type != null) ? type : "jpg", outputImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void openURL(String url) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return;
        }

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            return;
        }
        try {

            java.net.URI uri = new java.net.URI(url);
            desktop.browse(uri);
        } catch (Exception ignored) {
        }
    }

    /**
     * Check if tile is in current room
     *
     * @param tile to check
     * @return true if it is
     */
    public static boolean tileInRoom(Tile tile) {
        AStar pf = new AStar()
        {
            @Override
            public java.util.List<AStar.Node> customSuccessors(Node t) {
                java.util.LinkedList<Node> path = new java.util.LinkedList<Node>();
                for (Tile tile : getSurroundingTiles(t.toTile(Game.getMapBase().getX(), Game.getMapBase().getY()),
                                                     true)) {
                    if ((getCollisionFlagAtTile(tile) & GameConstants.WALL) == 0) {
                        path.add(new Node(tile.getX() - Game.getMapBase().getX(),
                                          tile.getY() - Game.getMapBase().getY()));
                    }
                }
                return path;
            }
        };
        if (pf.findPath(MyPlayer.location(), tile) != null)
            return true;
        else
            return false;
    }

    public static Object callMethod(Object object, String name) {
        Method[] allMethods = object.getClass().getDeclaredMethods();
        for (Method m : allMethods) {
            String mname = m.getName();
            if (!mname.equals(name)) {
                continue;
            }
            m.setAccessible(true);
            try {
                return m.invoke(object);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Handle any exceptions thrown by method to be invoked.
        }
        return null;
    }

    public static Object callMethod(Object object, String name, Object argument) {
        try {
            Method[] allMethods = object.getClass().getDeclaredMethods();
            for (Method m : allMethods) {
                String mname = m.getName();
                if (!mname.equals(name)) {
                    continue;
                }
                try {
                    m.setAccessible(true);
                    return m.invoke(object, argument);

                    // Handle any exceptions thrown by method to be invoked.
                } catch (InvocationTargetException x) {
                    Throwable cause = x.getCause();
                    cause.printStackTrace();
                }
            }
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        }
        return null;
    }

    /**
     * Gets center point of GameObject
     *
     * @param obj GameObject to get center point of
     * @return Point representing center point of model if one was found, else null
     */
    public static Point getModelCenter(GameObject obj) {
        if (obj == null || obj.getModel() == null)
            return null;
        Model model = obj.getModel();
        Polygon[] polygons = model.getTriangles();
        System.out.println(polygons.length);
        ArrayList<Point> points = new ArrayList<Point>();
        for (Polygon polygon : polygons) {
            for (int i = 0; i < polygon.npoints; i++) {
                points.add(new Point(polygon.xpoints[i], polygon.ypoints[i]));
            }
        }
        int xTotal = 0;
        int yTotal = 0;
        for (Point p : points) {
            xTotal += p.x;
            yTotal += p.y;
        }
        if (points.size() <= 5) {
            System.out.println("Points < 5");
            return null;
        }
        return new Point(xTotal / points.size(), yTotal / points.size());
    }

    /**
     * Parse time millis into readable String format
     *
     * @param millis to parse
     * @return parsed millis
     */
    public static String parseTime(long millis) {
        long time = millis / 1000;
        String seconds = Integer.toString((int) (time % 60));
        String minutes = Integer.toString((int) ((time % 3600) / 60));
        String hours = Integer.toString((int) (time / 3600));
        for (int i = 0; i < 2; i++) {
            if (seconds.length() < 2) {
                seconds = "0" + seconds;
            }
            if (minutes.length() < 2) {
                minutes = "0" + minutes;
            }
            if (hours.length() < 2) {
                hours = "0" + hours;
            }
        }
        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * Click random tile in the current room on the map
     */
    public static void clickRandomTileOnMap() {
        Tile[] tiles = MyPlayer.currentRoom().getArea().getTileArray();
        Tile randTile = tiles[Random.nextInt(0, tiles.length)];
        randTile.clickOnMap();
    }

    /**
     * Click Item
     *
     * @param item to click
     */
    public static void clickItem(Item item) {
        Mouse.move(item.getComponent().getCenter());
        Mouse.moveRandomly(0, 4);
        Mouse.click(true);
    }
}