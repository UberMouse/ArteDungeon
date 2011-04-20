package nz.uberutils.paint.components;

import nz.uberutils.helpers.Utils;
import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/20/11
 * Time: 2:12 AM
 * Package: nz.uberutils.paint.components;
 */
public class PListBox extends PComponent
{
    private final ArrayList<Item> CONTENT = new ArrayList<Item>();
    private final ColorScheme SCHEME;

    public PListBox(int x, int y, int width, int height, String[] content, ColorScheme scheme) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        for (String s : content)
            CONTENT.add(new Item(s));
        this.SCHEME = scheme;
    }

    public PListBox(int x, int y, int height, String[] content, ColorScheme scheme) {
        this(x, y, -1, height, content, scheme);
    }

    public PListBox(int x, int y, int height, String[] content) {
        this(x, y, -1, height, content, PListBox.ColorScheme.GRAPHITE);
    }

    public PListBox(int x, int y, int height) {
        this(x, y, -1, height, null, PListBox.ColorScheme.GRAPHITE);
    }

    @Override
    public void repaint(Graphics2D g) {
    }

    public static enum ColorScheme
    {
        GRAPHITE(new Color(55, 55, 55, 240), new Color(15, 15, 15, 240), new Color(200, 200, 200)),
        LIME(new Color(0, 187, 37, 240), new Color(0, 128, 28), new Color(255, 153, 51)),
        HOT_PINK(new Color(255, 102, 204, 240), new Color(255, 0, 170), new Color(110, 255, 110)),
        WHITE(new Color(200, 200, 200, 240), new Color(180, 180, 180, 240), new Color(51, 51, 51));

        ColorScheme(Color mainColor1, Color mainColor2, Color textColor) {
            main1 = mainColor1;
            main2 = mainColor2;
            text = textColor;
        }

        private Color main1, main2, text;
    }

    public void addItem(String item) {
        CONTENT.add(new Item(item));
    }

    public void removeItem(String item) {
        CONTENT.remove(new Item(item));
    }

    public int[] getSelectedItems() {
        ArrayList<Integer> items = new ArrayList<Integer>();
        for (int i = 0; i < CONTENT.size(); i++) {
            Item item = CONTENT.get(0);
            if (item.isSelected())
                items.add(i);
        }
        int[] itemz = new int[items.size()];
        for (int i = 0; i < items.size(); i++)
            itemz[i] = items.get(i);
        return itemz;
    }

    private class Item
    {
        private String text;
        private boolean isSelected;
        private int id;

        public Item(String text) {
            this.text = text;
            id = Utils.random(0, 999999999);
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void flipSelect() {
            isSelected = !isSelected();
        }

        public String getText() {
            return text;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Item item = (Item) o;

            return id == item.id && !(text != null ? !text.equals(item.text) : item.text != null);

        }

        @Override
        public int hashCode() {
            int result = text != null ? text.hashCode() : 0;
            result = 31 * result + id;
            return result;
        }
    }
}
