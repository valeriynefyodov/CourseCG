package FF_13312i_Nefedov_Span.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class describing the polygon
 * Implements Shape interface
 */
public class CustomPolygon extends Polygon implements Shape{

    float width;

    /**
     * Constructor that allows to set polygon's base line and line's width
     * @param line - base line
     * @param width - line's width
     */
    public CustomPolygon(Line line, float width) {
        for (Point p : line.points) {
            this.addPoint(p.x, p.y);
        }

        this.width = width;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(this.width));
        g.drawPolygon(this.xpoints, this.ypoints, this.npoints);
    }

    @Override
    public String getShapeName() { return "POLYGON"; }

    @Override
    public List<Point> getPoints() {
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < this.npoints; i++)
            points.add(new Point(this.xpoints[i], this.ypoints[i]));

        return points;
    }

    @Override
    public float getLineWidth() { return this.width; }

    @Override
    public Point getCenter() { return null; }

    @Override
    public int getRadius() { return -1; }

    @Override
    public Point getSeed() { return null; }

    @Override
    public int getFillColor() { return -1; }

    @Override
    public int getPC() { return -1; }

    @Override
    public void delete() {}
}
