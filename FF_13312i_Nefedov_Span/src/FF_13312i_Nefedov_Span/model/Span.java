package FF_13312i_Nefedov_Span.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Class describing the span used in span filling algorithm
 */
public class Span {
    public int x_start;
    public int x_end;
    public int y;
    public Color color;

    /**
     * Constructor that sets the spans's start and end x coordinates, it's y coordinate and a color
     * @param x_start - start coordinate
     * @param x_end - end coordinate
     * @param y - y coordinate
     * @param fillColor - color
     */
    public Span(int x_start, int x_end, int y, Color fillColor) {
        this.x_start = x_start;
        this.x_end = x_end;
        this.y = y;
        this.color = fillColor;
    }

    /**
     * Draws a span
     * @param g - graphics component
     */
    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(1.0f));
        g.setColor(this.color);
        g.drawLine(x_start, y, x_end, y);
    }

    /**
     * Initializes a single span
     * @param seed - seed point
     * @param img - buffered image containing the picture
     * @param seedColor - seed's point color
     * @param fillColor - fill color
     * @return - a new span
     */
    private static Span initSpan(Point seed, BufferedImage img, int seedColor, Color fillColor) {
        int left     = seed.x;
        int right    = seed.x;

        if(seed.y == img.getHeight() || seed.y < 0)
            return null;

        if(img.getRGB(seed.x, seed.y) != seedColor)
            return null;

        while ( (left > 0 && img.getRGB(left, seed.y) == seedColor) || (right < img.getWidth() - 1 && img.getRGB(right, seed.y) == seedColor) ) {

            if (left > 0 && img.getRGB(left, seed.y) == seedColor)
                left--;

            if (right < img.getWidth() - 1 && img.getRGB(right, seed.y) == seedColor)
                right++;
        }

        return new Span(left + 1, right - 1, seed.y, fillColor);
    }

    /**
     * Finds spans connected with the parent span from above and below it
     * @param parentSpan - parent span
     * @param fillType - fill type: 4-link 8-link
     * @param img - buffered image containing the picture
     * @param color - seed's color
     * @param fillColor - fill color
     * @return - a list of connected spans
     */
    private static List<Span> findConnectedSpans(Span parentSpan, int fillType, BufferedImage img, int color, Color fillColor) {
        List<Span> connectedSpans = new ArrayList<>();
        int[] y_step = {-1, 1};
        int   x_step = 0;
        int   i;

        if (fillType == 4)
            x_step = 0;
        else if (fillType == 8)
            x_step = 1;

        for (int y : y_step) {
            i = parentSpan.x_start - x_step;

            while (i <= parentSpan.x_end + x_step) {
                Span newSpan = Span.initSpan(new Point(i, parentSpan.y + y), img, color, fillColor);

                if (newSpan == null) {
                    i++;
                    continue;
                } else {
                    if (newSpan.x_end <= i)
                        i++;
                    else
                        i = newSpan.x_end;

                    connectedSpans.add(newSpan);
                }
            }
        }

        for (Span d_span : connectedSpans)
            d_span.draw((Graphics2D) img.getGraphics());

        return connectedSpans;
    }

    /**
     * Finds all spans in the area specified by the seed
     * @param img - buffered image containing the picture
     * @param fillType - fill type: 4-link 8-link
     * @param seed - seed point
     * @param fillColor - fill color
     * @return - a list of the all spans in the area
     */
    public static List<Span> findAllSpans(BufferedImage img, int fillType, Point seed, Color fillColor) {
        List<Span> spans = new ArrayList<>();
        int seedColor = img.getRGB(seed.x, seed.y);
        Span basicSpan = Span.initSpan(seed, img, seedColor, fillColor);

        basicSpan.draw((Graphics2D) img.getGraphics());

        spans.add(basicSpan);
        for(int i = 0; i < spans.size(); i++)
            spans.addAll(findConnectedSpans(spans.get(i), fillType, img, seedColor, fillColor));

        return spans;
    }
}
