package FF_13312i_Nefedov_Span.model;

import java.awt.*;
import java.util.List;
import FF_13312i_Nefedov_Span.view.View;

/**
 * Class describing the circle
 * Implements Shape interface
 */
public class Oval implements Shape {
    Point center;
    int   radius;
    float width;

    /**
     * Default constructor
     */
    public Oval(){}

    /**
     * Custom constructor that allows to set the circle's center, radius and width
     * @param center - center point
     * @param radius - radius in integer format
     * @param width - width in float format
     */
    public Oval(Point center, int radius, float width){
        this.setCenter(center);
        this.setRadius(radius);
        this.width  = width;
    }

    /**
     * Draws the circle by Bresenhem's algorithm
     * @param g - graphics component
     */
    @Override
    public void draw(Graphics2D g){
        int buf_x = 0;
        int buf_y = radius;
        int dlt = 1 - 2 * radius;
        int err = 0;

        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(width));

        while (buf_y >= 0) {
            View.setPoint(g, center.x + buf_x, center.y + buf_y);
            View.setPoint(g, center.x + buf_x, center.y - buf_y);
            View.setPoint(g, center.x - buf_x, center.y + buf_y);
            View.setPoint(g, center.x - buf_x, center.y - buf_y);

            err = 2 * (dlt + buf_y) - 1;
            if (dlt < 0 && err <= 0) {
                buf_x++;
                dlt += 2 * buf_x + 1;
            }
            else {
                err = 2 * (dlt - buf_x) - 1;
                if (dlt > 0 && err > 0) {
                    buf_y--;
                    dlt += 1 - 2 * buf_y;
                }
                else {
                    buf_x++;
                    dlt += 2 * (buf_x - buf_y);
                    buf_y--;
                }
            }
        }
    }

    @Override
    public String getShapeName() { return "CIRCLE"; }

    @Override
    public Point getCenter() {
        return center;
    }

    @Override
    public int getRadius() {
        return radius;
    }

    @Override
    public float getLineWidth() { return this.width; }

    @Override
    public List<Point> getPoints() { return null; }

    @Override
    public Point getSeed() { return null; }

    @Override
    public int getFillColor() { return -1; }

    @Override
    public int getPC() { return -1; }

    @Override
    public void delete() {
        center = null;
        radius = 0;
    }

    /**
     * Sets the circle's line width
     * @param width - line width in float format
     */
    public void setWidth(float width) { this.width = width; }

    /**
     * Sets the center of the circle
     * @param p - center point
     */
    public void setCenter(Point p) {
        center = p;
    }

    /**
     * Sets the raduis of the circle
     * @param r - radus in integer format
     */
    public void setRadius(int r) {
        radius = r;
    }

    /**
     * Sets the radius of the circle
     * @param p - point on the circle
     */
    public void setRadius(Point p) { radius = (int)Math.sqrt(Math.pow(center.x - p.x, 2) + Math.pow(center.y - p.y, 2)); }
}
