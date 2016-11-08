package FF_13312i_Nefedov_Span.model;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Class describing the line described by it's points
 */
public class Line implements Shape{

    public List<Point> points;
    public float       width;

    /**
     * Default class constructor
     * Initializes array of line's points
     */
    public Line(){ points = new ArrayList<>(); }

    /**
     * Custom class constructor
     * Initializes array of line's points and sets line's width
     * @param width - line's width
     */
    public Line(float width){
        points = new ArrayList<>();
        this.width = width;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(width));
        
        for (int i = 0; i < this.points.size() - 1; i++) {
            g.drawLine(this.points.get(i).x, this.points.get(i).y,
                    this.points.get(i + 1).x, this.points.get(i + 1).y);
        }
    }

    @Override
    public String getShapeName() {
        return "POLYLINE";
    }

    @Override
    public float getLineWidth() { return this.width; }

    @Override
    public List<Point> getPoints() { return this.points; }

    @Override
    public Point getCenter() { return null; }

    @Override
    public Point getSeed() { return null; }

    @Override
    public int getFillColor() { return -1; }

    @Override
    public int getPC() { return -1; }

    @Override
    public int getRadius() { return -1; }

    @Override
    public void delete(){
        this.points.clear();
    }

    /**
     * Sets the line's width
     * @param width - line's width
     */
    public void setWidth(float width) {
        this.width = width;
    }
}
