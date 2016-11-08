package FF_13312i_Nefedov_IS.view;

import FF_13312i_Nefedov_IS.controller.Controller;
import FF_13312i_Nefedov_IS.controller.MouseController;
import FF_13312i_Nefedov_IS.model.Function;

import java.util.List;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * Custom view class extended from JPanel class
 */
public class View extends JPanel {

    public Controller controller;
    public BufferedImage buffImage;

    /**
     * Class constructor where you can specify the main controller
     * @param cntrl - specified controller
     */
    public View(Controller cntrl){
        this.setPreferredSize(new Dimension(600, 600));

        buffImage = new BufferedImage(600, 600, TYPE_INT_ARGB);

        controller = cntrl;
        MouseController msCntrl = new MouseController(controller);

        addMouseListener(msCntrl.createMouseListener());
        addMouseMotionListener(msCntrl.createMouseMotionListener());
    }

    /**
     * Overrides library's paintComponent function
     * Checks the conditions and draws required graphics
     * @param g - graphic's component
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D gg = (Graphics2D) buffImage.getGraphics();
        gg.setColor(Color.WHITE);
        gg.fillRect(0, 0, buffImage.getWidth(), buffImage.getHeight());
        gg.setColor(Color.BLACK);

        if (controller.mode == "norm")
            drawColorMap(gg);

        if (controller.mode == "interpol")
            drawInterpolarColorMap(gg);

        if (controller.isShowGrid)
            drawGrid(gg);

        if (controller.isDrawIsoline && !controller.func_vals.isEmpty())
            for (Double val : controller.func_vals)
                drawIsolines(gg, val);

        if (controller.curr_point != null) {
            double[] coord = controller.pixToCoord(controller.curr_point.x, controller.curr_point.y);
            double   val = Function.circle(coord[0], coord[1]);

            g.drawString("X: " + Double.toString(coord[0]) + "   Y: "  + Double.toString(coord[1]) + "   F(X,Y): " + Double.toString(val), 20, 20 + buffImage.getHeight() + 20);
        }

        g.drawImage(buffImage, 0, 0, this);

        drawLegend(g);
    }

    /**
     * Draws a color legend
     * @param g - graphics component
     */
    private void drawLegend(Graphics g) {
        BufferedImage legend = new BufferedImage(50, buffImage.getHeight(), TYPE_INT_ARGB);
        Graphics2D gg = legend.createGraphics();
        int rectHeight = (int) ((double)legend.getHeight() / (double)controller.n_levels);

        if (controller.mode == "norm") {
            for (int i = 0; i < controller.n_levels; i++) {
                gg.setColor(controller.colors.get(i));
                gg.fillRect(0, i * rectHeight, 50, rectHeight);
            }
        }

        if (controller.mode == "interpol") {
            for (int x = 0; x < legend.getWidth(); x++) {
                for(int y = 0; y < legend.getHeight(); y++) {
                    for (int i = 0; i < controller.n_levels; i++) {

                        double l_val = i * rectHeight;
                        double r_val = (i + 1) * rectHeight;
                        double func_result = y;

                        if (l_val <= y && y <= r_val) {

                            Color c1 = controller.colors.get(i);
                            Color c2 = null;

                            if (i == 5)
                                c2 = controller.colors.get(i);
                            else
                                c2 = controller.colors.get(i + 1);

                            int new_r = (int) ((double) c1.getRed() * (r_val - func_result) / (r_val - l_val) + (double) c2.getRed() * (func_result - l_val) / (r_val - l_val));
                            int new_g = (int) ((double) c1.getGreen() * (r_val - func_result) / (r_val - l_val) + (double) c2.getGreen() * (func_result - l_val) / (r_val - l_val));
                            int new_b = (int) ((double) c1.getBlue() * (r_val - func_result) / (r_val - l_val) + (double) c2.getBlue() * (func_result - l_val) / (r_val - l_val));

                            gg.setColor(new Color(new_r, new_g, new_b));
                            setPoint(gg, x, y);
                        }

                    }
                }
            }
        }

        g.drawImage(legend, buffImage.getWidth() + 50, 0, this);
    }

    /**
     * Draws a grid
     * @param g - graphics component
     */
    private void drawGrid(Graphics2D g) {
        double x_step = (double)buffImage.getWidth() / (double)controller.k_grid;
        double y_step = (double)buffImage.getHeight() / (double)controller.m_grid;

        g.setColor(Color.BLUE);

        for (int i = 0; i < controller.k_grid; i++) {
            for (int j = 0; j < controller.m_grid; j++) {
                int x1 = (int) (x_step * (double)i + 0.5);
                int y1 = (int) (y_step * (double)j + 0.5);
                int x2 = (int) (x_step * (double)(i + 1) + 0.5);
                int y2 = (int) (y_step * (double)(j + 1) + 0.5);

                g.drawRect(x1, y1, x2 - x1, y2 - y1);
            }
        }
    }

    /**
     * Gets an enter point of an isoline on the side of a grid cell
     * @param p1 - first point of the side
     * @param p2 - second point of the side
     * @param value - value of an isoline
     * @return - enter point or null if there is no some
     */
    private Point getSidePoint(Point p1, Point p2, double value) {
        double p1_val = controller.getFunctionValue(p1);
        double p2_val = controller.getFunctionValue(p2);

        if ( (p1_val < value && value < p2_val)
                || (p2_val < value && p1_val < value) ) {

            double dx = (p2.x - p1.x);
            double dy = (p2.y - p1.y);

            int x = (int) (p1.x + dx * (value - p1_val) / (p2_val - p1_val));
            int y = (int) (p1.y + dy * (value - p1_val) / (p2_val - p1_val));

            return new Point(x,y);
        } else {
            return null;
        }
    }

    /**
     * Draws an isolines inside specified grid cell
     * @param g - graphics component
     * @param points - list of an enter points of an isoline
     * @param val - value of an isoline
     */
    private void drawGridCellIsolines(Graphics2D g, List<Point> points, double val) {
        g.setColor(controller.is_color);

        List<Point> enterPoints = new ArrayList();

        for (int i = 0; i < 4; i++) {
            Point p = null;

            if (i == 3) {
                p = getSidePoint(points.get(i), points.get(0), val);
            } else {
                p = getSidePoint(points.get(i), points.get(i + 1), val);
            }

            if (p != null)
                enterPoints.add(p);
        }
        if (!enterPoints.isEmpty()) {
            if (enterPoints.size() == 2) {
                g.drawLine(enterPoints.get(0).x, enterPoints.get(0).y,
                           enterPoints.get(1).x, enterPoints.get(1).y);
            } else if (enterPoints.size() == 4) {
                Point center = new Point((points.get(0).x + points.get(2).x) / 2, (points.get(0).y + points.get(2).y) / 2);
                double centerValue = controller.getFunctionValue(center) - val;

                double comp_val_left  = controller.getFunctionValue(points.get(0)) - val;
                double comp_val_right = controller.getFunctionValue(points.get(1)) - val;

                if (comp_val_left * centerValue < 0) {
                    g.drawLine(enterPoints.get(0).x, enterPoints.get(0).y,
                               enterPoints.get(3).x, enterPoints.get(3).y);

                    g.drawLine(enterPoints.get(2).x, enterPoints.get(2).y,
                               enterPoints.get(1).x, enterPoints.get(1).y);
                }

                if (comp_val_right * centerValue < 0){
                    g.drawLine(enterPoints.get(0).x, enterPoints.get(0).y,
                               enterPoints.get(1).x, enterPoints.get(1).y);

                    g.drawLine(enterPoints.get(2).x, enterPoints.get(2).y,
                               enterPoints.get(4).x, enterPoints.get(4).y);
                }
            }
        } else { return; }
    }

    /**
     * Draws function's isolines
     * @param g  - graphics component
     * @param val - isoline's value
     */
    private void drawIsolines(Graphics2D g, double val) {
        double x_step = (double)buffImage.getWidth() / (double)controller.k_grid;
        double y_step = (double)buffImage.getHeight() / (double)controller.m_grid;

        for (int i = 0; i < controller.k_grid; i++) {
            for (int j = 0; j < controller.m_grid; j++) {
                int x1 = (int) (x_step * (double)i + 0.5);
                int y1 = (int) (y_step * (double)j + 0.5);
                int x2 = (int) (x_step * (double)(i + 1) + 0.5);
                int y2 = (int) (y_step * (double)(j + 1) + 0.5);

                List<Point> cellPoints = new ArrayList<>();
                cellPoints.add(new Point(x1, y1));
                cellPoints.add(new Point(x2, y1));
                cellPoints.add(new Point(x2, y2));
                cellPoints.add(new Point(x1, y2));

                drawGridCellIsolines(g, cellPoints, val);
            }
        }
    }

    /**
    * Draws a color map of the function
    * @param g - graphics component
    */
    private void drawColorMap(Graphics2D g) {
        double interval = (controller.function.max - controller.function.min) / controller.n_levels;

        for (int i = 0; i < buffImage.getWidth(); i++) {
            for (int j = 0; j < buffImage.getHeight(); j++) {
                double[] coordinate = controller.pixToCoord(i, j);
                double   func_result = controller.function.circle(coordinate[0], coordinate[1]);

                for (int k = 0; k < controller.n_levels; k++) {
                    if (func_result >= Function.circle(k * interval, k * interval)
                            && func_result <= Function.circle((k + 1) * interval, (k + 1) * interval)) {
                        g.setColor(controller.colors.get(k));
                        setPoint(g, i, j);
                    }
                }
            }
        }
    }

    /**
     * Draws interpolated color map of the function
     * @param g - graphics component
     */
    private void drawInterpolarColorMap(Graphics2D g) {
        double interval = (controller.function.max - controller.function.min) / controller.n_levels;

        for (int i = 0; i < buffImage.getWidth(); i++) {
            for (int j = 0; j < buffImage.getHeight(); j++) {
                double[] coordinate = controller.pixToCoord(i, j);
                double   func_result = controller.function.circle(coordinate[0], coordinate[1]);

                for (int k = 0; k < controller.n_levels; k++) {
                    if (func_result >= Function.circle(k * interval, k * interval)
                            && func_result <= Function.circle((k + 1) * interval, (k + 1) * interval)) {

                        double l_val = Function.circle(k * interval, k * interval);
                        double r_val = Function.circle((k + 1) * interval, (k + 1) * interval);

                        Color c1 = controller.colors.get(k);
                        Color c2 = null;

                        if (k == 5)
                            c2 = controller.colors.get(k);
                        else
                            c2 = controller.colors.get(k + 1);

                        int new_r = (int) ((double)c1.getRed() * (r_val - func_result) / (r_val - l_val) + (double)c2.getRed() * (func_result - l_val) / (r_val - l_val));
                        int new_g = (int) ((double)c1.getGreen() * (r_val - func_result) / (r_val - l_val) + (double)c2.getGreen() * (func_result - l_val) / (r_val - l_val));
                        int new_b = (int) ((double)c1.getBlue() * (r_val - func_result) / (r_val - l_val) + (double)c2.getBlue() * (func_result - l_val) / (r_val - l_val));

                        g.setColor(new Color(new_r, new_g, new_b));
                        setPoint(g, i, j);
                    }
                }
            }
        }
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

    /**
     * Shows an error dialog
     * @param message - string containing a message to be shown
     * @param dialogName - name of the dialog
     */
    public void showErrorDialog (String message, String dialogName) {
        JOptionPane.showMessageDialog(this, message, dialogName, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a message dialog
     * @param message - string containing a message to be shown
     * @param dialogName - name of the dialog
     */
    public void showMessageDialog (String message, String dialogName) {
        JOptionPane.showMessageDialog(this, message, dialogName, JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a scrollable message dialog
     * @param message - a list of strings containing a message to be shown
     * @param dialogName - name of the dialog
     */
    public void showScrollableMessageDialog (DefaultListModel message, String dialogName) {
        JList textArea = new JList(message);
        JScrollPane scrollPane = new JScrollPane(textArea);

        scrollPane.setPreferredSize(new Dimension(700, 500));

        JOptionPane.showMessageDialog(null, scrollPane, dialogName, JOptionPane.INFORMATION_MESSAGE);
    }
}




















