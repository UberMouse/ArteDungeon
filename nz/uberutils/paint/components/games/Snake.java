package nz.uberutils.paint.components.games;

import nz.uberutils.helpers.Utils;
import nz.uberutils.paint.abstracts.PGame;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 5/4/11
 * Time: 7:14 PM
 * Package: nz.uberutils.paint.components.games;
 */
public class Snake extends PGame
{
    private static final ArrayList<Point> snakePoints = new ArrayList<Point>();
    private static Direction direction = Direction.N;
    private static Point lastPos;
    private static Point foodPosition;
    private static Rectangle2D bounds;
    private static final GradientPaint bgGradient = new GradientPaint(7,
                                                                      345,
                                                                      new Color(55, 55, 55, 240),
                                                                      512,
                                                                      472,
                                                                      new Color(15, 15, 15, 240));
    private static final Color outline = new Color(200, 200, 200);
    private static final Color snakeColor = new Color(55, 55, 55, 240);
    private static int speed = 5;
    private static int level = 1;
    private static double score = 1;
    private static int life = 1;
    private static final int width = 15, height = 15;
    private static boolean lost;

    public Snake(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        bounds = new Rectangle(x, y, width, height);
        snakePoints.add(new Point((int) bounds.getCenterX(), (int) bounds.getCenterY()));
        lastPos = new Point(snakePoints.get(0).x, snakePoints.get(0).y);
        foodPosition = generateFoodPosition();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void repaint(Graphics2D g) {

        g.setColor(outline);
        g.drawRect((int) bounds.getX() - 1,
                   (int) bounds.getY() - 1,
                   (int) bounds.getWidth() + 1,
                   (int) bounds.getHeight() + 1);
        Paint p = g.getPaint();
        g.setPaint(bgGradient);
        g.fill(bounds);
        g.setPaint(p);
        g.setColor(snakeColor);
        for (Point point : snakePoints)
            g.fillRect(point.x, point.y, width, height);
        g.setColor(outline);
        g.fillRect(foodPosition.x, foodPosition.y, width, height);
        int y = 10;
        g.drawString("" + lost, 10, 10);
        if (lost) {
            g.setFont(new Font("Arial", Font.BOLD, 22));
            String text = "Game over! You scored: " + score;
            Rectangle2D textBounds = g.getFontMetrics(g.getFont()).getStringBounds(text, g);
            g.drawString(text, (int) (bounds.getCenterX() - (textBounds.getWidth() / 2)), (int) bounds.getCenterY());
        }
        else
            gameLogic();
    }

    public void gameLogic() {
        for (Point collision : snakePoints) {
            for (Point check : snakePoints) {
                Rectangle colRect = new Rectangle(collision.x, collision.y, width, height);
                Rectangle checkRect = new Rectangle(check.x, check.y, width, height);
                if (colRect.intersects(checkRect) && !collision.equals(check)) {
                    lost = true;
                }
            }
        }
        if (lastPos.distance(snakePoints.get(0)) >= (width + height) / 2 + 5) {
            snakePoints.add(1, lastPos.getLocation());
            lastPos = snakePoints.get(0).getLocation();
        }
        if (snakePoints.size() > life) {
            snakePoints.remove(snakePoints.size() - 1);
        }
        Point p = snakePoints.get(0);
        if (!bounds.contains(p)) {
            switch (direction) {
                case N:
                    p.y = (int) (bounds.getY() + bounds.getHeight());
                    break;
                case S:
                    p.y = (int) (bounds.getY());
                    break;
                case E:
                    p.x = (int) bounds.getX();
                    break;
                case W:
                    p.x = (int) (bounds.getX() + bounds.getWidth());
                    break;
            }
        }
        switch (direction) {
            case N:
                p.y -= speed;
                break;
            case S:
                p.y += speed;
                break;
            case E:
                p.x += speed;
                break;
            case W:
                p.x -= speed;
                break;
        }
        if (foodPosition.distance(snakePoints.get(0)) <= 15) {
            level++;
            score += level;
            if (level % 10 == 0)
                speed++;
            life++;
            foodPosition = generateFoodPosition();
        }
    }

    public Point generateFoodPosition() {
        Point gen = new Point(Utils.random((int) bounds.getX(), (int) (bounds.getX() + bounds.getWidth())),
                              Utils.random((int) bounds.getY(), (int) (bounds.getY() + bounds.getHeight())));
        if (gen.distance(snakePoints.get(0)) >= 20)
            return gen;
        else
            return generateFoodPosition();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                direction = Direction.N;
                break;
            case KeyEvent.VK_DOWN:
                direction = Direction.S;
                break;
            case KeyEvent.VK_LEFT:
                direction = Direction.W;
                break;
            case KeyEvent.VK_RIGHT:
                direction = Direction.E;
                break;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && lost) {
            lost = false;
            reset();
        }
    }

    private void reset() {
        life = 1;
        level = 1;
        score = 1;
        snakePoints.clear();
        snakePoints.add(new Point((int) bounds.getCenterX(), (int) bounds.getCenterY()));
        lastPos = new Point(snakePoints.get(0).x, snakePoints.get(0).y);
        foodPosition = generateFoodPosition();
    }

    private static enum Direction
    {
        N, E, S, W
    }
}
