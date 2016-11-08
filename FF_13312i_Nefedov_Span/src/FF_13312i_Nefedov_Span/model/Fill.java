package FF_13312i_Nefedov_Span.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Class describing fill component
 * Implements Shape interface
 */
public class Fill implements Shape {
    private List<Span> spans;
    private Point      seed;
    private Color      color;
    private int        type;

    /**
     * Constructor that allows to set BufferedImage to fill on, seed point, fill's type and fill's color
     * @param img - BufferedImage to fill on
     * @param seed - seed point
     * @param type - fill's type
     * @param color - fill's color: 0 - dark, 1 - bright
     */
    public Fill(BufferedImage img, Point seed, int type, int color) {
        this.seed = seed;
        this.type = type;

        if (color == 0)
            this.color = Color.BLUE;
        if (color == 1)
            this.color = Color.YELLOW;

        this.spans = Span.findAllSpans(img, this.type, this.seed, this.color);
    }

    @Override
    public void draw(Graphics2D g) {
        for (Span span : this.spans)
            span.draw(g);
    }

    @Override
    public String getShapeName() { return "FILL"; }

    @Override
    public Point getSeed() { return this.seed; }

    @Override
    public int getFillColor() {
        int ret = -1;

        if (this.color == Color.BLUE)
            ret = 0;

        if (this.color == Color.YELLOW)
            ret = 1;

        return ret;
    }

    @Override
    public int getPC() { return this.type; };

    @Override
    public Point getCenter() { return null; };

    @Override
    public int getRadius() { return -1; }

    @Override
    public float getLineWidth() { return -1; }

    @Override
    public List<Point> getPoints() { return null; }

    @Override
    public void delete() { }

}
