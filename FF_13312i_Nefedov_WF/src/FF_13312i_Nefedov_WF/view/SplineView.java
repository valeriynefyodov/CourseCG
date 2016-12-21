package FF_13312i_Nefedov_WF.view;

import FF_13312i_Nefedov_WF.controller.Controller;
import FF_13312i_Nefedov_WF.controller.SplineMouseController;
import FF_13312i_Nefedov_WF.model.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Custom originalView class extended from JPanel class
 */
public class SplineView extends JPanel {

    private int             IMG_SIZE_W;
    private int             IMG_SIZE_H;
    private Color           bg_color;
    private Controller      controller;

    public BufferedImage    canvas;
    public Graphics2D       g2d;

    /**
     * Class constructor where you can specify the main controller and a file you'd like to create splineView for
     * @param cntrl - specified controller
     */
    public SplineView(Controller cntrl){
        this.setPreferredSize(new Dimension(500, 500));

        bg_color = new Color(128, 128, 128);

        canvas = new BufferedImage(500, 500, TYPE_INT_ARGB);

        IMG_SIZE_W = canvas.getWidth();
        IMG_SIZE_H = canvas.getHeight();

        g2d = canvas.createGraphics();

        this.setPreferredSize(new Dimension(IMG_SIZE_W, IMG_SIZE_H));

        this.controller = cntrl;

        SplineMouseController msCntrl = new SplineMouseController(this.controller);

        this.addMouseListener(msCntrl.createMouseListener());
        this.addMouseMotionListener(msCntrl.createMouseMotionListener());
    }

    /**
     * Overrides library's paintComponent function
     * Checks the conditions and draws required graphics
     * @param g - graphic's component
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        clearPanel(g);
        clearImage();

        for (Spline s : controller.splines) {
            g2d.setColor(Color.BLACK);

            if (s.points.size() > 0)
                for (Point p : s.points) {
                    g2d.fillOval(p.x, p.y, 8, 8);
                }

            if (s.points.size() > 3) {
                g2d.setColor(Color.RED);

                for (int i = 1; i <= s.points.size() - 3; i++) {
                    for (double t = 0; t <= 1; t += 0.001) {
                        Point p = controller.getSplinePoint(s, i, t);

                        SplineView.setPoint(g2d, p.x, p.y);
                    }
                }
            }
        }

        g.drawImage(canvas, 0, 0, null);
    }

    /**
     * Clears the image - sets default background color to the canvas
     */
    public void clearImage() {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, IMG_SIZE_W, IMG_SIZE_H);
        g2d.setColor(Color.BLACK);
    }

    private void clearPanel(Graphics g) {
        g.setColor(this.bg_color);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

    /**
     * Draws a pixel
     * @param g - graphics component
     * @param x - pixel's X-coordinate
     * @param y - pixel's Y-coordinate
     */
    public static void setPoint(Graphics2D g, int x, int y) {
        g.drawLine(x, y, x, y);
    }
}




















