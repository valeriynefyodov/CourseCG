package FF_13312i_Nefedov_WF.view;

import FF_13312i_Nefedov_WF.controller.Controller;
import FF_13312i_Nefedov_WF.controller.WFMouseController;
import FF_13312i_Nefedov_WF.model.Point3D;
import FF_13312i_Nefedov_WF.model.Ring;
import FF_13312i_Nefedov_WF.model.Spline;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Custom originalView class extended from JPanel class
 */
public class WFView extends JPanel {
    private int             BORDER;
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
    public WFView(Controller cntrl){
        BORDER = 30;

        bg_color = new Color(128, 128, 128);
//        Color border_color = new Color(67, 160, 255);

        canvas = new BufferedImage(500, 500, TYPE_INT_ARGB);

        IMG_SIZE_W = canvas.getWidth();
        IMG_SIZE_H = canvas.getHeight();

        g2d = canvas.createGraphics();

        this.setPreferredSize(new Dimension(IMG_SIZE_W, IMG_SIZE_H));

        this.controller = cntrl;

//        this.setBorder(BorderFactory.createMatteBorder(BORDER, BORDER, BORDER, BORDER, border_color));

        WFMouseController msCntrl = new WFMouseController(this.controller);

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

        g2d.setColor(Color.BLACK);

        for (Spline s : controller.splines) {
            s.rings.clear();

            if (s.points.size() > 3) {
                for (int i = 1; i <= s.points.size() - 3; i++) {
                    for (double t = 0; t <= 1; t += 1. / (controller.n * controller.k)) {
                        s.rings.add(new Ring());

                        for (double phi = 0; phi <= 2 * Math.PI; phi += (2 * Math.PI) / (controller.m * controller.k)) {
                            Point3D p = controller.getProjectionPoint(s, i, t, phi);

                            s.rings.get(s.rings.size() - 1).ringPoints.add(p);
                        }
                    }
                }
            }


            for (Ring ring : s.rings) {
                Point3D prevPoint = null;
                Point3D firstPoint = null;
                boolean isFirst = true;

                for (Point3D p : ring.ringPoints) {
                    if (isFirst) {
                        prevPoint = p;
                        firstPoint = p;
                        isFirst = false;
                        continue;
                    } else if (prevPoint != null) {
                        doClipping(prevPoint, p);
//                            g2d.drawLine((int) prevPoint.x + canvas.getWidth() / 2, (int) prevPoint.y + canvas.getHeight() / 2, (int) p.x + canvas.getWidth() / 2, (int) p.y + canvas.getHeight() / 2);
                        prevPoint = p;
                    }
                }
                doClipping(prevPoint, firstPoint);
            }

            for (int i = 0; i < s.rings.size() - 1; i++) {
                for (int j = 0; j < s.rings.get(0).ringPoints.size(); j += (s.rings.get(0).ringPoints.size() / controller.m)) {
                    Point3D p1 = s.rings.get(i).ringPoints.get(j);
                    Point3D p2 = s.rings.get(i + 1).ringPoints.get(j);

                    doClipping(p1, p2);
//                    g2d.drawLine((int) p1.x + canvas.getWidth() / 2, (int) p1.y + canvas.getHeight() / 2, (int) p2.x + canvas.getWidth() / 2, (int) p2.y + canvas.getHeight() / 2);
                }
            }
        }

        g.drawImage(canvas, 0, 0, null);
    }

    public void doClipping(Point3D p1, Point3D p2) {
        int top    = BORDER;
        int bottom = canvas.getHeight() - BORDER;
        int left   = BORDER;
        int right  = canvas.getWidth() - BORDER;
//        int front  = (int)controller.zn;
//        int back   = (int)controller.zf;

        if ((p1.x < left && p2.x < left)
                || (p1.x > right && p2.x > right)
                || (p1.y < top && p2.y < top)
                || (p1.y > bottom && p2.y > bottom))
            return;

        Point3D p1_tmp = p1;
        Point3D p2_tmp = p2;

        p1 = fitPoints(p2, p1);
        p1 = fitPoints(p2_tmp, p1);
        p2 = fitPoints(p1, p2);
        p2 = fitPoints(p1_tmp, p2);

        int dx = canvas.getWidth() / 2;
        int dy = canvas.getHeight() / 2;

        g2d.drawLine((int)p1.x, (int)p1.y, (int)p2.x, (int)p2.y );
    }

    private Point3D fitPoints(Point3D pointToFit, Point3D secondPoint) {
        int top    = BORDER;
        int bottom = canvas.getHeight() - BORDER;
        int left   = BORDER;
        int right  = canvas.getWidth() - BORDER;

        double x_new = pointToFit.x;
        double y_new = pointToFit.y;

        if (pointToFit.x < left) {
            x_new = left + 1;
            y_new = pointToFit.y - (pointToFit.x - x_new) * ((secondPoint.y - pointToFit.y) / (secondPoint.x - pointToFit.x));
        }

        if (pointToFit.x > right) {
            x_new = right - 1;
            y_new = pointToFit.y - (pointToFit.x - x_new) * ((secondPoint.y - pointToFit.y) / (secondPoint.x - pointToFit.x));
        }

        if (pointToFit.y < top) {
            y_new = top + 1;
            x_new = pointToFit.x - (pointToFit.y - y_new) * ((secondPoint.x - pointToFit.x) / (secondPoint.y - pointToFit.y));
        }

        if (pointToFit.y > bottom) {
            y_new = bottom - 1;
            x_new = pointToFit.x - (pointToFit.y - y_new) * ((secondPoint.x - pointToFit.x) / (secondPoint.y - pointToFit.y));
        }

        return new Point3D(x_new, y_new, 0.);
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




















