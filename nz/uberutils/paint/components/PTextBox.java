package nz.uberutils.paint.components;

import nz.uberutils.paint.abstracts.PComponent;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 4/2/11
 * Time: 12:20 AM
 * Package: nz.uberutils.paint.components;
 */
public class PTextBox extends PComponent
{
    private ColorScheme colorScheme;
    public String currentText = "*";
    private Font font = null;
    private boolean focused = false;
    private FontMetrics metrics;
    int curIndex = 0;
    int lastIndex = -1;

    public PTextBox(int x, int y, int width, int height, ColorScheme colorScheme, Font font) {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.colorScheme = colorScheme;
        this.font = font;
    }

    public PTextBox(int x, int y, int width, int height, ColorScheme colorScheme) {
        this(x, y, width, height, colorScheme, null);
    }

    int cursorSpeed = 10;
    boolean up = true;

    private String getVisibleText(String text) {
        if (metrics.stringWidth(text) < width - 8) {
            return text;
        }
        else {
            String toReturn = "";
            for (char ch : text.toCharArray()) {
                if (metrics.stringWidth(toReturn + ch) < width - (14)) {
                    toReturn += ch;
                }
                else if (metrics.stringWidth(toReturn.substring(1) + ch) < width - (14)) {
                    toReturn = toReturn.substring(1) + ch;
                }
                else if (metrics.stringWidth(toReturn.substring(2) + ch) < width - (14)) {
                    toReturn = toReturn.substring(2) + ch;
                }
                else if (metrics.stringWidth(toReturn.substring(3) + ch) < width - (14)) {
                    toReturn = toReturn.substring(3) + ch;
                }
            }
            return toReturn;
        }
    }

    public enum ColorScheme
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

    public String getText() {
        return currentText;
    }

    public void setText(String text) {
        currentText = text;
    }

    public void addChar(char ch) {
        addChar(ch, -1);
    }

    public void addChar(char ch, int index) {
        onChange();
        currentText = (index != -1) ? new StringBuffer(currentText).insert(index, ch).toString() : currentText + ch;
    }

    public void removeChar(int index) {
        onChange();
        currentText = new StringBuffer(currentText).replace(index, index + 1, "").toString();
    }

    public void removeLastChar() {
        if (!currentText.isEmpty()) {
            removeChar(curIndex);
            currentText = currentText.substring(0, currentText.length() - 1);
        }

    }

    public boolean pointInTextBox(Point point) {
        return new Rectangle(x, y, width, height).contains(point);
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return this.focused;
    }

    @Override
    public void repaint(Graphics2D g) {
        if (font == null)
            font = g.getFont();
        g.setFont(font);
        metrics = g.getFontMetrics();

        Paint p = g.getPaint();

        g.setPaint(new GradientPaint(x - 8,
                                     y - 8,
                                     new Color(230, 230, 230, 180),
                                     x - 8,
                                     y + 16,
                                     new Color(200, 200, 200, 180)));
        g.fillRect(x, y, width, height);

        g.setPaint(new GradientPaint(x, y, colorScheme.main1, x, y + height, colorScheme.main2));
        g.fillRect(x + 4, y + 4, width - 8, height - 8);

        g.setPaint(p);
        if (focused) {
            g.setColor(colorScheme.text);
            g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.drawRect(x + 4, y + 4, width - 8, height - 8);
        }
        if (cursorSpeed <= 20)
            cursorSpeed++;
        else cursorSpeed = 0;

        g.setColor(colorScheme.text);
        g.drawString(getVisibleText(currentText), x + 8, y + (height / 2)
                                                         +
                                                         (int) (metrics.getMaxAscent() / (font.getSize() / 4.5)));

        g.setColor(new Color(255, 255, 255, 65));
        g.fillRect(x + 1, y + 1, width - 2, (int) (height / 2.222));

        g.setStroke(new BasicStroke());
    }

    public void keyTyped(KeyEvent e) {
        if (curIndex < 0)
            curIndex = 0;
        if (curIndex > currentText.length())
            curIndex = currentText.length() - 1;
        currentText = new StringBuffer(currentText).replace(curIndex, curIndex + 1, "").toString();
        switch (e.getKeyChar()) {
            case KeyEvent.VK_BACK_SPACE:
                if (curIndex > 0) {
                    removeChar(curIndex - 1);
                }
                curIndex--;
                addChar("*".charAt(0), curIndex);
                break;

            case KeyEvent.VK_LEFT:
                removeChar(curIndex);
                curIndex--;
                addChar("*".charAt(0), curIndex);
                break;

            case KeyEvent.VK_RIGHT:
                removeChar(curIndex);
                curIndex++;
                addChar("*".charAt(0), curIndex);
                break;

            default:
                addChar(e.getKeyChar(), curIndex);
                curIndex++;
                addChar("*".charAt(0), curIndex);
                break;
        }
    }

    public void keyPressed(KeyEvent e) {
        if (curIndex < 0)
            curIndex = 0;
        if (curIndex > currentText.length())
            curIndex = currentText.length() - 1;
        currentText = new StringBuffer(currentText).replace(curIndex, curIndex + 1, "").toString();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                curIndex--;
                break;

            case KeyEvent.VK_RIGHT:
                curIndex++;
                break;
        }
        addChar("*".charAt(0), curIndex);
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        setFocused(pointInTextBox(mouseEvent.getPoint()));
    }

    public void onChange() {

    }

    public boolean shouldHandleKeys() {
        return focused;
    }
}
