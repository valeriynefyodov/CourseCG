package FF_13312i_Nefedov_Span.model;

import java.awt.*;
import java.util.List;

/**
 * Shapes interface
 */
public interface Shape {

    /**
     * Draws the specified shape
     * @param g - graphics component
     */
    void draw(Graphics2D g);

    /**
     * Gets the shape's points
     * @return - List of the shape's points
     */
    List<Point> getPoints();

    /**
     * Gets the shape's radius
     * @return - shape's radius in integer format
     */
    int getRadius();

    /**
     * Gets the shape's seed
     * @return - shape's seed point
     */
    Point getSeed();

    /**
     * Gets the shape's center
     * @return - shape's center point
     */
    Point getCenter();

    /**
     * Gets the shape's fill color
     * @return - shape's fill color in integer format
     */
    int getFillColor();

    /**
     * Gets the width of the shape's line
     * @return - shape's line width in float format
     */
    float getLineWidth();

    /**
     * Gets the shape's link component
     * @return - shape's link component in integer format
     */
    int getPC();

    /**
     * Gets the name of the shape
     * @return - string containing shape's name
     */
    String getShapeName();

    /**
     * Deletes the shape
     */
    void delete();

}
